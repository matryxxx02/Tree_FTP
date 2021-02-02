package TreeFTP;

public class ClientFTPBuilder {
    private String urlServer;
    private String trait;
    private String login;
    private String password;
    private int port;

    public ClientFTPBuilder(String url){
        this.urlServer = url;
        this.login = "anonymous";
        this.password = "";
        this.port = 21;
        this.trait = "";
    }

    public String getUrlServer() {
        return urlServer;
    }

    public String getTrait() {
        return trait;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public int getPort() {
        return port;
    }

    public ClientFTPBuilder login(String login){
        this.login = login;
        return this;
    }

    public ClientFTPBuilder password(String password){
        this.password = password;
        return this;
    }

    public ClientFTPBuilder port(int port){
        this.port = port;
        return this;
    }

    public ClientFTP build() throws UrlException {
        ClientFTP client = new ClientFTP(this);
        validationClientFTPObject(client);
        return client;
    }

    public void validationClientFTPObject(ClientFTP client) throws UrlException {
        if(client.getUrlServer().isEmpty())
            throw new UrlException("L'url ne peux pas être vide");
    }
}
