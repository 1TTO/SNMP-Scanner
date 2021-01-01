package manuel;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.soulwing.snmp.*;

public class SNMPtrapsListener {
    private final SnmpListener listener;
    private final ObservableList<SNMPtrapsListenerItem> snmpTrapsListenerItems;

    SNMPtrapsListener(Mib mib) {
        snmpTrapsListenerItems = FXCollections.observableArrayList();
        this.listener = SnmpFactory.getInstance().newListener(162, mib);

        Platform.runLater(()->{
            Controller.staticTrapsListView.setItems(snmpTrapsListenerItems);
        });

        run();
    }

    void run(){
        listener.addHandler(trap -> {
            Platform.runLater(()->{
                snmpTrapsListenerItems.add(new SNMPtrapsListenerItem(trap));
            });
            return true;
        });
    }

    void stop(){
        listener.close();
    }
}
