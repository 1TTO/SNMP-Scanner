package manuel;

import java.util.ArrayList;

public class Network {
    private String networkID = "";
    private int subnetmask;
    private ArrayList<String> networkHosts = new ArrayList<>();

    Network(String singleNetworkAddress, int subnetmask){
        this.subnetmask = subnetmask;

        setNetworkID(singleNetworkAddress, subnetmask);
        setNetworkHosts(this.networkID, this.subnetmask, 0);
    }

    Network(String firstAddress, String lastAddress){
        setNetworkHosts(getFullAddress(firstAddress), getFullAddress(lastAddress), 0);
    }

    private void setNetworkID(String singleNetworkAddress, int subnetmask){
        String[] networkParts = singleNetworkAddress.split("\\.");

        for (int i = 0; i < networkParts.length; i++){
            networkParts[i] = String.format("%03d", Integer.valueOf(networkParts[i]));
        }

        for (int i = 0; i < subnetmask / 8; i++){
            if (i == 0) networkID = networkID.concat(networkParts[i]);
            else networkID = networkID.concat(".".concat(networkParts[i]));
        }

        for (int i = subnetmask; i < 32; i += (8 - i % 8)){
            int delta = 256 - (int)(Math.pow(2, 32 - i)/Math.pow(256, (3 - i / 8)));

            if (i < 8) networkID = networkID.concat(String.format("%03d",delta));
            else networkID = networkID.concat(".".concat(String.format("%03d",delta)));
        }
    }

    private String getFullAddress(String address){
        String[] addressParts = address.split("\\.");
        String ret = "";

        for (int i = 0; i < addressParts.length; i++){
            if (i == 0) ret = String.format("%03d", Integer.parseInt(addressParts[i]));
            else ret = ret.concat(".").concat(String.format("%03d", Integer.parseInt(addressParts[i])));
        }
        return ret;
    }

    private void setNetworkHosts(String firstAddress, String lastAddress, int recI){
        String[] firstAddressParts = firstAddress.split("\\.");
        String[] lastAddressParts = lastAddress.split("\\.");

        if (recI == firstAddressParts.length - 1) {
            int limit;

            if (firstAddress.substring(0, 11).equals(lastAddress.substring(0, 11))) limit = Integer.parseInt(lastAddressParts[3]);
            else limit = 255;

            for (int i = Integer.parseInt(firstAddressParts[3]); i <= limit; i++){
                this.getNetworkHosts().add(String.join(".", firstAddressParts[0], firstAddressParts[1], firstAddressParts[2], String.format("%03d", i)));
            }
        }else{
            for (int j = Integer.parseInt(firstAddressParts[recI]); j <= Integer.parseInt(lastAddressParts[recI]); j++){
                firstAddressParts[recI] = String.format("%03d", j);

                setNetworkHosts( String.join(".", firstAddressParts[0], firstAddressParts[1], firstAddressParts[2], firstAddressParts[3]), lastAddress, recI + 1);
                for (int i = recI + 1; i < firstAddressParts.length; i++){
                    firstAddressParts[i] = "000";
                }
            }
        }
    }

    private void setNetworkHosts(String networkID, int subnetmask, int recI){
        String[] networkParts = networkID.split("\\.");

        if (subnetmask < (8 * (recI + 1))){
            if (8 * (recI + 1) != 32){
                for (int i = Integer.parseInt(networkParts[recI]); i < 256; i++){
                    networkParts[recI] = String.format("%03d", i);
                    setNetworkHosts(String.join(".", networkParts[0], networkParts[1], networkParts[2], networkParts[3]), subnetmask, recI + 1);
                    networkParts[recI + 1] = "000";
                }
            }else{
                for (int i = Integer.parseInt(networkParts[3]); i < 256; i++){
                    this.networkHosts.add(String.join(".",networkParts[0], networkParts[1], networkParts[2], String.format("%03d", i)));
                }
            }
        }else setNetworkHosts(networkID, subnetmask, recI + 1);

        if (recI == 0){
            this.networkHosts.remove(0);
            this.networkHosts.remove(this.networkHosts.size() - 1);
        }
    }

    ArrayList<String> getNetworkHosts(){
        return networkHosts;
    }
}
