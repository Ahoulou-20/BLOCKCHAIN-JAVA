package org.example.blockchainapp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.URL;

public class BlockchainClientGUI extends Application {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8888;

    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;

    private Label statusLabel;
    private Button connectButton;
    private Button disconnectButton;
    private TextArea resultArea;

    private Thread listenerThread;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        statusLabel = new Label("Non connecté");

        VBox root = new VBox(18);
        root.getStyleClass().add("vbox");

        connectButton = new Button("Se connecter au serveur");
        disconnectButton = new Button("Se déconnecter du serveur");
        disconnectButton.setDisable(true);

        Button afficherBlockchainButton = new Button("1 ---> AFFICHER LA BLOCKCHAIN");
        Button ajouterBlocButton = new Button("2 ---> AJOUTER UN BLOC");
        Button minerBlocButton = new Button("3 ---> MINER UN BLOC");
        Button testerDoubleDepenseButton = new Button("4 ---> TESTER L’IMPOSSIBILITÉ DE LA DOUBLE DÉPENSE");
        Button quitterButton = new Button("5 ---> QUITTER");
        quitterButton.getStyleClass().add("quitter");

        resultArea = new TextArea("--->");
        resultArea.setEditable(false);

        root.getChildren().addAll(
                statusLabel,
                connectButton,
                disconnectButton,
                afficherBlockchainButton,
                ajouterBlocButton,
                minerBlocButton,
                testerDoubleDepenseButton,
                quitterButton,
                resultArea
        );

        connectButton.setOnAction(event -> connectToServer());
        disconnectButton.setOnAction(event -> disconnectFromServer());
        quitterButton.setOnAction(event -> primaryStage.close());

        afficherBlockchainButton.setOnAction(event -> sendCommand("AFFICHER_BLOCKCHAIN"));
        ajouterBlocButton.setOnAction(event -> ajouterBloc());
        minerBlocButton.setOnAction(event -> sendCommand("MINER_BLOC"));
        testerDoubleDepenseButton.setOnAction(event -> sendCommand("TESTER_DOUBLE_DEPENSE"));

        Scene scene = new Scene(root, 750, 800);
        URL cssURL = getClass().getResource("styles.css");
        if (cssURL != null) {
            scene.getStylesheets().add(cssURL.toExternalForm());
        } else {
            System.err.println("Fichier CSS non trouvé : styles.css");
        }

        primaryStage.setTitle("Interface Client: BLOCKCHAINCLIENT");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void connectToServer() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            statusLabel.setText("CONNECTÉ AU SERVEUR BLOCKCHAIN");
            connectButton.setDisable(true);
            disconnectButton.setDisable(false);

            startListeningToServer();

        } catch (IOException e) {
            showError("Impossible de se connecter au serveur : " + e.getMessage());
        }
    }

    private void disconnectFromServer() {
        try {
            if (listenerThread != null && listenerThread.isAlive()) {
                listenerThread.interrupt();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }

            statusLabel.setText("DÉCONNECTÉ DU SERVEUR BLOCKCHAIN");
            connectButton.setDisable(false);
            disconnectButton.setDisable(true);

        } catch (IOException e) {
            showError("Erreur lors de la déconnexion : " + e.getMessage());
        }
    }

    private void ajouterBloc() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Ajouter un Bloc");
        dialog.setHeaderText("Entrez les données pour le nouveau bloc :");
        dialog.setContentText("Données du bloc :");

        dialog.showAndWait().ifPresent(data -> sendCommand("AJOUTER_BLOC", data));
    }

    private void sendCommand(String command) {
        sendCommand(command, null);
    }

    private void sendCommand(String command, String data) {
        if (socket == null || socket.isClosed()) {
            showError("Connectez-vous au serveur avant d'envoyer une commande !");
            return;
        }

        out.println(command);
        if (data != null) {
            out.println(data);
        }
    }

    private void startListeningToServer() {
        listenerThread = new Thread(() -> {
            try {
                String serverResponse;
                while (!Thread.currentThread().isInterrupted() && (serverResponse = in.readLine()) != null) {
                    String finalResponse = serverResponse;
                    Platform.runLater(() -> resultArea.appendText(finalResponse + "\n"));
                }
            } catch (IOException e) {
                if (!Thread.currentThread().isInterrupted()) {
                    Platform.runLater(() -> showError("Connexion perdue avec le serveur : " + e.getMessage()));
                }
            }
        });
        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    private void showError(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
}
