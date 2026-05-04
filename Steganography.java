import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Steganography {

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

        // Embed bits
        int width = image.getWidth();
        int height = image.getHeight();
        int dataIndex = 0;
        int bitIndex = 0;

        outer: for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (dataIndex >= data.length) break outer;

                int rgb = image.getRGB(x, y);
                int component = bitIndex % 3; // 0=R, 1=G, 2=B
                int bit = (data[dataIndex] >> (7 - bitIndex % 8)) & 1;

                int newRgb;
                if (component == 0) { // Red
                    int red = (rgb >> 16) & 0xFF;
                    red = (red & 0xFE) | bit;
                    newRgb = (rgb & 0xFF00FFFF) | (red << 16);
                } else if (component == 1) { // Green
                    int green = (rgb >> 8) & 0xFF;
                    green = (green & 0xFE) | bit;
                    newRgb = (rgb & 0xFFFF00FF) | (green << 8);
                } else { // Blue
                    int blue = rgb & 0xFF;
                    blue = (blue & 0xFE) | bit;
                    newRgb = (rgb & 0xFFFFFF00) | blue;
                }

                image.setRGB(x, y, newRgb);

                bitIndex++;
                if (bitIndex % 8 == 0) {
                    dataIndex++;
                }
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

        // Extract length first
        byte[] lengthBytes = new byte[4];
        int bitIndex = 0;
        int dataIndex = 0;

        outer: for (int y = 0; y < height && dataIndex < 4; y++) {
            for (int x = 0; x < width && dataIndex < 4; x++) {
                int rgb = image.getRGB(x, y);
                int component = bitIndex % 3;
                int bit;

                if (component == 0) { // Red
                    bit = ((rgb >> 16) & 0xFF) & 1;
                } else if (component == 1) { // Green
                    bit = ((rgb >> 8) & 0xFF) & 1;
                } else { // Blue
                    bit = (rgb & 0xFF) & 1;
                }

                lengthBytes[dataIndex] |= (bit << (7 - bitIndex % 8));

                bitIndex++;
                if (bitIndex % 8 == 0) {
                    dataIndex++;
                }
            }
        }

        int messageLength = ((lengthBytes[0] & 0xFF) << 24) | ((lengthBytes[1] & 0xFF) << 16) | ((lengthBytes[2] & 0xFF) << 8) | (lengthBytes[3] & 0xFF);

        // Extract message
        byte[] messageBytes = new byte[messageLength];
        dataIndex = 0;

        outer2: for (int y = 0; y < height && dataIndex < messageLength; y++) {
            for (int x = 0; x < width && dataIndex < messageLength; x++) {
                int rgb = image.getRGB(x, y);
                int component = bitIndex % 3;
                int bit;

                if (component == 0) { // Red
                    bit = ((rgb >> 16) & 0xFF) & 1;
                } else if (component == 1) { // Green
                    bit = ((rgb >> 8) & 0xFF) & 1;
                } else { // Blue
                    bit = (rgb & 0xFF) & 1;
                }

                messageBytes[dataIndex] |= (bit << (7 - bitIndex % 8));

                bitIndex++;
                if (bitIndex % 8 == 0) {
                    dataIndex++;
                }
            }
        }

        return new String(messageBytes, "UTF-8");
    }
}