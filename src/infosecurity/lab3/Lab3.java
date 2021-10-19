package infosecurity.lab3;

import infosecurity.util.RabinMiller;

import java.math.BigInteger;
import java.util.ArrayList;

public class Lab3 {

    public static void main(String[] args) {

        Receiver receiver = new Receiver(RabinMiller.getPrime(), RabinMiller.getPrime());
        Transmitter transmitter = new Transmitter(receiver.getPublicKey());

        ArrayList<BigInteger> msgCodes = transmitter.encryptMessage("LOL XD");
        System.out.println(msgCodes.toString());
        System.out.println(receiver.decryptMessage(msgCodes));

    }

}
