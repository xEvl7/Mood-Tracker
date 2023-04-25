package my.edu.utar.moodtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//Done by Khor Jia Jun 2101593
public class Settings extends AppCompatActivity {

    FirebaseAuth auth;
    TextView logoutButton,textViewUserName,textViewUserEmail;
    ImageView accountButton,reminderButton,pinLockButton,helpButton,termButton,privacyButton,previousPageButton;

    FirebaseUser user;
    private DatabaseReference rootRef;
    private DatabaseReference userRef;
    private String username, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        auth = FirebaseAuth.getInstance();
        logoutButton = findViewById(R.id.logout);
        textViewUserName = findViewById(R.id.user_username);
        textViewUserEmail = findViewById(R.id.user_email);
        accountButton = findViewById(R.id.accountInformation);
        reminderButton = findViewById(R.id.reminderSettings);
        pinLockButton = findViewById(R.id.pinLock);
        helpButton = findViewById(R.id.help);
        termButton = findViewById(R.id.termOfService);
        privacyButton = findViewById(R.id.privacyPolicy);
        previousPageButton = findViewById(R.id.previousPage);

        user = auth.getCurrentUser();
        rootRef = FirebaseDatabase.getInstance("https://userauthorisation-300e8-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
        userRef = rootRef.child("Users");

        if(user == null){
            Intent intent = new Intent(getApplicationContext(),Login.class);
            startActivity(intent);
            finish();
        }else{
            userRef.child(user.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange( @NonNull DataSnapshot snapshot) {
                            username = snapshot.child("username").getValue().toString();
                            email = snapshot.child("email").getValue().toString();

                            textViewUserName.append(" "+username);
                            textViewUserEmail.append(" "+email);
                        }
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Settings.this,"Database Error",Toast.LENGTH_SHORT).show(); }
                    });
        }

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AccountInformation.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        pinLockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), pinLockSettings.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        reminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Reminder.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Help.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        termButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TermOfService.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        privacyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PrivacyPolicy.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        previousPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), mainPage.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

    }
}