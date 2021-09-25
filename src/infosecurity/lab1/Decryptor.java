package infosecurity.lab1;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static java.util.Map.entry;

public class Decryptor {

    private int symbolCounter;

    private final ArrayList<Character> alphabet = new ArrayList<>(Arrays.asList(
            'а','б','в','г','д','е',
            'ж','з','и','й','к','л',
            'м','н','о','п','р','с',
            'т','у','ф','х','ц','ч','ш',
            'щ','ъ','ы','ь','э','ю','я'
    ));

    private final ArrayList<Character> alphabetFrequency2 = new ArrayList<>(Arrays.asList(
            'о','е','а','и','н','т',
            'р','с','в','л','к','м',
            'д','п','у','я','ы','ь',
            'г','з','б','ч','й','х','ж',
            'ш','ю','ц','щ','э','ф','ъ'
    ));

    private final Map<Character, Float> alphabetFrequency = Map.ofEntries(
            entry('о', 0.10983f),
            entry('е', 0.08483f),
            entry('а', 0.07998f),
            entry('и', 0.07367f),
            entry('н', 0.06700f),
            entry('т', 0.06318f),
            entry('с', 0.05473f),
            entry('р', 0.04746f),
            entry('в', 0.04533f),
            entry('л', 0.04343f),
            entry('к', 0.03486f),
            entry('м', 0.03203f),
            entry('д', 0.02977f),
            entry('п', 0.02804f),
            entry('у', 0.02615f),
            entry('я', 0.02001f),
            entry('ы', 0.01898f),
            entry('ь', 0.01735f),
            entry('г', 0.01687f),
            entry('з', 0.01641f),
            entry('б', 0.01592f),
            entry('ч', 0.0145f),
            entry('й', 0.01208f),
            entry('х', 0.00866f),
            entry('ж', 0.00738f),
            entry('ш', 0.00718f),
            entry('ю', 0.00639f),
            entry('ц', 0.000486f),
            entry('щ', 0.00268f),
            entry('э', 0.00331f),
            entry('ф', 0.00267f),
            entry('ъ', 0.00037f)

    );

    private Map<Character, Float> entryDataFreq = new HashMap<>();
    private Map<Character, Character> decryptMatrix = new HashMap<>();

    public Decryptor(){}

    public StringBuilder decryptWithMonograms(StringBuilder encryptedData){

        System.out.println("////Decryptor////");

        int i = 0;
        int len = encryptedData.length();

        while(i<len) {

            if(alphabet.contains(encryptedData.charAt(i))){

                symbolCounter++;
                if(!entryDataFreq.containsKey(encryptedData.charAt(i))){
                    entryDataFreq.put(encryptedData.charAt(i),1f);
                }else{
                    float buff = entryDataFreq.get(encryptedData.charAt(i));
                    entryDataFreq.replace(encryptedData.charAt(i), buff+1);
                }

            }
            i++;

        }

        for (char c: entryDataFreq.keySet()) {
            float val = entryDataFreq.get(c);
            //(float) (Math.round((val/symbolCounter) * Math.pow(10, 4)) / Math.pow(10, 4))
            entryDataFreq.replace(c, (float) (Math.round((val/symbolCounter) * Math.pow(10, 4)) / Math.pow(10, 4)));
        }

        System.out.println("symb count: "+symbolCounter);
        System.out.println(alphabetFrequency);
        System.out.println(entryDataFreq);

        LinkedList<Map.Entry<Character, Float>> list = new LinkedList(entryDataFreq.entrySet());
        Comparator<Map.Entry<Character, Float>> comparator = Map.Entry.comparingByValue();
        list.sort(comparator.reversed());

        ArrayList<Character> chars = new ArrayList<>();

        for (Map.Entry<Character, Float> c: list) {
            chars.add(c.getKey());
        }

        System.out.println(alphabetFrequency2);
        System.out.println(chars);

        System.out.println("");

        int j = 0;
        for (Map.Entry<Character, Float> c: list) {
            decryptMatrix.put(c.getKey(),alphabetFrequency2.get(j));
            j++;
        }

        //System.out.println(decryptMatrix);


        StringBuilder decryptedData = new StringBuilder();
        i = 0;
        len = encryptedData.length();

        while(i<len) {

            if(decryptMatrix.containsKey(encryptedData.charAt(i))){
                decryptedData.append(decryptMatrix.get(encryptedData.charAt(i)));
            }else{
                decryptedData.append(encryptedData.charAt(i));
            }
            i++;

        }

        //System.out.println("\n"+"Decrypted data: "+decryptedData.toString());


        return null;

    }


