package com.example.berkeleysockets;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Button botaoEnviar;
    @FXML
    private TextField campoMensagem;
    @FXML
    private VBox vboxMensagens;
    @FXML
    private ScrollPane scrollMensagens;

    private Server server;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            server = new Server(new ServerSocket(1234));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro ao criar o servidor");
        }

        vboxMensagens.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number valorAntigo, Number valorNovo) {
                scrollMensagens.setVvalue((Double) valorNovo);
            }
        });

        server.receberMensagemCliente(vboxMensagens);

        botaoEnviar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent evento) {
                String mensagemEnviar = campoMensagem.getText();
                if (!mensagemEnviar.isEmpty()) {
                    HBox hBox = new HBox();
                    hBox.setAlignment(Pos.CENTER_RIGHT);
                    hBox.setPadding(new Insets(5, 5, 5, 10));

                    Text texto = new Text(mensagemEnviar);
                    TextFlow textFlow = new TextFlow(texto);

                    textFlow.setStyle("-fx-color: rgb(239, 242, 255)" +
                            "-fx-background-color: rgb(15, 125, 252)" +
                            "-fx-background-radius: 20px"
                    );
                    textFlow.setPadding(new Insets(5, 10, 5, 10));
                    texto.setFill(Color.color(0.934, 0.945, 0.996));

                    hBox.getChildren().add(textFlow);
                    vboxMensagens.getChildren().add(hBox);

                    server.enviarMensagemCliente(mensagemEnviar);
                    campoMensagem.clear();
                }
            }
        });
    }
    public static void adicionarLabel(String mensagemCliente, VBox vbox) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 10, 5, 5));

        Text texto = new Text(mensagemCliente);
        TextFlow textFlow = new TextFlow(texto);

        textFlow.setStyle(
                "-fx-background-color: rgb(233, 233, 235)" +
                "-fx-background-radius: 20px"
        );
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        hBox.getChildren().add(textFlow);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vbox.getChildren().add(hBox);
            }
        });
    }
}