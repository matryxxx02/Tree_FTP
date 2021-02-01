package TreeFTP;

/**
 * Cette Classe permet de modéliser les fichiers obtenu par la commande FTP 'LIST'.
 * @author : Nicolas Fernandes.
 */
public class FileFTP {
    private String filename;
    private char type;


    public FileFTP(String f) {
        String[] infoFiles = f.split(" ");
        this.filename = infoFiles[infoFiles.length - 1];
        this.type = infoFiles[0].charAt(0);
    }


    public String getFilename() {
        return filename;
    }

    public char getType() {
        return type;
    }

    /**
     * @return true ou false si le fichier est un repertoire et si ce n'est pas l'un des deux repertoire : (. ou ..)
     */
    public boolean fileIsDirectory() {
        return type == 'd' && fileIsPrintable();
    }

    /**
     *
     * @return true ou false si le fichier n'est pas l'un des deux repertoire : (. ou ..)
     */
    public boolean fileIsPrintable() {
        return !filename.equals(".") && !filename.equals("..");
    }

}
