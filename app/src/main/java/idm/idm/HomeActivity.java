package idm.idm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
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
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.concurrent.Executor;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import idm.idm.servercom.FaceRecognizer;
import idm.idm.servercom.Server;

public class HomeActivity extends AppCompatActivity {

    private TextView Name;
    private Button Face;
    private Button Fingerprint;
    private Button Voice;
    private Button Logout;
    private Button ViewUsers;
    private Button EditProfile;
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

        if (Server.isAdmin == null) {
            Server.isAdmin = 0;
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

                try {
                    KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
                    keyGenerator.init(new KeyGenParameterSpec.Builder("Key",
                            KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                            .build());
                    SecretKey secretKey = keyGenerator.generateKey();
                    Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
                    cipher.init(Cipher.ENCRYPT_MODE, secretKey);

                        byte[] encryptionIv = cipher.getIV();
                        byte[] passwordBytes = Server.password.getBytes("UTF-8");
                        byte[] encryptedPasswordBytes = cipher.doFinal(passwordBytes);

                    String encryptedPassword = Base64.encodeToString(encryptedPasswordBytes, Base64.DEFAULT);

                    SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("username", Server.username);
                    editor.putString("password", encryptedPassword);
                    editor.putString("encryptionIv", Base64.encodeToString(encryptionIv, Base64.DEFAULT));
                    editor.commit();
                    Toast.makeText(getApplicationContext(), "Fingerprint Successfully Registered.",
                            Toast.LENGTH_SHORT)
                            .show();

                } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidAlgorithmParameterException | NoSuchPaddingException | InvalidKeyException | UnsupportedEncodingException | BadPaddingException | IllegalBlockSizeException e) {
                    e.printStackTrace();
                }
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

                    Uri imageUri = FileProvider.getUriForFile(HomeActivity.this,
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
                startActivity(new Intent(HomeActivity.this, RecordAudioActivity.class));
            }
        });

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Server.session_cookie = "";
                Toast.makeText(HomeActivity.this, "Logout Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            }
        });

        EditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, EditActivity.class));
            }
        });

        if (Server.isAdmin == 1) {
            ViewUsers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(HomeActivity.this, UsersActivity.class));
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK ) {

            if (FaceRecognizer.FACERECOGNIZER.Upload(imageFile)) {
                Toast.makeText(HomeActivity.this, "Face registered successfully.", Toast.LENGTH_SHORT).show();
                System.out.println("success");
            }
            else {
                System.out.println("fail");
            }

        }
    }

}