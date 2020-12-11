package idm.idm;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import idm.idm.model.User;

public class UsersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        ListView mListView = (ListView) findViewById(R.id.listView);

        User lily = new User("Lilia", "Suau",  "liliasuau");
        User lexy = new User("Lexy", "Pan",  "lexypan");
        ArrayList<User> userList = new ArrayList<>();

        //Add the Person objects to an ArrayList
        userList.add(lily);
        userList.add(lexy);

        UsersAdapter adapter = new UsersAdapter(this, R.layout.adapter_users, userList);
        mListView.setAdapter(adapter);

    }

}
