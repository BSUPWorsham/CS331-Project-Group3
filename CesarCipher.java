
import java.util.Scanner;

import static java.lang.Character.toUpperCase;

public class Ceasar {
    public static char[] letters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W','X', 'Y', 'Z'};

     public static void main(String[] args) {
        System.out.println("Ceasar Cipher");

        boolean continue = true;
        while(continue){
            System.out.println("Please enter your plaintext:");
            String plaintext = input.nextLine().toUpperCase().replaceAll(" ", "");
            System.out.println("Plain Text : " + plaintext);
            System.out.println("Please enter a Integer as the key");
            int key = 
        }

    }

    public String encrypt(String plainText , int key){
        String ciperText;

        return ciperText;
    }

    public String elaboratedEncrypt(String plainText , int key){
        String ciperText;

        return ciperText;
    }

    private int[] CharsIndex(String plain){
        int[] chars = int[plain.length()];
        for(Char char : plain){
            int index = -1;
            boolean found = false;
            
        }
    }

}
