package server;

import java.util.concurrent.Callable;

public class Search implements Callable<Boolean> {

    private String words;
    private String location;
    private Data data;

    public Search(String words, String location, Data database) {
        this.words = words;
        this.location = location;
        this.data = database;
    }
    @Override
    public Boolean call() {
        return data.research(words,location);
    }
}
