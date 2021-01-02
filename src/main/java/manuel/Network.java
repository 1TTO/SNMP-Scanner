package manuel;

import java.util.ArrayList;

public class Network {
    private String networkID = "";
    private int netmask;
    private final ArrayList<Address> networkHosts = new ArrayList<>();

    Network(Address singleNetworkAddress, int netmask){
        this.netmask = netmask;

        setNetworkID(singleNetworkAddress, netmask);
        setNetworkHosts(this.networkID, this.netmask, 0);
    }

    Network(Address firstAddress, Address lastAddress){
        setNetworkHosts(firstAddress, lastAddress, 0);
    }

    private void setNetworkID(Address singleNetworkAddress, int netmask){
        String[] networkParts = singleNetworkAddress.getFullAddress().split("\\.");

        for (int i = 0; i < networkParts.length; i++){
            networkParts[i] = String.format("%03d", Integer.valueOf(networkParts[i]));
        }

        for (int i = 0; i < netmask / 8; i++){
            if (i == 0) networkID = networkID.concat(networkParts[i]);
            else networkID = networkID.concat(".".concat(networkParts[i]));
        }

        for (int i = netmask; i < 32; i += (8 - i % 8)){
            int delta = 256 - (int)(Math.pow(2, 32 - i)/Math.pow(256, (3 - i / 8)));

            if (i < 8) networkID = networkID.concat(String.format("%03d",delta));
            else networkID = networkID.concat(".".concat(String.format("%03d",delta)));
        }
    }

    private void setNetworkHosts(Address firstAddress, Address lastAddress, int recI){
        String[] firstAddressParts = firstAddress.getFullAddress().split("\\.");
        String[] lastAddressParts = lastAddress.getFullAddress().split("\\.");

        if (recI == firstAddressParts.length - 1) {
            int limit;

            if (firstAddress.getFullAddress().substring(0, 11).equals(lastAddress.getFullAddress().substring(0, 11))){
                limit = Integer.parseInt(lastAddressParts[3]);
            }else limit = 255;

            for (int i = Integer.parseInt(firstAddressParts[3]); i <= limit; i++){
                String strAddr = String.join(".", firstAddressParts[0], firstAddressParts[1], firstAddressParts[2], String.format("%03d", i));
                this.getNetworkHosts().add(new Address(strAddr));
            }
        }else{
            for (int j = Integer.parseInt(firstAddressParts[recI]); j <= Integer.parseInt(lastAddressParts[recI]); j++){
                firstAddressParts[recI] = String.format("%03d", j);
                String strAddr = String.join(".", firstAddressParts[0], firstAddressParts[1], firstAddressParts[2], firstAddressParts[3]);

                setNetworkHosts(new Address(strAddr), lastAddress, recI + 1);
                for (int i = recI + 1; i < firstAddressParts.length; i++){
                    firstAddressParts[i] = "000";
                }
            }
        }
    }

    private void setNetworkHosts(String networkID, int netmask, int recI){
        String[] networkParts = networkID.split("\\.");

        if (netmask < (8 * (recI + 1))){
            if (8 * (recI + 1) != 32){
                for (int i = Integer.parseInt(networkParts[recI]); i < 256; i++){
                    networkParts[recI] = String.format("%03d", i);
                    setNetworkHosts(String.join(".", networkParts[0], networkParts[1], networkParts[2], networkParts[3]), netmask, recI + 1);
                    networkParts[recI + 1] = "000";
                }
            }else{
                for (int i = Integer.parseInt(networkParts[3]); i < 256; i++){
                    String strAddr = String.join(".",networkParts[0], networkParts[1], networkParts[2], String.format("%03d", i));
                    this.networkHosts.add(new Address(strAddr));
                }
            }
        }else setNetworkHosts(networkID, netmask, recI + 1);

        if (recI == 0){
            this.networkHosts.remove(0);
            this.networkHosts.remove(this.networkHosts.size() - 1);
        }
    }

    ArrayList<Address> getNetworkHosts(){
        return networkHosts;
    }
}
