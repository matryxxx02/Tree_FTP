package TreeFTP;

import java.io.IOException;
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

        ClientFTPBuilder clientBuilder = new ClientFTPBuilder(urlServer);

        if(args.length>2 && isNotParam(args[1]))
            clientBuilder.login(args[1]);

        if(args.length>3 && isNotParam(args[2]))
            clientBuilder.password(args[2]);

        ClientFTP client = clientBuilder.build();
        client.connectionToServer();
        client.listDirectories(depth, hideFile);
    }
}
