package my.edu.utar.moodtracker;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
public class pinLockSettings extends AppCompatActivity {

    private TextView pinView;
    FirebaseAuth auth;
    FirebaseUser user;
    private DatabaseReference rootRef;
    private DatabaseReference userRef;
    private String pin = "";

    private ImageView backSettingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_lock_settings);

        backSettingsButton = findViewById(R.id.backSettings);

        backSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Settings.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        auth = FirebaseAuth.getInstance();
        pinView = findViewById(R.id.pinView);
        user = auth.getCurrentUser();
        rootRef = FirebaseDatabase.getInstance("https://userauthorisation-300e8-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
        userRef = rootRef.child("Users");
        
        Button button1 = findViewById(R.id.button_1);
        Button button2 = findViewById(R.id.button_2);
        Button button3 = findViewById(R.id.button_3);
        Button button4 = findViewById(R.id.button_4);
        Button button5 = findViewById(R.id.button_5);
        Button button6 = findViewById(R.id.button_6);
        Button button7 = findViewById(R.id.button_7);
        Button button8 = findViewById(R.id.button_8);
        Button button9 = findViewById(R.id.button_9);
        Button button0 = findViewById(R.id.button_0);
        Button saveButton = findViewById(R.id.savePin);
        Button removeDigit = findViewById(R.id.button_removeDigit);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDigit("1");
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDigit("2");
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { addDigit("3"); }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDigit("4");
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDigit("5");
            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDigit("6");
            }
        });

        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDigit("7");
            }
        });

        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDigit("8");
            }
        });

        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDigit("9");
            }
        });

        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDigit("0");
            }
        });

        removeDigit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeDigit();
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(pin)) {
                    Toast.makeText(pinLockSettings.this, "Enter pin", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pin.length() !=4){
                    Toast.makeText(pinLockSettings.this, "Pin length must be 4!!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                userRef.child(user.getUid()).child("pinLock").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        userRef.child(user.getUid()).child("pinLock").setValue(pin);
                        Toast.makeText(pinLockSettings.this, "PinLock is updated.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), Settings.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(pinLockSettings.this,"Database Error",Toast.LENGTH_SHORT).show(); }
                });
            }
        });
    }

    private void addDigit(String digit) {
        if (pin.length() < 4) {
            pin += digit;
            pinView.setText(pin);
        }
    }

    private void removeDigit(){
        if (pin.length() > 0) {
            pin = pin.substring(0, pin.length() - 1);
            pinView.setText(pin);
        }
    }
}

