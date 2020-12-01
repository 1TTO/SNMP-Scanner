package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.soulwing.snmp.*;

import java.util.concurrent.ExecutorService;

//http://www.net-snmp.org/docs/mibs/
//https://github.com/soulwing/tnm4j

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        BorderPane root = new BorderPane();
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

        String[] tags = {"sysDescr", "sysUpTime", "sysContact", "sysName", "sysLocation", "ipAdEntAddr", "hrStorageSize"};
        String[] ipAddresses = {"10.10.30.254", "10.10.30.208"};
        ExecutorService ex;

        Mib mib = MibFactory.getInstance().newMib();
        mib.load("SNMPv2-MIB");
        mib.load("IF-MIB");
        mib.load("IP-MIB");
        mib.load("HOST-RESOURCES-MIB");

        for (int i = 0; i < ipAddresses.length; i++){
            System.out.println("-------------------------------------------------");
            SNMPrecord sr = new SNMPrecord(ipAddresses[i], "public", mib);
            VarbindCollection vr = sr.getVarbindsByTagName(tags);
            sr.close();

            for (Varbind v : vr){
                System.out.println(v.toString());
            }
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
