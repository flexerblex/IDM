package idm.idm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
                Intent intent = new Intent (RegisterActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

    }

}
