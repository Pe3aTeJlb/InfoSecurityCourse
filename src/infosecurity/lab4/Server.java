package infosecurity.lab4;

import infosecurity.util.SHA256;

import java.math.BigInteger;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Random;

public class Server {

    private BigInteger N;
    private BigInteger g;
    private BigInteger k = BigInteger.valueOf(3);

    private HashMap<String,AbstractMap.SimpleImmutableEntry<String, BigInteger>> clients = new HashMap<>();

    private BigInteger A;
    private BigInteger B;
    private BigInteger b;

    private BigInteger key;

    public Server(BigInteger n, BigInteger g){
        System.out.println("Server created");
        System.out.println("n = " + n + " g = " + g + "\n");
        this.N = n;
        this.g = g;
    }

    public void registerClient(String login, String salt, BigInteger v){
        clients.put(login, new AbstractMap.SimpleImmutableEntry<>(salt, v));
    }

    public AbstractMap.SimpleImmutableEntry<String,BigInteger> generateB(AbstractMap.SimpleImmutableEntry<String,BigInteger> entry){

        System.out.println("Server generate B as k * (v + g ^ b % N) ");

        A = entry.getValue();
        b = new BigInteger(1000,new Random());

        AbstractMap.SimpleImmutableEntry<String, BigInteger> pair = clients.get(entry.getKey());

        B = k.multiply(pair.getValue()).add(g.modPow(b,N));

        System.out.println("B = "+B.toString()+"\n");

        return new AbstractMap.SimpleImmutableEntry<>(pair.getKey(),B);

    }

    public BigInteger generateSessionKey(String login){

        System.out.println("Server generate session key as (A * (v ^ (A concat B hash) % N)) ^ b % N");

        String u = SHA256.encrypt(A.toString(16)+B.toString(16));
        key = A.multiply(clients.get(login).getValue().modPow(new BigInteger(u,16),N)).modPow(b,N);

        return key;

    }

    public void verifyM1(String m1){
        System.out.println("Server verify M1 (A concat B concat Key) hash eq client m1");
        if(SHA256.encrypt(A.toString()+B.toString()+key.toString()).equals(m1)){
            System.out.println("Connection established");
        }else{
            System.out.println("Connection terminated");
        }

    }

}
