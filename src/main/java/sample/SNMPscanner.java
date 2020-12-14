package sample;

import org.soulwing.snmp.*;

import java.io.IOException;
import java.util.ArrayList;

public class SNMPscanner implements SnmpCallback<VarbindCollection> {
    private final ArrayList<VarbindCollection> varbindCollections;
    private final Mib mib;

    SNMPscanner() throws IOException {
        varbindCollections = new ArrayList<>();

        mib = MibFactory.getInstance().newMib();
        mib.load("SNMPv2-MIB");
        mib.load("IF-MIB");
        mib.load("IP-MIB");
        mib.load("HOST-RESOURCES-MIB");
    }

    void scanNetwork(Network network, String community, String[] tags){
        this.varbindCollections.clear();
        ArrayList<String> addresses = network.getNetworkHosts();

        for (int i = 0; i < addresses.size(); i++){
            new SNMPrecord(addresses.get(i), community, mib, this).run(tags);
        }
    }

    ArrayList<VarbindCollection> getVarbindCollections(){
        return varbindCollections;
    }

    void scanAddress(String address, String community, String[] tags){
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
}
