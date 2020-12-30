package manuel;

public class Address {
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
}
