import data.Database;

public class Main {

    public static void main(String[] args) {
        Database.init();

        Server server = new Server(8083);
        server.acceptConnections();
    }
}
