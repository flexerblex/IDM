package com.example.idm;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LogoutActivity extends AppCompatActivity {

    private Button LogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
         LogOut = (Button) findViewById(R.id.buttonLogOut);
        Intent in = getIntent();
        String string = in.getStringExtra("message");
        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LogoutActivity.this);
                builder.setTitle("Confirmation SignOut!").setMessage("You sure You want Logout");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i = new Intent(getApplicationContext(),
                                        AppCompatActivity.class);
                                startActivity(i);
                            }

                            });
                builder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                 dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder.create();
                alert11.show();
                        }

            });
        }
    }