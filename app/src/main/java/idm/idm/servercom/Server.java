package idm.idm.servercom;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

import idm.idm.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import okhttp3.OkHttpClient;
import okhttp3.*;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Lily
 * Used to make requests to a server.
 */

public class Server {

    public static final Server SERVER = new Server();

    private final String ADDRESS = "http://3.128.46.46/";
    private URL url;
    public  static String session_cookie;

    public static String firstName;
    public static String lastName;
    public static String id;
    public static String email;
    public static String username;
    public static String password;
    public static Integer isAdmin;
    public static Integer isLocked;
  
    private static String status;

    public boolean login(String username, String password)
    {
        try {
            new LoginRequest().execute(username, password).get();
            if(status.contains("200")) {
                return true;
            }

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
        @RequiresApi(api = Build.VERSION_CODES.O)
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loginTask (String username, String password) throws JSONException {

        try {

            JSONObject loginData = new JSONObject();
            loginData.put("username", username);
            loginData.put("password", password);



            //Creating Objects
            url = new URL(ADDRESS+"login");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            Log.d(con.toString(), "HttpURLConnection established...");

            //set up connection
            con.setRequestMethod("POST");
            //con.setDoOutput(true); //don't include this in POST request
            con.setRequestProperty("Content-Type", "application/json");

            DataOutputStream outputStream = new DataOutputStream(con.getOutputStream());
            outputStream.writeBytes(loginData.toString());
            outputStream.flush();
            outputStream.close();

            Log.d(loginData.toString(), "LOGIN DATA"); //debug

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            System.out.println("Got inputStream: " + in);
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            System.out.println("Response: " + response.toString());

            status = response.toString();

            JSONObject jsonObj = new JSONObject(status);

            session_cookie = jsonObj.getString("message");
            System.out.println(session_cookie); //debug

            DecodeJWT();

            con.disconnect();

        }

        catch(IOException exc)
        {
            System.out.println(exc.getMessage());
        }
    }

    /**
     * Register ////////////////////////////////////////////////////////////////////////////////////////////////////////
     */

    public void register(JSONObject postData) {
        try {
            new registerRequest().execute(postData.toString());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private class registerRequest extends AsyncTask<String, Integer, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {
            try {
                registerTask(strings[0]);
            }
            catch(JSONException e) {
                System.out.println(e.getMessage());
                System.exit(1);
            }
            return null;
        }
    }

    private void registerTask (String JSON) throws JSONException {

        try {

            Log.i("JSON", JSON);

            url = new URL(ADDRESS+"register");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept","application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(JSON);
            wr.flush();
            wr.close();

            Log.i("STATUS", String.valueOf(conn.getResponseCode()));
            Log.i("MSG" , conn.getResponseMessage());

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Lock ////////////////////////////////////////////////////////////////////////////////////////////////////////
     */

    public void Lock(String lockuser, String function) {
        try {
            new lockRequest().execute(lockuser, function);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private class lockRequest extends AsyncTask<String, Integer, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {
            try {
                lockTask(strings[0], strings[1]);
            }
            catch(JSONException e) {
                System.out.println(e.getMessage());
                System.exit(1);
            }
            return null;
        }
    }

    private void lockTask (String username, String function) throws JSONException {

        try {

            //Log.i("JSON", JSON);

            JSONObject json = new JSONObject();
            json.put("user", username);
            json.put("task", function);

            url = new URL(ADDRESS+"manage");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept","application/json");
            conn.setRequestProperty("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoxLCJmbmFtZSI6IkxpbHkiLCJsbmFtZSI6IlN1YXUiLCJlbWFpbCI6ImxpbGlhc3VhdUBvYWtsYW5kLmVkdSIsImlzX2FkbWluIjoxLCJpc0xvY2tlZCI6MCwicGFzc3dvcmQiOiJ0ZXN0IiwidXNlcm5hbWUiOiJsaWxpYXN1YXUifSwiaWF0IjoxNjA3NjQyOTE2fQ.W5JgsCiuR4n-Zo3Ja4mJppHGbpjjyQSYMhTG-fYmZF8");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(json.toString());
            wr.flush();
            wr.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            System.out.println("Got inputStream: " + in);
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            System.out.println("Response: " + response.toString());

            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Update ////////////////////////////////////////////////////////////////////////////////////////////////////////
     */

    public boolean update(JSONObject user)
    {
        try {
            new UpdateRequest().execute(user.toString());
            if(status.contains("200")) {
                return true;
            }
        }
        catch(Exception exc)
        {
            System.out.println(exc.getMessage());
        }
        return false;
    }

    private class UpdateRequest extends AsyncTask<String, Integer, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {
            try {
                updateTask(strings[0]);
            }
            catch(JSONException e) {
                System.out.println(e.getMessage());
                System.exit(1);
            }
            return null;
        }
    }

    private void updateTask (String JSON) throws JSONException {
        try {
            Log.i("JSON", JSON);
            url = new URL(ADDRESS+"update");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept","application/json");
            conn.setRequestProperty("Authorization", session_cookie);
            conn.setDoOutput(true);
            conn.setDoInput(true);

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(JSON);
            wr.flush();
            wr.close();

            Log.i("STATUS", String.valueOf(conn.getResponseCode()));
            Log.i("MSG" , conn.getResponseMessage());

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * JWT Decoding ////////////////////////////////////////////////////////////////////////////////////////////////////////
     */

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void DecodeJWT() {
        try {

            String[] split_string = session_cookie.split("\\.");
            String base64EncodedBody = split_string[1];

            String body = new String(Base64.getUrlDecoder().decode(base64EncodedBody));

            System.out.println(body);

            JSONObject jsonObject = new JSONObject(body);

            String user = jsonObject.getString("user");
            System.out.println(user);

            JSONObject jsonObject2 = new JSONObject(user);
            id = jsonObject2.getString("id");
            firstName = jsonObject2.getString("fname");
            username = jsonObject2.getString("username");
            password = jsonObject2.getString("password");
            lastName = jsonObject2.getString("lname");
            email = jsonObject2.getString("email");
            isAdmin = jsonObject2.getInt("is_admin");
            isLocked = jsonObject2.getInt("isLocked");

        }
        catch(Exception exc)
        {
            System.out.println(exc.getMessage());
        }
    }
}
