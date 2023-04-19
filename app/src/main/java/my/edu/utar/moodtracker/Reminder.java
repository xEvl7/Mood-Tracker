package my.edu.utar.moodtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class Reminder extends AppCompatActivity {

    private DailyReminder mDailyReminder;
    private TimePicker mTimePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        TextView tvReminder = findViewById(R.id.daily_reminder_label);

        mDailyReminder = new DailyReminder(this);
        mTimePicker = findViewById(R.id.time_picker);

        Button setReminderButton = findViewById(R.id.set_daily_reminder_button);
        setReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the hour and minute from the TimePicker
                int hour = mTimePicker.getCurrentHour();
                int minute = mTimePicker.getCurrentMinute();

                // Set the reminder using the DiaryReminder class
                mDailyReminder.setReminder(hour, minute);

                // Show a toast message confirming that the reminder has been set
                Toast.makeText(Reminder.this, "Daily reminder set for " + hour + ":" + minute + "...",
                        Toast.LENGTH_SHORT).show();
            }
        });

        Button cancelReminderButton = findViewById(R.id.cancel_daily_reminder_button);
        cancelReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDailyReminder.cancelReminder();

                Toast.makeText(Reminder.this, "Daily reminder cancelled...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}