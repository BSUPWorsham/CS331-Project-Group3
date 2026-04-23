/**
 * Program implementing various encryption methods
 *   while also explaining what they do.
 * 
 * @author Bradlee Borgholthaus
 * @author James Taylor
 * @author Piper Worsham
 * @author Trust Migisha
 * @author Nikola Hodson
 * @author Nikki Schellenberg
 */

import java.util.Scanner;

public class Driver {
    private static Scanner scan = new Scanner(System.in);
    private static String[] passThrough;

    public static void main(String[] args) {
        menuDisplay();
        
        while (true) {
            System.out.print("> ");
            String opt = scan.nextLine();
            switch (opt.toLowerCase()) {
                case "p":
                case "polybius":
                    PolybiusSquare.main(passThrough);
                    break;
                case "s":
                case "steganography":
                    //Steganography.main(passThrough);
                    break;
                case "v":
                case "vigenere":
                    //Vigenere.main(passThrough);
                    break;
                case "e": // remove whichever one is not used
                case "el gamal":
                case "ellipitical curve":
                    //ElGamal.main(passThrough);
                    //EllipticalCurve.main(passThrough);
                    break;
                case "c":
                case "ceasar":
                    //Ceasar.main(passThrough);
                    break;
                case "r":
                case "rsa":
                    RSA.main(passThrough);
                    break;
                case "m":
                case "menu":
                case "h":
                case "help":
                    menuDisplay();
                    break;
                case "q":
                case "quit":
                case "exit":
                    System.exit(0);
                default:
                    System.out.println("Invalid option, press (M)enu for options");
                    break;
            }
        }
    }

    private static void menuDisplay() {
        System.out.println("What Cryptosystem do you want to try?");
        System.out.println("(P)olybius");
        System.out.println("(S)teganography");
        System.out.println("(V)igenere");
        System.out.println("(E)l Gamal or Elliptical Curve");
        System.out.println("(C)easar Cipher");
        System.out.println("(R)SA");
        System.out.println("(Q)uit program");
    }
}
