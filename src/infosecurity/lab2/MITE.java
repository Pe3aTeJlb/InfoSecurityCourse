package infosecurity.lab2;

import infosecurity.util.RabinMiller;
import java.math.BigInteger;

public class MITE {

    private String name;

    private BigInteger g, p;
    private BigInteger key;

    public MITE(String name, BigInteger g, BigInteger p){

        this.name = name;
        this.g = g;
        this.p = p;
        this.key = RabinMiller.getPrime();

        System.out.println(name + " g " + g + " p " + p + " key "+ this.key);

    }

    public BigInteger getPartKey(){

        BigInteger partKey = g.modPow(key,p);

        System.out.println(name+" part key is "+partKey);
        return partKey;

    }

    public BigInteger getFullKey(BigInteger partKey){

        BigInteger fullKey = partKey.modPow(key,p);
        System.out.println(name+" full key is "+fullKey);
        return fullKey;

    }

    public void setNewKey(){
        this.key = RabinMiller.getPrime();
    }

}
