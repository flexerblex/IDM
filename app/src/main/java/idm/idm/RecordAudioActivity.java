package idm.idm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class RecordAudioActivity extends AppCompatActivity {

    Button StartRecording, StopRecording;
    String pathSave = "";
    MediaRecorder mediaRecorder;

    final int REQUEST_PERMISSION_CODE = 786;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_audio);

        if (checkPermission()) requestPermission();
        StartRecording = findViewById(R.id.button);
        StopRecording = findViewById(R.id.button2);

        StartRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkPermission()) {
                    pathSave = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + UUID.randomUUID().toString() + "_audio_record.3gp"; // TODO Change the uer id
                    setupMediaRecorder();
                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(RecordAudioActivity.this, "Recording Started...", Toast.LENGTH_SHORT).show();

                } else {
                    requestPermission();
                }
            }
        });

        StopRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaRecorder.stop();
                StopRecording.setEnabled(false);
            }
        });


    }

    private void setupMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(pathSave);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        }, REQUEST_PERMISSION_CODE);
    }

    private boolean checkPermission() {
        int writeInternalStorageResult = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int recordAudioResult = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return writeInternalStorageResult == PackageManager.PERMISSION_GRANTED &&
                recordAudioResult == PackageManager.PERMISSION_GRANTED;
    }

}
