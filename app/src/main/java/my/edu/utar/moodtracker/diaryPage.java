package my.edu.utar.moodtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moodtracker.utils.Shared;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class diaryPage extends AppCompatActivity {

    private EditText titleDiary;
    private EditText contentDiary;

    private Button saveAll;
    private Button loadAll;
    private Button deleteAll;

    private TextView tvDate;
    private TextView tvEmoji;
    private ImageView emojiImage;

    private MediaPlayer mp;

    boolean DiaryEXIST = false;
    boolean DiaryEXIST2 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_page);

        //back navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Shared.initialize(getBaseContext());

        mp = MediaPlayer.create(getApplicationContext(), R.raw.btn_sound);


        titleDiary = findViewById(R.id.diaryTitleInputText);
        contentDiary = findViewById(R.id.diaryContentInputText);

        titleDiary.setTypeface(Shared.fontRegular);
        contentDiary.setTypeface(Shared.fontRegular);

        saveAll = findViewById(R.id.saveButton);
        loadAll = findViewById(R.id.loadButton);
        deleteAll = findViewById(R.id.deleteButton);

        saveAll.setTypeface(Shared.fontBold);
        loadAll.setTypeface(Shared.fontBold);
        deleteAll.setTypeface(Shared.fontBold);

        // Get selectedDate and selectedEmoji from previous
        String selectedDateDiary = getIntent().getStringExtra("selectedDate");
        tvDate = findViewById(R.id.dateText);
        tvDate.setText(selectedDateDiary);
        tvDate.setTypeface(Shared.fontRegular);

        String selectedEmojiDiary = getIntent().getStringExtra("selectedEmoji");
        tvEmoji = findViewById(R.id.moodSelectedText);
        tvEmoji.setText(selectedEmojiDiary + "~");
        tvEmoji.setTypeface(Shared.fontRegular);

        emojiImage = findViewById(R.id.moodSelectedImage);

        byte[] byteArray = getIntent().getByteArrayExtra("emojiImage");
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        emojiImage.setImageBitmap(bitmap);

        BottomNavigationView bottomView = findViewById(R.id.bottomNavigationView2);

        //contentLayout = findViewById(R.id.contentLayout);

        //bottom navigation for 1.Insert picture button 2.Emoji & Sticker button

        bottomView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                //case R.id.item1:
                //insert text
                //TextView inputText = new TextView(this);
                //inputText.setText("Hello, World!");

