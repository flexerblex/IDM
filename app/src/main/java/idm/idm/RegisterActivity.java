package idm.idm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import idm.idm.servercom.Server;

public class RegisterActivity extends AppCompatActivity {

    private EditText Username;
    private EditText Email;
    private EditText FirstName;
    private EditText LastName;
    private EditText Password;
    private EditText Confirmation;
    private Button CreateAccount;
    private TextView Cancel;

    @Override
    protected void onCreate(Bundle savedInstancesState) {
        super.onCreate(savedInstancesState);
        setContentView(R.layout.activity_register);

        Username = (EditText)findViewById(R.id.userName);
        Email = (EditText)findViewById(R.id.emailAddress);
        FirstName = (EditText)findViewById(R.id.firstName);
        LastName = (EditText)findViewById(R.id.lastName);
        Password = (EditText)findViewById(R.id.password);
        Confirmation = (EditText)findViewById(R.id.conPassword);

        CreateAccount = (Button)findViewById(R.id.createAccount);
        Cancel = (TextView) findViewById(R.id.cancelText);

        CreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Password.getText().toString().equals(Confirmation.getText().toString())) {
                    JSONObject mainpostData = new JSONObject();
                    JSONObject inpostData = new JSONObject();
                    try {
                        inpostData.put("username", Username.getText().toString());
                        inpostData.put("email", Email.getText().toString());
                        inpostData.put("fname", FirstName.getText().toString());
                        inpostData.put("lname", LastName.getText().toString());
                        inpostData.put("password", Password.getText().toString());
                        mainpostData.put("user", inpostData);

                        Server.SERVER.register(mainpostData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else {
                    Context context = getApplicationContext();
                    CharSequence text = "Password Confirmation does not match.";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(toLogin);
            }
        });

    }
}