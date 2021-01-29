
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Main {

    public static void main(String[] args) throws IOException {
        String urlServer = "ftp.ubuntu.com";
        Socket commandSocket = new Socket(urlServer,21);
        BufferedReader reader = new BufferedReader(new InputStreamReader(commandSocket.getInputStream()));
        PrintWriter printer = new PrintWriter(commandSocket.getOutputStream(),true);

        ClientFTP client = new ClientFTP(urlServer);
        client.connectionToServer(reader,printer);
        Socket dataSocket = client.passifMode(reader,printer);
        BufferedReader dataReader = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));

        client.listDirectories(reader,printer);
    }
}
