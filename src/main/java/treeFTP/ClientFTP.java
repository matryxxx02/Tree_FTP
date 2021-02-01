package treeFTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientFTP {
    private String urlServer;
    private String trait = "";
    private String login;
    private String password;

    public ClientFTP(String urlServer, String login, String password){
        this.urlServer = urlServer;
        this.login = login;
        this.password = password;
    }

    /*
    *
    *
     */
    public void connectionToServer(BufferedReader reader, PrintWriter printer) throws IOException {
        System.out.println(reader.readLine());
        printer.println("USER "+login);
        System.out.println(reader.readLine());
        printer.println("PASS "+password);
        System.out.println(reader.readLine());
        //throw une exception si la connection a echoué
    }

    //return la socket sur la quel nous allons recevoir les datas
    public Socket passifMode(BufferedReader reader, PrintWriter printer) throws IOException {
        printer.println("PASV ");
        String pasv = reader.readLine();
        String[] array = pasv.substring(pasv.indexOf("(")+1,pasv.indexOf(")")).split(",");
        //(port_TCP = p1 * 256 + p2).
        int port = Integer.parseInt(array[4])*256+ Integer.parseInt(array[5]);
        String urlData = array[0]+"."+array[1]+"."+array[2]+"."+array[3];
        return new Socket(urlData,port);
    }

    public String currentDirectory(BufferedReader reader, PrintWriter printer) throws IOException {
        printer.println("PWD");
        String currentDir = reader.readLine().split(" ")[1].replace("\"","");
        return currentDir.equals("/") ? "" : currentDir;
    }

    public void listDirectories(BufferedReader reader, PrintWriter printer, int depth, boolean hideFile) throws IOException {
        Socket dataSocket = this.passifMode(reader,printer);
        BufferedReader dataReader = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));

        //lister les fichiers
        if (hideFile)
            printer.println("LIST -a");
        else
            printer.println("LIST ");

        //pour chaque fichier verifier si c'est un repertoire et si on peux l'ouvrir
        reader.readLine();
        reader.readLine();
        String files = dataReader.readLine();
        String temp = trait;
        String currentDir = this.currentDirectory(reader, printer);
        while (files != null) {
            File f = new File(files);
            //on affiche le nom du fichier
            files = dataReader.readLine();
            if(f.fileIsPrintable() && files == null)
                System.out.println(trait + "└── " + f.getFilename());
            else if (f.fileIsPrintable())
                System.out.println(trait + "├── " + f.getFilename());
            if (f.fileIsDirectory()) {
                //on essaye de rentrer dans le repertoire
                String changeDir = "CWD " + currentDir + "/" + f.getFilename();
                printer.println(changeDir);
                //on verifie si on a reussi a rentrer dans le repertoire
                String statusDirectory = reader.readLine();
                if (Integer.parseInt(statusDirectory.split(" ")[0]) == 250 && f.fileIsPrintable()) {
                    //afficher l'interieur et appel recursif
                    trait += "│    ";
                    if (depth > 0) {
                        this.listDirectories(reader, printer, depth - 1, hideFile);
                    }
                    trait = trait.substring(0,trait.length()-5);
                }
            }
        }
        trait = temp;
        dataSocket.close();
    };

}
