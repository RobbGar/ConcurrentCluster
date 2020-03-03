package server;

import java.io.*;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

public class Data {

    private ConcurrentHashMap<String, ConcurrentHashMap<String,Integer>> searches = new ConcurrentHashMap<>();
    private static int totWords = 0; //Total words searched

    public synchronized static int getTotWords() {
        return totWords;
    }

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
            incrementTotWords();
        }
        return true;
    }

    private synchronized void incrementTotWords(){
        this.totWords ++;
    }

    private String normalize(String s) {
        if (s.isEmpty())
            throw new IllegalArgumentException();
        s = s.replaceAll("(\\W)|(\\s+)", " ");
        //TODO delete if everything works
        //s = s.replaceAll("^\\s", "");
        s = s.toLowerCase();
        return s;
    }

    public synchronized String MostSearchedW(String location) throws IllegalArgumentException{
        location = normalize(location);
        String res = "";
        //TODO manage exception
        if (searches.get(location) == null) throw new IllegalArgumentException("Location not in the system");
        ConcurrentHashMap<String, Integer> temp = new ConcurrentHashMap<>(searches.get(location));

        int count = temp.size()<3 ? temp.size() : 3;
        for(int i = 0; i < count ; ++i) {
            String s = (Collections.max(temp.entrySet(), ConcurrentHashMap.Entry.comparingByValue()).getKey());
            float r = temp.get(s);
            float perc = ((r / totWords) * 100);
            res += " " + s + " " + perc + "%" + "\n";
            temp.remove(s);

        }
        return res;

    }

    protected void save() {
        try {
            FileOutputStream fos =
                    new FileOutputStream("hashmap.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(searches);
            oos.close();
            fos.close();
            System.out.printf("Serialized HashMap data is saved in hashmap.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void load(){
        try
        {
            FileInputStream fis = new FileInputStream("hashmap.json");
            ObjectInputStream ois = new ObjectInputStream(fis);
            searches = (ConcurrentHashMap) ois.readObject();
            ois.close();
            fis.close();
        }catch(IOException ioe)
        {
            ioe.printStackTrace();
            return;
        }catch(ClassNotFoundException c)
        {
            System.out.println("Class not found");
            c.printStackTrace();
            return;
        }
    }

    public static void main (String [] args){
       /* Data s = new Data();
        s.search("Ciao come     va in quel di di milano", "rapallo");
        s.search("CIAO come MILANO MILANO MILANO", "rapallo");
        s.search("cangioloni Giacomo", "Chiavari");
        s.search("va quel cangioloni ciao ciao", "Rapallo");
        try {
            System.out.println(s.MostSearchedW("rapallo"));
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
        }*/
       //TODO Remove Debug
    }



}
