package idm.idm.servercom;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Used to make requests to a server.
 */

public class Server {

    public static final Server SERVER = new Server();

    private final String ADDRESS = "3.128.46.46";
    private URL url;

    public boolean login(String username, String password) {
        try {

        }
        catch (Exception exc) {
            System.out.println(exc.getMessage());
        }
        return false;
    }

    public void loginTask (String username, String password) throws IOException {

        url = new URL("http://3.128.46.46/");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");

        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
        }

    }

    public URLConnection openConnection() throws IOException {
        throw new RuntimeException("Stub");
    }

}
