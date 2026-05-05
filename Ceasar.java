
import java.util.Scanner;

import static java.lang.Character.toUpperCase;

public class Ceasar {
    public static char[] letters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W','X', 'Y', 'Z'};

     public static void main(String[] args) {
        System.out.println("Ceasar Cipher");
        System.out.println("Please enter your plaintext:");
        String plaintext = input.nextLine().toUpperCase().replaceAll(" ", "");
        System.out.println("Plain Text : " + plaintext);
        System.out.println("Please enter a Integer as the key");
        int key = input.nextInt();
        elaboratedEncrypt(plaintext,key);
    }

    public String encrypt(String plainText , int key){
        String ciperText = "";
        for(char plain : plainText){
            ciperText += letters[(CharsIndex(plain) + key)];
        }
        return ciperText;
    }

    public String decrypt(String plainText , int key){
        String ciperText = "";
        for(char plain : plainText){
            ciperText += letters[(CharsIndex(plain) - key)];
        }
        return ciperText;
    }

    public String elaboratedEncrypt(String plainText , int key){
        System.out.println("The encyphering function for the ceasar cipher is char + key mod range of possible charecters");
        System.out.println("In this case the index of the current plaintext charecter plus the key  mod 26 ");
        String ciperText = "";
        for(char plain : plainText){
            char cipherChar = letters[(CharsIndex(plain) + key)];
            ciperText += cipherChar;
            System.out.println(plain + " encrypted using key " + key  + " is " + cipherChar);
        }
        System.out.println(plaintext + " encrypted using key " + key  + " is " + ciphertext);
        return ciperText;
    }

    public String elaboratedDecrypt(String plainText , int key){
        System.out.println("The decyphering function for the ceasar cipher is char - key mod range of possible charecters");
        System.out.println("In this case the index of the current plaintext charecter minus the key  mod 26 ");
        String ciperText = "";
        for(char plain : plainText){
            char cipherChar = letters[(CharsIndex(plain) - key)];
            ciperText += cipherChar;
            System.out.println(plain + " decrypted using key " + key  + " is " + cipherChar);
        }
        System.out.println(plaintext + " decrypted using key " + key  + " is " + ciphertext);
        return ciperText;
    }

    private int[] CharsIndex(String plain){
        int[] charsindex = int[plain.length()];
        char[] chars = plain.toCharArray();
        for(int i =0 ; i < chars.length ; i++){
            int currindex = 0;
            boolean found = false;
            while(!found && currIndex<letters.length){
                if(letters[currIndex] = chars[i]){
                    found = true;
                }
            }
            if(!found){
                System.out.println("something went wrong");
                return null;
            }
            charsindex[i] = currindex;
        }
        return charsindex;
    }

}
