package my.edu.utar.moodtracker;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//Done by Khor Jia Jun 2101593
public class changePassword extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    private DatabaseReference rootRef;
    private DatabaseReference userRef;
    ImageView backInfoButton;
    TextInputEditText editTextOldPassword,editTextNewPassword,editTextNewPasswordRt;
    TextView saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        auth = FirebaseAuth.getInstance();
        backInfoButton = findViewById(R.id.backAccountInformation);
        saveButton = findViewById(R.id.passwordSave);
        editTextOldPassword = findViewById(R.id.oldPassword);
        editTextNewPassword = findViewById(R.id.newPassword);
        editTextNewPasswordRt = findViewById(R.id.newPasswordRt);

        user = auth.getCurrentUser();
        rootRef = FirebaseDatabase.getInstance("https://userauthorisation-300e8-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
        userRef = rootRef.child("Users");

        backInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), my.edu.utar.moodtracker.AccountInformation.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPassword = editTextOldPassword.getText().toString().trim();
                String newPassword = editTextNewPassword.getText().toString().trim();
                String newPasswordRt = editTextNewPasswordRt.getText().toString().trim();

                if (TextUtils.isEmpty(oldPassword)) {
                    Toast.makeText(changePassword.this, "Enter your old Password", Toast.LENGTH_SHORT).show();
                    editTextOldPassword.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(newPassword)) {
                    Toast.makeText(changePassword.this, "Enter your new Password", Toast.LENGTH_SHORT).show();
                    editTextNewPassword.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(newPasswordRt)) {
                    Toast.makeText(changePassword.this, "Re-enter your new Password", Toast.LENGTH_SHORT).show();
                    editTextNewPasswordRt.requestFocus();
                    return;
                }
                if (!newPassword.equals(newPasswordRt)) {
                    Toast.makeText(changePassword.this, "Both new password are not matched.", Toast.LENGTH_SHORT).show();
                    editTextNewPasswordRt.requestFocus();
                    return;
                }

                if (newPassword.equals(oldPassword)) {
                    Toast.makeText(changePassword.this, "New and old password cannot be same.", Toast.LENGTH_SHORT).show();
                    editTextNewPassword.requestFocus();
                    return;
                }
                //Retrieve password from FireBase
                userRef.child(user.getUid()).child("password").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String oldPasswordFireBased = snapshot.getValue(String.class);
                        if (oldPassword.equals(oldPasswordFireBased)) {
                            userRef.child(user.getUid()).child("password").setValue(newPassword); // set new password
                            Toast.makeText(changePassword.this, "Username is updated.", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), my.edu.utar.moodtracker.AccountInformation.class);
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            finish();

                        } else {
                            Toast.makeText(changePassword.this, "The old password not matched.", Toast.LENGTH_SHORT).show();
                            editTextOldPassword.requestFocus();
                            return;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(changePassword.this,"Database Error",Toast.LENGTH_SHORT).show(); }
                });
            }
        });
    }
}