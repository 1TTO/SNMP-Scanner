package manuel;

import java.util.ArrayList;

public class Network {
    private String networkID = "";
    private int netmask;
    private final ArrayList<Address> networkHosts = new ArrayList<>();

    /**
     * Returns a Network object which contains all hosts, the netmask (if provided)
     * and the networkID
     * @param singleNetworkAddress Address object of an address which is in the network
     * @param netmask Integer of the netmask in decimal; e.g. 24
     */
    Network(Address singleNetworkAddress, int netmask){
        this.netmask = netmask;

        setNetworkID(singleNetworkAddress, netmask);
        setNetworkHosts(this.networkID, this.netmask, 0);
    }

    /**
     * Returns a Network object which contains all hosts, the netmask (if provided)
     * and the networkID
     * @param firstAddress Address object of the first included address in the network
     * @param lastAddress Address object of the last included address in the network
     */
    Network(Address firstAddress, Address lastAddress){
        setNetworkHosts(firstAddress, lastAddress, 0);
    }

    /**
     * Sets the networkID of the network object
     * @param singleNetworkAddress Address object of an address which is in the network
     * @param netmask Integer of the netmask e.g. 24
     */
    private void setNetworkID(Address singleNetworkAddress, int netmask){
        String[] networkParts = singleNetworkAddress.getFullAddress().split("\\.");

        //As long i is smaller as the netmask/8, we can be sure, that the block is part of the networkID
        for (int i = 0; i < netmask / 8; i++){
            if (i == 0) networkID = networkID.concat(networkParts[i]);
            else networkID = networkID.concat(".".concat(networkParts[i]));
        }

        //After the blocks have been added to the networkID, we need to calculate where the subnet exactly starts
        //As long i is smaller as 32 (Netmask max) we can add (8 - i%8); so that if we would have 18 as netmask;
        //The next iteration would be 24 and therefore it is easier, because we can simply add 000 at the end
        for (int i = netmask; i < 32; i += (8 - i % 8)){
            //256 = max addresses per block;
            //18 netmask in 3rd block = 2^(32-18)/256^(3-i/8); 3 (0;1;2;3) because of the amount of blocks = 0.0.0.0
            //= 256 - 16.384 / 256 = 192
            //Now we know that the third block starts with 192 -> 10.10.192.0
            int delta = 256 - (int)(Math.pow(2, 32 - i)/Math.pow(256, (3 - i / 8)));

            if (i < 8) networkID = networkID.concat(String.format("%03d",delta));
            else networkID = networkID.concat(".".concat(String.format("%03d",delta)));
        }
    }

    /**
     * Sets all hosts of the network to the arraylist of addresses of the instance
     * @param firstAddress Address object of the first included address in the network
     * @param lastAddress Address object of the last included address in the network
     * @param recI Recursive i-counter for function; starts always with 0
     */
    private void setNetworkHosts(Address firstAddress, Address lastAddress, int recI){
        String[] firstAddressParts = firstAddress.getFullAddress().split("\\.");
        String[] lastAddressParts = lastAddress.getFullAddress().split("\\.");

        //if the current block is the last
        if (recI == firstAddressParts.length - 1) {
            int limit;

            //if the 3 first blocks are the same we can simply say, that the limit is the last number of the second address
            if (firstAddress.getFullAddress().substring(0, 11).equals(lastAddress.getFullAddress().substring(0, 11))){
                limit = Integer.parseInt(lastAddressParts[3]);
            //else we need to loop to the end, because we need to recall this; e.g. 10.10.29.1 & 10.10.30.20
            //we loop till the end of 10.10.29, to then recall this function to proceed with 10.10.30 until 20
            }else limit = 255;

            //Add the addresses to networkHosts List starting from the first to the last
            for (int i = Integer.parseInt(firstAddressParts[3]); i <= limit; i++){
                String strAddr = String.join(".", firstAddressParts[0], firstAddressParts[1], firstAddressParts[2], String.format("%03d", i));
                this.getNetworkHosts().add(new Address(strAddr));
            }
        }else{
            //As long the current address-block of the first and second address doesnt match we add them from the beginning of
            //block[i] of address one till block[i] of address two
            for (int j = Integer.parseInt(firstAddressParts[recI]); j <= Integer.parseInt(lastAddressParts[recI]); j++){
                firstAddressParts[recI] = String.format("%03d", j);
                String strAddr = String.join(".", firstAddressParts[0], firstAddressParts[1], firstAddressParts[2], firstAddressParts[3]);

                //the function gets recalled to start with the next block; if there is
                setNetworkHosts(new Address(strAddr), lastAddress, recI + 1);
                //after the function has been called
                //e.g. with 10.10.29.30
                //we need to go on with 10.10.30.0 and not with .30
                for (int i = recI + 1; i < firstAddressParts.length; i++){
                    firstAddressParts[i] = "000";
                }
            }
        }
    }

    /**
     * Sets all hosts of the network to the arraylist of addresses of the instance
     * @param networkID String of the networkID which previously has been set with the function setNetworkID()
     * @param netmask Integer of the netmask; e.g. 24
     * @param recI Recursive i-counter for function; starts always with 0
     */
    private void setNetworkHosts(String networkID, int netmask, int recI){
        String[] networkParts = networkID.split("\\.");

        //if the netmask is smaller then the block-netmask e.g. 10 < then netmask of second block (0;1) 10 < 8+8
        if (netmask < (8 * (recI + 1))){
            if (8 * (recI + 1) != 32){
                //start from the current-block-value e.g. second block 10.10.30.1 = 10 and loop till 255 and add the address
                for (int i = Integer.parseInt(networkParts[recI]); i < 256; i++){
                    networkParts[recI] = String.format("%03d", i);
                    setNetworkHosts(String.join(".", networkParts[0], networkParts[1], networkParts[2], networkParts[3]), netmask, recI + 1);
                    //since we are sure that we are not in the last block, we need to set this block back to 000
                    //e.g. 10.10.30.1 block = 3
                    //first loop = 10.10.30.1
                    //second loop = 10.10.31.000
                    networkParts[recI + 1] = "000";
                }
            }else{
                //if the current block is the last block we can simply start from the value of the address and loop until 255
                for (int i = Integer.parseInt(networkParts[3]); i < 256; i++){
                    String strAddr = String.join(".",networkParts[0], networkParts[1], networkParts[2], String.format("%03d", i));
                    this.networkHosts.add(new Address(strAddr));
                }
            }
        // if the netmask is bigger then the block-netmask, we can be sure, that this part is always part of the address
        // e.g. 10.10.30.1/16; we know that 10.10. will always be part of all hosts, so we can go to the next block
        }else setNetworkHosts(networkID, netmask, recI + 1);

        //At the end we need to remove the netmask and broadcast-address from the host list
        if (recI == 0){
            this.networkHosts.remove(0);
            this.networkHosts.remove(this.networkHosts.size() - 1);
        }
    }

    /**
     * Returns all hosts in the network
     * @return ArrayList of Address of hosts in the network
     */
    ArrayList<Address> getNetworkHosts(){
        return networkHosts;
    }
}
