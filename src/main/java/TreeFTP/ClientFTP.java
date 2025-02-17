package TreeFTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Cette Classe permet de communiquer avec un serveur FTP via son url et le port.
 * Elle contient les methodes qui peuvent servir a communiquer avec le serveur.
 * Ici l'objectif principal est de lister tous les repertoires et fichiers de ce serveur.
 * @author : Nicolas Fernandes.
 */
public class ClientFTP {
    private String urlServer;
    private String trait;
    private String login;
    private String password;
    private int port;
    private BufferedReader reader;
    private PrintWriter printer;

    public String getUrlServer() {
        return urlServer;
    }

    public ClientFTP(ClientFTPBuilder builder){
        this.urlServer = builder.getUrlServer();
        this.login = builder.getLogin();
        this.password = builder.getPassword();
        this.trait = builder.getTrait();
        this.port = builder.getPort();
    }

    /**
     * Cette methode permet de se connecter au serveur FTP et de s'authentifier.
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
     * Cette methode permet de relever le type d'exception qu'on peut rencontrer à l'authentification.
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
     * Cette methode permet de passer le serveur FTP en mode passif.
     * Le client reçoit une chaine de caractère (91,189,88,142,156,101), ces caractère correspont a l'url et
     * le port du socket de donnée. C'est via cette socket que nous recevons les données.
     * Voici la forme de l'url et le port url = ip_a.ip_b.ip_c.ip_d, port = ip_e*256+ip_f.
     * @return la socket connecté a l'url et au port obtenu.
     * @throws IOException
     */
    //return la socket sur la quel nous allons recevoir les datas
    public Socket passifMode() throws IOException {
        printer.println("PASV ");
        String pasv = reader.readLine();
        return new Socket(this.dataSocketURL(pasv),this.dataSocketPort(pasv));
    }

    /**
     * Cette methode renvoi l'url a laquel le serveur nous demande de se connecter
     * @param pasv le resultat de la commande FTP 'PASV'
     * @return l'url du socket de donnée calculé avec pasv
     */
    public String dataSocketURL(String pasv) {
        String[] array = pasv.substring(pasv.indexOf("(")+1,pasv.indexOf(")")).split(",");
        return array[0]+"."+array[1]+"."+array[2]+"."+array[3];
    }

    /**
     * Cette methode renvoi le port sur lequel le serveur nous demande de se connecter
     * @param pasv le resultat de la commande FTP 'PASV'
     * @return le port du socket de donnée calculé avec pasv
     */
    public int dataSocketPort(String pasv) {
        String[] array = pasv.substring(pasv.indexOf("(")+1,pasv.indexOf(")")).split(",");
        return Integer.parseInt(array[4])*256+ Integer.parseInt(array[5]);
    }

    /**
     * @return Cette methode retourne le chemin du repertoire sur lequel on se trouve grâce à la commande FTP 'PWD'
     * @throws IOException
     */
    public String currentDirectory() throws IOException {
        printer.println("PWD");
        String currentDir = reader.readLine().split(" ")[1].replace("\"","");
        return currentDir.equals("/") ? "" : currentDir;
    }

    /**
     * Cette methode permet de changer de repertoire, grâce à la commande FTP 'CWD'.
     * @param dirname Elle prend en parametre le nom du repertoire
     * @param currentDir et le repertoire courant.
     * @return elle retourne true ou false en fonction du code obtenu (changement de repertoire reussi ou non).
     * @throws IOException
     */
    public boolean changeDirectory(String dirname, String currentDir) throws IOException {
        String changeDir = "CWD " + currentDir + "/" + dirname;
        printer.println(changeDir);
        String statusDirectory = reader.readLine();
        return Integer.parseInt(statusDirectory.split(" ")[0]) == 250;
    }

    /**
     * Cette methode permet de d'afficher l'arborescence du serveur sur lequel le client FTP est connecté.
     * C'est une methode recursif, lorsque le fichier est un repertoire elle s'appel recursivement.
     * @param depth Elle prend en parametre un entier qui est la profondeur de l'arborescence
     * @param hideFile et un boolean qui permet d'activer l'affichage des fichiers cachés.
     * @throws IOException
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
                if (this.changeDirectory(f.getFilename(), currentDir) && f.fileIsPrintable()) {
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
