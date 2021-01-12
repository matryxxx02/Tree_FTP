import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientFTP {
    public static void main(String[] args) throws IOException {

        Scanner sc = new Scanner(System.in);
        Socket s = new Socket("ftp.ubuntu.com",21);
        BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
        PrintWriter printer = new PrintWriter(s.getOutputStream());
        System.out.println(reader.readLine());
        printer.println("AUTH TLS");
        printer.flush();
        System.out.println(reader.readLine());

        String line = sc.nextLine();
        while(line!=""){
            line = sc.nextLine();

        }

    }
}
