package server;

import java.util.concurrent.Callable;

public class CallPrint implements Callable<String> {

    private Data data;
    private String location;

    public CallPrint(Data database, String location) {
        this.data = database;
        this.location = location;
    }
    @Override
    public String call() {
        return data.MostSearchedW(location);
    }
}
