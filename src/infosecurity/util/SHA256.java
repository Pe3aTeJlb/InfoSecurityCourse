package infosecurity.util;

import java.math.BigInteger;
import java.util.ArrayList;

public class SHA256 {

    private static final BigInteger[] hex = new BigInteger[]{
            BigInteger.valueOf(0x6A09E667),
            BigInteger.valueOf(0xBB67AE85),
            BigInteger.valueOf(0x3C6EF372),
            BigInteger.valueOf(0xA54FF53A),
            BigInteger.valueOf(0x510e527f),
            BigInteger.valueOf(0x9B05688C),
            BigInteger.valueOf(0x1F83D9AB),
            BigInteger.valueOf(0x5BE0CD19)
    };

    private static final BigInteger[] k = new BigInteger[]{
            BigInteger.valueOf(0x428a2f98),  BigInteger.valueOf(0x71374491),  BigInteger.valueOf(0xb5c0fbcf),  BigInteger.valueOf(0xe9b5dba5),  BigInteger.valueOf(0x3956c25b),  BigInteger.valueOf(0x59f111f1),  BigInteger.valueOf(0x923f82a4),  BigInteger.valueOf(0xab1c5ed5),
            BigInteger.valueOf(0xd807aa98),  BigInteger.valueOf(0x12835b01),  BigInteger.valueOf(0x243185be),  BigInteger.valueOf(0x550c7dc3),  BigInteger.valueOf(0x72be5d74),  BigInteger.valueOf(0x80deb1fe),  BigInteger.valueOf(0x9bdc06a7),  BigInteger.valueOf(0xc19bf174),
            BigInteger.valueOf(0xe49b69c1),  BigInteger.valueOf(0xefbe4786),  BigInteger.valueOf(0x0fc19dc6),  BigInteger.valueOf(0x240ca1cc),  BigInteger.valueOf(0x2de92c6f),  BigInteger.valueOf(0x4a7484aa),  BigInteger.valueOf(0x5cb0a9dc),  BigInteger.valueOf(0x76f988da),
            BigInteger.valueOf(0x983e5152),  BigInteger.valueOf(0xa831c66d),  BigInteger.valueOf(0xb00327c8),  BigInteger.valueOf(0xbf597fc7),  BigInteger.valueOf(0xc6e00bf3),  BigInteger.valueOf(0xd5a79147),  BigInteger.valueOf(0x06ca6351),  BigInteger.valueOf(0x14292967),
            BigInteger.valueOf(0x27b70a85),  BigInteger.valueOf(0x2e1b2138),  BigInteger.valueOf(0x4d2c6dfc),  BigInteger.valueOf(0x53380d13),  BigInteger.valueOf(0x650a7354),  BigInteger.valueOf(0x766a0abb),  BigInteger.valueOf(0x81c2c92e),  BigInteger.valueOf(0x92722c85),
            BigInteger.valueOf(0xa2bfe8a1),  BigInteger.valueOf(0xa81a664b),  BigInteger.valueOf(0xc24b8b70),  BigInteger.valueOf(0xc76c51a3),  BigInteger.valueOf(0xd192e819),  BigInteger.valueOf(0xd6990624),  BigInteger.valueOf(0xf40e3585),  BigInteger.valueOf(0x106aa070),
            BigInteger.valueOf(0x19a4c116),  BigInteger.valueOf(0x1e376c08),  BigInteger.valueOf(0x2748774c),  BigInteger.valueOf(0x34b0bcb5),  BigInteger.valueOf(0x391c0cb3),  BigInteger.valueOf(0x4ed8aa4a),  BigInteger.valueOf(0x5b9cca4f),  BigInteger.valueOf(0x682e6ff3),
            BigInteger.valueOf(0x748f82ee),  BigInteger.valueOf(0x78a5636f),  BigInteger.valueOf(0x84c87814),  BigInteger.valueOf(0x8cc70208),  BigInteger.valueOf(0x90befffa),  BigInteger.valueOf(0xa4506ceb),  BigInteger.valueOf(0xbef9a3f7),  BigInteger.valueOf(0xc67178f2)
    };

