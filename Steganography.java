import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Steganography {

    public static void main(String[] args) {
        if (args.length < 1) {
            printUsage();
            return;
        }

        String command = args[0].toLowerCase();

        try {
            if (command.equals("embed")) {
                if (args.length < 4) {
                    printUsage();
                    return;
                }
                String inputImagePath = args[1];
                String outputImagePath = args[args.length - 1];
                StringBuilder messageBuilder = new StringBuilder();
                for (int i = 2; i < args.length - 1; i++) {
                    if (i > 2) {
                        messageBuilder.append(" ");
                    }
                    messageBuilder.append(args[i]);
                }
                String message = messageBuilder.toString();
                embedMessage(inputImagePath, message, outputImagePath);
                System.out.println("Message embedded successfully into: " + outputImagePath);
            } else if (command.equals("extract")) {
                if (args.length != 2) {
                    printUsage();
                    return;
                }
                String imagePath = args[1];
                String message = extractMessage(imagePath);
                System.out.println("Extracted message: " + message);
            } else {
                printUsage();
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("  java Steganography embed <inputImage> <message> <outputImage>");
        System.out.println("  java Steganography extract <inputImage>");
        System.out.println("Note: wrap multi-word messages in quotes, e.g. \"Hello World.\"");
    }

    public static void embedMessage(String inputImagePath, String message, String outputImagePath) throws IOException {
        BufferedImage image = ImageIO.read(new File(inputImagePath));
        if (image == null) {
            throw new IOException("Could not read image: " + inputImagePath);
        }

        byte[] messageBytes = message.getBytes("UTF-8");
        int messageLength = messageBytes.length;

        // Convert length to 4 bytes
        byte[] lengthBytes = new byte[4];
        lengthBytes[0] = (byte) (messageLength >> 24);
        lengthBytes[1] = (byte) (messageLength >> 16);
        lengthBytes[2] = (byte) (messageLength >> 8);
        lengthBytes[3] = (byte) messageLength;

        // Combine length and message
        byte[] data = new byte[4 + messageLength];
        System.arraycopy(lengthBytes, 0, data, 0, 4);
        System.arraycopy(messageBytes, 0, data, 4, messageLength);

        // Embed bits, 3 bits per pixel in RGB order
        int width = image.getWidth();
        int height = image.getHeight();
        int dataIndex = 0;
        int bitIndex = 0;

        outer: for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (dataIndex >= data.length) break outer;

                int rgb = image.getRGB(x, y);
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;

                for (int component = 0; component < 3; component++) {
                    if (dataIndex >= data.length) break;

                    int bit = (data[dataIndex] >> (7 - bitIndex % 8)) & 1;
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
                image.setRGB(x, y, newRgb);
            }
        }

        if (dataIndex < data.length) {
            throw new IOException("Image too small to embed the message");
        }

        // Save the image
        ImageIO.write(image, "png", new File(outputImagePath));
    }

    public static String extractMessage(String imagePath) throws IOException {
        BufferedImage image = ImageIO.read(new File(imagePath));
        if (image == null) {
            throw new IOException("Could not read image: " + imagePath);
        }

        int width = image.getWidth();
        int height = image.getHeight();
        int totalPixels = width * height;
        int totalBits = totalPixels * 3;

        // Extract length first
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
            int component = bitIndex % 3;
            int bit;

            if (component == 0) {
                bit = ((rgb >> 16) & 0xFF) & 1;
            } else if (component == 1) {
                bit = ((rgb >> 8) & 0xFF) & 1;
            } else {
                bit = (rgb & 0xFF) & 1;
            }

            lengthBytes[bitIndex / 8] |= (bit << (7 - bitIndex % 8));
        }

        int messageLength = ((lengthBytes[0] & 0xFF) << 24) | ((lengthBytes[1] & 0xFF) << 16) | ((lengthBytes[2] & 0xFF) << 8) | (lengthBytes[3] & 0xFF);
        if (messageLength < 0) {
            throw new IOException("Invalid hidden message length: " + messageLength);
        }

        int requiredBits = 32 + messageLength * 8;
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
}