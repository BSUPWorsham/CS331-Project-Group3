import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Scanner;

public class ElGamal {

    //encrypts a given message assumes y is correct
    private static BigInteger[] encrypt(String m, String p, String g, String k , String y){
        BigInteger prime = new BigInteger(p);
        BigInteger generator = new BigInteger(g);
        BigInteger ephemeral = new BigInteger(k);
        BigInteger publicKey = new BigInteger(y);
        BigInteger cipherOne;
        BigInteger cipherTwo;
        BigInteger message = new BigInteger(stringToChar(m));
        BigInteger retVal[] = new BigInteger[2];

        //checks validity of inputs
        if(!prime.isProbablePrime(112)){
            System.out.println("The given prime value " + prime + " is not a prime number please try again with a new number");
            return null;
        }
        else{
            System.out.println(prime + " is a probable prime");
        }

        if (!generatorCheck(generator, prime)){
            System.out.println("The given generator value " + generator + " is not a primitive prime of " + prime + " please try again with a new number");
            return null;
        }
        else{
             System.out.println("The given generator value " + generator + " is a primitive prime of " + prime);
        }

        //this should be changed if we want to use signatures
        if( ephemeral.compareTo(BigInteger.ZERO) <= 0 || ephemeral.compareTo(prime.subtract(BigInteger.ONE)) >= 0){
            System.out.println("The given ephemeral key value " + ephemeral + " is outside the range of 0 - " + prime.subtract(BigInteger.ONE) + " please try again with a new number");
            return null;
        }
        else{
            System.out.println("The given ephemeral key value " + ephemeral + " is within the range of 0 - "  + prime.subtract(BigInteger.ONE) + " and has a GCD of 1");
        }

        if( publicKey.compareTo(BigInteger.ZERO) <= 0 || publicKey.compareTo(prime.subtract(BigInteger.ONE)) >= 0){
            System.out.println("The given public key value " + publicKey + " is outside the range of 0 - (" + prime + " - 1) please try again with a new number");
            return null;
        }
        else{
            System.out.println("The given public key value " + publicKey + " is within the range of 0 - (" + prime + " - 1)");
        }

        if(message.compareTo(prime) >=0){
            System.out.println("The message provided is too big for the given prime " + prime + " please try a shorter message or larger prime value");
            return null;
        }
        else{
            System.out.println("The message " + message + " is within prime range");
        }

        //method for actually encoding the message

        cipherOne = generator.modPow(ephemeral, prime);
        cipherTwo = message.multiply(publicKey.modPow(ephemeral, prime)).mod(prime);
        System.out.println("Cipher one = (Generator ^ Ephemeral key) mod prime = " + cipherOne);
        System.out.println("Cipher Two = Message * (public Key ^ Ephemeral key) mod prime = " + cipherTwo);

        retVal[0] = cipherOne;
        retVal[1] = cipherTwo;
        return retVal;

    }

    //decrypts a given message
    private static String decrypt(String c1, String c2, String x, String p){
        String retVal = "";
        BigInteger cipherOne = new BigInteger(c1);
        BigInteger cipherTwo = new BigInteger(c2);
        BigInteger key = new BigInteger(x);
        BigInteger prime = new BigInteger(p);
        BigInteger[] numberChecks = {cipherOne, cipherTwo, key};
        String[] checksName = {"cipherOne", "cipherTwo", "key"};
        BigInteger sharedSecret;
        BigInteger modShared;
        BigInteger messageAsInts;
        String message;

        //number validity check
        if(!prime.isProbablePrime(112)){
            System.out.println("The given prime value " + prime + " is not a prime number please try again with a new number");
            return null;
        }
        else{
            System.out.println("The given prime value " + prime + " is a probable prime");
        }

        for(int i = 0; i < numberChecks.length; i++){
            if(numberChecks[i].compareTo(prime) >=0){
                System.out.println(numberChecks[i] + " must be smaller than " + prime + " please try again");
                return null;
            }
            else{
                System.out.println("The value of " + checksName[i] + " " +  numberChecks[i] + " is smaller than " + prime);
            }
        }
        
        sharedSecret = cipherOne.modPow(key, prime);
        System.out.println("The shared secret which is equivalent to the generator ^ ephemeral key which is calculated as (C1 ^ key) mod prime is: \n" + sharedSecret);

        System.out.println("Next we calculate the modular inverse of this");
        modShared = sharedSecret.modInverse(prime);
        System.out.println("Mod inverse = " + modShared);

        messageAsInts = cipherTwo.multiply(modShared).mod(prime);
        System.out.println("finally we can calculate the message by doing (C2 *(shared^-1) mod prime and then converting it back to text");
        message = charToString(messageAsInts);
        return message;
    }

