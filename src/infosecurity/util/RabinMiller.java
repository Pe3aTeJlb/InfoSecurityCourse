package infosecurity.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

public class RabinMiller {

    private final int iteration = 5;

    private long prime = 6;
    private boolean result = true;

    private ArrayList<Thread> threads = new ArrayList<>();

    public Long getPrime(){

        Random rand = new Random();

        while(!isPrime(prime)){

            prime = rand.nextInt(10);
            System.out.println(prime+" "+isPrime(prime));

        }

        return prime;

    }

    public void interruptAll(){

        ArrayList<Thread> th = new ArrayList<>(threads);

        for (Thread t: th) {
            t.interrupt();
        }

        result = false;

    }

    public boolean isPrime(long n) {

        Runnable task = () -> {
            boolean res = isPrime_(n);
            if(!res)interruptAll();
        };

        for(int i =0; i < iteration; i++){
            Thread thread = new Thread(task);
            threads.add(thread);
            thread.start();
        }

        int c = 0;
        boolean threadEnd = false;
        while(!threadEnd){

            for (Thread f: threads) {
                if(f.getState() == Thread.State.TERMINATED)c++;
            }
            if(c==threads.size())threadEnd=true;
            else c = 0;
        }

        return result;


    }

    public boolean isPrime_(long n) {

        //n>3
        if (n == 0 || n == 1)
            return false;
        if (n == 2)
            return true;
        if (n % 2 == 0)
            return false;

        long s = n - 1;
        while (s % 2 == 0)
            s /= 2;

        Random rand = new Random();
        for (int i = 0; i < 1; i++) {
            long r = Math.abs(rand.nextLong());
            long a = r % (n - 1) + 1, temp = s;
            long mod = modPow(a, temp, n);
            while (temp != n - 1 && mod != 1 && mod != n - 1) {
                mod = mulMod(mod, mod, n);
                temp *= 2;
            }
            if (mod != n - 1 && temp % 2 == 0)
                return false;
        }
        return true;

    }

    /** (a ^ b) % c **/
    public long modPow(long a, long b, long c) {

        long res = 1;
        for (int i = 0; i < b; i++) {
            res *= a;
            res %= c;
        }
        return res % c;

    }

    /** (a * b) % c **/
    public long mulMod(long a, long b, long mod) {
        return BigInteger.valueOf(a).multiply(BigInteger.valueOf(b)).mod(BigInteger.valueOf(mod)).longValue();
    }

}
