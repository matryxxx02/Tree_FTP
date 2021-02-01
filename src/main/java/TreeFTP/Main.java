package TreeFTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static List<String> options = new ArrayList<>(Arrays.asList("-p", "-a"));

    public static boolean isNotParam(String opt){
        return (!options.contains(opt.substring(0,2)) && opt != "");
    }

    /**
     * Methode principal qui verifie les paramètres et qui fait tourner le client FTP.
     * @param args
     * @throws IOException
     * @throws ConnectionException
     * @throws UrlException
     */
    public static void main(String[] args) throws IOException, ConnectionException, UrlException {
        int depth = -1;
        boolean hideFile = false;
        for (String param : args){
            if(param.contains("-p"))
                depth = Integer.parseInt(param.split("=")[1]);
            if(param.contains("-a"))
                hideFile = true;
        }

        String urlServer = args[0];
        if(urlServer.isEmpty())
            throw new UrlException("L'url ne peux pas être vide");

        String login = (args.length>2 && isNotParam(args[1])) ? args[1] : "anonymous";
        String password = (args.length>3 && isNotParam(args[2])) ? args[2] : " ";

        ClientFTP client = new ClientFTP(urlServer, login, password);
        client.connectionToServer();
        client.listDirectories(depth, hideFile);
    }
}
