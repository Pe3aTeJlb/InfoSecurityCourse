package infosecurity.lab3;

import infosecurity.util.RabinMiller;

import java.math.BigInteger;
import java.util.ArrayList;

public class Receiver {

    private final BigInteger module, publicExponent, privateExponent, euler;

    public Receiver(BigInteger p, BigInteger q){

        System.out.println("P: "+p +" "+ "Q: "+ q);

        module = p.multiply(q);
        System.out.println("Module: "+ module);

        euler = ((p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE)));
        System.out.println("Euler: "+ euler);

        publicExponent = generatePublicExponent();
        privateExponent = correctKey(generateSecretKey(publicExponent, euler)[1]);
        System.out.println("Public exponent: "+ publicExponent);
        System.out.println("Private exponent: "+ privateExponent);

    }

    public BigInteger[] getPublicKey(){
        return new BigInteger[]{publicExponent, module};
    }

    private BigInteger[] generateSecretKey(BigInteger a, BigInteger b) {

        BigInteger[] res = new BigInteger[3];

        if (b.equals(BigInteger.ZERO)) {
            res[0] = a;
            res[1] = BigInteger.ONE;
            res[2] = BigInteger.ZERO;
            return res;
        }

        res = generateSecretKey(b, a.mod(b));

        BigInteger s = res[2];
        res[2] = res[1].subtract((a.divide(b)).multiply(res[2]));
        res[1] = s;

        return res;

    }

    private BigInteger correctKey(BigInteger euler){
        if(euler.compareTo(BigInteger.ZERO) >= 0) return euler;
        return this.euler.add(euler);
    }

    private BigInteger generatePublicExponent(){

        while (true){
            BigInteger buff = RabinMiller.getPrimeOf(euler);
            if (euler.gcd(buff).equals(BigInteger.ONE)) return buff;
        }

    }

    public String decryptMessage(ArrayList<BigInteger> codes){

        StringBuilder builder = new StringBuilder();
        for (BigInteger code: codes) {
            builder.append((char)code.modPow(privateExponent, module).intValue());
        }

        return builder.toString();

    }

}
