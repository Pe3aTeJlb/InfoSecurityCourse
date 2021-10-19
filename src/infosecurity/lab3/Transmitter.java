package infosecurity.lab3;

import java.math.BigInteger;
import java.util.ArrayList;

public class Transmitter {

    private BigInteger exponent, module;

    public Transmitter(BigInteger[] publicKey){
        this.exponent = publicKey[0];
        this.module = publicKey[1];
    }

    public ArrayList<BigInteger> encryptMessage(String msg){

        ArrayList<BigInteger> encryptedCodes = new ArrayList<>();

        StringBuilder builder = new StringBuilder(msg);
        for(int i = 0; i < builder.length(); i++){
            encryptedCodes.add(new BigInteger(Integer.toString(builder.charAt(i))).modPow(exponent,module));
        }

        return encryptedCodes;

    }

}