    //encrypts a given message using randomly generated values
    private static void randomEncrypt(String message){
        SecureRandom secRand = new SecureRandom();
        BigInteger bigMessage = new BigInteger(stringToChar(message));

        BigInteger ranPrime = BigInteger.valueOf(secRand.nextLong() & Long.MAX_VALUE).add(bigMessage).nextProbablePrime();
        BigInteger ranGen = BigInteger.valueOf(secRand.nextLong() & Long.MAX_VALUE);
        BigInteger ranEphem = BigInteger.valueOf(secRand.nextLong() & Long.MAX_VALUE);
        BigInteger ranX = BigInteger.valueOf(secRand.nextLong() & Long.MAX_VALUE);
        BigInteger calcY;

        //checks randomly generated variables
        while(!generatorCheck(ranGen, ranPrime)){
            ranGen = BigInteger.valueOf(secRand.nextLong() & Long.MAX_VALUE);
        }

        while(ranEphem.compareTo(BigInteger.ZERO) <= 0 || ranEphem.compareTo(ranPrime.subtract(BigInteger.ONE)) >= 0){
           ranEphem = BigInteger.valueOf(secRand.nextLong() & Long.MAX_VALUE);
        }

        while(ranX.compareTo(BigInteger.ZERO) <= 0 || ranX.compareTo(ranPrime.subtract(BigInteger.ONE)) >= 0){
            ranX = BigInteger.valueOf(secRand.nextLong() & Long.MAX_VALUE);
        }

        calcY = ranGen.modPow(ranX, ranPrime);

        System.out.println("The private key is " + ranX);

        encrypt(message, ranPrime.toString(), ranGen.toString(), ranEphem.toString(), calcY.toString());
    }

    //encrypts a given message from a file
    private static void fileEncrypt(String pathName,String fileP, String fileG, String fileK, String fileY){
        File file = new File(pathName);

        try(Scanner fileScan = new Scanner(file)){
            while( fileScan.hasNextLine()){
                String fileMessage = fileScan.nextLine();
                encrypt(fileMessage, fileP, fileG, fileK, fileY);
            }
        }
        catch(FileNotFoundException e){
            System.out.println("The file could not be found please ensure that the name is correct");
            e.printStackTrace();
            return;
        }
    }

     //encrypts a given message from a file using random variables
    private static void randomFileEncrypt(String pathName){
        File file = new File(pathName);

        try(Scanner fileScan = new Scanner(file)){
            while( fileScan.hasNextLine()){
                String fileMessage = fileScan.nextLine();
                randomEncrypt(fileMessage);
            }
        }
        catch(FileNotFoundException e){
            System.out.println("The file could not be found please ensure that the name is correct");
            e.printStackTrace();
            return;
        }
    }

    //decrypts a given message from a file
    private static void fileDecrypt(String pathName){

        File file = new File(pathName);

        try(Scanner fileScan = new Scanner(file)){
            while( fileScan.hasNextLine()){
                String fileC1 = fileScan.nextLine();
                String fileC2 = fileScan.nextLine();
                String fileKey = fileScan.nextLine();
                String filePrime = fileScan.nextLine();
                decrypt(fileC1, fileC2, fileKey, filePrime);
            }
        }
        catch(FileNotFoundException e){
            System.out.println("The file could not be found please ensure that the name is correct");
            e.printStackTrace();
            return;
        }
    } 

