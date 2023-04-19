package my.edu.utar.moodtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import my.edu.utar.moodtracker.utils.Shared;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class mainPage extends AppCompatActivity {

    //2)Calendar
    private Button addBtn;

    TextView tvMainPage;
    CalendarView cvMainPage;

    boolean exist = false;
    String selectedEmoji;

    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        Shared.initialize(getBaseContext());

        mp = MediaPlayer.create(getApplicationContext(), R.raw.btn_sound);

        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //2)Calendar
        addBtn = (Button) findViewById(R.id.addButton);

        DiaryDBHelper dbHelper = new DiaryDBHelper(mainPage.this);

        //String selectedDate = tvMainPage.getText().toString();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String todayDate = dateFormat.format(new Date());

        tvMainPage = findViewById(R.id.textViewDateMainPage);
        cvMainPage = findViewById(R.id.calendarView);

        cvMainPage.setMaxDate(System.currentTimeMillis());

        cvMainPage.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "/" + (month + 1) + "/" + year;

                mp.start();
                //add on
                DateFormat inputFormat = new SimpleDateFormat("d/M/yyyy");
                DateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd");
                Date date1 = null;
                try {
                    date1 = inputFormat.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String formattedDate = outputFormat.format(date1);
                tvMainPage.setText(formattedDate);
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mp.start();
                String selectedDate = tvMainPage.getText().toString();

                String[] projection = {
                        Diary.DiaryEntry._ID,
                        Diary.DiaryEntry.COLUMN_TITLE,
                        Diary.DiaryEntry.COLUMN_CONTENT,
                        Diary.DiaryEntry.COLUMN_SELECTED_DATE,
                        Diary.DiaryEntry.COLUMN_SELECTED_EMOJI
                };

                // Filter results WHERE "title" = 'My Title'
                String selection = null;
                String[] selectionArgs = null;

                // How you want the results sorted in the resulting Cursor
                String sortOrder =
                        Diary.DiaryEntry.COLUMN_SELECTED_DATE + " DESC";

                //List<Diary.DiaryEntry> entries = new ArrayList<>();

                try (SQLiteDatabase db = dbHelper.getReadableDatabase()) {
                    Cursor cursor = db.query(
                            Diary.DiaryEntry.TABLE_NAME,   // The table to query
                            projection,                                 // The columns to return
                            selection,                                  // The columns for the WHERE clause
                            selectionArgs,                              // The values for the WHERE clause
                            null,                                // Don't group the rows
                            null,                                 // Don't filter by row groups
                            sortOrder                                   // The sort order
                    );

                    while (cursor.moveToNext()) {
                        int id = cursor.getInt(
                                cursor.getColumnIndexOrThrow(Diary.DiaryEntry._ID));
                        String dateStr = cursor.getString(
                                cursor.getColumnIndexOrThrow(Diary.DiaryEntry.COLUMN_SELECTED_DATE)); //2023/04/18
                        String emojiStr = cursor.getString(
                                cursor.getColumnIndexOrThrow(Diary.DiaryEntry.COLUMN_SELECTED_EMOJI));
                        String title = cursor.getString(
                                cursor.getColumnIndexOrThrow(Diary.DiaryEntry.COLUMN_TITLE));
                        String content = cursor.getString(
                                cursor.getColumnIndexOrThrow(Diary.DiaryEntry.COLUMN_CONTENT));

                        if (selectedDate.length() > 8) { //means user clicked the date
                            if (dateStr.equals(selectedDate)) {
                                exist = true;
                                selectedEmoji = emojiStr;
                                break;
                            }
                            else
                                exist = false;
                        }
                        else if (dateStr.equals(todayDate)) { //means default today date
                            exist = true;
                            selectedEmoji = emojiStr;
                            break;
                        }
                        else
                            exist = false;

                    }
                    cursor.close();

                } catch (SQLException e) {
                    // handle exception
                }

                if(exist == true) {

                    Toast.makeText(mainPage.this, "This day's record already exists! Redirecting...", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(mainPage.this, diaryPage.class);
                    //Intent intent = new Intent(mainPage.this, pickMood.class);

                    intent.putExtra("selectedEmoji", selectedEmoji);

                    if(selectedEmoji.equals("happy")){
                        Bitmap happyFaceBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.happy);
                        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), happyFaceBitmap);
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        intent.putExtra("emojiImage", byteArray);
                    }
                    else if(selectedEmoji.equals("angry")){
                        Bitmap angryFaceBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.angry);
                        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), angryFaceBitmap);
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        intent.putExtra("emojiImage", byteArray);
                    }
                    else if(selectedEmoji.equals("anxious")){
                        Bitmap anxiousFaceBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.anxious);
                        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), anxiousFaceBitmap);
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        intent.putExtra("emojiImage", byteArray);
                    }
                    else if(selectedEmoji.equals("confuse")){
                        Bitmap confuseFaceBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.confused);
                        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), confuseFaceBitmap);
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        intent.putExtra("emojiImage", byteArray);
                    }
                    else if(selectedEmoji.equals("meh")){
                        Bitmap mehFaceBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.meh);
                        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), mehFaceBitmap);
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        intent.putExtra("emojiImage", byteArray);
                    }
                    else if(selectedEmoji.equals("sad")){
                        Bitmap sadFaceBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.sad);
                        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), sadFaceBitmap);
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        intent.putExtra("emojiImage", byteArray);
                    }
                    else if(selectedEmoji.equals("speechless")){
                        Bitmap speechlessFaceBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.speechless);
                        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), speechlessFaceBitmap);
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        intent.putExtra("emojiImage", byteArray);
                    }

                    if (selectedDate.length() > 8) {
                        intent.putExtra("selectedDate", selectedDate);
                    } else {
                        intent.putExtra("selectedDate", todayDate);
                    }
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
                else {

                    Toast.makeText(mainPage.this, "Make new record...", Toast.LENGTH_SHORT).show();
                    Intent intentAdd = new Intent(mainPage.this, pickMood.class);

                    if (selectedDate.length() > 8) {
                        intentAdd.putExtra("selectedDate", selectedDate);
                    } else {
                        intentAdd.putExtra("selectedDate", todayDate);
                    }

                    startActivity(intentAdd);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        });

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
