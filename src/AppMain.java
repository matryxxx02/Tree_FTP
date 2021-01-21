import com.sun.security.ntlm.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class AppMain {

    public static void main(String[] args) throws IOException {
        String urlServer = "ftp.free.fr";
        Socket commandSocket = new Socket(urlServer,21);
        BufferedReader reader = new BufferedReader(new InputStreamReader(commandSocket.getInputStream()));
        PrintWriter printer = new PrintWriter(commandSocket.getOutputStream(),true);

        ClientFTP client = new ClientFTP(urlServer);
        client.connectionToServer(reader,printer);
        Socket dataSocket = client.passifMode(reader,printer);
        BufferedReader dataReader = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));

        //Liste les repertoires
        printer.println("LIST -a ");
        //System.out.println("LIST -a");
        reader.readLine();
        reader.readLine();
        String files = dataReader.readLine();
        while(files!=null){
            String[] infoFiles = files.split(" ");
            String fileName = infoFiles[infoFiles.length-1];
            System.out.println(fileName);
            if(infoFiles[0].charAt(0)=='d'){
                printer.println("CWD /"+fileName);
                //System.out.println("CWD /"+fileName);
                String statusDirectory = reader.readLine();
                //System.out.println(statusDirectory);
                //TODO: verifier le code de retour pour savoir si on peut lister le fichier
                if(Integer.parseInt(statusDirectory.split(" ")[0])==250 && !infoFiles[infoFiles.length-1].equals(".") && !infoFiles[infoFiles.length-1].equals("..")){
                    Socket repertorySocket = client.passifMode(reader,printer);
                    BufferedReader repertoryReader = new BufferedReader(new InputStreamReader(repertorySocket.getInputStream()));
                    printer.println("LIST -a ");
                    //System.out.println("LIST -a");
                    reader.readLine();
                    reader.readLine();
                    String file = repertoryReader.readLine();
                    while(file!=null){
                        System.out.println("    "+file.split(" ")[file.split(" ").length-1]);
                        file = repertoryReader.readLine();
                    }

                }
            }
            files=dataReader.readLine();
        }
        System.out.println(reader.readLine());
        System.out.println(reader.readLine());

        //printer.println("CWD /.mirrors18");
        //TODO: verifier le code de retour pour savoir si on peut lister le fichier
        //System.out.println(reader.readLine());

    }
}
