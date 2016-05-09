package sinc.hinc.clientgui;

import com.aquafx_project.AquaFx;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

//    @FXML
//    private TabPane mainTabPane;
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainPanel.fxml"));

//        mainTabPane.getTabs().get(0).setGraphic(new Label("Global manager"));
//        AquaFx.style();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/MistSilverSkin.css");

        stage.setTitle("IoT Resource Management");
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
        launch(args);
    }

}
