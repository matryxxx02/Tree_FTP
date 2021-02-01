package treeFTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ScannerCommandFTP {

    /**
     * Scanner qui permet de tester le retour de certaines commande FTP
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        Socket commandSocket = new Socket("ftp.free.fr",21);
        BufferedReader reader = new BufferedReader(new InputStreamReader(commandSocket.getInputStream()));
        PrintWriter printer = new PrintWriter(commandSocket.getOutputStream(),true);
        System.out.println(reader.readLine());
        while(true){
            String line = sc.nextLine();
            printer.println(line);
            System.out.println(reader.readLine());
        }
    }
}