    //runs encryption and decryption tests
    private static void systemTest(){
        String messages[] = new String[9];
        String prime[] = new String[9];
        String generator[] = new String[9];
        String ephemeral[] = new String[9];
        String publicY[] = new String[9];
        String cipherOne[] = new String[9];
        String cipherTwo[] = new String[9];
        String privateKey[] = new String[9];
        String testNames[] = {"all Letters", "Symbols", "Escape Character", "Maths", "Single Symbol", "Escape and text", "Line end challenge", "Repeated case", "Kitchen Sink"};
        String encryptionResult[] = new String[9];
        String decryptionResult[] = new String[9];

        System.out.println("testing system against files");
        System.out.println("\nTesting encryption\n");

        File file = new File("ElgamalTestEncrypt.txt");
        File file2 = new File("ElgamalTestDecrypt.txt");

        //pulls test variables from files
        try(Scanner fileScan = new Scanner(file)){
            int i = 0;
            while( fileScan.hasNextLine()){
                messages[i] = fileScan.nextLine();
                prime[i] = fileScan.nextLine();
                generator[i] = fileScan.nextLine();
                ephemeral[i] = fileScan.nextLine();
                publicY[i] = fileScan.nextLine();
                i++;
            }
        }
        catch(FileNotFoundException e){
            System.out.println("The file could not be found please ensure that the name is correct");
            e.printStackTrace();
            return;
        }

        try(Scanner fileScan = new Scanner(file2)){
            int i = 0;
            while( fileScan.hasNextLine()){
                cipherOne[i] = fileScan.nextLine();
                cipherTwo[i] = fileScan.nextLine();
                privateKey[i] = fileScan.nextLine();
                i++;
            }
        }
        catch(FileNotFoundException e){
            System.out.println("The file could not be found please ensure that the name is correct");
            e.printStackTrace();
            return;
        }

        System.out.println("\n\n Encryption Tests \n\n");

        for(int i = 0; i < 9; i++){
            System.out.println("\n" + testNames[i] + "\n");
            BigInteger results[] = encrypt(messages[i], prime[i], generator[i], ephemeral[i], publicY[i]);
            if(results[0].toString().equals(cipherOne[i]) && results[1].toString().equals(cipherTwo[i])){
                System.out.println("\n PASS");
                encryptionResult[i] = "PASS";
            }
            else{
                System.out.println("\n FAIL");
                encryptionResult[i] = "FAIL";
            }
        }

        System.out.println("\n\n Decryption Tests \n\n");

        for(int i = 0; i < 9; i++){
            System.out.println("\n" + testNames[i] + "\n");
            String cleanMessage = decrypt(cipherOne[i], cipherTwo[i], privateKey[i], prime[i]).replace("\u0001", "");
            if(cleanMessage.equals(messages[i])){
                System.out.println("\n PASS");
                decryptionResult[i] = "PASS";
            }
            else{
                debugStringDifference(decrypt(cipherOne[i], cipherTwo[i], privateKey[i], prime[i]), messages[i]);
                System.out.println("\n FAIL");
                decryptionResult[i] = "FAIL";
            }
        }

        System.out.println("\n\n Test Results \n\n");

        for(int i = 0; i < 9; i++){
            System.out.println("\n " + testNames[i]);
            System.out.println("Encryption result: " + encryptionResult[i]);
            System.out.println("Decryption result: " + decryptionResult[i]);
            System.out.println();
        }
    }

    //checks the given generator is valid
    private static boolean generatorCheck(BigInteger generator, BigInteger prime){
        boolean accepted = false;
        BigInteger euler = prime.subtract(BigInteger.ONE).divide(BigInteger.TWO);

        if((!generator.modPow(BigInteger.TWO, prime).equals(BigInteger.ONE)) && (!generator.modPow(euler, prime).equals(BigInteger.ONE)) && (!generator.equals(BigInteger.ONE))){
            accepted = true;
        }

        return accepted;
    }

    //converts the message to text
    private static String charToString(BigInteger plain){
        ArrayList<Integer> asciiList = new ArrayList<>();
        BigInteger thousand = new BigInteger("1000");
        BigInteger hundred = new BigInteger("100");
        String retVal = "";

        while (plain.compareTo(BigInteger.ZERO) > 0) {
            asciiList.add(plain.remainder(thousand).intValue()); //adds value to list
            plain = plain.divide(thousand);
        }
        
        //list needs to be reversed then converted to chars
        System.out.println("\nOriginal message conversion = ");
        for(int i =asciiList.size() - 1; i >= 0 ; i--){
            int charVal = asciiList.get(i);
            retVal += (char)charVal;
            System.out.print((char)charVal);
        }
        System.out.println();
        return retVal;
    }

    private static String stringToChar(String text){
        StringBuilder numRep = new StringBuilder("1"); //added for padding
        for(int i = 0; i < text.length(); i++){

            numRep.append(String.format("%03d", (int)text.charAt(i)));
        }
        return numRep.toString();
    }

    private static void menu(){
        System.out.println();
        System.out.println("Please choose from one of the following options:");
        System.out.println("(E)  Encrypt");
        System.out.println("(D)  Decrypt");
        System.out.println("(R)  Random variable encrypt");
        System.out.println("(FE) Encrypt with message from file");
        System.out.println("(FD) Decrypt with message from file");
        System.out.println("(FR) Random variable encrypt from file");
        System.out.println("(T)  Run test program for encryption and decryption");
        System.out.println("(Q)  Quit the program and return to the main menu");
    }


