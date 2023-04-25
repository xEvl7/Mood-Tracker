package my.edu.utar.moodtracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

//Done by Khor Jia Jun 2101593
public class TermOfService extends AppCompatActivity {

    ImageView backSettingsButton;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_of_service);
        textView = findViewById(R.id.showText);
        backSettingsButton = findViewById(R.id.backSettings);

        String text = "Welcome to our MOOD TRACKER! We create a user-friendly apps that cater to your needs. please take some time to read our terms of service:\n\n" +
                "1. By using our apps, you agree to comply with our terms of service. If you do not agree with our terms, please do not use our apps.\n\n" +
                "2. Our mobile app is for education purpose only.\n\n" +
                "3. We reserve the right to modify our terms of service at any time. Any such modifications will constitute your acceptance of the updated terms.\n\n" +
                "We appreciate your cooperation in complying with our terms of service.";
        textView.append(text);

        backSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), my.edu.utar.moodtracker.Settings.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

    }
}