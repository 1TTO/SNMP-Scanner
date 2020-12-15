package sample;

import org.soulwing.snmp.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SNMPscanner implements SnmpCallback<VarbindCollection> {
    private final ArrayList<VarbindCollection> varbindCollections;
    private final Mib mib;
    private String scanMethod = "getNext";

    SNMPscanner(List<String> mibTags) throws IOException {
        varbindCollections = new ArrayList<>();
        mib = MibFactory.getInstance().newMib();

        for (String mibTag : mibTags) {
            mib.load(mibTag);
        }
    }

    void scanNetwork(Network network, String community, List<String> tags){
        this.varbindCollections.clear();
        ArrayList<String> addresses = network.getNetworkHosts();

        for (String address : addresses) {
            new SNMPrecord(address, community, mib, this).run(tags);
        }
    }

    void scanAddress(String address, String community, List<String> tags){
        this.varbindCollections.clear();
        new SNMPrecord(address, community, mib, this).run(tags);
    }

    @Override
    public void onSnmpResponse(SnmpEvent<VarbindCollection> event) {
        synchronized (this.varbindCollections){
            try{
                varbindCollections.add(event.getResponse().get());
            }catch (org.soulwing.snmp.TimeoutException e){
                return;
            }
        }

        //updatedisplay
        event.getContext().close();
    }

    void setScanMethod(String scanMethod){this.scanMethod = scanMethod;}

    String getScanMethod(){return this.scanMethod; }

    ArrayList<VarbindCollection> getVarbindCollections(){
        return varbindCollections;
    }
}
