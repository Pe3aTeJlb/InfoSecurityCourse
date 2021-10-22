package infosecurity.lab4;

import infosecurity.util.RabinMiller;

import java.math.BigInteger;
import java.util.AbstractMap;

public class Lab4 {

    public static void main(String[] args) {

        BigInteger n = RabinMiller.getSafePrime(BigInteger.valueOf(200));
        System.out.println("n: " + n);

        BigInteger g = RabinMiller.getPrimitiveRoot(n);
        System.out.println("g: " + g + "\n");


        Server server = new Server(n, g);


        Client client = new Client(n, g, "login", "password");
        client.connectToServer(server);

        AbstractMap.SimpleImmutableEntry<String,BigInteger> aMessage = client.generateA();
        AbstractMap.SimpleImmutableEntry<String,BigInteger> bMessage = server.generateB(aMessage);

        System.out.println("Client session key is: " + client.generateSessionKey(bMessage).toString()+"\n");
        System.out.println("Server session key is: " + server.generateSessionKey("login")+"\n");

        String m1 = client.generateM1();
        server.verifyM1(m1);

    }

}
