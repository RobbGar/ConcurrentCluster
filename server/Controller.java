package server;

public class Controller {
    private ServerGUI view;


    public Controller(ServerGUI v){
        view = v;
        initController();
    }

    private void initController(){
        view.getExitBtn().addActionListener(e ->
                System.exit(0)
        );
    }

}
