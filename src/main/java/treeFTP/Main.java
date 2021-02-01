package treeFTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Main {

    public static void main(String[] args) throws IOException {
        //TODO : verifier que args[0] n'est pa svide
        //String urlServer = args[0];
        //String opt = args[3]
        //
        int depth = 3;
        boolean hideFile = false;
        for (String param : args){
            if(param.contains("-p"))
                depth = Integer.parseInt(param.split("=")[1]);
            if(param.contains("-a"))
                hideFile = true;
        }

        String urlServer = "ftp.ubuntu.com";
        String login = "anonymous";//args[1] != "" ? args[1] : "anonymous";
        String password = " ";//args[2] != "" ? args[2] : " ";
        Socket commandSocket = new Socket(urlServer,21);

        BufferedReader reader = new BufferedReader(new InputStreamReader(commandSocket.getInputStream()));
        PrintWriter printer = new PrintWriter(commandSocket.getOutputStream(),true);

        ClientFTP client = new ClientFTP(urlServer, login, password);
        client.connectionToServer(reader,printer);

        client.listDirectories(reader,printer,depth, hideFile);
    }
}
