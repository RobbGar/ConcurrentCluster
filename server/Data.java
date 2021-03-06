package server;

import java.io.*;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

public class Data{

    private ConcurrentHashMap<String, ConcurrentHashMap<String,Integer>> searches = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, Integer> totWords = new ConcurrentHashMap<>();

    public boolean search(String search, String location) throws IllegalArgumentException {
        location = normalize(location);
        search = normalize(search);
        if(location.isEmpty() || search.isEmpty())
            throw new IllegalArgumentException();
        String [] split = search.split("\\s+");

        ConcurrentHashMap<String, Integer> temp = searches.get(location);
        if(temp == null) {
            temp = new ConcurrentHashMap<>();
            searches.put(location, temp);
        }

        for(String elem : split) {
            Integer s = temp.get(elem);
            if (s == null)
                temp.put(elem, 1);
            else
                temp.replace(elem, s+1);

            incrementTotWords(location);
        }
        return true;
    }

    private synchronized void incrementTotWords(String location){
        Integer count = totWords.get(location);
        if (count == null)
            totWords.put(location, 1);
        else
            totWords.replace(location, count + 1);
    }

    private String normalize(String s) {
        if (s == null)
            throw new IllegalArgumentException();
        s = s.replaceAll("(\\W)|(\\s+)", " ");
        s = s.toLowerCase();
        return s;
    }

    synchronized String getTopThree(String location){
        location = normalize(location);
        String res = "";
        if (searches.get(location) == null) return null;
        ConcurrentHashMap<String, Integer> temp = new ConcurrentHashMap<>(searches.get(location));

        int count = temp.size() < 3 ? temp.size() : 3;
        float totwords = totWords.get(location);
        for(int i = 0; i < count ; ++i) {
            String s = (Collections.max(temp.entrySet(), ConcurrentHashMap.Entry.comparingByValue()).getKey());
            float r = temp.get(s);
            float perc = ((r / totwords) * 100);
            res += " " + s + " " + perc + "%" + "\n";
            temp.remove(s);
        }
        return res;

    }

    boolean save() {
        File file = new File("server/data/hashmap.ser");
        File file1 = new File("server/data/totwords.ser");
        file.delete();
        file1.delete();
        try {
            FileOutputStream fos =
                    new FileOutputStream("server/data/hashmap.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(searches);
            oos.close();
            fos.close();
            fos = new FileOutputStream("server/data/totwords.ser");
            oos = new ObjectOutputStream(fos);
            oos.writeObject(totWords);
            oos.close();
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    boolean load(){
        try
        {
            FileInputStream hm = new FileInputStream("server/data/hashmap.ser");
            ObjectInputStream ois = new ObjectInputStream(hm);
            searches = (ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>>) ois.readObject();
            ois.close();
            hm.close();
            hm = new FileInputStream("server/data/totwords.ser");
            ois = new ObjectInputStream(hm);
            totWords = (ConcurrentHashMap<String, Integer>) ois.readObject();
            ois.close();
            hm.close();
            return true;
        }catch(ClassNotFoundException | IOException ioe){
            return false;
        }
    }

    boolean reset(){
        searches = new ConcurrentHashMap<>();
        totWords = new ConcurrentHashMap<>();
        File file = new File("server/data/hashmap.ser");
        File file1 = new File("server/data/totwords.ser");
        return file.delete() && file1.delete();
    }
}
