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
public class changeUsername extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    private DatabaseReference rootRef;
    private DatabaseReference userRef;
    ImageView backInfoButton;
    TextInputEditText editTextUsername;
    TextView saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);

        auth = FirebaseAuth.getInstance();
        backInfoButton = findViewById(R.id.backAccountInformation);
        saveButton = findViewById(R.id.usernameSave);
        editTextUsername = findViewById(R.id.newUsername);

        user = auth.getCurrentUser();
        rootRef = FirebaseDatabase.getInstance("https://userauthorisation-300e8-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
        userRef = rootRef.child("Users");

        backInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), com.example.moodtracker.AccountInformation.class);
                startActivity(intent);
                finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newUsername = editTextUsername.getText().toString().trim();
                if (TextUtils.isEmpty(newUsername)) {
                    Toast.makeText(changeUsername.this, "Enter username", Toast.LENGTH_SHORT).show();
                    editTextUsername.requestFocus();
                    return;
                }

                // Retrieve the old username from Firebase
                userRef.child(user.getUid()).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String oldUsername = snapshot.getValue(String.class);
                        if (newUsername.equals(oldUsername)) {
                            Toast.makeText(changeUsername.this, "The new same as the old username.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Update the username in Firebase
                            userRef.child(user.getUid()).child("username").setValue(newUsername);
                            Toast.makeText(changeUsername.this, "Username is updated.", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), com.example.moodtracker.AccountInformation.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(changeUsername.this,"Database Error",Toast.LENGTH_SHORT).show(); }
                });
            }
        });

    }
}