package idm.idm;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import idm.idm.model.User;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UsersActivity extends AppCompatActivity {

    private final String ADDRESS = "http://3.128.46.46/";
    ArrayList<User> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        ListView mListView = (ListView) findViewById(R.id.listView);

        getUsers();

        UsersAdapter adapter = new UsersAdapter(this, R.layout.adapter_users, userList);
        mListView.setAdapter(adapter);

    }

    public void getUsers()
    {
        try {
            new UsersRequest().execute().get();
        }
        catch(Exception exc)
        {
            System.out.println(exc.getMessage());
        }
    }

    private class UsersRequest extends AsyncTask<String, Integer, JSONObject>
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
                usersTask();
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
    private void usersTask () throws JSONException {

        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url(ADDRESS+"users")
                    .method("GET", null)
                    .addHeader("authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoxLCJmbmFtZSI6IkxpbHkiLCJsbmFtZSI6IlN1YXUiLCJlbWFpbCI6ImxpbGlhc3VhdUBvYWtsYW5kLmVkdSIsImlzX2FkbWluIjoxLCJpc0xvY2tlZCI6MCwicGFzc3dvcmQiOiJ0ZXN0IiwidXNlcm5hbWUiOiJsaWxpYXN1YXUifSwiaWF0IjoxNjA3NDQ4MzkwfQ.o9zvrR_QmyKhcB7VqVZ4IiEHKIS35TGDEPygQUm4P4A")
                    .build();
            Response response = client.newCall(request).execute();

            System.out.println(response);
            String body = response.body().string();
            JSONObject j = new JSONObject(body);
            String message = j.getString("message");

            JSONObject j2 = new JSONObject(message);
            String users = j2.getString("data");

            JSONArray json = new JSONArray(users);
            CompileUsers(json);

        }

        catch(IOException exc)
        {
            System.out.println(exc.getMessage());
        }
    }

    public void CompileUsers(JSONArray arr) throws JSONException {

        for (int i = 0; i < arr.length(); i++) {
            String username = arr.getJSONObject(i).getString("username");
            String fname = arr.getJSONObject(i).getString("fname");
            String lname = arr.getJSONObject(i).getString("lname");
            Integer isLocked = arr.getJSONObject(i).getInt("isLocked");
            Integer is_admin = arr.getJSONObject(i).getInt("is_admin");

            User user = new User(fname, lname, username);
            userList.add(user);
        }
        System.out.println(userList.toString());
    }

}
