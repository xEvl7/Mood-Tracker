package my.edu.utar.moodtracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

//Done by Khor Jia Jun 2101593
public class Help extends AppCompatActivity {

    ImageView backSettingsButton;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        textView = findViewById(R.id.showText);
        backSettingsButton = findViewById(R.id.backSettings);

        String text = "Welcome to our MOOD TRACKER! We create a user-friendly apps that cater to your needs. Here are a few tips to help you get started:\n\n" +
                "1. Register yourself as user!!!\n\n" + "2. Choose your mood on the calender.\n\n" + "3. You can always keep track your mood!!!\n\n" +
                "We hope you enjoy using MOOD TRACKER and hopefully to providing more great mobile app in the future!!!";
        textView.append(text);

        backSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Settings.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

    }
}