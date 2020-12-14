package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

//http://www.net-snmp.org/docs/mibs/
//https://github.com/soulwing/tnm4j

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        BorderPane root = new BorderPane();
        primaryStage.setTitle("SNMP Scanner");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

        String[] tags = {"sysDescr", "sysUpTime", "sysContact", "sysName", "sysLocation", "ipAdEntAddr", "hrStorageSize"};
        String[] mibtags = {"SNMPv2-MIB", "IF-MIB", "IP-MIB", "HOST-RESOURCES-MIB"};

        try{
            SNMPscanner scanner = new SNMPscanner(mibtags);
            //scanner.scanNetwork(new Network("10.10.30.208", 24), "public", tags);
            scanner.scanNetwork(new Network("10.10.29.208", "10.10.30.201"), "public", tags);
            //scanner.scanAddress("10.10.30.208", "public", tags);

            Thread.sleep(30000);
            for (int i = 0; i < scanner.getVarbindCollections().size(); i++){
                System.out.println(scanner.getVarbindCollections().get(i).get(5).toString());
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Could not create MIB!");
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
