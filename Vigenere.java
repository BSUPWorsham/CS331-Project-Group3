import java.util.Scanner;

import static java.lang.Character.toUpperCase;

public class Vigenere {
    public static char[] letters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W','X', 'Y', 'Z'};

    private static char[] keyArray;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String key;
        String plaintext;

        System.out.println("Welcome to the Vigenere cryptography!");
        System.out.println("Please enter your plaintext:");
        plaintext = input.nextLine().toUpperCase().replaceAll(" ", "");

        keyArray = new char[plaintext.length()];

        do {
            System.out.println("Please enter a 3 to 5-letter key:");
            key = input.nextLine().toUpperCase();

        } while ((key.length() < 3) || (key.length() > 5));

        for (int i = 0; i < plaintext.length(); i++) {
            keyArray[i] = key.charAt(i % key.length());
        }

        System.out.println();
        System.out.println("Encryption function: E = (Pi + Ki) % 26");
        System.out.println("Plaintext: " + plaintext);
        System.out.println("Encrypted message: " + encryptionFunction(plaintext));
        String encrypted = encryptionFunction(plaintext);

        System.out.println();
        System.out.println();
        System.out.println("Decryption function: D = (Ci - Ki) % 26");
        System.out.println("Ciphertext: " + encrypted);
        System.out.println("Decrypted message: " + decryptionFunction(encrypted));
    }


    private static String encryptionFunction(String plaintext) {
        StringBuilder message = new StringBuilder();


        for (int i = 0; i < plaintext.length(); i++) {
            int index;

//            System.out.println("Pi: " + getLetterIndex(plaintext.charAt(i)));
//            System.out.println("Ki: " + getLetterIndex(keyArray[i]));


            index = (getLetterIndex(plaintext.charAt(i)) + getLetterIndex(keyArray[i])) % letters.length;
//            System.out.println("Index: " + index);
//            System.out.println();

            message.append(letters[index]);
        }

        return message.toString();

    }

    private static String decryptionFunction(String ciphertext) {
        StringBuilder message = new StringBuilder();


        for (int i = 0; i < ciphertext.length(); i++) {
            int index;

//            System.out.println("Pi: " + getLetterIndex(ciphertext.charAt(i)));
//            System.out.println("Ki: " + getLetterIndex(keyArray[i]));

            int firstPart = getLetterIndex(ciphertext.charAt(i)) - getLetterIndex(keyArray[i]);
            if (firstPart < 0) {
                index = letters.length + firstPart;
            } else {
                index = firstPart % letters.length;
            }

//            System.out.println("Index: " + index);
//            System.out.println();

            message.append(letters[index]);
        }

        return message.toString();
    }


    private static int getLetterIndex(char letter) {
        int index = 0;
        char ch = toUpperCase(letter);

        for (int i = 0; i < letters.length; i++) {
            if (ch == letters[i]) {
                index = i;
            }
        }

        return index;
    }

}
