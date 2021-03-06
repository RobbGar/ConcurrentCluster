package server;

import java.util.concurrent.Callable;

public class TopThree implements Callable<String> {

    private Data data;
    private String location;

    public TopThree(Data database, String location) {
        this.data = database;
        this.location = location;
    }

    @Override
    public String call(){
        return data.getTopThree(location);
    }
}
