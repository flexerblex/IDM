package idm.idm.servercom;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

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

    public void login(String username, String password) {
        Log.d("test", "Login Method Hit.");
        try {
            new LoginRequest().execute(username, password).get();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    private class LoginRequest extends AsyncTask<String, Integer, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {
            try {
                loginTask(strings[0], strings[1]);
            }
            catch(JSONException e) {
                System.out.println(e.getMessage());
                System.exit(1);
            }
            return null;
        }
    }

    private void loginTask (String username, String password) throws JSONException {

        try {
            url = new URL("http://3.128.46.46/");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            Log.d(conn.toString(), "HttpURLConnection established...");

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
                Log.d(response.toString(), "SERVER RESPONSE");
            }
        }
        catch(IOException exc)
        {
            System.out.println(exc.getMessage());
        }
    }

    public URLConnection openConnection() throws IOException {
        throw new RuntimeException("Stub");
    }

}
