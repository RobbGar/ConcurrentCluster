package server;

import java.util.concurrent.ConcurrentHashMap;

public class Datas{

    private ConcurrentHashMap<String, ConcurrentHashMap<String,Integer>> searches = new ConcurrentHashMap<>();

    public boolean search(String search, String location) throws IllegalArgumentException {
        location = normalize(location);
        search = normalize(search);
        if(location.isEmpty() || search.isEmpty())
            throw new IllegalArgumentException();
        String [] split = search.split(" ");

        ConcurrentHashMap<String, Integer> temp = searches.get(location);
        boolean empty = false;
        if(temp == null) {
            temp = new ConcurrentHashMap<>();
            empty = true;
        }

        for(String elem : split) {
            Integer s = temp.get(elem);
            if (s == null)
                temp.put(elem, 1);
            else {
                //TODO not sure if thread safe
                temp.replace(elem, s+1);
            }
        }

        if(empty) searches.put(location, temp);
        return true;
    }

    private String normalize(String s) {
        if (s == null)
            throw new IllegalArgumentException();
        s = s.replaceAll("(\\W)|(\\s+)", " ");
        //TODO delete if everything works
        //s = s.replaceAll("^\\s", "");
        s = s.toLowerCase();
        return s;
    }

    public static void main (String [] args){
        Datas s = new Datas();
        s.search("Ciao come va in quel di di milano", "rapallo");
        s.search("CIAO come MILANO MILANO MILANO", "rapallo");
        s.search("cangioloni Giacomo", "Chiavari");
        s.search("va quel cangioloni", "Rapallo");
    }



}
