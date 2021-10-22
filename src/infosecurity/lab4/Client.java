package infosecurity.lab4;

import infosecurity.util.SHA256;

import java.math.BigInteger;
import java.util.*;
import java.util.Random;

public class Client {

    private Server server;

    private BigInteger N;
    private BigInteger g;
    private BigInteger k = BigInteger.valueOf(3);

    private BigInteger x;
    private BigInteger v;

    private String login;
    private String password;

    private BigInteger A;
    private BigInteger a;

    private BigInteger B;
    private BigInteger key;

    private String salt;

    public Client(BigInteger n, BigInteger g, String login, String password){

        System.out.println("Create client");

        this.N = n;
        this.g = g;
        this.login = login;
        this.password = password;

        salt = "1jkb327g";
        System.out.println("Salt : "+salt);

        x = new BigInteger(SHA256.encrypt(this.password+salt),16);
        v = g.modPow(x,this.N);

    }

    public void connectToServer(Server server){
        server.registerClient(login, salt, v);
    }

    public AbstractMap.SimpleImmutableEntry<String,BigInteger> generateA(){

        System.out.println("Client generats A");

        a = new BigInteger(1000,new Random());
        A = g.modPow(a,N);

        System.out.println("A : "+A.toString()+"\n");

        return new AbstractMap.SimpleImmutableEntry<>(login, A);

    }

    public BigInteger generateSessionKey(AbstractMap.SimpleImmutableEntry<String,BigInteger> entry){

        System.out.println("Client generate session key");

        B = entry.getValue();
        BigInteger u = new BigInteger(SHA256.encrypt(A.toString(16)+B.toString(16)),16);

        BigInteger tmp1 = B.subtract(g.modPow(x,N).multiply(k));
        BigInteger tmp2 = u.multiply(x);

        key = tmp1.modPow(a.add(tmp2),N);

        return key;

    }

    public String generateM1(){
        System.out.println("Client generate M1");
        return SHA256.encrypt(A.toString()+B.toString()+key.toString());
    }

}
