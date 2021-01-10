package manuel;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.soulwing.snmp.*;

import java.util.ArrayList;

public class SNMPscanner implements SnmpCallback<VarbindCollection> {
    private final ArrayList<VarbindCollection> varbindCollections;
    private final ObservableList<SNMPrecordItem> snmpRecordItems;
    private final Mib mib;
    private String scanMethod = "getNext";

    /**
     * Returns a SNMPscanner object
     * @param mib MIB object
     */
    SNMPscanner(Mib mib){
        snmpRecordItems = FXCollections.observableArrayList();
        varbindCollections = new ArrayList<>();
        this.mib = mib;
        Controller.staticResultListView.setItems(snmpRecordItems);
    }

    /**
     * Scans a network for SNMP information
     * @param network Network object with all the hosts
     * @param community String of community: public/private
     * @param tags ArrayList of Strings with OIDs
     */
    void scanNetwork(Network network, String community, ArrayList<String> tags){
        this.varbindCollections.clear();
        ArrayList<Address> addresses = network.getNetworkHosts();

        for (Address address : addresses) {
            new SNMPrecord(address.getFullAddress(), community, mib, this).run(tags);
        }
    }

    /**
     * Scans single address for SNMP-information
     * @param address Address object to scan
     * @param community String of community: public/private
     * @param tags ArrayList of Strings with OIDs
     */
    void scanAddress(String address, String community, ArrayList<String> tags){
        this.varbindCollections.clear();
        new SNMPrecord(address, community, mib, this).run(tags);
    }

    /**
     * Function which will be called after a result of the scan is provided
     * @param event SnmpEvent containing the result
     */
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

    /**
     * Sets the scanMethod
     * @param scanMethod scanMethod get/getNext
     */
    void setScanMethod(String scanMethod){this.scanMethod = scanMethod;}

    /**
     * Returns the ScanMethod
     * @return String with scanMethod get/getNext
     */
    String getScanMethod(){return this.scanMethod; }

    /**
     * Returns the VarbindCollections of the Scan
     * @return ArrayList of Varbindcollection containing the scan-results
     */
    ArrayList<VarbindCollection> getVarbindCollections(){
        return varbindCollections;
    }

    /**
     * Returns the SNMPrecordItems for the ListView
     * @return ObservableList of SNMPrecordItem
     */
    ObservableList<SNMPrecordItem> getSNMPrecordItems(){
        return snmpRecordItems;
    }
}
