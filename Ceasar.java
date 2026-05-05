
import java.util.Scanner;

import static java.lang.Character.toUpperCase;

public class Ceasar {
    public static char[] letters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W','X', 'Y', 'Z'};

     public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Ceasar Cipher");
        System.out.println("Please enter your plaintext:");
        String plaintext = input.nextLine().toUpperCase().replaceAll(" ", "");
        System.out.println("Plain Text : " + plaintext);
        System.out.println("Please enter a Integer as the key");
        int key = input.nextInt();
        elaboratedEncrypt(plaintext,key);
        
        input.close();
    }

    public String encrypt(String plainText , int key){
        String ciperText = "";
        char[] chars = plainText.toCharArray();
        for(char plain : chars){
            ciperText += letters[(CharIndex(plain) + key)];
        }
        return ciperText;
    }

    public String decrypt(String plainText , int key){
        String ciperText = "";
        char[] chars = plainText.toCharArray();
        for(char plain : chars){
            ciperText += letters[(CharIndex(plain) - key)];
        }
        return ciperText;
    }

    public static String elaboratedEncrypt(String plainText , int key){
        System.out.println("The encyphering function for the ceasar cipher is char + key mod range of possible charecters");
        System.out.println("In this case the index of the current plaintext charecter plus the key  mod 26 ");
        String ciperText = "";
        char[] chars = plainText.toCharArray();
        for(char plain : chars){
            char cipherChar = letters[(CharIndex(plain) + key)];
            ciperText += cipherChar;
            System.out.println(plain + " encrypted using key " + key  + " is " + cipherChar);
        }
        System.out.println(plainText + " encrypted using key " + key  + " is " + ciperText);
        return ciperText;
    }

    public String elaboratedDecrypt(String plainText , int key){
        System.out.println("The decyphering function for the ceasar cipher is char - key mod range of possible charecters");
        System.out.println("In this case the index of the current plaintext charecter minus the key  mod 26 ");
        String ciperText = "";
        char[] chars = plainText.toCharArray();
        for(char plain : chars){
            char cipherChar = letters[(CharIndex(plain) - key)];
            ciperText += cipherChar;
            System.out.println(plain + " decrypted using key " + key  + " is " + cipherChar);
        }
        System.out.println(plainText + " decrypted using key " + key  + " is " + ciperText);
        return ciperText;
    }

    private static int CharIndex(char plain){
        int currIndex = 0;
        boolean found = false;
        while(!found && currIndex<letters.length){
            if(letters[currIndex] == plain){
                found = true;
            }
        }
        if(!found){
            System.out.println("something went wrong");
            return -1;
        }
        return currIndex;
    }

     /* 
    private int[] CharsIndex(String plain){
        char[] chars = plain.toCharArray();
        int[] charsindex = int[chars.length()];
        for(int i =0 ; i < chars.length ; i++){
            int currIndex = 0;
            boolean found = false;
            while(!found && currIndex <letters.length){
                if(letters[currIndex] == chars[i]){
                    found = true;
                }
            }
            if(!found){
                System.out.println("something went wrong");
                return null;
            }
            charsindex[i] = currIndex;
        }
        return charsindex;
    }
        */
}
