package my.edu.utar.moodtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class mainPage extends AppCompatActivity {

    //2)Calendar
    private Button addBtn;

    TextView tvMainPage;
    CalendarView cvMainPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        //2)Calendar
        addBtn = (Button) findViewById(R.id.addButton);

        DiaryDBHelper dbHelper = new DiaryDBHelper(mainPage.this);

        //String selectedDate = tvMainPage.getText().toString();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String todayDate = dateFormat.format(new Date());

        tvMainPage = findViewById(R.id.textViewDateMainPage);
        cvMainPage = findViewById(R.id.calendarView);

        //BottomNavigation
        BottomNavigationView bottomView = findViewById(R.id.bottomNavigationView);

        bottomView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.quote:
                    mp.start();
                    Intent intentQuote = new Intent(mainPage.this, quotePage.class);
                    startActivity(intentQuote);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    return true;

                case R.id.summary:
                    mp.start();
                    Intent intentDiary = new Intent(mainPage.this, SummaryPage.class);
                    startActivity(intentDiary);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    return true;

                default:
                    return false;
            }
        });

    }
    //1) Setting
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int item_id = item.getItemId();

        if (item_id == R.id.setting) {

            Intent intentSetting = new Intent(mainPage.this, Settings.class);
            startActivity(intentSetting);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            return true;
        }

        else if (item_id == R.id.reminder) {

           /* mTimePicker = findViewById(R.id.time_picker);
            mReminder = new Reminder(this);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 20); // Reminder time is 8 PM
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            mReminder.setReminder(calendar);*/

            Intent intentReminder = new Intent(mainPage.this, Reminder.class);
            startActivity(intentReminder);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            return true;
        }

        return false;
    }
}
