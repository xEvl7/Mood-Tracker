package my.edu.utar.moodtracker;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

//Done by Khor Jia Jun 2101593
public class Register extends AppCompatActivity {

    TextInputEditText editTextUsername, editTextEmail,editTextPassword;
    TextView loginNow,buttonReg;
    FirebaseAuth mAuth;
    boolean isChecked = false;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), Settings.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        editTextUsername = findViewById(R.id.usernameReg);
        editTextEmail = findViewById(R.id.emailReg);
        editTextPassword = findViewById(R.id.passwordReg);
        buttonReg = findViewById(R.id.register);
        loginNow = findViewById(R.id.loginNow);
        CheckBox checkBox = findViewById(R.id.checkbox_tnp);

        loginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChecked = !isChecked;
            }
        });

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username, email, password,userId;

                userId = UUID.randomUUID().toString();
                username = editTextUsername.getText().toString().trim();
                email = editTextEmail.getText().toString().trim();
                password = editTextPassword.getText().toString().trim();
                if(TextUtils.isEmpty(username)){
                    Toast.makeText(Register.this, "Enter Username", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(Register.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    Toast.makeText(Register.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;

                }if (!isChecked) {
                    Toast.makeText(Register.this, "Agree the Term of Privacy", Toast.LENGTH_SHORT).show();
                    return;
                }

                //check format of email
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    editTextEmail.setError("Please provide valid Email!");
                    editTextEmail.requestFocus();
                    return;
                }

                mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(
                        new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                //new user.
                                boolean newUser = task.getResult().getSignInMethods().isEmpty();

                                if(newUser){
                                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override public void onComplete(
                                                @NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()){
                                                //Declare the User object
                                                Toast.makeText(Register.this, "Account Created!", Toast.LENGTH_SHORT).show();
                                                User user = new User(username, password, email, userId, ""); // add user into firebase
                                                //insert the user data into the firebase realtime database
                                                FirebaseDatabase.getInstance("https://userauthorisation-300e8-default-rtdb.asia-southeast1.firebasedatabase.app")
                                                        .getReference("Users").child(mAuth.getCurrentUser().getUid()).setValue(user).addOnCompleteListener(
                                                                new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if(task.isSuccessful()){
                                                                            //if successfully sign up, go to the login page
                                                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                                            Intent intent = new Intent(getApplicationContext(), pinLockSettings.class);
                                                                            startActivity(intent);
                                                                            Toast.makeText(Register.this,"Sign Up successfully.",Toast.LENGTH_SHORT).show();
                                                                        }else{
                                                                            //fail to sign up
                                                                            Toast.makeText(Register.this,"Fail to sign up. Please try again.",Toast.LENGTH_SHORT).show();
                                                                        }
                                                                        editTextEmail.setText("");
                                                                        editTextPassword.setText("");
                                                                        editTextUsername.setText("");
                                                                    } }); }else{
                                                //fail to sign up
                                                Toast.makeText(Register.this,"Fail to sign up. Please try again",Toast.LENGTH_SHORT).show();
                                                editTextEmail.setText("");
                                                editTextPassword.setText("");
                                                editTextUsername.setText("");
                                            }
                                        }
                                    });
                                }else{
                                    //email existed
                                    Toast.makeText(Register.this,"This email is already registered. Please login to your account.",Toast.LENGTH_SHORT).show();
                                    editTextEmail.setText("");
                                    editTextPassword.setText("");
                                    editTextUsername.setText("");
                                }
                            }
                        });
            }
        });
    }
}




















