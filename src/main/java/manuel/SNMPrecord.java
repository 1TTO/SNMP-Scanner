package manuel;

import org.soulwing.snmp.*;

import java.util.ArrayList;

public class SNMPrecord{
    SimpleSnmpV2cTarget target;
    SnmpContext context;
    SNMPscanner scanner;

    /**
     * Returns an SNMPrecord object
     * @param ipAddress String of address which needs to be scanned
     * @param community community tag: public/private
     * @param mib MIB object
     * @param scanner SNMPScanner object
     */
    SNMPrecord(String ipAddress, String community, Mib mib, SNMPscanner scanner){
        this.scanner = scanner;

        target = new SimpleSnmpV2cTarget();
        target.setAddress(ipAddress);
        target.setCommunity(community);

        context = SnmpFactory.getInstance().newContext(target, mib);
    }

    /**
     * Scans address with given method asynchronously
     * @param tags ArrayList of Strings with the OIDs
     */
    public void run(ArrayList<String> tags) {
        try {
            if (scanner.getScanMethod().equals("get")){
                context.asyncGet(scanner, tags);
            }else if (scanner.getScanMethod().equals("getNext")) {
                context.asyncGetNext(scanner, tags);
            }
        }catch (Exception ignored){}
    }
}
