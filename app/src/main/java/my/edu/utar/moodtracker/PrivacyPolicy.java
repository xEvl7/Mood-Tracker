package my.edu.utar.moodtracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

//Done by Khor Jia Jun 2101593
public class PrivacyPolicy extends AppCompatActivity {

    ImageView backSettingsButton;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        textView = findViewById(R.id.showText);
        backSettingsButton = findViewById(R.id.backSettings);

        String text = "Welcome to our MOOD TRACKER! We create a user-friendly apps that cater to your needs. please take some time to read our privacy policy:\n\n" +
                "1. We collect personal information that you provide to us.\n\n" +
                "2. We use your personal information to provide you with our services, such as sending you updates and notifications.\n\n" +
                "3. We reserve the right to modify our privacy policy at any time. Any such modifications will constitute your acceptance of the updated policy.\n\n" +
                "We appreciate your cooperation in complying with our privacy policy.";
        textView.append(text);

        backSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), com.example.moodtracker.Settings.class);
                startActivity(intent);
                finish();
            }
        });

    }
}