package server;

import java.io.*;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

public class Data{

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

    synchronized String getTopThree(String location) throws IllegalArgumentException{
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

    boolean save() {
        reset();
        try {
            FileOutputStream fos =
                    new FileOutputStream("server/data/hashmap.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(searches);
            oos.close();
            fos.close();
            FileOutputStream fos1 =
                    new FileOutputStream("server/data/totwords.ser");
            ObjectOutputStream os = new ObjectOutputStream(fos1);
            os.writeObject(totWords);
            os.close();
            fos1.close();
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
            searches = (ConcurrentHashMap) ois.readObject();
            totWords = (Integer) ois1.readObject();
            ois.close();
            tw.close();
            ois1.close();
            hm.close();
            return true;
        }catch(IOException ioe)
        {
            ioe.printStackTrace();
            return false;
        }catch(ClassNotFoundException c)
        {
            System.out.println("Class not found");
            c.printStackTrace();
            return false;
        }
    }

    boolean reset(){
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
