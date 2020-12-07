package sample;

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
