package idm.idm.servercom;

import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Lexy
 */

public class VoiceRecognizer {

    public static final VoiceRecognizer VOICERECOGNIZER = new VoiceRecognizer();

    private final String ADDRESS = "http://3.128.46.46/";
    private URL url;

    private static String status;
    public static String name;

    public static class AuthParams {
        File file;
        String username;

        AuthParams(File file, String username) {
            this.file = file;
            this.username = username;
        }
    }

    // used to register VoiceID for the first time
    public boolean Upload(File path)
    {
        try {
            new UploadAsync().execute(path).get();
        }
        catch(Exception exc)
        {
            System.out.println(exc.getMessage());
        }
        return false;
    }

    private class UploadAsync extends AsyncTask<File, Integer, JSONObject>
    {

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected JSONObject doInBackground(File... files) {
            try {
                UploadMethod(files[0]);
            }
            catch(JSONException jsonexc)
            {
                System.out.println(jsonexc.getMessage());
                System.exit(1);
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
        }
    }

    public void UploadMethod(File path) throws JSONException {
        try {

            System.out.println(path.getName()); //this is "photo5127015921858211407.jpg"
            System.out.println(Server.firstName);

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("file",path.getName(),
                            RequestBody.create(MediaType.parse("application/octet-stream"),
                                    new File(path.getPath())))
                    .addFormDataPart("username", Server.firstName) //will replace with user when endpoint is complete
                    .addFormDataPart("type", "voice")
                    .addFormDataPart("method", "create")
                    .build();
            Request request = new Request.Builder()
                    .url(ADDRESS+"upload")
                    .method("POST", body)
                    .build();
            Response response = client.newCall(request).execute();
            System.out.println(response);

            String resStr = response.body().string();
            JSONObject json = new JSONObject(resStr);

            System.out.println(json.toString());

        }

        catch (IOException exc ) {
            System.out.println(exc.getMessage());
        }
    }

    // used to login using VoiceID
    public boolean Authenticate(File path, String username)
    {
        AuthParams params = new AuthParams(path, username);
        try {
            new AuthenticateAsync().execute(params).get();
            if (status.contains("200")) {
                return true;
            }
        }
        catch(Exception exc)
        {
            System.out.println(exc.getMessage());
        }
        return false;
    }

    private class AuthenticateAsync extends AsyncTask<AuthParams, Integer, JSONObject> {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected JSONObject doInBackground(AuthParams... authParams) {
            String user = authParams[0].username;
            File file = authParams[0].file;
            try {
                AuthenticateMethod(file, user);
            } catch (JSONException jsonexc) {
                System.out.println(jsonexc.getMessage());
                System.exit(1);
            }
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void AuthenticateMethod(File path, String user) throws JSONException {
        try {

            System.out.println(user);

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .connectTimeout(50, TimeUnit.SECONDS)
                    .writeTimeout(50, TimeUnit.SECONDS)
                    .readTimeout(50, TimeUnit.SECONDS)
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("file",path.getName(),
                            RequestBody.create(MediaType.parse("application/octet-stream"),
                                    new File(path.getPath())))
                    .addFormDataPart("username", user)
                    .addFormDataPart("type", "voice")
                    .addFormDataPart("method", "authenticate")
                    .build();
            Request request = new Request.Builder()
                    .url(ADDRESS+"upload")
                    .method("POST", body)
                    .build();
            Response response = client.newCall(request).execute();
            System.out.println(response);
            String resStr = response.body().string();
            JSONObject json = new JSONObject(resStr);

            System.out.println(json.toString());

            String stat = json.getString("status");
            String token = json.getString("token");

            DecodeJWT(token);
            status = stat;

        }

        catch (IOException exc ) {
            System.out.println(exc.getMessage());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void DecodeJWT(String tokenString) {
        try {

            String[] split_string = tokenString.split("\\.");
            String base64EncodedBody = split_string[1];

            String body = new String(Base64.getUrlDecoder().decode(base64EncodedBody));

            System.out.println(body);

            JSONObject jsonObject = new JSONObject(body);

            String user = jsonObject.getString("user");
            System.out.println(user);

            JSONObject jsonObject2 = new JSONObject(user);
            name = jsonObject2.getString("fname");
            System.out.println(name);

        }
        catch(Exception exc)
        {
            System.out.println(exc.getMessage());
        }
    }

}