    public static String encrypt(String message){

        System.out.println("SHA 256 " + message + "\n");

        StringBuilder msg = new StringBuilder(message);
        StringBuilder formatted = format(msg);
        ArrayList<StringBuilder> schedules  = msgSchedule(formatted);
        indexReplace(schedules);
        return compress(schedules);

    }

    private static StringBuilder format(StringBuilder msg){

        System.out.println("Format"+"\n");

        msg = toBin(msg);
        int len = msg.toString().length();

        msg.append("1");

        //StringBuilder formatted = new StringBuilder(String.format("%448s", msg.toString()).replace(" ", "0"));
        StringBuilder formatted = new StringBuilder(msg.toString());
        formatted.append("0".repeat(Math.max(0, 448 - msg.length())));
        System.out.println(formatted.toString());

        String msgLen = String.format("%64s", Integer.toBinaryString(len)).replace(" ","0");
        System.out.println(msgLen+"\n");

        formatted.append(msgLen);

        return formatted;

    }

    private static StringBuilder toBin(StringBuilder msg){

        StringBuilder buff = new StringBuilder();

        for(int i = 0; i < msg.length(); i++){
            String bin = String.format("%8s", Integer.toBinaryString(msg.charAt(i))).replace(" ","0");
            System.out.println(msg.charAt(i)+" "+bin);
            buff.append(bin);
        }

        return buff;

    }

    private static ArrayList<StringBuilder> msgSchedule(StringBuilder msg){

        System.out.println("Scheduling"+"\n");

        ArrayList<StringBuilder> msgSchedule = new ArrayList<>();

        for(int i = 32; i <= msg.length(); i += 32){
            msgSchedule.add(new StringBuilder(msg.substring(i-32,i)));
        }

        for(int i = 0; i < 48; i++ ){
            msgSchedule.add(new StringBuilder(String.format("%32s", "").replace(" ","0")));
        }

        System.out.println(msgSchedule);
        System.out.println();

        return msgSchedule;

    }

    private static ArrayList<StringBuilder> indexReplace(ArrayList<StringBuilder> schedules){

        System.out.println("Index replace"+"\n");

        for(int i = 16; i < 64; i++){

            String tmp1 = cycleShift(schedules,i,15,7);
            String tmp2 = cycleShift(schedules,i,15,18);
            String tmp3 = logicShift(schedules,i,15,3);

            BigInteger s0 =  new BigInteger(tmp1,2)
                    .xor(new BigInteger(tmp2,2))
                    .xor(new BigInteger(tmp3, 2));

            tmp1 = cycleShift(schedules,i,2,17);
            tmp2 = cycleShift(schedules,i,2,19);
            tmp3 = logicShift(schedules,i,2,10);

            BigInteger s1 =  new BigInteger(tmp1,2)
                    .xor(new BigInteger(tmp2,2))
                    .xor(new BigInteger(tmp3, 2));

            BigInteger res = new BigInteger(schedules.get(i-16).toString(),2)
                    .add(s0)
                    .add(new BigInteger(schedules.get(i-7).toString(),2))
                    .add(s1)
                    .mod(BigInteger.TWO.pow(32));

            schedules.set(i,new StringBuilder(String.format("%32s", res.toString(2)).replace(" ","0")));

        }

        for(int i = 0; i< schedules.size(); i+=2){
            System.out.println(schedules.get(i)+" "+schedules.get(i+1));
        }
        System.out.println();

        return schedules;

    }

    private static String cycleShift(ArrayList<StringBuilder> schedules, int i, int offset, int shift){

        String tmp = schedules.get(i-offset).toString();
        String buff = tmp.substring(tmp.length()-shift);
        return buff + tmp.substring(0,tmp.length()-shift);

    }

    private static String logicShift(ArrayList<StringBuilder> schedules, int i, int offset, int shift){

        String tmp = schedules.get(i-offset).toString();
        return "0".repeat(shift) + tmp.substring(0, tmp.length()-shift);

    }

