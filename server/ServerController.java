package server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

public class ServerController {
    private ServerGUI view;
    private Server server;


    ServerController(ServerGUI v){
        view = v;
        initController();
    }

    private void initController(){
        server  = new Server(view);

        view.getDarkMode().addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                view.getContentPane().setBackground(Color.decode("#37474f"));
                view.getTextArea().setBackground(Color.decode("#62727b"));
                view.getTextArea().setForeground(Color.WHITE);
                view.getLblAddr().setForeground(Color.WHITE);
                view.getLblIndirizzoIpServer().setForeground(Color.WHITE);


            }
        });

        view.getLightMode().addItemListener((e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                view.getContentPane().setBackground(Color.decode("#f5f5f5"));
                view.getTextArea().setBackground(Color.WHITE);
                view.getTextArea().setForeground(Color.BLACK);
                view.getLblAddr().setForeground(Color.BLACK);
                view.getLblIndirizzoIpServer().setForeground(Color.BLACK);

            }
        });

        view.getExit().addActionListener(e -> {
            try {
                FileOutputStream fos =
                        new FileOutputStream("server/data/settings.ser");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(view.getAutosave().getState());
                oos.close();
                fos.close();
            }
            catch(IOException ex){
            }
            if (view.getAutosave().getState())
                server.saveData();
            System.exit(0);
        });

        view.getInfoItem().addActionListener(e ->
                JOptionPane.showMessageDialog(view,
                                      "Created by Roberto Garbarino, NotMyUid & Cristiano Olivari",
                                         "Info",
                                             JOptionPane.PLAIN_MESSAGE,
                                             new ImageIcon("server/resources/ico.png")
                                                ));

        view.getReset().addActionListener(e -> {
                    if (!server.resetData())
                        view.update("Error: could not reset data");
                    else
                        view.update("Reset data");
                }
        );

        view.getLoad().addActionListener(e -> {
                    if (server.loadData())
                        view.update("Data loaded");
                    else
                        view.update("Error loading Data");
            }

        );
        view.getSave().addActionListener(e -> {
            if (server.saveData())
                view.update("Data saved");
            else
                view.update("Error saving data");
        });

        view.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                    try {
                        FileOutputStream fos =
                                new FileOutputStream("server/data/settings.ser");
                        ObjectOutputStream oos = new ObjectOutputStream(fos);
                        oos.writeObject(view.getAutosave().getState());
                        oos.close();
                        fos.close();
                    }
                    catch(IOException e){

                    }
                if (view.getAutosave().getState()){
                    server.saveData();
                    System.out.println("qua");
                }

                System.exit(0);
            }
        });
        view.addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent we) {
                boolean res = false;
                try {
                    FileInputStream hm = new FileInputStream("server/data/settings.ser");
                    ObjectInputStream ois = new ObjectInputStream(hm);
                    res = (Boolean) ois.readObject();
                    ois.close();
                    hm.close();
                }
                catch (IOException | ClassNotFoundException e){

                }
                view.getAutosave().setState(res);

                if(res)
                    server.loadData();

            }
        });
    }
}
