package infosecurity.lab1;

import infosecurity.util.RabinMiller;

public class Lab1 {

    private static final String filepath = "./src/infosecurity/lab1/file.txt";

    public static void main(String[] args) {

        Encryptor encryptor = new Encryptor(3, "ключ");
        StringBuilder encryptedData = encryptor.encrypt(filepath);

        Decryptor decryptor = new Decryptor();

        decryptor.decryptWithMonograms(encryptedData);

        decryptor.prepareBigramFreq(filepath);
        decryptor.decryptWithBigrams(encryptedData);

    }



}
