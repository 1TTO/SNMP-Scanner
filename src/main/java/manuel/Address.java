package manuel;

public class Address {
    private final String address,fullAddress;

    Address(String address){
        if (!isAddress(address)) throw new IllegalArgumentException();

        this.address = address;
        this.fullAddress = getFullAddress(address);
    }


    static boolean isAddress(String address){
        int count = 0;

        for (int i = 0; i < address.length(); i++){
            if (address.charAt(i) == '.') count++;
            else{
                try{
                    Integer.parseInt(String.valueOf(address.charAt(i)));
                }catch (Exception ex){
                    return false;
                }
            }
        }

        return count == 3;
    }

    private static String getFullAddress(String address){
        String[] addressParts = address.split("\\.");
        String ret = "";

        for (int i = 0; i < addressParts.length; i++){
            if (i == 0) ret = String.format("%03d", Integer.parseInt(addressParts[i]));
            else ret = ret.concat(".").concat(String.format("%03d", Integer.parseInt(addressParts[i])));
        }
        return ret;
    }

    String getAddress(){return address;}
    String getFullAddress(){return fullAddress;}
}
