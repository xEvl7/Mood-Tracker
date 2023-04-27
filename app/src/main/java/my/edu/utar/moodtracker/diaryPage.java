package my.edu.utar.moodtracker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import my.edu.utar.moodtracker.utils.Shared;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class diaryPage extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private EditText titleDiary;
    private EditText contentDiary;

    private AppCompatButton saveAll;
    private AppCompatButton deleteAll;

    private TextView tvDate;
    private TextView tvEmoji;
    private ImageView emojiImage;

    private ImageView backSettingsButton;

    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_page);
        Shared.initialize(getBaseContext());
        mp = MediaPlayer.create(getApplicationContext(), R.raw.btn_sound);

        backSettingsButton = findViewById(R.id.backSettings);

        titleDiary = findViewById(R.id.diaryTitleInputText);
        contentDiary = findViewById(R.id.diaryContentInputText);
        saveAll = findViewById(R.id.saveButton);
        deleteAll = findViewById(R.id.deleteButton);
        emojiImage = findViewById(R.id.moodSelectedImage);

        DiaryDBHelper dbHelper = new DiaryDBHelper(diaryPage.this);

        titleDiary.setTypeface(Shared.fontRegular);
        contentDiary.setTypeface(Shared.fontRegular);
        saveAll.setTypeface(Shared.fontRegular);
        deleteAll.setTypeface(Shared.fontRegular);

        String selectedDateDiary = getIntent().getStringExtra("selectedDate");
        tvDate = findViewById(R.id.dateText);
        tvDate.setText(selectedDateDiary);
        tvDate.setTypeface(Shared.fontRegular);

        String selectedEmojiDiary = getIntent().getStringExtra("selectedEmoji");
        tvEmoji = findViewById(R.id.moodSelectedText);
        tvEmoji.setText(selectedEmojiDiary + "~");
        tvEmoji.setTypeface(Shared.fontRegular);

        byte[] byteArray = getIntent().getByteArrayExtra("emojiImage");
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        emojiImage.setImageBitmap(bitmap);

        String selectedDate = getIntent().getStringExtra("selectedDate");

        BottomNavigationView bottomView = findViewById(R.id.bottomNavigationView2);

        backSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), mainPage.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        //load data directly
        boolean DiaryEXIST = false;

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
                    Diary.DiaryEntry.TABLE_NAME,                // The table to query
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

        }

        if(DiaryEXIST == false)
            Toast.makeText(diaryPage.this,
                    "Yay you can create new record!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(diaryPage.this,
                    "Loaded! Here is your record...\nMy Friend!\nNow you can edit it!",
                    Toast.LENGTH_SHORT).show();

        //bottom navigation for 1.Insert picture button 2.Emoji & Sticker button
        bottomView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {

                case R.id.item1:
                    mp.start();
                    //insert picture
                    /*Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    //startActivity(intent);
                    startActivityForResult(intent, PICK_IMAGE_REQUEST);*/

                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                    return true;

                case R.id.item2:
                    mp.start();
                    //insert emoji & sticker
                    return true;

                default:
                    return false;
            }
        });

        saveAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mp.start();
                String selectedDate = getIntent().getStringExtra("selectedDate");
                String selectedEmoji = getIntent().getStringExtra("selectedEmoji");
                String diaryTitle = titleDiary.getText().toString();
                String diaryContent = contentDiary.getText().toString();

                if (TextUtils.isEmpty(diaryTitle)) {

                    Toast.makeText(diaryPage.this,
                            "Please enter title...", Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(diaryContent)) {

                    Toast.makeText(diaryPage.this,
                            "Please enter your story...", Toast.LENGTH_SHORT).show();

                } else {

                    boolean DiaryEXIST = getDiaryEXIST(selectedDate);

                    if(DiaryEXIST == false) {
                        Toast.makeText(diaryPage.this,
                                "New record will be saved...\nMy Friend!", Toast.LENGTH_SHORT).show();

                        SQLiteDatabase db = dbHelper.getWritableDatabase();

                        ContentValues values = new ContentValues();
                        values.put(Diary.DiaryEntry.COLUMN_SELECTED_DATE, selectedDate);
                        values.put(Diary.DiaryEntry.COLUMN_SELECTED_EMOJI, selectedEmoji);
                        values.put(Diary.DiaryEntry.COLUMN_TITLE, diaryTitle);
                        values.put(Diary.DiaryEntry.COLUMN_CONTENT, diaryContent);

                        long newRowId = db.insert(Diary.DiaryEntry.TABLE_NAME, null, values);

                        if (newRowId == -1) {
                            Toast.makeText(diaryPage.this,
                                    "Error: Failed to save data to database",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(diaryPage.this,
                                    "Saved! Live in the moment! \nMy Friend!",
                                    Toast.LENGTH_SHORT).show();
                        }

                        Intent intent = new Intent(diaryPage.this, mainPage.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();

                    } else {

                        Toast.makeText(diaryPage.this,
                                "Your record already exist!\nUpdate your record...\nMy Friend!",
                                Toast.LENGTH_SHORT).show();
                        SQLiteDatabase db = dbHelper.getWritableDatabase();

                        ContentValues values = new ContentValues();
                        //values.put(Diary.DiaryEntry.COLUMN_SELECTED_DATE, selectedDate);
                        //values.put(Diary.DiaryEntry.COLUMN_SELECTED_EMOJI, selectedEmoji);
                        values.put(Diary.DiaryEntry.COLUMN_TITLE, diaryTitle);
                        values.put(Diary.DiaryEntry.COLUMN_CONTENT, diaryContent);

                        String strFilter = "date='" + selectedDate +"'";
                        long newRowId = db.update(Diary.DiaryEntry.TABLE_NAME, values, strFilter, null);

                        if (newRowId == -1) {
                            Toast.makeText(diaryPage.this,
                                    "Error: Failed to save data to database",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(diaryPage.this,
                                    "Updated! Live in the moment! \nMy Friend!",
                                    Toast.LENGTH_SHORT).show();
                        }

                        Intent intent = new Intent(diaryPage.this, mainPage.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                    }

                }

            }
        });

        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mp.start();

                String selectedDate = getIntent().getStringExtra("selectedDate");
                boolean DiaryEXIST = getDiaryEXIST(selectedDate);

                if(DiaryEXIST == false)
                    Toast.makeText(diaryPage.this,
                            "Error: Failed to delete your record...\nYour record doesn't exist!",
                            Toast.LENGTH_SHORT).show();

                else {

                    try (SQLiteDatabase db = dbHelper.getWritableDatabase();) {

                        String selection2 = Diary.DiaryEntry.COLUMN_SELECTED_DATE + " LIKE ?";
                        String[] selectionArgs2 = { selectedDate };

                        db.delete(Diary.DiaryEntry.TABLE_NAME, selection2, selectionArgs2);

                    } catch (SQLException e) {

                    }

                    Toast.makeText(diaryPage.this,
                            "Deleted! \nMy Friend!\nNow you can select another mood for it!",
                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(diaryPage.this, pickMood.class);
                    intent.putExtra("selectedDate", selectedDate);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        });
    }

    boolean getDiaryEXIST(String selectedDate) {
        DiaryDBHelper dbHelper = new DiaryDBHelper(diaryPage.this);

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
                    Diary.DiaryEntry.TABLE_NAME,                // The table to query
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
                    return true;
                }

            }
            cursor.close();

        } catch (SQLException e) {

        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                // resize bitmap
                int nh = (int) ( bitmap.getHeight() * (512.0 / bitmap.getWidth()) );
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);

                // add image to diary page EditText
                int selectionStart = contentDiary.getSelectionStart();
                contentDiary.getText().insert(selectionStart, "\n");
                selectionStart = contentDiary.getSelectionStart();
                contentDiary.getText().insert(selectionStart, " ");
                selectionStart = contentDiary.getSelectionStart();
                ImageSpan imageSpan = new ImageSpan(this, scaled, ImageSpan.ALIGN_BOTTOM);
                SpannableStringBuilder builder = new SpannableStringBuilder(contentDiary.getText());
                builder.setSpan(imageSpan, selectionStart-1, selectionStart, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                contentDiary.setText(builder);
                contentDiary.setSelection(selectionStart);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}