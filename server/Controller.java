package server;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Controller {
    private ServerGUI view;
    private Server server;


    public Controller(ServerGUI v){
        view = v;
        initController();
    }

    private void initController(){
        server  = new Server(view);

        view.getRstBtn().addActionListener(e -> {
                    if (!server.resetData())
                        view.update("Error: could not reset data");
                    else
                        view.update("Reset data");
                }
        );
        view.getLoadBtn().addActionListener(e -> {
                    if (server.loadData())
                        view.update("Data loaded");
                    else
                        view.update("Error loading Data");
            }

        );
        view.getSaveBtn().addActionListener(e -> {
            if (server.saveData())
                view.update("Data saved");
            else
                view.update("Error saving data");
        });

        view.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
    }

}
