import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientFTP {
    private String urlServer;
    private String trait = "";
    private int niveau = 0;

    public ClientFTP(String urlServer){
        this.urlServer = urlServer;
    }

    public void connectionToServer(BufferedReader reader, PrintWriter printer) throws IOException {
        System.out.println(reader.readLine());
        //TODO : username for connection or anonymous
        printer.println("USER anonymous");
        System.out.println(reader.readLine());
        //TODO : password or nothing.
        printer.println("PASS ");
        System.out.println(reader.readLine());
    }

    //return la socket sur la quel nous allons recevoir les datas
    public Socket passifMode(BufferedReader reader, PrintWriter printer) throws IOException {
        printer.println("PASV ");
        String pasv = reader.readLine();
        String[] array = pasv.substring(pasv.indexOf("("),pasv.indexOf(")")).split(",");
        //(port_TCP = p1 * 256 + p2).
        int port = Integer.parseInt(array[4])*256+ Integer.parseInt(array[5]);
        return new Socket(urlServer,port);
    }

    public String currentDirectory(BufferedReader reader, PrintWriter printer) throws IOException {
        printer.println("PWD");
        String currentDir = reader.readLine().split(" ")[1].replace("\"","");
        //System.out.println("currentDir: "+currentDir);
        return currentDir.equals("/") ? "" : currentDir;
    }

    public void listDirectories(BufferedReader reader, PrintWriter printer) throws IOException {
        Socket dataSocket = this.passifMode(reader,printer);
        BufferedReader dataReader = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));

        //lister les fichiers
        printer.println("LIST ");
        //pour chaque fichier verifier si c'est un repertoire et si on peux l'ouvrir
        reader.readLine();
        reader.readLine();
        String files = dataReader.readLine();
        String temp = trait;
        //on se replace
        //├──
        //│
        //└──
        String currentDir = this.currentDirectory(reader, printer);
        niveau++;

        while(files!=null){
            File f = new File(files);
            //on affiche le nom du fichier
            if(f.fileIsPrintable()) System.out.println(trait+"├── "+f.getFilename());
            /*if(niveau==2){
                niveau=0;
                break;
            }*/
            if(f.fileIsDirectory()) {

                //on essaye de rentrer dans le repertoire
                String changeDir = "CWD "+currentDir+"/"+ f.getFilename();
                //System.out.println(changeDir);
                printer.println(changeDir);
                //on verifie si on a reussi a rentrer dans le repertoire
                String statusDirectory = reader.readLine();

                if(Integer.parseInt(statusDirectory.split(" ")[0])==250 && f.fileIsPrintable()){
                    //afficher l'interieur et appel recursif
                    trait+="│    ";
                    this.listDirectories(reader, printer);
                }
            }
            files=dataReader.readLine();
        }
        trait = temp;
    };

}
