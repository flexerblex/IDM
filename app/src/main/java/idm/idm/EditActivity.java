package idm.idm;

import androidx.appcompat.app.AppCompatActivity;
import idm.idm.servercom.Server;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class EditActivity extends AppCompatActivity {

    private Button Update;
    private Button Cancel;
    private EditText FirstName;
    private EditText LastName;
    private EditText Email;
    private EditText Password;
    private EditText ConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Update = (Button)findViewById(R.id.update);
        Cancel = (Button)findViewById(R.id.cancel);
        FirstName = (EditText)findViewById(R.id.FirstName);
        FirstName.setText(Server.firstName, TextView.BufferType.EDITABLE);
        LastName = (EditText)findViewById(R.id.LastName);
        LastName.setText(Server.lastName, TextView.BufferType.EDITABLE);
        Email = (EditText)findViewById(R.id.Email);
        Email.setText(Server.email, TextView.BufferType.EDITABLE);
        Password = (EditText)findViewById(R.id.Password);
        Password.setText(Server.password, TextView.BufferType.EDITABLE);
        ConfirmPassword = (EditText)findViewById(R.id.ConfirmPassword);
        ConfirmPassword.setText(Server.password, TextView.BufferType.EDITABLE);
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(FirstName.getText().toString())) {
                    Toast.makeText(EditActivity.this, "First Name is required.", Toast.LENGTH_SHORT).show();
                    return;
                } if(TextUtils.isEmpty(LastName.getText().toString())) {
                    Toast.makeText(EditActivity.this, "Last Name is required.", Toast.LENGTH_SHORT).show();
                    return;
                } if(TextUtils.isEmpty(Email.getText().toString())) {
                    Toast.makeText(EditActivity.this, "Email is required.", Toast.LENGTH_SHORT).show();
                    return;
                } if(TextUtils.isEmpty(Password.getText().toString())) {
                    Toast.makeText(EditActivity.this, "Password is required.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (Password.getText().toString().equals(ConfirmPassword.getText().toString())) {
                        JSONObject user = new JSONObject();
                        JSONObject userData = new JSONObject();
                        try {
                            user.put("id", Server.id);
                            userData.put("email", Email.getText().toString());
                            userData.put("password", Password.getText().toString());
                            userData.put("fname", FirstName.getText().toString());
                            userData.put("lname", LastName.getText().toString());
                            user.put("user", userData);

                            System.out.println(user.toString());

                            SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("password", Password.getText().toString());
                            editor.commit();

                            Server.firstName = FirstName.getText().toString();
                            Server.lastName = LastName.getText().toString();
                            Server.email = Email.getText().toString();
                            Server.password = Password.getText().toString();

                            if (Server.SERVER.update(user)) {
                                Toast.makeText(EditActivity.this, "Profile Updated Successfully.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(EditActivity.this, HomeActivity.class));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Context context = getApplicationContext();
                        CharSequence text = "Password Confirmation does not match.";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                }
            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditActivity.this, HomeActivity.class));
            }
        });
    }
}