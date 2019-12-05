package handler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;

public class ConnectionHandler extends Thread {
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    public ConnectionHandler(Socket socket, BufferedReader reader, BufferedWriter writer) {
        this.socket = socket;
        this.reader = reader;
        this.writer = writer;
    }

    public void run() {
        RequestHandler requestHandler = new RequestHandler(reader, writer);
        requestHandler.handle();

        try {
            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
