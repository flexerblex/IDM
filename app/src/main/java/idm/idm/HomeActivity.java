package idm.idm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Base64;
import java.util.concurrent.Executor;

import idm.idm.servercom.FaceRecognizer;
import idm.idm.servercom.Server;

public class HomeActivity extends AppCompatActivity {

    private TextView Name;
    private ImageButton Face;
    private ImageButton Fingerprint;
    private ImageButton Voice;
    private String currentPhotoPath;
    private File imageFile;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);

        Name = (TextView)findViewById(R.id.personName);
        if (Server.firstName != null) {
            Name.setText(Server.firstName);
        }

        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(HomeActivity.this,
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

        Face = (ImageButton)findViewById(R.id.faceRegister);
        Fingerprint = (ImageButton)findViewById(R.id.fingerprintRegister);
        Voice = (ImageButton)findViewById(R.id.voiceRegister);

        Face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName = "photo";
                File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                try {
                    imageFile = File.createTempFile(fileName, ".jpg", storageDirectory);
                    currentPhotoPath = imageFile.getAbsolutePath();

                    Log.d("currentPhotoPath", currentPhotoPath);

                    Uri imageUri = FileProvider.getUriForFile(HomeActivity.this,
                            "idm.idm.provider", imageFile);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK ) {

            if (FaceRecognizer.FACERECOGNIZER.Upload(imageFile)) {
                System.out.println("success");
            }
            else {
                System.out.println("fail");
            }

        }
    }

}