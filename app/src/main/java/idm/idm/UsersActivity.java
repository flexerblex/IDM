package idm.idm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import idm.idm.servercom.Server;

public class UsersActivity extends AppCompatActivity {

    private TextView Name;
    private TextView Cancel;

    @Override
    protected void onCreate(Bundle savedInstancesState) {
        super.onCreate(savedInstancesState);
        setContentView(R.layout.activity_users);

        //Username = (EditText)findViewById(R.id.userName);

        Cancel = (TextView) findViewById(R.id.cancel);

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UsersActivity.this, HomeActivity.class));
            }
        });


    }
}
