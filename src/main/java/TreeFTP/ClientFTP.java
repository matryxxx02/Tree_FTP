package TreeFTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * @author : Nicolas Fernandes
 *
 */
public class ClientFTP {
    private String urlServer;
    private String trait;
    private String login;
    private String password;
    private int port;
    private BufferedReader reader;
    private PrintWriter printer;

    public ClientFTP(String urlServer, String login, String password){
        this.urlServer = urlServer;
        this.login = login;
        this.password = password;
        this.trait = "";
        this.port = 21;
    }

    /**
     *
     * @throws IOException
     * @throws ConnectionException
     */
    public void connectionToServer() throws IOException, ConnectionException {
        Socket commandSocket = null;
        try{
            commandSocket = new Socket(urlServer,port);
        } catch (Exception e) {
            System.err.println("Une erreur s'est produite lors de la connexion, Veuillez verifier l'url du serveur ");
            System.exit(1);
        }
        this.reader = new BufferedReader(new InputStreamReader(commandSocket.getInputStream()));
        this.printer = new PrintWriter(commandSocket.getOutputStream(),true);

        reader.readLine();
        printer.println("USER "+login);
        this.checkConnection(reader.readLine());
        printer.println("PASS "+password);
        this.checkConnection(reader.readLine());
    }

    /**
     *
     * @param s
     * @throws ConnectionException
     */
    private void checkConnection(String s) throws ConnectionException {
        int errorCode = Integer.parseInt(s.split(" ")[0]);
        switch (errorCode){
            case 530:
                throw new ConnectionException("Ce serveur est uniquement disponible en anonyme.");
            case 430:
                throw new ConnectionException("Identifiant ou mot de passe incorrect.");
            case 332:
                throw new ConnectionException("Besoin d'un compte de connexion.");
        }
    }

    /**
     *
     * @return
     * @throws IOException
     */
    //return la socket sur la quel nous allons recevoir les datas
    public Socket passifMode() throws IOException {
        printer.println("PASV ");
        String pasv = reader.readLine();
        String[] array = pasv.substring(pasv.indexOf("(")+1,pasv.indexOf(")")).split(",");
        //(port_TCP = p1 * 256 + p2).(91,189,88,142,156,101)
        int port = Integer.parseInt(array[4])*256+ Integer.parseInt(array[5]);
        String urlData = array[0]+"."+array[1]+"."+array[2]+"."+array[3];
        return new Socket(urlData,port);
    }

    /**
     *
     */
    public String currentDirectory() throws IOException {
        printer.println("PWD");
        String currentDir = reader.readLine().split(" ")[1].replace("\"","");
        return currentDir.equals("/") ? "" : currentDir;
    }

    /**
     *
     */
    public int changeDirectory(String filename, String currentDir) throws IOException {
        String changeDir = "CWD " + currentDir + "/" + filename;
        printer.println(changeDir);
        String statusDirectory = reader.readLine();
        return Integer.parseInt(statusDirectory.split(" ")[0]);
    }

    /**
     *
     */
    public void listDirectories(int depth, boolean hideFile) throws IOException {
        Socket dataSocket = this.passifMode();
        BufferedReader dataReader = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));

        if (hideFile)
            printer.println("LIST -a");
        else
            printer.println("LIST ");

        String temp = trait;
        reader.readLine();
        reader.readLine();
        String files = dataReader.readLine();
        String currentDir = this.currentDirectory();

        while (files != null) {
            FileFTP f = new FileFTP(files);
            files = dataReader.readLine();

            if(f.fileIsPrintable() && files == null)
                System.out.println(trait + "└── " + f.getFilename());
            else if (f.fileIsPrintable())
                System.out.println(trait + "├── " + f.getFilename());

            if (f.fileIsDirectory()) {
                int cwdResult = this.changeDirectory(f.getFilename(), currentDir);
                if (cwdResult == 250 && f.fileIsPrintable()) {
                    trait += "│    ";
                    if (depth != 0) this.listDirectories(depth - 1, hideFile);
                    trait = trait.substring(0,trait.length()-5);
                }
            }
        }
        trait = temp;
        dataSocket.close();
    };

}
