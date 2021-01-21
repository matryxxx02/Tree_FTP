import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.CharBuffer;
import java.util.Scanner;

public class ClientFTP {
    private String urlServer;

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
        return new Socket("ftp.free.fr",port);
    }

    public void listDirectory() {

    }

}
