package manuel;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.soulwing.snmp.*;

import java.io.IOException;
import java.util.ArrayList;

public class SNMPscanner implements SnmpCallback<VarbindCollection> {
    private final ArrayList<VarbindCollection> varbindCollections;
    private final ObservableList<SNMPrecordItem> snmpRecordItems;
    private final Mib mib;
    private String scanMethod = "getNext";

    SNMPscanner(Mib mib) throws IOException {
        snmpRecordItems = FXCollections.observableArrayList();
        varbindCollections = new ArrayList<>();
        this.mib = mib;
        Controller.staticResultListView.setItems(snmpRecordItems);
    }

    void scanNetwork(Network network, String community, ArrayList<String> tags){
        this.varbindCollections.clear();
        ArrayList<String> addresses = network.getNetworkHosts();

        for (String address : addresses) {
            new SNMPrecord(address, community, mib, this).run(tags);
        }
    }

    void scanAddress(String address, String community, ArrayList<String> tags){
        this.varbindCollections.clear();
        new SNMPrecord(address, community, mib, this).run(tags);
    }

    @Override
    public void onSnmpResponse(SnmpEvent<VarbindCollection> event) {
        synchronized (this.varbindCollections){
            try{
                varbindCollections.add(event.getResponse().get());
                Platform.runLater(()->{
                    snmpRecordItems.add(new SNMPrecordItem(event.getResponse().get()));
                });
            }catch (org.soulwing.snmp.TimeoutException e){
                return;
            }
        }

        event.getContext().close();
    }

    void setScanMethod(String scanMethod){this.scanMethod = scanMethod;}

    String getScanMethod(){return this.scanMethod; }

    ArrayList<VarbindCollection> getVarbindCollections(){
        return varbindCollections;
    }

    ObservableList<SNMPrecordItem> getSNMPrecordItems(){
        return snmpRecordItems;
    }
}
