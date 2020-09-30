package idm.idm.servercom;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

/**
 * Used to make requests to a server.
 */

public class Server {

    public static final Server SERVER = new Server();

    private final String ADDRESS = "http://3.128.46.46/";
    private URL url;

    private static String status;

    public boolean login(String username, String password)
    {
        try {
            new LoginRequest().execute(username, password).get();
            if(status.contains("200"))
                return true;
        }
        catch(Exception exc)
        {
            System.out.println(exc.getMessage());
        }
        return false;
    }

    private class LoginRequest extends AsyncTask<String, Integer, JSONObject>
    {

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
        }
        @Override
        protected JSONObject doInBackground(String... strings) {
            try {
                loginTask(strings[0], strings[1]);
            }
            catch(JSONException jsonexc)
            {
                System.out.println(jsonexc.getMessage());
                System.exit(1);
            }
            return null;
        }
    }

    private void loginTask (String username, String password) throws JSONException {

        try {

            url = new URL(ADDRESS+"login");

            JSONObject loginData = new JSONObject();
            loginData.put("username", username);
            loginData.put("password", password);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            Log.d(con.toString(), "HttpURLConnection established...");

            con.setRequestMethod("POST");
            //con.setDoOutput(true); //don't include this in POST request
            con.setRequestProperty("Content-Type", "application/json");

            DataOutputStream outputStream = new DataOutputStream(con.getOutputStream());
            outputStream.writeBytes(loginData.toString());
            outputStream.flush();
            outputStream.close();

            Log.d(loginData.toString(), "LOGIN DATA");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            System.out.println("Got inputStream: " + in);
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            String verification = con.getHeaderField("status");
            System.out.println("Header Field: " + verification);

            System.out.println("Response: " + response.toString());

            status = response.toString();

            con.disconnect();

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
