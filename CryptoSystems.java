import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class CryptoSystems extends Application {

    @Override
    public void start(Stage primaryStage) {
        TabPane tabPane = new TabPane();

        // --- TAB 1: POLYBIUS SQUARE ---
        Tab polybiusTab = new Tab("Polybius Square");
        polybiusTab.setClosable(false); // Prevents users from closing the tab
        polybiusTab.setContent(createPolybiusUI());

        // --- TAB 2: CAESAR CIPHER ---
        Tab caesarCipherTab = new Tab("Caesar Cipher");
        caesarCipherTab.setClosable(false);
        caesarCipherTab.setContent(new Label(" Nikolas's code goes here!"));

        // --- TAB 3: VIGENERE CIPHER ---
        Tab vigenereCipherTab = new Tab("Vigenere Cipher");
        vigenereCipherTab.setClosable(false);
        vigenereCipherTab.setContent(new Label(" Trusts's code goes here!"));

        // --- TAB 4: RSA ---
        Tab rsaTab = new Tab("RSA");
        rsaTab.setClosable(false);
        rsaTab.setContent(new Label(" Nikki's code goes here!"));

        // --- TAB 5: STEGANOGRAPHY ---
        Tab steganographyTab = new Tab("Steganography");
        steganographyTab.setClosable(false);
        steganographyTab.setContent(new Label(" Brad's code goes here!"));

        // --- TAB 6: EL GAMAL or ELLIPTIC CURVE ---
        Tab elGamalOrEllipticCurveTab = new Tab("ElGamal or Elliptic Curve");
        elGamalOrEllipticCurveTab.setClosable(false);
        elGamalOrEllipticCurveTab.setContent(new Label(" James's code goes here!"));

        tabPane.getTabs().addAll(polybiusTab, caesarCipherTab, vigenereCipherTab, rsaTab, steganographyTab, elGamalOrEllipticCurveTab);

        Scene scene = new Scene(tabPane, 600, 500);
        primaryStage.setTitle("CS Security Group Project - Cryptosystems");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createPolybiusUI() {
        PolybiusSquare cipher = new PolybiusSquare();

        HBox columns = new HBox(40);
        columns.setPadding(new Insets(30));
        columns.setStyle("-fx-background-color: #f4f4f4; -fx-text-fill: black;");

        VBox encodeColumn = new VBox(15);
        Label encodeLabel = new Label("ENCODE MESSAGE");
        encodeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: black;");

        TextField encodeInput = new TextField();
        encodeInput.setPromptText("Type text here...");
        Button btnEncode = new Button("Explain Encoding");

        Label encodeOutput = new Label();
        encodeOutput.setFont(Font.font("Monospaced", 13));
        encodeOutput.setWrapText(true);
        encodeOutput.setStyle("-fx-text-fill: black;");

        ScrollPane encodeScroll = new ScrollPane(encodeOutput);
        encodeScroll.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-border-color: transparent;");
        encodeScroll.setFitToWidth(true);
        encodeScroll.setPrefHeight(500);

        encodeColumn.getChildren().addAll(encodeLabel, encodeInput, btnEncode, encodeScroll);

        VBox decodeColumn = new VBox(15);
        Label decodeLabel = new Label("DECODE COORDINATES");
        decodeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: black;");

        TextField decodeInput = new TextField();
        decodeInput.setPromptText("Type numbers here...");
        Button btnDecode = new Button("Explain Decoding");

        Label decodeOutput = new Label();
        decodeOutput.setFont(Font.font("Monospaced", 13));
        decodeOutput.setWrapText(true);
        decodeOutput.setStyle("-fx-text-fill: black;");

        ScrollPane decodeScroll = new ScrollPane(decodeOutput);
        decodeScroll.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-border-color: transparent;");
        decodeScroll.setFitToWidth(true);
        decodeScroll.setPrefHeight(500);

        decodeColumn.getChildren().addAll(decodeLabel, decodeInput, btnDecode, decodeScroll);

        btnEncode.setOnAction(e -> {
            String text = encodeInput.getText();
            if (text != null && !text.isEmpty()) {
                encodeOutput.setText(cipher.getStepByStep(text));
            }
        });

        btnDecode.setOnAction(e -> {
            String text = decodeInput.getText();
            if (text != null && !text.isEmpty()) {
                decodeOutput.setText(cipher.getStepByStepDecode(text));
            }
        });

        HBox.setHgrow(encodeColumn, Priority.ALWAYS);
        HBox.setHgrow(decodeColumn, Priority.ALWAYS);
        columns.getChildren().addAll(encodeColumn, decodeColumn);

        return new VBox(columns);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
