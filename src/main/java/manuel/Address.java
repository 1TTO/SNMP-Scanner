package manuel;

public class Address {
    private final String address,fullAddress;

    /**
     * Returns an Address object
     * @param address String which needs to be an Address
     * @throws IllegalArgumentException if the String provided isn't an address
     */
    Address(String address){
        if (!isAddress(address)) throw new IllegalArgumentException();

        this.address = address;
        this.fullAddress = getFullAddress(address);
    }

    /**
     * Checks if a String can be an address
     * @param address a string which possibly can be an address object
     * @return        If a string can be converted to an address object or not
     */
    static boolean isAddress(String address){
        int blocks = 0;

        for (int i = 0; i < address.length(); i++){
            if (address.charAt(i) == '.') blocks++;
            else{
                try{
                    Integer.parseInt(String.valueOf(address.charAt(i)));
                }catch (Exception ex){
                    return false;
                }
            }
        }

        return blocks == 3;
    }

    /**
     * Returns the address as string with 3 numeric chars per block
     * e.g. 10.10.30.1 -> 010.010.030.001
     * @param address String that has to be a valid address
     * @return String with of address with 3 numeric chars per block
     */
    private static String getFullAddress(String address){
        String[] addressParts = address.split("\\.");
        String ret = "";

        for (int i = 0; i < addressParts.length; i++){
            if (i == 0) ret = String.format("%03d", Integer.parseInt(addressParts[i]));
            else ret = ret.concat(".").concat(String.format("%03d", Integer.parseInt(addressParts[i])));
        }
        return ret;
    }

    /**
     * Returns the address as string
     * @return Address as String
     */
    String getAddress(){return address;}

    /**
     * Returns the full address with 3 numeric chars per block
     * e.g. 10.10.30.1 -> 010.010.030.001
     * @return full address as String
     */
    String getFullAddress(){return fullAddress;}
}