    private static String compress(ArrayList<StringBuilder> schedules){

        System.out.println("Compression"+"\n");

        String a = String.format("%32s", hex[0].toString(2)).replace(" ","0");
        String b = String.format("%32s", hex[1].toString(2)).replace(" ","0");
        String c = String.format("%32s", hex[2].toString(2)).replace(" ","0");
        String d = String.format("%32s", hex[3].toString(2)).replace(" ","0");
        String e = String.format("%32s", hex[4].toString(2)).replace(" ","0");
        String f = String.format("%32s", hex[5].toString(2)).replace(" ","0");
        String g = String.format("%32s", hex[6].toString(2)).replace(" ","0");
        String h = String.format("%32s", hex[7].toString(2)).replace(" ","0");

        for(int i = 0; i < 64; i++){

            String tmp1 = e.substring(e.length()-6) + e.substring(0,e.length()-6);
            String tmp2 = e.substring(e.length()-11) + e.substring(0,e.length()-11);
            String tmp3 = e.substring(e.length()-25) + e.substring(0,e.length()-25);

            BigInteger s1 = new BigInteger(tmp1,2)
                    .xor(new BigInteger(tmp2,2))
                    .xor(new BigInteger(tmp3, 2));

            BigInteger ch = new BigInteger(e,2).and(new BigInteger(f,2)).xor(new BigInteger(e, 2).not().and(new BigInteger(g,2)));

            BigInteger t1 = new BigInteger(h,2)
                    .add(s1)
                    .add(ch)
                    .add(k[i])
                    .add(new BigInteger(schedules.get(i).toString(),2))
                    .mod(BigInteger.TWO.pow(32));

            tmp1 = a.substring(e.length()-2) + a.substring(0,a.length()-2);
            tmp2 = a.substring(e.length()-13) + a.substring(0,a.length()-13);
            tmp3 = a.substring(e.length()-22) + a.substring(0,a.length()-22);

            BigInteger s0 = new BigInteger(tmp1,2).xor(new BigInteger(tmp2,2)).xor(new BigInteger(tmp3, 2));
            BigInteger maj = new BigInteger(a,2).and(new BigInteger(b,2)).xor(new BigInteger(a,2).and(new BigInteger(c,2))).xor(new BigInteger(b,2).and(new BigInteger(c,2)));
            BigInteger t2 = s0.add(maj).mod(BigInteger.TWO.pow(32));

            h = g;
            g = f;
            f = e;
            e = String.format("%32s", new BigInteger(d,2).add(t1).mod(BigInteger.TWO.pow(32)).toString(2)).replace(" ","0");

            d = c;
            c = b;
            b = a;
            a = String.format("%32s", t1.add(t2).mod(BigInteger.TWO.pow(32)).toString(2)).replace(" ","0");

        }

        String s0 = new BigInteger(a,2).add(hex[0]).mod(BigInteger.TWO.pow(32)).toString(16);
        String s1 = new BigInteger(b,2).add(hex[1]).mod(BigInteger.TWO.pow(32)).toString(16);
        String s2 = new BigInteger(c,2).add(hex[2]).mod(BigInteger.TWO.pow(32)).toString(16);
        String s3 = new BigInteger(d,2).add(hex[3]).mod(BigInteger.TWO.pow(32)).toString(16);
        String s4 = new BigInteger(e,2).add(hex[4]).mod(BigInteger.TWO.pow(32)).toString(16);
        String s5 = new BigInteger(f,2).add(hex[5]).mod(BigInteger.TWO.pow(32)).toString(16);
        String s6 = new BigInteger(g,2).add(hex[6]).mod(BigInteger.TWO.pow(32)).toString(16);
        String s7 = new BigInteger(h,2).add(hex[7]).mod(BigInteger.TWO.pow(32)).toString(16);

        System.out.println(new BigInteger(a,2).toString(16)+"\n"+
                new BigInteger(b,2).toString(16)+"\n"+
                new BigInteger(c,2).toString(16)+"\n"+
                new BigInteger(d,2).toString(16)+"\n"+
                new BigInteger(e,2).toString(16)+"\n"+
                new BigInteger(f,2).toString(16)+"\n"+
                new BigInteger(g,2).toString(16)+"\n"+
                new BigInteger(h,2).toString(16));
        //System.out.println(s0+"\n"+s1+"\n"+s2+"\n"+s3+"\n"+s4+"\n"+s5+"\n"+s6+"\n"+s7);

        return s0 + s1 + s2 + s3 + s4 + s5 + s6 + s7;

    }

}
