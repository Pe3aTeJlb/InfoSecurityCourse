package infosecurity.lab2;

import infosecurity.util.RabinMiller;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

public class Lab2 {

    public static void main(String[] args) {

        ArrayList<MITE> users = new ArrayList<>();

        BigInteger g = RabinMiller.getPrime();
        BigInteger p = RabinMiller.getPrime();

        MITE Alice = new MITE("Alice", g, p);
        MITE Bob = new MITE("Bob", g, p);
        users.add(Alice);
        users.add(Bob);

        MITM Eva = new MITM(g, p);

        Random rnd = new Random();

        Runnable task = () -> {

            try {

                while(true){

                    int id = rnd.nextInt(2);
                    int id2 = id == 0 ? 1: 0;

                    BigInteger k = users.get(id).getPartKey();
                    users.get(id2).getFullKey(k);

                    Eva.interceptKey(k);

                    k = users.get(id2).getPartKey();
                    users.get(id).getFullKey(k);

                    Eva.interceptKey(k);

                    users.get(id).setNewKey();
                    users.get(id2).setNewKey();

                    System.out.println("");

                    Thread.sleep(500);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        };

        Thread conversation = new Thread(task);
        conversation.start();

    }

}
