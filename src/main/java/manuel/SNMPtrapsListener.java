package manuel;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.soulwing.snmp.*;

public class SNMPtrapsListener {
    private final SnmpListener listener;
    private final ObservableList<SNMPtrapsListenerItem> snmpTrapsListenerItems;

    /**
     * Returns a SNMPtrapsListener object
     * @param mib MIB object
     * @param port listenerPort as Integer
     */
    SNMPtrapsListener(Mib mib, int port) {
        snmpTrapsListenerItems = FXCollections.observableArrayList();
        this.listener = SnmpFactory.getInstance().newListener(port, mib);

        Platform.runLater(()->{
            Controller.staticTrapsListView.setItems(snmpTrapsListenerItems);
        });

        run();
    }

    /**
     * Starts the SNMPtrapListener
     */
    void run(){
        listener.addHandler(trap -> {
            Platform.runLater(()->{
                snmpTrapsListenerItems.add(new SNMPtrapsListenerItem(trap));
            });
            return true;
        });
    }

    /**
     * Stops the SNMPtrapListener
     */
    void stop(){
        listener.close();
    }
}