    public static void debugStringDifference(String s1, String s2) {
        if (s1 == null || s2 == null) {
            System.out.println("One of the strings is null!");
            return;
        }

        int minLength = Math.min(s1.length(), s2.length());
        
        for (int i = 0; i < minLength; i++) {
            if (s1.charAt(i) != s2.charAt(i)) {
                System.out.printf("Difference at index %d: '%s' (code: %d) vs '%s' (code: %d)%n", 
                    i, s1.charAt(i), (int)s1.charAt(i), s2.charAt(i), (int)s2.charAt(i));
                return;
            }
        }

        if (s1.length() != s2.length()) {
            System.out.printf("Strings match up to index %d, but lengths differ: %d vs %d%n", 
                minLength, s1.length(), s2.length());
            
            // Show the extra character in the longer string
            String longer = s1.length() > s2.length() ? s1 : s2;
            char extraChar = longer.charAt(minLength);
            System.out.printf("Extra character in longer string: '%s' (ASCII code: %d)%n", 
                extraChar, (int)extraChar);
        } else {
            System.out.println("Strings are actually identical.");
        }
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String userMessage;
        String userPrime;
        String userGenerator;
        String userEphem;
        String userY;
        String userC1;
        String userC2;
        String userKey;
        
        System.out.println("Welcome to the El-Gamal crypto-system.");
        menu();
        
        String input = "";
        
        while(input != "Q"){
            input = scan.nextLine().toUpperCase().replaceAll("[^a-zA-Z0-9]", "");

            switch(input){
                case "E":
                    System.out.println("Please enter your message:");
                    userMessage = scan.nextLine();
                    
                    System.out.println("\nPlease enter your prime number:");
                    userPrime = scan.nextLine();
                    
                    System.out.println("\nPlease enter your generator value:");
                    userGenerator = scan.nextLine();
                    
                    System.out.println("\nPlease enter your ephemeral K number:");
                    userEphem = scan.nextLine();

                    System.out.println("\nPlease enter your public Y number:");
                    userY = scan.nextLine();

                    System.out.println("\nAttempting to encrypt your message with the given information\n");
                    encrypt(userMessage, userPrime, userGenerator, userEphem, userY);
                    menu();
                    break;
                
                case "D":
                   System.out.println("Please enter your Cipher One number:");
                    userC1 = scan.nextLine();
                    
                    System.out.println("\nPlease enter your Cipher Two number:");
                    userC2 = scan.nextLine();
                    
                    System.out.println("\nPlease enter your private key:");
                    userKey = scan.nextLine();
                    
                    System.out.println("\nPlease enter your prime number:");
                    userPrime = scan.nextLine();

                    System.out.println("\nAttempting to decrypt your message with the given information\n");
                    decrypt(userC1,userC2,userKey,userPrime);
                    menu();
                    break;
                
                case "R":
                    System.out.println("Please enter your message:");
                    userMessage = scan.nextLine();
                    
                    System.out.println("\nAttempting to encrypt your message with the given information\n");
                    randomEncrypt(userMessage);
                    menu();
                    break;
                
                case "FE":
                    System.out.println("Please enter your file path:");
                    userMessage = scan.nextLine();
                    
                    System.out.println("\nPlease enter your prime number:");
                    userPrime = scan.nextLine();
                    
                    System.out.println("\nPlease enter your generator value:");
                    userGenerator = scan.nextLine();
                    
                    System.out.println("\nPlease enter your ephemeral K number:");
                    userEphem = scan.nextLine();

                    System.out.println("\nPlease enter your public Y number:");
                    userY = scan.nextLine();

                    System.out.println("\nAttempting to encrypt your message with the given information\n");
                    fileEncrypt(userMessage, userPrime, userGenerator, userEphem, userY);
                    menu();
                    break;

                case "FD":
                    System.out.println("Please enter your file path:");
                    userMessage = scan.nextLine();
                    
                    System.out.println("\nAttempting to decrypt your message with the given information\n");
                    fileDecrypt(userMessage);
                    menu();
                    break;

                case "FR":
                    System.out.println("Please enter your file path:");
                    userMessage = scan.nextLine();

                    System.out.println("\nAttempting to encrypt your message with the given information\n");
                    randomFileEncrypt(userMessage);
                    menu();
                    break;

                case "T":
                    systemTest();
                    menu();
                    break;
                case "Q":
                    System.out.println("returning to main menu\n\n");
                    return;
                default:
                    System.out.println("Input was not a valid listed option\n");
                    menu();
                    break;
            }
        }
    }
}
