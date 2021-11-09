package infosecurity.util;

import java.util.ArrayList;

public class Test {

    public static void main(String[] args) {

        //System.out.println(RabinMiller.getPrime());
        //System.out.println(SHA256.encrypt("hello world"));
        //System.out.println(Hash.encrypt("hello word"));

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

}
