package my.edu.utar.moodtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class pickMood extends AppCompatActivity {

    //2)
    TextView tv;
    TextView tv2;
    TextView tv3;

    //Emoji Variable
    private ImageView happyFace;
    private ImageView angryFace;
    private ImageView anxiousFace;
    private ImageView confuseFace;
    private ImageView mehFace;
    private ImageView sadFace;
    private ImageView speechlessFace;

    //Variable
    String selectedEmoji;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_mood);

        //backNavigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Show today Date
        String selectedDate = getIntent().getStringExtra("selectedDate");
        tv = findViewById(R.id.textViewDate);
        tv.setText(" "+selectedDate+".");

        tv2 = findViewById(R.id.textView);
        tv3 = findViewById(R.id.textViewHello);

        //Click Emoji
        happyFace = findViewById(R.id.happyEmoji);
        angryFace = findViewById(R.id.angryEmoji);
        anxiousFace = findViewById(R.id.anxiousEmoji);
        confuseFace = findViewById(R.id.confuseEmoji);
        mehFace = findViewById(R.id.mehEmoji);
        sadFace = findViewById(R.id.sadEmoji);
        speechlessFace = findViewById(R.id.speechlessEmoji);

    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Jump to diaryPage
    private void jumpToDiaryPage(byte[] byteArray) {
        Intent intent = new Intent(pickMood.this, diaryPage.class);
        intent.putExtra("selectedDate", getIntent().getStringExtra("selectedDate"));
        intent.putExtra("selectedEmoji", selectedEmoji);
        intent.putExtra("emojiImage", byteArray);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}