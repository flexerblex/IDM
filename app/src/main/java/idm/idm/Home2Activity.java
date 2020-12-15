package idm.idm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executor;

import idm.idm.servercom.FaceRecognizer;
import idm.idm.servercom.Server;

public class Home2Activity extends AppCompatActivity {

    private TextView Name;
    private Button Face;
    private Button Fingerprint;
    private Button Voice;
    private Button Logout;
    private Button ViewUsers;
    private Button EditProfile;
    private ImageButton Home;
    private String currentPhotoPath;
    private File imageFile;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home2);

        Name = (TextView)findViewById(R.id.personName);
        if (Server.firstName != null) {
            Name.setText(Server.firstName);
        }

        if (Server.isAdmin == null) {
            Server.isAdmin = 0;
        }

        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(Home2Activity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                        "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("username", Server.username);
                editor.putString("password", Server.password);
                editor.commit();
                Toast.makeText(getApplicationContext(), "Fingerprint Successfully Registered.",
                        Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Register your fingerprint")
                .setNegativeButtonText("Cancel")
                .build();

        Face = (Button)findViewById(R.id.faceID);
        Fingerprint = (Button)findViewById(R.id.fingerprintRegister);
        Voice = (Button)findViewById(R.id.voiceRegister);
        Logout = (Button)findViewById(R.id.logout);
        EditProfile = (Button)findViewById(R.id.editProfile);
        ViewUsers = (Button)findViewById(R.id.viewUsers);
        Home = (ImageButton)findViewById(R.id.goBack);

        if (Server.isAdmin == 0){
            ViewUsers.setVisibility(View.GONE);
        }else {
            ViewUsers.setVisibility(View.VISIBLE);
        }

        Face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName = "photo";
                File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                try {
                    imageFile = File.createTempFile(fileName, ".jpg", storageDirectory);
                    currentPhotoPath = imageFile.getAbsolutePath();

                    Log.d("currentPhotoPath", currentPhotoPath);

                    Uri imageUri = FileProvider.getUriForFile(Home2Activity.this,
                            "idm.idm.provider", imageFile);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    intent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
                    intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
                    intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
                    startActivityForResult(intent,1);

                    Log.d("imageUri", imageUri.toString());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Fingerprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);
            }
        });

        Voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home2Activity.this, RecordAudioActivity.class));
            }
        });

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Server.session_cookie = "";
                Toast.makeText(Home2Activity.this, "Logout Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Home2Activity.this, LoginActivity.class));
            }
        });

        EditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home2Activity.this, EditActivity.class));
            }
        });

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home2Activity.this, FeedActivity.class));
            }
        });

        if (Server.isAdmin == 1) {
            ViewUsers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Home2Activity.this, UsersActivity.class));
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK ) {

            if (FaceRecognizer.FACERECOGNIZER.Upload(imageFile)) {
                Toast.makeText(Home2Activity.this, "Face registered successfully.", Toast.LENGTH_SHORT).show();
                System.out.println("success");
            }
            else {
                System.out.println("fail");
            }

        }
    }

}