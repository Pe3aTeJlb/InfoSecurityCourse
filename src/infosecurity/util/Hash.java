package infosecurity.util;

import java.math.BigInteger;
import java.util.ArrayList;

public class Hash {

    private static BigInteger len;

    public static void main(String[] args) {

        ArrayList<String> list = new ArrayList<>();
        int c = 0;

        for(int i = 0; i < 100; i++){
            String s = Hash.hash(Integer.toString(i));
            if(list.contains(s)) c++;
            list.add(s);
            System.out.println(s);
        }

        System.out.println(c+" Collisions detected");

    }

    public static String hash(String message){

       // System.out.println("Hash input message : " + message);

        StringBuilder msg = new StringBuilder(message);

        return format(msg).substring(23,87);

    }

    private static String format(StringBuilder msg){

      //  System.out.println("Format"+"\n");

        msg = toBin(msg);
        len = BigInteger.valueOf(msg.toString().length());

        BigInteger val = new BigInteger(msg.toString(), 2);
        val = val.pow(len.intValue());

        for(int i = 0; i < 5; i++){

            if(i % 2 == 0) {
                val = val.xor(len.modPow(len, BigInteger.valueOf(3)));
            }else{
                val = val.pow(len.mod(val).intValue());
            }

        }

        return val.toString(16);

    }

    private static StringBuilder toBin(StringBuilder msg){

        StringBuilder buff = new StringBuilder();

        for(int i = 0; i < msg.length(); i++){
            String bin = String.format("%8s", Integer.toBinaryString(msg.charAt(i))).replace(" ","0");
           // System.out.println(msg.charAt(i)+" "+bin);
            buff.append(bin);
        }

        return buff;

    }

}