/*
                    // create a new AlertDialog builder
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);

                    // set the title and message of the dialog
                    builder.setTitle("Enter Text");
                    builder.setMessage("Please enter some text:");

                    // create a new EditText view and set it as the dialog's view
                    final EditText input = new EditText(this);
                    builder.setView(input);

                    // set the positive button to save the entered text
                    builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (contentLayout.getChildCount() == 0) {
                                String enteredText = input.getText().toString();
                                inputText.setText(enteredText);
                            }
                        }
                    });

                    // set the negative button to cancel the dialog
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    // show the dialog
                    AlertDialog dialog = builder.create();
                    dialog.show();*/



                //contentLayout.addView(inputText);
                // return true;

                case R.id.item1:
                    mp.start();
                    //insert picture
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    return true;

                case R.id.item2:
                    mp.start();
                    //insert emoji & sticker
                    return true;

                default:
                    return false;
            }
        });

        DiaryDBHelper dbHelper = new DiaryDBHelper(diaryPage.this);

        saveAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mp.start();
                String title = titleDiary.getText().toString();
                String content = contentDiary.getText().toString();

                if (TextUtils.isEmpty(title)) {
                    Toast.makeText(diaryPage.this, "Please enter title...", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(content)) {
                    Toast.makeText(diaryPage.this, "Please enter your story...", Toast.LENGTH_SHORT).show();
                } else {

                    String selectedDate = getIntent().getStringExtra("selectedDate");
                    String selectedEmoji = getIntent().getStringExtra("selectedEmoji");
                    String diaryTitle = ((TextInputEditText) findViewById(R.id.diaryTitleInputText)).getText().toString();
                    String diaryContent = ((TextInputEditText) findViewById(R.id.diaryContentInputText)).getText().toString();

                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    ContentValues values = new ContentValues();
                    values.put(Diary.DiaryEntry.COLUMN_SELECTED_DATE, selectedDate);
                    values.put(Diary.DiaryEntry.COLUMN_SELECTED_EMOJI, selectedEmoji);
                    values.put(Diary.DiaryEntry.COLUMN_TITLE, diaryTitle);
                    values.put(Diary.DiaryEntry.COLUMN_CONTENT, diaryContent);

                    long newRowId = db.insert(Diary.DiaryEntry.TABLE_NAME, null, values);

                    if (newRowId == -1) {
                        Toast.makeText(diaryPage.this, "Error: Failed to save data to database", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(diaryPage.this, "Saved! Live in the moment! \nMy Friend!", Toast.LENGTH_SHORT).show();
                    }

                    Intent intent = new Intent(diaryPage.this, mainPage.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }

            }
        });

        loadAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mp.start();
                String selectedDate = getIntent().getStringExtra("selectedDate");

                String[] projection = {
                        Diary.DiaryEntry._ID,
                        Diary.DiaryEntry.COLUMN_TITLE,
                        Diary.DiaryEntry.COLUMN_CONTENT,
                        Diary.DiaryEntry.COLUMN_SELECTED_DATE,
                        Diary.DiaryEntry.COLUMN_SELECTED_EMOJI
                };

                String selection = null;
                String[] selectionArgs = null;

                String sortOrder =
                        Diary.DiaryEntry.COLUMN_SELECTED_DATE + " DESC";

                List<Diary.DiaryEntry> entries = new ArrayList<>();

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

                        if (dateStr.equals(selectedDate)) {
                            DiaryEXIST = true;
                            titleDiary.setText(title);
                            contentDiary.setText(content);
                            break;
                        }
                        else
                            DiaryEXIST = false;
                    }
                    cursor.close();
                } catch (SQLException e) {
                    // handle exception
                }

                if(DiaryEXIST == false)
                    Toast.makeText(diaryPage.this, "Error: Failed to load your record...\nYour record doesn't exist!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(diaryPage.this, "Loaded! Here is your record...\nMy Friend!", Toast.LENGTH_SHORT).show();

            }
        });

        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mp.start();
                String selectedDate = getIntent().getStringExtra("selectedDate");

                /*SQLiteDatabase db = dbHelper.getWritableDatabase();

                String selection = Diary.DiaryEntry.COLUMN_SELECTED_DATE + " LIKE ?";
                String[] selectionArgs = { selectedDate };

                db.delete(Diary.DiaryEntry.TABLE_NAME, selection, selectionArgs);*/

                String[] projection = {
                        Diary.DiaryEntry._ID,
                        Diary.DiaryEntry.COLUMN_TITLE,
                        Diary.DiaryEntry.COLUMN_CONTENT,
                        Diary.DiaryEntry.COLUMN_SELECTED_DATE,
                        Diary.DiaryEntry.COLUMN_SELECTED_EMOJI
                };

                String selection = null;
                String[] selectionArgs = null;

                String sortOrder =
                        Diary.DiaryEntry.COLUMN_SELECTED_DATE + " DESC";

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
                        if (dateStr.equals(selectedDate)) {
                            DiaryEXIST2= true;
                            break;
                        }
                        else
                            DiaryEXIST2= false;
                    }
                    cursor.close();
                } catch (SQLException e) {
                    // handle exception
                }

                if(DiaryEXIST2 == false)
                    Toast.makeText(diaryPage.this, "Error: Failed to delete your record...\nYour record doesn't exist!", Toast.LENGTH_SHORT).show();
                else {
                    try (SQLiteDatabase db = dbHelper.getWritableDatabase();) {
                        String selection2 = Diary.DiaryEntry.COLUMN_SELECTED_DATE + " LIKE ?";
                        String[] selectionArgs2 = { selectedDate };

                        db.delete(Diary.DiaryEntry.TABLE_NAME, selection2, selectionArgs2);
                    } catch (SQLException e) {
                        // handle exception
                    }
                    Toast.makeText(diaryPage.this, "Deleted! \nMy Friend!", Toast.LENGTH_SHORT).show();
                    titleDiary.setText("");
                    contentDiary.setText("");
                }
            }
        });
    }
}