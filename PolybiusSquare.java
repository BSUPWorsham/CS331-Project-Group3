import java.util.Scanner;

public class PolybiusSquare {

    private char[][] square = new char[5][5];

    public PolybiusSquare() {
        char alphabet = 'A';

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                if (alphabet == 'J') {
                    alphabet++;
                }

                square[row][col] = alphabet++;
            }
        }
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        PolybiusSquare poly = new PolybiusSquare();

        System.out.println("\n--- Polybius Cipher ---");
        System.out.println("(E)ncode");
        System.out.println("(D)ecode");
        System.out.print("> ");

        String option = scan.nextLine().toLowerCase();

        switch (option) {
            case "e":
            case "encode":
                System.out.print("Enter text to encode: ");
                String text = scan.nextLine();

                System.out.println(poly.getStepByStep(text));
                break;

            case "d":
            case "decode":
                System.out.print("Enter coordinates (ex: 11 24 35): ");
                String code = scan.nextLine();

                System.out.println(poly.getStepByStepDecode(code));
                break;

            default:
                System.out.println("Invalid option.");
        }

        System.out.println("\nReturning to menu...\n");
    }

    public String getGridVisual() {
        StringBuilder sb = new StringBuilder("Current Polybius Square Grid:\n");
        sb.append("   1 2 3 4 5\n");
        sb.append("  -----------\n");

        for (int i = 0; i < 5; i++) {
            sb.append(i + 1).append("| ");

            for (int j = 0; j < 5; j++) {
                sb.append(square[i][j]).append(" ");
            }

            sb.append("\n");
        }

        return sb.toString();
    }

    public String getStepByStep(String text) {
        StringBuilder explanation = new StringBuilder();

        String cleanText = text.toUpperCase()
                .replace("J", "I")
                .replaceAll("[^A-Z]", "");

        explanation.append(getGridVisual()).append("\n");
        explanation.append("Input: ")
                .append(text)
                .append(" -> Processed: ")
                .append(cleanText)
                .append("\n\n");

        explanation.append("Encoding Steps:\n");

        for (char letter : cleanText.toCharArray()) {
            for (int row = 0; row < 5; row++) {
                for (int col = 0; col < 5; col++) {
                    if (square[row][col] == letter) {
                        explanation.append("'")
                                .append(letter)
                                .append("' found at [Row ")
                                .append(row + 1)
                                .append(", Col ")
                                .append(col + 1)
                                .append("] -> ")
                                .append(row + 1)
                                .append(col + 1)
                                .append("\n");
                    }
                }
            }
        }

        return explanation.toString();
    }

    public String getStepByStepDecode(String code) {
        StringBuilder explanation = new StringBuilder();

        explanation.append(getGridVisual()).append("\n");
        explanation.append("Input Coordinates: ")
                .append(code)
                .append("\n\n");

        String[] pairs = code.trim().split("\\s+");

        explanation.append("Decoding Steps:\n");

        StringBuilder decodedWord = new StringBuilder();

        for (String pair : pairs) {

            if (pair.length() != 2) {
                explanation.append(pair)
                        .append(" -> Invalid coordinate format\n");
                continue;
            }

            int row = Character.getNumericValue(pair.charAt(0)) - 1;
            int col = Character.getNumericValue(pair.charAt(1)) - 1;

            if (row >= 0 && row < 5 && col >= 0 && col < 5) {
                char found = square[row][col];

                decodedWord.append(found);

                explanation.append(pair)
                        .append(" -> Row ")
                        .append(row + 1)
                        .append(", Col ")
                        .append(col + 1)
                        .append(" = '")
                        .append(found)
                        .append("'\n");
            } else {
                explanation.append(pair)
                        .append(" -> Invalid coordinates\n");
            }
        }

        explanation.append("\nDecoded Message: ")
                .append(decodedWord);

        return explanation.toString();
    }
}