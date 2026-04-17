public class PolybiusSquare {
    private char[][] square = new char[5][5];

    public PolybiusSquare() {
        char alphabet = 'A';
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                if (alphabet == 'J') alphabet++;
                square[row][col] = alphabet++;
            }
        }
    }

    public String getGridVisual() {
        StringBuilder sb = new StringBuilder("Current Polybius Square Grid:\n");
        sb.append("   1 2 3 4 5\n -------------\n");
        for (int i = 0; i < 5; i++) {
            sb.append((i + 1) + "| ");
            for (int j = 0; j < 5; j++) {
                sb.append(square[i][j] + " ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public String getStepByStep(String text) {
        StringBuilder explanation = new StringBuilder();
        String cleanText = text.toUpperCase().replace("J", "I").replaceAll("[^A-Z]", "");

        explanation.append(getGridVisual()).append("\n");
        explanation.append("Input: ").append(text).append(" -> Processed: ").append(cleanText).append("\n\n");

        for (char c : cleanText.toCharArray()) {
            for (int r = 0; r < 5; r++) {
                for (int c_idx = 0; c_idx < 5; c_idx++) {
                    if (square[r][c_idx] == c) {
                        explanation.append(String.format("'%c' found at [Row %d, Col %d] -> %d%d\n",
                                c, r + 1, c_idx + 1, r + 1, c_idx + 1));
                    }
                }
            }
        }
        return explanation.toString();
    }

    public String getStepByStepDecode(String code) {
        StringBuilder explanation = new StringBuilder();
        explanation.append(getGridVisual()).append("\n");
        explanation.append("Input Coordinates: ").append(code).append("\n\n");

        // Split the input by spaces to get each coordinate pair
        String[] pairs = code.trim().split("\\s+");
        explanation.append("Step-by-step translation:\n");

        for (String pair : pairs) {
            if (pair.length() != 2) continue; // Basic validation

            // Convert chars to numbers, then subtract 1 for 0-based array index
            int row = Character.getNumericValue(pair.charAt(0)) - 1;
            int col = Character.getNumericValue(pair.charAt(1)) - 1;

            if (row >= 0 && row < 5 && col >= 0 && col < 5) {
                char found = square[row][col];
                explanation.append(String.format(" - %s -> Row %d, Col %d matches character '%c'\n",
                        pair, row + 1, col + 1, found));
            } else {
                explanation.append(" - ").append(pair).append(" -> Invalid coordinates!\n");
            }
        }
        return explanation.toString();
    }
}