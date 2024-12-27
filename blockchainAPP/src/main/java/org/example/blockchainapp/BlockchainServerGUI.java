package org.example.blockchainapp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class BlockchainServerGUI extends Application {
    private static final int SERVER_PORT = 8888;

    private TextArea logsArea;
    private ServerSocket serverSocket;

    // La liste représentant la blockchain (simple pour cette démonstration)
    private final List<String> blockchain = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Interface GUI
        Label headerLabel = new Label("SERVEUR BLOCKCHAIN\n\n");
        headerLabel.getStyleClass().add("header");

        VBox root = new VBox(18);
        root.getChildren().add(headerLabel);
        root.getStyleClass().add("vbox");

        Button startServerButton = new Button("1 ---> DEMARRER LE SERVEUR");
        Button stopServerButton = new Button("2 ---> ARRETER LE SERVEUR");
        Button quitButton = new Button("3 ---> QUITTER");
        quitButton.getStyleClass().add("quitter");

        logsArea = new TextArea("---> LOGS DU SERVEUR :");
        logsArea.setEditable(false);

        root.getChildren().addAll(
                startServerButton,
                stopServerButton,
                quitButton,
                logsArea
        );

        startServerButton.setOnAction(event -> startServer());
        stopServerButton.setOnAction(event -> stopServer());
        quitButton.setOnAction(event -> {
            stopServer();
            primaryStage.close();
        });

        Scene scene = new Scene(root, 750, 800);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        primaryStage.setTitle("Interface Serveur: BLOCKCHAINSERVER");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void startServer() {
        Thread serverThread = new Thread(() -> {
            try {
                serverSocket = new ServerSocket(SERVER_PORT);
                logMessage("SERVEUR BLOCKCHAIN EN ÉCOUTE SUR LE PORT " + SERVER_PORT);

                while (!serverSocket.isClosed()) {
                    Socket clientSocket = serverSocket.accept();
                    String clientAddress = clientSocket.getInetAddress().toString();
                    logMessage("CLIENT CONNECTÉ : " + clientAddress);

                    new Thread(new ClientHandler(clientSocket)).start();
                }
            } catch (IOException e) {
                logMessage("Erreur : " + e.getMessage());
            }
        });
        serverThread.setDaemon(true);
        serverThread.start();
    }

    private void stopServer() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                logMessage("SERVEUR ARRÊTÉ.");
            }
        } catch (IOException e) {
            logMessage("Erreur lors de l'arrêt du serveur : " + e.getMessage());
        }
    }

    private void logMessage(String message) {
        Platform.runLater(() -> logsArea.appendText(message + "\n"));
    }

    private class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {

                try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                    out.println("Bienvenue sur le serveur Blockchain!");

                    String clientMessage;
                    while ((clientMessage = in.readLine()) != null) {
                        logMessage("Message reçu du client : " + clientMessage);

                        switch (clientMessage) {
                            case "AFFICHER_BLOCKCHAIN":
                                afficherBlockchain(out);
                                break;
                            case "AJOUTER_BLOC":
                                String blocData = in.readLine(); // Lire les données du bloc
                                ajouterBloc(blocData, out);
                                break;
                            case "MINER_BLOC":
                                minerBloc(out);
                                break;
                            case "TESTER_DOUBLE_DEPENSE":
                                testerDoubleDepense(out);
                                break;
                            default:
                                out.println("Commande inconnue !");
                                break;
                        }
                    }
                } catch (IOException e) {
                    logMessage("Erreur avec le client : " + e.getMessage());

            }
        }

        private void afficherBlockchain(PrintWriter out) {
            out.println("=== BLOCKCHAIN ===");
            if (blockchain.isEmpty()) {
                out.println("La blockchain est vide.");
            } else {
                for (int i = 0; i < blockchain.size(); i++) {
                    out.println("Bloc " + i + ": " + blockchain.get(i));
                }
            }
            out.println("END"); // Signal de fin pour le client
        }

        private void ajouterBloc(String data, PrintWriter out) {
            blockchain.add(data);
            out.println("Bloc ajouté : " + data);
            logMessage("Bloc ajouté : " + data);
        }

        private void minerBloc(PrintWriter out) {
            // Simule le minage en ajoutant un bloc de type "miné"
            String minedBlock = "Bloc miné (timestamp: " + System.currentTimeMillis() + ")";
            blockchain.add(minedBlock);
            out.println("BLOC_MINÉ");
            logMessage("Bloc miné et ajouté : " + minedBlock);
        }

        private void testerDoubleDepense(PrintWriter out) {
            out.println("Test de la double dépense :");
            if (blockchain.size() < 2) {
                out.println("Pas assez de blocs pour simuler une double dépense.");
            } else {
                out.println("Simulation : La double dépense est détectée et empêchée !");
            }
            out.println("END");
        }
    }
}
