package sample;

import org.soulwing.snmp.*;

public class SNMPrecord {
    Mib mib;
    SimpleSnmpV2cTarget target;
    SnmpContext context;

    SNMPrecord(String ipAddress, String community, Mib mib){
        this.mib = mib;

        target = new SimpleSnmpV2cTarget();
        target.setAddress(ipAddress);
        target.setCommunity(community);

        context = SnmpFactory.getInstance().newContext(target, mib);
    }

    VarbindCollection getVarbindsByTagName(String[] tagNames){
        return context.getNext(tagNames).get();
    }

    void close(){
        context.close();
    }
}
