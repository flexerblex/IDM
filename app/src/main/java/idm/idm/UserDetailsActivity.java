package idm.idm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
        String username = intent.getStringExtra("name");
        String fname = intent.getStringExtra("fname");
        String lname = intent.getStringExtra("lname");
        String email = intent.getStringExtra("email");
        Integer isAdmin = intent.getIntExtra("isAdmin", 0);
        Integer isLocked = intent.getIntExtra("isLocked", 0);

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

        if (isAdmin == 0) {
            AdminCheck = false;
            GrantText();
        }
        else {
            AdminCheck = true;
            RevokeText();
        }
        if (isLocked == 0) {
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
                    //add db function
                }
                else {
                    UnlockText();
                    StatusCheck = true;
                }
            }
        });

        ChangeAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AdminCheck) {
                    GrantText();
                    AdminCheck = false;
                }
                else {
                    RevokeText();
                    AdminCheck = true;
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
