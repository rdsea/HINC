package sinc.hinc.clientgui;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainPanel.fxml"));

        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/MistSilverSkin.css");

        stage.setTitle("HINC 1.0");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application. main() serves only as fallback in case the application can not be launched through
     * deployment artifacts, e.g., in IDEs with limited FX support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Starting HINC GUI...");
        
        int num = args.length;
        if (num==1){
            System.out.println("Get one parameter, should be the IP: " + args[0]);
            UserSettings.setIp(args[0]);
        } else if (num==2){
            System.out.println("Get one parameter, should be the IP and port: " + args[0] +" " + args[1]);
            UserSettings.setIp(args[0]);
            UserSettings.setPort(args[1]);
        } else if (num>2){
            System.out.println("Invalid parameters. The client can get maximum 2 parameters: HINC_Global_IP and HINC_Global_Port");
            return;
        }
        System.out.println("HINC Global is set to: " + UserSettings.getIp() + ":" +UserSettings.getPort());
        launch(args);
    }

}
