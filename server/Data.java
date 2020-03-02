package server;

import java.util.concurrent.ConcurrentHashMap;

public class Data {

    private String words;
    private String location;
    private static final int maxWords = 3;//numero delle parole, più frequenti per ogni città, da stampare
    private ConcurrentHashMap<String,ConcurrentHashMap<String,Integer>> searches = new ConcurrentHashMap<>(); //hashMap che funziona da "database" in cui salviamo le parole cercate nei vari luoghi
    private ConcurrentHashMap<String,ConcurrentHashMap<String,Integer>> mostsearchedwords = new ConcurrentHashMap<>();//usiamo per memorizzare le parole più frequenti

    public boolean research(String words, String location) {
        this.location = normalize(location);
        this.words = normalize(words);
        if(this.location.isEmpty() || this.words.isEmpty())
            return false;
        store();
        return true;
    }

    private void store() {
        ConcurrentHashMap<String, Integer> mapWords = new ConcurrentHashMap<>();
        if(searches.putIfAbsent(location, mapWords)==null)
            storeWords(mapWords);
        else
            storeWords(searches.get(location));
    }

    private void storeWords(ConcurrentHashMap<String, Integer> mapWords) {
        String [] SplitW = words.split(" ");
        for(String eachWord : SplitW) {
            if(mapWords.putIfAbsent(eachWord, 1)!=null) {
                incrementValue(mapWords, eachWord);
            }
            updateMSW(eachWord, mapWords.get(eachWord));
        }
    }

    private synchronized void incrementValue(ConcurrentHashMap<String, Integer> mapWords, String key) {
        int count = mapWords.get(key);
        mapWords.replace(key, count+1);
    }

    private void updateMSW(String eachWord, int valueI) {
        ConcurrentHashMap<String, Integer> MSWmapWords = new ConcurrentHashMap<>();
        if(mostsearchedwords.putIfAbsent(location, MSWmapWords) == null)
            updateWordsMSW(MSWmapWords, eachWord, valueI);
        else
            updateWordsMSW(mostsearchedwords.get(location), eachWord, valueI);
    }

    private synchronized void updateWordsMSW(ConcurrentHashMap<String, Integer> MSWmapWords, String eachWord, int valueI) {
        if(MSWmapWords.size()<maxWords)
            if(MSWmapWords.putIfAbsent(eachWord, valueI)==null)
                return;
        if(MSWmapWords.replace(eachWord, valueI)!=null)
            return;
        String wordMin=findMin(MSWmapWords);
        int value = MSWmapWords.get(wordMin);
        if(value < valueI) {
            MSWmapWords.remove(wordMin, value);
            MSWmapWords.put(eachWord, valueI);
        }
    }

    private String findMin(ConcurrentHashMap<String, Integer> MSWmapWords){
        int min = 0;
        String wordMin = null;
        boolean flag = true;
        for (String W: MSWmapWords.keySet()){
            if(flag) {
                wordMin=W;
                min=MSWmapWords.get(W);
                flag=false;
            }else{
                int value = MSWmapWords.get(W);
                if(value < min) {
                    min = value;
                    wordMin=W;
                }
            }
        }
        return wordMin;
    }

    private String normalize(String s) {
        if (s == null)
            throw new IllegalArgumentException();
        s = s.replaceAll("(\\W)|(\\s+)", " ");
        s = s.replaceAll("^\\s", "");
        s = s.toLowerCase();
        return s;
    }

    public synchronized String MostSearchedW() {
        String res = "";
        for (String loc: mostsearchedwords.keySet()){ //
            String value = mostsearchedwords.get(loc).toString();
            value = value.replaceAll("=", ":");
            value = value.replace("{", "[");
            value = value.replace("}", "]");
            res = res.concat(loc + ": " + value + ", ");
        }
        return res;
    }
}
