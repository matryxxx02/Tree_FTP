package TestTreeFTP;

import TreeFTP.ClientFTP;
import TreeFTP.ClientFTPBuilder;
import TreeFTP.ConnectionException;
import TreeFTP.UrlException;
import org.junit.Test;

import java.io.IOException;

public class ConnectionExceptionTest {

    @Test(expected = ConnectionException.class)
    public void testNotAnonymeUser() throws ConnectionException, IOException, UrlException {
        ClientFTP client = new ClientFTPBuilder("ftp.ubuntu.com").login("toto").build();
        client.connectionToServer();
    }
}