    private Map<String, Float> bigramFreqInput = new HashMap<>();
    private LinkedList<Map.Entry<String, Float>> bigramFreqInputList;
    private Map<String, Float> bigramMap = new HashMap<>();
    private LinkedList<Map.Entry<String, Float>> bigramMapList;
    private Map<String, String> bigramMatrix = new HashMap<>();

    public void prepareBigramFreq(String filepath){

        StringBuilder data = new StringBuilder();

        try {

            FileReader reader = new FileReader(filepath);

            char[] buf = new char[1024];
            int numRead;

            while ((numRead=reader.read(buf)) != -1) {
                String readData = String.valueOf(buf, 0, numRead).toLowerCase();
                data.append(readData);
            }

            reader.close();

        }
        catch(IOException e) {
            System.out.println("Cant read File " + e.getMessage());
        }

        for(int i = 0; i < data.length()-1; i++){

            String bigram = data.charAt(i) +Character.toString(data.charAt(i+1));
            if(alphabet.contains(data.charAt(i)) && alphabet.contains(data.charAt(i+1))){
                if(!bigramFreqInput.containsKey(bigram))bigramFreqInput.put(bigram,1f);
                else{bigramFreqInput.put(bigram, bigramFreqInput.get(bigram)+1);}
            }

        }

        bigramFreqInputList = new LinkedList(bigramFreqInput.entrySet());
        Comparator<Map.Entry<String, Float>> comparator = Map.Entry.comparingByValue();
        bigramFreqInputList.sort(comparator.reversed());

        System.out.println(bigramFreqInputList);

    }

    public void bigramCounter(StringBuilder data){

        for(int i = 0; i < data.length()-1; i++){

            String bigram = data.charAt(i) +Character.toString(data.charAt(i+1));
            if(alphabet.contains(data.charAt(i)) && alphabet.contains(data.charAt(i+1))){
                if(!bigramMap.containsKey(bigram))bigramMap.put(bigram,1f);
                else{bigramMap.put(bigram, bigramMap.get(bigram)+1);}
            }

        }

        bigramMapList = new LinkedList(bigramMap.entrySet());
        Comparator<Map.Entry<String, Float>> comparator = Map.Entry.comparingByValue();
        bigramMapList.sort(comparator.reversed());

        System.out.println(bigramMapList);

    }

    public StringBuilder decryptWithBigrams(StringBuilder edata){

        bigramCounter(edata);

        for(int i = 0; i < bigramMapList.size(); i++){
            bigramMatrix.put(bigramMapList.get(i).getKey(),bigramFreqInputList.get(i).getKey());
        }

        StringBuilder decryptedData = new StringBuilder();
        int i = 0;
        int len = edata.length()-1;

        while(i<len) {

            String bigram = edata.charAt(i)+Character.toString(edata.charAt(i+1));

            if(bigramMatrix.containsKey(bigram)){
                decryptedData.append(bigramMatrix.get(bigram));
                edata.replace(i,i+2,bigramMatrix.get(bigram));
                i+=2;
            }
            else if(decryptMatrix.containsKey(edata.charAt(i))){
                decryptedData.append(decryptMatrix.get(edata.charAt(i)));
                i++;
            }
            else {
                decryptedData.append(edata.charAt(i));
                i++;
            }

        }

        System.out.println("\n"+"Decrypted data: "+decryptedData.toString());

        return decryptedData;

    }

}
