import handler.ConnectionHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;

    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void acceptConnections() {

        while (true) {
            System.out.println("Waiting for connection");
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Connected");
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                ConnectionHandler connectionHandler = new ConnectionHandler(socket, reader, writer);
                connectionHandler.run();
                System.out.println("Connection closed");

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
