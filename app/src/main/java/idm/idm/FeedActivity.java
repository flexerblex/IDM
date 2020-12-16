package idm.idm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import idm.idm.model.Feed;

import idm.idm.servercom.Server;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FeedActivity extends AppCompatActivity {

    ArrayList<Feed> feedList = new ArrayList<>();
    private TextView User;
    private ImageButton Settings;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);


        User = (TextView)findViewById(R.id.userText);
        if (Server.firstName != null) {
            User.setText("Welcome, "+Server.firstName+".");
        }

        ListView mListView = (ListView) findViewById(R.id.listView);

        System.out.println(Server.username);
        if (Server.username.contains("liliasuau")) {
            feed(getResources().getString(R.string.liliasuau));
        }
        else if (Server.username.contains("agamache")) {
            feed(getResources().getString(R.string.agamache));
        }
        else {
            feed(getResources().getString(R.string.idm_capstone));
        }

        FeedAdapter adapter = new FeedAdapter(this, R.layout.adapter_feed, feedList);
        mListView.setAdapter(adapter);

        Settings = (ImageButton)findViewById(R.id.settings);
        Settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FeedActivity.this, Home2Activity.class));
            }
        });

    }
    public void feed(String username) {
        try {
            new feedRequest().execute(username).get();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private class feedRequest extends AsyncTask<String, Integer, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {
            feedTask(strings[0]);
            return null;
        }
    }

    public void feedTask (String username) {

        try {

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url("https://graph.instagram.com/me/media?fields=username,media_url,caption,timestamp&access_token="+
                            username)
                    .method("GET", null)
                    .build();
            Response response = client.newCall(request).execute();

            System.out.println(response);
            String resStr = response.body().string();
            JSONObject json = new JSONObject(resStr);

            System.out.println(json.toString());

            String stat = json.getString("data");
            System.out.println(stat);

            JSONArray jsonFeed = new JSONArray(stat);

            CompileFeed(jsonFeed);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void CompileFeed(JSONArray arr) throws JSONException {

        System.out.println("compiling users....");

        for (int i = 0; i < arr.length(); i++) {
            String caption,timestamp;
            String name = arr.getJSONObject(i).getString("username");
            String media_url = arr.getJSONObject(i).getString("media_url");
            if (arr.getJSONObject(i).has("caption")) {
                caption = arr.getJSONObject(i).getString("caption");
            }
            else {
                caption = "";
            }
            if (arr.getJSONObject(i).has("timestamp")) {
                timestamp = arr.getJSONObject(i).getString("timestamp");
            }
            else {
                timestamp = "";
            }

            Feed feed = new Feed(name, media_url, caption, timestamp);
            feedList.add(feed);

        }
    }

}
