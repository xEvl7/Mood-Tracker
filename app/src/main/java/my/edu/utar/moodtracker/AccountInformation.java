package my.edu.utar.moodtracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//Done by Khor Jia Jun 2101593
public class AccountInformation extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    private DatabaseReference rootRef;
    private DatabaseReference userRef;
    TextView textViewUserName,textViewUserId,textViewPassword,textViewEmail;
    ImageView backSettingsButton,modifyUsernameButton,modifyPasswordButton;
    private String username, userId,email;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_information);

        auth = FirebaseAuth.getInstance();
        backSettingsButton = findViewById(R.id.backSettings);
        modifyUsernameButton = findViewById(R.id.changeUsername);
        modifyPasswordButton = findViewById(R.id.changePassword);
        textViewUserName = findViewById(R.id.usernameShow);
        textViewUserId = findViewById(R.id.userIdShow);
        textViewPassword = findViewById(R.id.passwordShow);
        textViewEmail = findViewById(R.id.emailShow);
        layout = findViewById(R.id.layout1);

        user = auth.getCurrentUser();
        rootRef = FirebaseDatabase.getInstance("https://userauthorisation-300e8-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
        userRef = rootRef.child("Users");

        //Retrieve data from FireBase
        userRef.child(user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange( @NonNull DataSnapshot snapshot) {
                        username = snapshot.child("username").getValue().toString();
                        userId = snapshot.child("userId").getValue().toString();
                        email = snapshot.child("email").getValue().toString();

                        textViewUserName.append(username);
                        textViewUserId.append(userId);
                        textViewPassword.append("************");
                        textViewEmail.append(email);
                    }
                    public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(AccountInformation.this,"Database Error",Toast.LENGTH_SHORT).show();
                    }
                });

        backSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Settings.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        modifyUsernameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),changeUsername.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        modifyPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),changePassword.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });
    }
}