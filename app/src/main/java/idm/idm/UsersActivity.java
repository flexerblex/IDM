package idm.idm;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

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
    private ImageButton Back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        ListView mListView = (ListView) findViewById(R.id.listView);

        getUsers();

        UsersAdapter adapter = new UsersAdapter(this, R.layout.adapter_users, userList);
        mListView.setAdapter(adapter);

        Back = (ImageButton)findViewById(R.id.goBack);

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UsersActivity.this, FeedActivity.class));
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(UsersActivity.this, UserDetailsActivity.class);
                intent.putExtra("name", userList.get(i).getUsername());
                intent.putExtra("fname", userList.get(i).getFirstName());
                intent.putExtra("lname", userList.get(i).getLastName());
                intent.putExtra("email", userList.get(i).getEmail());
                intent.putExtra("isAdmin", userList.get(i).getIsAdmin());
                intent.putExtra("isLocked", userList.get(i).getIsLocked());
                startActivity(intent);
            }
        });

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

        System.out.println("compiling users....");

        for (int i = 0; i < arr.length(); i++) {
            String username = arr.getJSONObject(i).getString("username");
            String fname = arr.getJSONObject(i).getString("fname");
            String lname = arr.getJSONObject(i).getString("lname");
            String email = arr.getJSONObject(i).getString("email");
            Integer is_admin = arr.getJSONObject(i).getInt("is_admin");
            Integer isLocked = arr.getJSONObject(i).getInt("isLocked");

            User user = new User(fname, lname, username, email, is_admin, isLocked);
            userList.add(user);

        }
    }

}
