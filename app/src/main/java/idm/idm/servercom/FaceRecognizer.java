package idm.idm.servercom;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Lily
 * Used to store and validate faceID's.
 */

public class FaceRecognizer {

    public static final FaceRecognizer FACERECOGNIZER = new FaceRecognizer();

    private final String ADDRESS = "http://3.128.46.46/";
    private URL url;

    public static class AuthParams {
        File file;
        String username;

        AuthParams(File file, String username) {
            this.file = file;
            this.username = username;
        }
    }

    // used to register FaceID for the first time
    public boolean Upload(File path)
    {
        try {
            new UploadAsync().execute(path).get();
            return true;
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

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("face",path.getName(),
                            RequestBody.create(MediaType.parse("application/octet-stream"),
                                    new File(path.getPath())))
                    .build();
            Request request = new Request.Builder()
                    .url(ADDRESS+"upload")
                    .method("POST", body)
                    //.addHeader("authorization", Server.session_cookie)
                    .build();
            Response response = client.newCall(request).execute();
            System.out.println(response);
        }

        catch (IOException exc ) {
            System.out.println(exc.getMessage());
        }
    }

    // used to login using FaceID
    public boolean Authenticate(File path, String username)
    {
        AuthParams params = new AuthParams(path, username);
        try {
            new AuthenticateAsync().execute(params).get();
            return true;
        }
        catch(Exception exc)
        {
            System.out.println(exc.getMessage());
        }
        return false;
    }

    private class AuthenticateAsync extends AsyncTask<AuthParams, Integer, JSONObject> {
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

    public void AuthenticateMethod(File path, String user) throws JSONException {
        try {

            System.out.println(path.getName()); //this is "photo5127015921858211407.jpg"
            System.out.println(user);

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("face",path.getName(),
                            RequestBody.create(MediaType.parse("application/octet-stream"),
                                    new File(path.getPath())))
                    .build();
            Request request = new Request.Builder()
                    .url(ADDRESS+"upload")
                    .method("POST", body)
                    //.addHeader("authorization", Server.session_cookie)
                    .build();
            Response response = client.newCall(request).execute();
            System.out.println(response);
        }

        catch (IOException exc ) {
            System.out.println(exc.getMessage());
        }
    }

}
