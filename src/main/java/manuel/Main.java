package manuel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//http://www.net-snmp.org/docs/mibs/
//https://github.com/soulwing/tnm4j

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/ui.fxml"));
        primaryStage.setTitle("SNMP Scanner");
        primaryStage.setScene(new Scene(root, 370, 440));
        primaryStage.show();

        try{
            SNMPscanner scanner = new SNMPscanner(File.getCSVContent(File.MIB_FILE_PATH));
            //scanner.scanNetwork(new Network("10.10.30.208", 24), "public", File.getCSVContent(File.OID_FILE_PATH));
            scanner.scanNetwork(new Network("10.10.29.208", "10.10.30.201"), "public", File.getCSVContent(File.OID_FILE_PATH));
            //scanner.scanAddress("10.10.30.208", "public", File.getCSVContent(File.OID_FILE_PATH));

            //Thread.sleep(30000);
            for (int i = 0; i < scanner.getVarbindCollections().size(); i++){
                System.out.println(scanner.getVarbindCollections().get(i).get(5).toString());
            }
        }catch (Exception e){
            System.out.println("Could not create MIB!");
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
