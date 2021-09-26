package infosecurity.lab2;

import java.math.BigInteger;
import java.util.ArrayList;

public class MITM {

    private BigInteger g, p;
    private ArrayList<BigInteger> keys = new ArrayList<>();

    public MITM(BigInteger g, BigInteger p){

        this.g = g;
        this.p = p;

    }

    public void interceptMsg(String msg){
        System.out.println(msg);
    }

    public void interceptKey(BigInteger key){
        keys.add(key);
        System.out.println("intersepted keys:"+keys);
    }

}
