package idm.idm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText Name;
    private EditText Password;
    private TextView Info;
    private Button Login;
    private TextView Create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        Name = (EditText)findViewById((R.id.loginUserName));
        Password = (EditText) findViewById(R.id.password);

        Login = (Button)findViewById(R.id.loginButton);
        Create = (TextView) findViewById(R.id.createAccount);


        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validID(Name.getText().toString(), Password.getText().toString());
            }
        });

        Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toRegister = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(toRegister);
            }
        });

    }
    private void validID(String userName, String userPass) {
        //if server authenticates user/password
        String test = userName;
        Intent toHome = new Intent (LoginActivity.this, HomeActivity.class);
        toHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(toHome);
    }

}
