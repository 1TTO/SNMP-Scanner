package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.soulwing.snmp.*;

import java.util.Arrays;

//http://www.net-snmp.org/docs/mibs/
//https://github.com/soulwing/tnm4j

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        BorderPane root = new BorderPane();
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

        Mib mib = MibFactory.getInstance().newMib();
        mib.load("SNMPv2-MIB");
        mib.load("IF-MIB");
        mib.load("IP-MIB");
        mib.load("HOST-RESOURCES-MIB");

        SimpleSnmpV2cTarget target = new SimpleSnmpV2cTarget();
        target.setAddress("10.10.30.254");
        target.setCommunity("public");

        SnmpContext context = SnmpFactory.getInstance().newContext(target, mib);
        String columns[] = {"sysDescr", "sysUpTime", "sysContact", "sysName",
                "sysLocation", "ipAdEntAddr", "hrStorageSize"};

        try {
            VarbindCollection varbinds = context.getNext(columns).get();

            for (Varbind varbind : varbinds) {
                System.out.println(varbind.toString() + " : " + varbind.getOid());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            context.close();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
