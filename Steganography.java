import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.channels.Pipe.SourceChannel;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class Steganography {

    static enum SRC_OPT { FILE, STRING };

    static String fpIn;
    static File fIn;
    static String fpOut;
    static String msgSrc;
    static String fpSrc;
    static File fileSrc;
    static long datasize;
    static SRC_OPT src = SRC_OPT.STRING;
    static Scanner scan;

    static byte[] dirtyBytes;
    static byte[] metadata;
    private static boolean doDisplay = true;



    public static void main(String[] args) {

        System.out.println("\n--- Steganographic Encryption ---");
        System.out.println("E(m)bed");
        System.out.println("E(x)tract");
        System.out.print("> ");

        scan = new Scanner(System.in);
        String option = scan.nextLine().toLowerCase();

        switch (option) {
            case "m":
                // get msg and size of msg
                while (dirtyBytes == null) {
                    System.out.print("Enter text to encode: ");
                    msgSrc = scan.nextLine();
                    try {
                        dirtyBytes = msgSrc.getBytes("UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        System.out.println("Please input a UTF-8 compatible string!");
                    }
                }


                metadata = new byte[4];
                for (int i = 0; i < 4; i++) {
                    metadata[i] = (byte) (msgSrc.length() >> (32 - (8*(i+1))));
                }

                // metadata[0] = (byte) (msgSrc.length() >> 24);
                // lengthBytes[1] = (byte) (msgSrc.length() >> 16);
                // lengthBytes[2] = (byte) (msgSrc.length() >> 8);
                // lengthBytes[3] = (byte) msgSrc.length();

                String fpTemp;
                while (fIn == null) {
                    System.out.print("Enter filepath of PNG: ");
                    fpTemp = scan.nextLine();
                    if (fpTemp.equalsIgnoreCase("default")) {
                        fpTemp = "Assets/Steganography/Input/500px-Mona_Lisa.png";
                    }
                    File file = new File(fpTemp);
                    if (file.exists()) {
                        fIn = file;
                    } else {
                        System.out.println("File not found!");
                    }
                }
                displayData();
                try {
                    embedMessage();
                } catch(Exception e) {
                    throw new RuntimeException(e);
                }

            break;

    

                

            case "x":
                System.out.print("Enter filepath: ");
                String path = scan.nextLine();
                if (path.equals("default")) {
                    path = "Assets/Steganography/Output/OUT.png";
                }
                try {
                    System.out.println("Extracted message: " + extractMessage(path));
                } catch (Exception e) {
                    System.out.println("Could not extract message!");
                }
                break;

            default:
                System.out.println("Invalid option.");
        }
        System.out.println("Press enter to continue");
        scan.nextLine();
        System.out.println("\nReturning to menu...\n");

    }

    private static void displayData() {
        System.out.println("\nString Information for: " + msgSrc);
        // System.out.println("\tByte array: ");
        showByteArray();
        System.out.println("Press enter to continue");
        scan.nextLine();
    }

    private static void showByteArray() {

        StringBuilder metadataStr = new StringBuilder();
        for (byte b : metadata) {
            metadataStr.append("[0x" + Integer.toHexString(b) + "]");
            metadataStr.append(" ");
        }

        char[] cs = msgSrc.toCharArray();

        System.out.println("Bytes to be encoded:");
        System.out.println("-- Metadata");
        System.out.println("\tLength: " + cs.length + " = " + metadataStr);
        System.out.println("-- Payload");
        
        for (char c : cs) {
            // try {
                byte[] bs = String.valueOf(c).getBytes();
                StringBuilder bStr = new StringBuilder();
                for (byte b : bs) {
                    bStr.append("[0x" + Integer.toHexString((b & 0xFF)) + "] ");
                }
                System.out.println("\t" + c + ": " + bStr);
            
        }

        System.out.println("Total Size: " + (dirtyBytes.length + metadata.length) + " bytes");
        System.out.println("  Requires " + (Math.ceil(((dirtyBytes.length + metadata.length)*8)/3)) + " pixels to embed message");
    }

    public static void encryptMenu() {
        System.out.println("\n--- Embedding Options: ---");
        System.out.println("(F)ilepath I/O");
        System.out.println("(D)ata to encode");
        System.out.println("(B)egin");
        System.out.println("(Q)uit");
    }

    public static void filepathMenu() {
        System.out.println("\n--- Filepath Options: ---");
        System.out.println("\tCurrent input PNG filepath: " + fpIn != null ? fpIn : "< File Not Specified! >");
        System.out.println("\t\tChange (I)nput");
        System.out.println("\tCurrent output PNG filepath: " + fpOut != null ? fpIn : "< File Not Specified! >");
        System.out.println("\t\tChange (O)utput");
        System.out.println("(R)eturn");
    }

    public static void dataMenu() {

        // String fp = "";

        // System.out.println("\n--- Data Options: ---");
        // System.out.println("\tCurrent Data Source: " + src);
        // if (src == SRC_OPT.FILE) {
            

        // } else if (src == SRC_OPT.STRING) {
        //     msgSrc.
        // } else {
        //     throw new RuntimeException("Specified source not yet implemented!");
        // }
        // System.out.println("(F) - Select file as source");
        // System.out.println("(S) - Prompt for string (default)");



    }

    private static long queryDataFile(String fp) throws FileNotFoundException, Steganography.DataException, Steganography.InvalidAccessException {
        File f = new File(fp);

        // valid data guards
        if (!f.exists()) {throw new FileNotFoundException("File does not exist at specified path: " + fp);}
        if (!f.isFile()) {throw new DataException("File is not a regular file!"); }
        if (!f.canRead()) {throw new InvalidAccessException("File is not readable by process!");}
        fileSrc = f;
        return f.getTotalSpace();
    }

    public static void embedMessage() throws IOException {
        BufferedImage image = ImageIO.read(fIn);
        if (image == null) {
            throw new IOException("Could not read image: " + fpIn);
        }

        // byte[] messageBytes = msgSrc.getBytes("UTF-8");
        // int messageLength = messageBytes.length;

        // Convert length to 4 bytes
        // int is 32 bit, i.e. 4 bytes. Break them up in MSB order for later
        // byte[] lengthBytes = new byte[4];
        // lengthBytes[0] = (byte) (messageLength >> 24);
        // lengthBytes[1] = (byte) (messageLength >> 16);
        // lengthBytes[2] = (byte) (messageLength >> 8);
        // lengthBytes[3] = (byte) messageLength;

        // Combine length and message
        byte[] data = new byte[4 + dirtyBytes.length];
        System.arraycopy(metadata, 0, data, 0, 4);
        System.arraycopy(dirtyBytes, 0, data, 4, dirtyBytes.length);

        // Embed bits, 3 bits per pixel in RGB order
        int width = image.getWidth();
        int height = image.getHeight();
        int dataIndex = 0;
        int bitIndex = 0;

        // confirm msg can be stored:
        int pxCount = width * height;
        int maxBits = 3 * pxCount; // each pixel can hold max 3 bits
        double maxBytes = Math.ceil(maxBits/8.0);
        if (maxBytes < data.length) {
            throw new IOException("Image is too small to store data!");
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (dataIndex >= data.length) break /*outer*/;

                int rgb = image.getRGB(x, y);
                // each R, G, B is 4 byte i.e. 2 hex. bitmask & 0xFF to isolate least 2 bytes,
                // shift to specify color channel
                int red = (rgb >> 16) & 0xFF; 
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;
                String rgbIn = toHexByte(red) + " " + toHexByte(green) + " " + toHexByte(blue);

                if(doDisplay) {
                    System.out.println("\n--- Pixel: (" + x + ", " + y + ") -----------");
                    // rgbIn("[R] [G] [B] = [0x" + Integer.toHexString(red) + "] [0x" + Integer.toHexString(green) + "] [0x" + Integer.toHexString(blue) +"]");
                    
                    boolean overlaps = (((bitIndex % 8) + 3) > 8) && dataIndex < data.length - 1;
                    System.out.println("Encoding Bits:");
                    StringBuilder ptrStr = new StringBuilder();
                    StringBuilder targetStr = new StringBuilder();
                    if (dataIndex == 0) {
                        ptrStr.append(" v");
                        targetStr.append(toHexByte(data[0]));
                        for (int i = 1; i < 4; i++) {
                            if (i < data.length) {
                                targetStr.append(toHexByte(data[dataIndex+i]));
                            }
                        }
                        if (overlaps) {
                            ptrStr.append("      v");
                        }
                        
                    } else {
                        ptrStr.append("        v");
                        // include prev and next 4 bytes (if available)
                        for (int i = -1; i < 4; i++) {
                            if (data.length > dataIndex + i) {
                                targetStr.append(toHexByte(data[dataIndex+i]));
                                targetStr.append(" ");
                            }
                        }
                        if (overlaps) {
                            ptrStr.append("      v");
                        }
                    }
                    System.out.println(ptrStr);
                    System.out.println(targetStr);
                
                }
                

                for (int component = 0; component < 3; component++) {
                    if (dataIndex >= data.length) break;

                    int bit = (data[dataIndex] >> (7 - bitIndex % 8)) & 1;
                    /*
                        bit refers to the current bit of data we're encoding in the image.
                        each member of the data[] array is a byte, so we must input 8 bits
                        before we can move on to the next index. Once we're through data[],
                        we have encoded all or information.

                        changing the LSB of any individual R, G, or B value makes a visually
                        imperceivable difference, so that is where we are "stuffing" our dirty bits

                        R, G, and B values are each 1 byte. Each pixel has an RGB value.
                        Therefore, the amount of bits we can store is the amount of pixels * 3.

                        To aid in extracting the data later on, we also use the first 4 bytes of data
                        to tell the extractor the length of the message they can expect to extract from the
                        raw bytes.

                        If we were wanting to hide the string "Hello, world!" in an image, we would sequentially
                        add all 8 bits of 'H' into the first ceil(8/3) pixels.


                    Ex: if rgb is 0x8944CA , and we're putting in a UTF-8 'e' (0x65),

                    'e' = 0b 0110 0101

                    red = 0b 1000 1001

                    
                    */

                    if (component == 0) {
                        red = (red & 0xFE) | bit;
                    } else if (component == 1) {
                        green = (green & 0xFE) | bit;
                    } else {
                        blue = (blue & 0xFE) | bit;
                    }

                    bitIndex++;
                    if (bitIndex % 8 == 0) {
                        dataIndex++;
                    }
                }

                int newRgb = (rgb & 0xFF000000) | (red << 16) | (green << 8) | blue;
                if (doDisplay) { 
                    System.out.println("RGB in:  " + rgbIn);
                    System.out.println("RGB out: " + toHexByte(red) + " " + toHexByte(green) + " " + toHexByte(blue)); }
                
                image.setRGB(x, y, newRgb);
                if (doDisplay) {
                    System.out.println("Enter to continue, or ; to skip all messages");
                    String next = scan.nextLine();
                    if (next.equals(";")) {
                        doDisplay = false;
                    }
                }
            }
        }

        if (dataIndex < data.length) {
            throw new IOException("Image too small to embed the message");
        }
    

        // Save the image
        ImageIO.write(image, "png", new File("Assets/Steganography/Output/OUT.png"));
        System.out.println("Image has been saved to Assets/Steganography/Output/OUT.png\n");
}


    public static String extractMessage(String imagePath) throws IOException {

        // load img into buffer
        BufferedImage image = ImageIO.read(new File(imagePath));
        if (image == null) {
            throw new IOException("Could not read image: " + imagePath);
        }

        // get pixel count
        int width = image.getWidth();
        int height = image.getHeight();
        int totalPixels = width * height;
        int totalBits = totalPixels * 3;

        // Extract length metadata
        byte[] lengthBytes = new byte[4];
        int bitIndex = 0;

        for (; bitIndex < 32; bitIndex++) {
            int pixelIndex = bitIndex / 3;
            if (pixelIndex >= totalPixels) {
                throw new IOException("Image does not contain enough data to read message length");
            }
            int x = pixelIndex % width;
            int y = pixelIndex / width;
            int rgb = image.getRGB(x, y);
            int color = bitIndex % 3;
            int bit;

            if (color == 0) {
                bit = ((rgb >> 16) & 0xFF) & 1;
            } else if (color == 1) {
                bit = ((rgb >> 8) & 0xFF) & 1;
            } else {
                bit = (rgb & 0xFF) & 1;
            }

            lengthBytes[bitIndex / 8] |= (bit << (7 - bitIndex % 8));
        }

        int messageLength = ((lengthBytes[0] & 0xFF) << 24) | 
                            ((lengthBytes[1] & 0xFF) << 16) | 
                            ((lengthBytes[2] & 0xFF) << 8) |
                            (lengthBytes[3] & 0xFF);

        if (messageLength < 0) {
            throw new IOException("Invalid hidden message length: " + messageLength);
        }

        // 32 bit integer (length) + each byte of the message bytes[]
        int requiredBits = 32 + (messageLength * 8);
        if (requiredBits > totalBits) {
            throw new IOException("Image does not contain enough data to read the hidden message");
        }

        byte[] messageBytes = new byte[messageLength];
        for (int i = 0; i < messageLength * 8; i++, bitIndex++) {
            int pixelIndex = bitIndex / 3;
            int x = pixelIndex % width;
            int y = pixelIndex / width;
            int rgb = image.getRGB(x, y);
            int component = bitIndex % 3;
            int bit;

            if (component == 0) {
                bit = ((rgb >> 16) & 0xFF) & 1;
            } else if (component == 1) {
                bit = ((rgb >> 8) & 0xFF) & 1;
            } else {
                bit = (rgb & 0xFF) & 1;
            }

            messageBytes[i / 8] |= (bit << (7 - i % 8));
        }

        return new String(messageBytes, "UTF-8");
    }

    private static class DataException extends Exception {
        private DataException(String err) { super(err); }
    }

    private static class InvalidAccessException extends Exception {
        private InvalidAccessException(String err) { super(err); }
    }

    private static String toHexByte(int val) {
        String hex = Integer.toHexString(val);
        if (hex.length() < 2) {
            return "[0x0" + hex + "]";
        }
        return "[0x" + hex + "]";
    }
}

