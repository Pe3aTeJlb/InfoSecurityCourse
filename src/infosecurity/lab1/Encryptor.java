package infosecurity.lab1;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class Encryptor {

    private final ArrayList<Character> alphabet = new ArrayList<>(Arrays.asList(
            'а','б','в','г','д','е',
            'ж','з','и','й','к','л',
            'м','н','о','п','р','с',
            'т','у','ф','х','ц','ч','ш',
            'щ','ъ','ы','ь','э','ю','я'
    ));

    private final ArrayList<Character> matrix = new ArrayList<>();

    private final HashMap<Character, Character> replacementMap = new HashMap<>();

    public Encryptor(int shift, String key){

        System.out.println("////Encryptor////");
        System.out.println("////Alphabet preparation////");
        System.out.println(alphabet);

        for (char c: key.toCharArray()) {
            matrix.add(c);
        }

        for (char c: alphabet) {
            if (!matrix.contains(c))matrix.add(c);
        }

        //System.out.println(matrix);

        if (shift == 0) return;

        Collections.rotate(matrix, shift);

        System.out.println(matrix);

        for(int i = 0; i < alphabet.size(); i++){
            replacementMap.put(alphabet.get(i),matrix.get(i));
        }

        //System.out.println(replacementMap);
        System.out.println("");

    }

    public StringBuilder encrypt(String filepath){

        //System.out.println("////File encryption////");

        StringBuilder fileData = new StringBuilder();
        StringBuilder encryptedData = new StringBuilder();

        try {

            FileReader reader = new FileReader(filepath);

            char[] buf = new char[1024];
            int numRead;

            while ((numRead=reader.read(buf)) != -1) {
                String readData = String.valueOf(buf, 0, numRead).toLowerCase();
                fileData.append(readData);
            }

            reader.close();

            //System.out.println("original text is: "+fileData.toString()+"\n\n\n\n");

            int i = 0;
            int len = fileData.length();
            while (i<len) {

                if(replacementMap.containsKey(fileData.charAt(i))){
                    //System.out.println(fileData.charAt(i)+" "+replacementMap.get(fileData.charAt(i)));
                    encryptedData.append(replacementMap.get(fileData.charAt(i)).toString());
                }
                else {
                    //System.out.println(fileData.charAt(i));
                    encryptedData.append(fileData.charAt(i));
                }
                i++;

            }

            //System.out.println("encrypted text is: "+encryptedData.toString());
            //System.out.println("\n\n");

        }
        catch(IOException e) {
            System.out.println("Cant read File " + e.getMessage());
        }

        return encryptedData;

    }

}
