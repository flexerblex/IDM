package idm.idm;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import idm.idm.servercom.Server;

public class HomeActivity extends AppCompatActivity {

    private EditText Name;
    private Button Face;
    private Button Fingerprint;
    private Button Voice;
    private String currentPhotoPath;
    private File imageFile;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);

        Name = (EditText)findViewById(R.id.firstName + R.id.lastName);

        Face = (Button)findViewById(R.id.faceRegister);
        Fingerprint = (Button)findViewById(R.id.fingerprintRegister);
        Voice = (Button)findViewById(R.id.vocalRegister);
        Voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,RecordAudioActivity.class));
            }
        });


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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK ) {

            Server.SERVER.UploadTask(imageFile);

        }
    }

//        Face.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String fileName = "photo";
//                File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//                try {
//                    File imageFile = File.createTempFile(fileName, ".jpg", storageDirectory);
//                    currentPhotoPath = imageFile.getAbsolutePath();
//
//                    Log.d("currentPhotoPath", currentPhotoPath);
//
//
//                    Uri imageUri = FileProvider.getUriForFile(HomeActivity.this,
//                            "idm.idm.provider", imageFile);
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                    startActivityForResult(intent,1);
//
//                    Log.d("imageUri", imageUri.toString());
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == 1 && resultCode == RESULT_OK ) {
//            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
//
//            Log.d("image succeeded", bitmap.toString());
//
//            Log.d("currentPhotoPath", currentPhotoPath);
//
//            Server.SERVER.UploadTask(bitmap);
//
//            //ImageView imageView = findViewById(R.id.imageView);
//            //imageView.setImageBitmap(bitmap);
//
//
//        }
//    }
}
