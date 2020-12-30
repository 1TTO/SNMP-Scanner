package manuel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//http://www.net-snmp.org/docs/mibs/
//https://github.com/soulwing/tnm4j

public class Main extends Application {
    public static SNMPscanner scanner;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/ui.fxml"));

        primaryStage.setTitle("SNMP Scanner");
        primaryStage.setScene(new Scene(root, 370, 440));
        primaryStage.show();


    }


    public static void main(String[] args) {
        launch(args);
    }
}
