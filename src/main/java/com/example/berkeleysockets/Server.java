package com.example.berkeleysockets;

import javafx.scene.layout.VBox;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public Server(ServerSocket serverSocket) {
        try {
            this.serverSocket = serverSocket;
            this.socket = serverSocket.accept();
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro ao criar o servidor");
            fecharTudo(socket, bufferedReader, bufferedWriter);
        }
    }

    public void enviarMensagemCliente(String mensagemCliente) {
        try {
            bufferedWriter.write(mensagemCliente);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro ao enviar mensagem para o cliente");
            fecharTudo(socket, bufferedReader, bufferedWriter);
        }
    }

    public void receberMensagensCliente(VBox vboxMensagem) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()) {
                    try {
                        String mensagemCliente = bufferedReader.readLine();
                        Controller.adicionarLabel(mensagemCliente, vboxMensagem);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Erro ao receber mensagem do cliente");
                        fecharTudo(socket, bufferedReader, bufferedWriter);
                        break;
                    }
                }
            }
        }).start();
    }

    public void fecharTudo(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if(bufferedReader != null) {
                bufferedReader.close();
            }
            if(bufferedWriter != null) {
                bufferedWriter.close();
            }
            if(socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            System.out.println("Erro ao fechar o servidor");
            e.printStackTrace();
        }
    }
}
