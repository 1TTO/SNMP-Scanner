package sample;

import org.soulwing.snmp.*;

public class SNMPrecord{
    SimpleSnmpV2cTarget target;
    SnmpContext context;
    SNMPscanner scanner;

    SNMPrecord(String ipAddress, String community, Mib mib, SNMPscanner scanner){
        this.scanner = scanner;

        target = new SimpleSnmpV2cTarget();
        target.setAddress(ipAddress);
        target.setCommunity(community);

        context = SnmpFactory.getInstance().newContext(target, mib);
    }

    public void run(String[] tags) {
        try {
            if (scanner.getScanMethod().equals("get")){
                context.asyncGet(scanner, tags);
            }else if (scanner.getScanMethod().equals("getNext")) {
                context.asyncGetNext(scanner, tags);
            }
        }catch (IllegalArgumentException ignored){}
    }
}
