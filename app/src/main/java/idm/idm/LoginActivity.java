package idm.idm;

import android.content.Intent;
//import android.icu.text.SimpleDateFormat;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.text.SimpleDateFormat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.tasks.Task;

import java.io.IOException;

import idm.idm.servercom.Server;

import static java.security.AccessController.getContext;

public class LoginActivity extends AppCompatActivity {

    private EditText Name;
    private EditText Password;
    private TextView Info;
    private Button Login;
    private TextView Create;
    private int counter = 3;
    private Date lockTime;

    private GoogleSignInClient mGoogleSignInClient;
    SignInButton signin;
    private static final int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        Name = (EditText)findViewById((R.id.loginUserName));
        Password = (EditText) findViewById(R.id.password);
        Info = (TextView)findViewById(R.id.logInfo);

        Login = (Button)findViewById(R.id.loginButton);
        Create = (TextView) findViewById(R.id.createAccount);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validID(Name.getText().toString(), Password.getText().toString());

                System.out.println("reached 1");
            }
        });

        Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toRegister = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(toRegister);
            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signin = findViewById(R.id.sign_in_button);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                    // ...
                }
            }
        });

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> result) {
        //Constants.Log("handleSignInResult method use------" + result.isSuccessful());
        if(result.isSuccessful()){
            gotoHome();
        }else{
            Toast.makeText(getApplicationContext(),"Sign in cancel", Toast.LENGTH_LONG).show();
        }
        /**
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            //updateUI(account);
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Error", "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }**/
    }

    private void gotoHome(){
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    //need to be completed

    @Override
    protected void onStart() {
        super.onStart();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
    }

    /**
    private void updateUI(boolean signedIn) {
        if (signedIn) {
            signin.setVisibility(View.GONE);
            signOutBtn.setVisibility(View.VISIBLE);
        } else {
            mStatusTextView.setText(R.string.signed_out);
            Bitmap icon =                  BitmapFactory.decodeResource(getContext().getResources(),R.drawable.user_defaolt);
            imgProfilePic.setImageBitmap(ImageHelper.getRoundedCornerBitmap(getContext(),icon, 200, 200, 200, false, false, false, false));
            signin.setVisibility(View.VISIBLE);
            signOutButton.setVisibility(View.GONE);
        }
    }
     **/


    private void validID(String userName, String userPass) {

        if(counter > 0 && Server.SERVER.login(userName,userPass)) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else {
            counter--;
            Info.setText("Attempts Remaining: " + String.valueOf(counter));

            //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            if(counter == 0){
                lockTime = new Date();
                //Login.setEnabled(false);
                Info.setText("Try 2 minutes later.");
            }
            else if (counter < 0) {

                Date currentTime = new Date();
                long remainingTime = currentTime.getTime() - lockTime.getTime();
                int minutes = (int) ((remainingTime / (1000*60)) % 60);

                if (remainingTime > 12000) {
                    counter = 3;
                    if (Server.SERVER.login(userName,userPass)) {
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }else{
                        counter --;
                        Info.setText("Attempts Remaining: " + String.valueOf(counter));
                    }
                }
                else {
                    //Login.setEnabled(false);
                    Info.setText("Try " + String.valueOf(minutes) + " minutes later.");
                }
            }
        }
    }

}
