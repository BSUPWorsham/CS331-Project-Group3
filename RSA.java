/**
 * A simple demo of RSA and how a key may be made.
 * 
 * @author Nikki Schellenberg
 */

import java.math.BigInteger;
import java.util.Scanner;

public class RSA {
    private static Scanner scan = new Scanner(System.in);
    private static BigInteger p;
    private static BigInteger q;
    private static BigInteger n;
    private static BigInteger phi;
    private static BigInteger olde;
    private static BigInteger e;
    private static BigInteger d;

    public static void main(String[] args) {
        // intro blurb
        System.out.println("\nRSA is a very widely used cryptosystem that became famous for its ease of implementation and being computationally simple while being secure.");
        System.out.println("Its security does rely on having it properly set up though.");
        System.out.println("For this, I will not delve into that attacks, but a good idea for a secure RSA key is to have the primes be far apart from eachother and have very few repeating zeros.");
        System.out.println("Now, about the primes mentioned, RSA keys are based off of two primes.");

        // actual RSA calculations after getting primes from user.
        do {
            p = getPrime("first");
            q = getPrime("second");

            n = p.multiply(q);
        
            phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE)); // phi = (p - 1) * (q - 1)
            e = phi.subtract(BigInteger.ONE);
            olde = e; // for posterity

            BigInteger x = BigInteger.ZERO;
            while(x.compareTo(BigInteger.ONE) != 0) { // while x != 0
                e = e.subtract(BigInteger.TWO);
                x = e.gcd(phi);
                if(e.compareTo(BigInteger.ONE) == -1) { // VERY UNLIKELY
                    System.out.println("You have done the unlikely and a public exponent cannot be found.");
                    System.out.println("Please try entering new primes.");
                }
            }

            d = e.modInverse(phi); // modular inverse
        } while (e.compareTo(BigInteger.ONE) == -1);

        // explanation of what RSA is.
        System.out.println("\nWe now have both primes!");
        System.out.println("RSA is an asymmetric public key cipher.");
        System.out.println("This means that the public key and private key should differ.");
        System.out.println("In RSA, both the public and private share a value though, n, the modulus.");
        System.out.println("To get n, we multiply both primes together, giving us " + n + ".");
        System.out.println("This is secure as factoring the primes from n would take far too long currently, as the primes are very large numbers, and prime.");
        
        waitOnUser();

        System.out.println("Now, the equation to encrypt a message takes three parts.");
        System.out.println("The plaintext message itself, M.");
        System.out.println("The public exponent, e.");
        System.out.println("The modulus, n.");
        System.out.println("The ciphertext is then acquried by solving the following equation:");
        System.out.println("(M^e) % n"); // message to the power of e, then modded by n
        System.out.println("Modding it loses so much information about the orginal value that it becomes secure with only one possible way to get that infomation back.");
        
        waitOnUser();
        
        System.out.println("Before going into that though, we still need to get e, the public exponent.");
        System.out.println("The easist way to do this is to make an intermediate value called phi (φ).");
        System.out.println("φ is equal to: (p - 1) * (q - 1).");
        System.out.println("Then e can equal φ - 1.");
        System.out.println("Now, e needs to have a greatest commond denominator (gcd) of 1 with φ.");
        System.out.println("This likely isn't true right now as e is " + olde + " while φ is " + phi + ".");
        System.out.println("To solve this problem, you can repeatedly subtract 2 from e and check if that has a gcd of 1 with φ.");
        System.out.println("Once that is true, you have found a your public exponent.");
        System.out.println("In this case, e ends at " + e + ".");
        
        waitOnUser();

        System.out.println("Now for the private key, which also needs three things.");
        System.out.println("The ciphertext, C.");
        System.out.println("The private exponent, d.");
        System.out.println("The modulus, n.");
        System.out.println("We already have n and C will be given, so we just need d.");
        System.out.println("The quickest way to find is to use the modular multiplicative inverse, which looks like this:");
        System.out.println("(e^-1) % phi");
        System.out.println("So our d value is " + d + ".");

        waitOnUser();

        // encryption/decryption
        System.out.println("Congrats, we now have all the infomation needed!");
        System.out.println("Now to use it.");
        System.out.println("Right now I do not have a ASCII to number converter, so please just input a number to cipher.");
        BigInteger m;
        while (true) {
            System.out.print("> ");
            try {
                m = BigInteger.valueOf(Long.parseLong(scan.nextLine()));
                break;
            } catch (Exception e) {
                System.out.println("A number please, for the plaintext.");
            }
        }
        System.out.println("\nNow, to encrypt we just run the following equation:");
        System.out.println("M^e % n");
        BigInteger c = m.modPow(e, n);
        System.out.println("This just us, C, which is " + c + ".");
        
        waitOnUser();

        System.out.println("Now to get M back, we run the following equation:");
        System.out.println("C^d % n");
        System.out.println("This should give us M, which it " + ((c.modPow(d, n).compareTo(m) == 0) ? "does!" : "doesn't... That isn't meant to happen..."));

        // sign off
        waitOnUser();
        System.out.println("Hopefully this shows how RSA is so simple yet effective.");
        System.out.println("Once past making the key, it very easy to use and computationally easy to use as well.");
        System.out.println("The fact that information is derived from two very large primes is how its secure, as the primes are never revealed.");
        System.out.println("Returning you to main menu.");
    }

    /**
     * Gets a prime number for RSA, with a slightly customizable message.
     * 
     * @param iteration
     * @return <b>prime</b>
     */
    private static BigInteger getPrime(String iteration) {
        BigInteger prime;
        while(true) {
            System.out.println("Please enter your " + iteration + " prime (a positive integer).");
            System.out.print("> ");
            try {
                prime = BigInteger.valueOf(Long.parseLong(scan.nextLine()));
                if(prime.isProbablePrime(Integer.MAX_VALUE)) {
                    return prime;
                } else {
                    System.out.println("Value is not prime, try again.");
                }
            } catch (Exception e) {
                System.out.println("Value needs to be a positive integer.");
            }
        }
    }

    /**
     * Slows the program to the users pace, avoiding walls of text all at once.
     */
    private static void waitOnUser() {
        System.out.print("(Press Enter to Continue)");
        scan.nextLine();
        System.out.println();
    }
}