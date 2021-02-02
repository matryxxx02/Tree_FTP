package TestTreeFTP;

import TreeFTP.ClientFTP;
import TreeFTP.ClientFTPBuilder;
import TreeFTP.ConnectionException;
import TreeFTP.UrlException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ClientFTPTest {
    @Test(expected = UrlException.class)
    public void testUrlException() throws UrlException, IOException, ConnectionException {
        ClientFTP client = new ClientFTPBuilder("").build();
        client.connectionToServer();
    }

    @Test
    public void testDataSocket() throws UrlException {
        ClientFTP client = new ClientFTPBuilder("ftp.ubuntu.com").build();
        String pasvResult = "(73,13,156,129,217,103)";
        String url = "73.13.156.129";
        int port = 217*256+103;

        assertEquals(url,client.dataSocketURL(pasvResult));
        assertEquals(port,client.dataSocketPort(pasvResult));
    }
}
