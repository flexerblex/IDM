package idm.idm;

import android.content.Intent;
//import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.util.Date;
import java.text.SimpleDateFormat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import org.json.JSONException;

import java.io.IOException;

import idm.idm.servercom.FaceRecognizer;
import idm.idm.servercom.Server;

public class LoginActivity extends AppCompatActivity {

    private EditText Name;
    private EditText Password;
    private TextView Info;
    private Button Login;
    private ImageButton LoginFace;
    private TextView Create;
    private int counter = 3;
    private Date lockTime;

    private String currentPhotoPath;
    private File imageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        Name = (EditText)findViewById((R.id.loginUserName));
        Password = (EditText) findViewById(R.id.password);
        Info = (TextView)findViewById(R.id.logInfo);

        Login = (Button)findViewById(R.id.loginButton);
        LoginFace = (ImageButton)findViewById((R.id.faceID));
        Create = (TextView) findViewById(R.id.createAccount);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validID(Name.getText().toString(), Password.getText().toString());

                System.out.println("reached 1");
            }
        });

        LoginFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName = "photo";
                File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                try {
                    imageFile = File.createTempFile(fileName, ".jpg", storageDirectory);
                    currentPhotoPath = imageFile.getAbsolutePath();

                    Uri imageUri = FileProvider.getUriForFile(LoginActivity.this,
                            "idm.idm.provider", imageFile);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent,1);

                } catch (IOException e) {
                    e.printStackTrace();
                }
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

        if(counter > 0 && Server.SERVER.login(userName,userPass)) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        else {
            counter--;
            Info.setText("ATTEMPTS REMAINING: " + String.valueOf(counter));

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
                        Info.setText("ATTEMPTS REMAINING: " + String.valueOf(counter));
                    }
                }
                else {
                    //Login.setEnabled(false);
                    Info.setText("Try " + String.valueOf(minutes) + " minutes later.");
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String username = "Dummy";

        if (requestCode == 1 && resultCode == RESULT_OK ) {

            if (FaceRecognizer.FACERECOGNIZER.Authenticate(imageFile, username)) {
                Intent toHome2 = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(toHome2);
            }
            else {
                System.out.println("Error authenticating.");
            }
        }
    }

}
