package server;

import java.util.concurrent.Callable;

public class Search implements Callable<Boolean> {

    private String words;
    private String location;
    private Data data;

    public Search(String words, String location, Data data) {
        this.words = words;
        this.location = location;
        this.data = data;
    }

    @Override
    public Boolean call() {
        return data.search(words,location);
    }
}
