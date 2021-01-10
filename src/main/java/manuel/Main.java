package manuel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.soulwing.snmp.Mib;
import org.soulwing.snmp.MibFactory;

import java.io.IOException;
import java.util.ArrayList;

//http://www.net-snmp.org/docs/mibs/
//https://github.com/soulwing/tnm4j
//https://sourceforge.net/projects/net-snmp/files/net-snmp%20binaries/5.5-binaries/
//https://knowledge.broadcom.com/external/article/57331/how-to-manually-generate-traps-via-windo.html

public class Main extends Application {
    public static SNMPscanner scanner;
    public static SNMPtrapsListener listener;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/ui.fxml"));

        primaryStage.setTitle("SNMP Scanner");
        primaryStage.setScene(new Scene(root, 370, 440));
        primaryStage.show();

        listener = new SNMPtrapsListener(getMib(), 162);
    }

    /**
     * Function which will be called when the program will be closed to stop the SNMPtrapListener
     */
    @Override
    public void stop(){
        listener.stop();
    }

    /**
     * Static method to load the MIB object which is the same for the whole file
     * @return a MIB object
     * @throws IOException when the file with the mib-content couldn't be found
     */
    static Mib getMib() throws IOException {
        ArrayList<String> mibFileContent;

        try{
            mibFileContent = File.getCSVContent(File.MIB_FILE_PATH);
        }catch (IOException ex){
            File.createExternalFiles();
            mibFileContent = File.getCSVContent(File.MIB_FILE_PATH);
        }

        Mib mib = MibFactory.getInstance().newMib();

        for (String mibTag : mibFileContent) {
            mib.load(mibTag);
        }

        return mib;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
