package idm.idm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import idm.idm.servercom.Server;

public class UserDetailsActivity extends AppCompatActivity {

    private TextView Name;
    private TextView Username;
    private TextView Email;
    private TextView IsAdmin;
    private TextView IsLocked;
    private TextView Cancel;
    private Button ChangeStatus;
    private Button ChangeAccess;
    public Boolean AdminCheck;
    public Boolean StatusCheck;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userdetails);

        Intent intent = getIntent();
        final String username = intent.getStringExtra("name");
        String fname = intent.getStringExtra("fname");
        String lname = intent.getStringExtra("lname");
        String email = intent.getStringExtra("email");
        Integer isAdmin = intent.getIntExtra("isAdmin", 0);
        final Integer isLocked = intent.getIntExtra("isLocked", 0);

        Name = (TextView)findViewById(R.id.fullname);
        Username = (TextView)findViewById(R.id.username);
        Email = (TextView)findViewById(R.id.email);
        IsAdmin = (TextView)findViewById(R.id.admin);
        IsLocked = (TextView)findViewById(R.id.status);
        ChangeStatus = (Button)findViewById(R.id.changeStatus);
        ChangeAccess = (Button)findViewById(R.id.changeAccess);
        Cancel = (TextView)findViewById(R.id.cancel);

        Name.setText(fname+" "+lname);
        Username.setText(username);
        Email.setText(email);

        if (isAdmin == 1) {
            AdminCheck = false;
            GrantText();
        }
        else {
            AdminCheck = true;
            RevokeText();
        }
        if (isLocked == 1) {
            StatusCheck = true;
            UnlockText();
        }
        else {
            StatusCheck = false;
            LockText();
        }

        ChangeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StatusCheck) {
                    LockText();
                    StatusCheck = false;
                    Server.SERVER.Lock(username, "lock");
                    Toast.makeText(UserDetailsActivity.this, "Account successfully locked.", Toast.LENGTH_SHORT).show();
                }
                else {
                    UnlockText();
                    StatusCheck = true;
                    Server.SERVER.Lock(username, "unlock");
                    Toast.makeText(UserDetailsActivity.this, "Account successfully unlocked.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ChangeAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AdminCheck) {
                    GrantText();
                    AdminCheck = false;
                    Server.SERVER.Lock(username, "revoke");
                    Toast.makeText(UserDetailsActivity.this, "Access revoked.", Toast.LENGTH_SHORT).show();
                    // set admin to 0
                }
                else {
                    RevokeText();
                    AdminCheck = true;
                    Server.SERVER.Lock(username, "grant");
                    Toast.makeText(UserDetailsActivity.this, "Access granted.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserDetailsActivity.this, UsersActivity.class));
            }
        });

    }

    public void LockText() {
        IsLocked.setText("Locked");
        ChangeStatus.setText("Unlock Account");
    }

    public void UnlockText() {
        IsLocked.setText("Active");
        ChangeStatus.setText("Lock Account");
    }

    public void GrantText() {
        IsAdmin.setText("User");
        ChangeAccess.setText("Grant Access");
    }

    public void RevokeText () {
        IsAdmin.setText("Admin");
        ChangeAccess.setText("Revoke Access");
    }
}
