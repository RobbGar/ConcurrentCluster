package server;

public class Controller {
    private ServerGUI view;
    private Server server;


    public Controller(ServerGUI v){
        view = v;
        initController();
    }

    private void initController(){
        server  = new Server(view);
        view.getExitBtn().addActionListener(e ->
                System.exit(0)
        );
        view.getLoadBtn().addActionListener(e ->
                server.loadData()
        );
        view.getSaveBtn().addActionListener(e ->
                server.saveData()
        );
    }

}
