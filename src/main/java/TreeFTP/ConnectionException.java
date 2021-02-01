package TreeFTP;

/**
 * Classe qui permet de relever les exceptions lors de la connexion du clientFTP au serveur
 */
public class ConnectionException extends Exception {
    public ConnectionException(){
        super();
    }

    public ConnectionException(String s){
        super(s);
    }
}
