package server;

import java.io.*;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

public class Data{

    private ConcurrentHashMap<String, ConcurrentHashMap<String,Integer>> searches = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, Integer> totWords = new ConcurrentHashMap<>(); //Total words searched

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
            else {
                //TODO not sure if thread safe
                temp.replace(elem, s+1);
            }
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
        if (s.isEmpty())
            throw new IllegalArgumentException();
        s = s.replaceAll("(\\W)|(\\s+)", " ");
        s = s.toLowerCase();
        return s;
    }

    synchronized String getTopThree(String location) throws IllegalArgumentException{
        location = normalize(location);
        String res = "";
        //TODO manage exception
        if (searches.get(location) == null) throw new IllegalArgumentException("Location not in the system");
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
            FileInputStream tw = new FileInputStream("server/data/totwords.ser");
            ObjectInputStream ois1 = new ObjectInputStream(tw);
            searches = (ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>>) ois.readObject();
            totWords = (ConcurrentHashMap<String, Integer>) ois1.readObject();
            ois.close();
            tw.close();
            ois1.close();
            hm.close();
            return true;
        }catch(IOException ioe){
            ioe.printStackTrace();
            return false;
        }catch(ClassNotFoundException c) {
            System.out.println("Class not found");
            c.printStackTrace();
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

    public static void main (String [] args){
       /* Data s = new Data();
        s.search("Ciao come     va in quel di di milano", "rapallo");
        s.search("CIAO come MILANO MILANO MILANO", "rapallo");
        s.search("cangioloni Giacomo", "Chiavari");
        s.search("va quel cangioloni ciao ciao", "Rapallo");
        try {
            System.out.println(s.getTopThree("rapallo"));
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
        }*/
       //TODO Remove Debug
    }



}
