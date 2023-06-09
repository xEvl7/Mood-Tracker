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
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import my.edu.utar.moodtracker.utils.Shared;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class diaryPage extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private byte[] pictureData;
    private byte[] loadPictureByte;
    //private List<Integer> picturePosition;
    private EditText titleDiary;
    private EditText contentDiary;

    private MotionEvent longPressEvent = null;

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
                Diary.DiaryEntry.COLUMN_SELECTED_EMOJI,
                Diary.DiaryEntry.COLUMN_PICTURE
        };

        String selection = null;
        String[] selectionArgs = null;

        String sortOrder =
                Diary.DiaryEntry.COLUMN_SELECTED_DATE + " DESC";

        try (SQLiteDatabase db4 = dbHelper.getReadableDatabase()) {

            Cursor cursor = db4.query(
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
                byte[] pictureByte = cursor.getBlob(
                        cursor.getColumnIndexOrThrow(Diary.DiaryEntry.COLUMN_PICTURE));
                /*int picturePosition = cursor.getInt(
                        cursor.getColumnIndexOrThrow(Diary.DiaryEntry.COLUMN_PICTURE_POSITION));*/

                if (dateStr.equals(selectedDate)) {

                    if(pictureByte == null) { // no picture

                        DiaryEXIST = true;
                        titleDiary.setText(title);
                        contentDiary.setText(content);

                        break;
                    }
                    else { // got picture

                        DiaryEXIST = true;
                        titleDiary.setText(title);
                        contentDiary.setText(content);

                        loadPictureByte = pictureByte; //save the loaded picture into global variable

                        Bitmap picture = BitmapFactory.decodeByteArray(pictureByte, 0, pictureByte.length);

                        /*ImageSpan imageSpan = new ImageSpan(this, picture, ImageSpan.ALIGN_BASELINE);

                        SpannableStringBuilder builder = new SpannableStringBuilder(contentDiary.getText());
                        builder.append(" ");
                        if (builder.length() > 0) {
                            builder.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // Insert the image at position 0
                        }
                        contentDiary.setText(builder);*/

                        Drawable drawable = new BitmapDrawable(getResources(), picture);
                        contentDiary.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);

                        break;
                    }

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

                    if(DiaryEXIST == false) { //diary not exist
                        Toast.makeText(diaryPage.this,
                                "New record will be saved...\nMy Friend!", Toast.LENGTH_SHORT).show();

                        SQLiteDatabase db = dbHelper.getWritableDatabase();

                        ContentValues values = new ContentValues();
                        values.put(Diary.DiaryEntry.COLUMN_SELECTED_DATE, selectedDate);
                        values.put(Diary.DiaryEntry.COLUMN_SELECTED_EMOJI, selectedEmoji);
                        values.put(Diary.DiaryEntry.COLUMN_TITLE, diaryTitle);
                        values.put(Diary.DiaryEntry.COLUMN_CONTENT, diaryContent);
                        values.put(Diary.DiaryEntry.COLUMN_PICTURE, pictureData); //save new pic data

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

                    } else { //diary exist

                        Toast.makeText(diaryPage.this,
                                "Your record already exist!\nUpdate your record...\nMy Friend!",
                                Toast.LENGTH_SHORT).show();
                        SQLiteDatabase db2 = dbHelper.getWritableDatabase();

                        ContentValues values = new ContentValues();
                        //values.put(Diary.DiaryEntry.COLUMN_SELECTED_DATE, selectedDate);
                        //values.put(Diary.DiaryEntry.COLUMN_SELECTED_EMOJI, selectedEmoji);
                        values.put(Diary.DiaryEntry.COLUMN_TITLE, diaryTitle);
                        values.put(Diary.DiaryEntry.COLUMN_CONTENT, diaryContent);

                        if (loadPictureByte == null) { // originally no picture
                            values.put(Diary.DiaryEntry.COLUMN_PICTURE, pictureData);
                        } else { //originally got picture
                            if(loadPictureByte == pictureData) //but user did not change the picture
                                values.put(Diary.DiaryEntry.COLUMN_PICTURE, loadPictureByte);
                            else //but user changed the picture
                                values.put(Diary.DiaryEntry.COLUMN_PICTURE, pictureData);
                        }

                        String strFilter = "date='" + selectedDate + "'";
                        long newRowId = db2.update(Diary.DiaryEntry.TABLE_NAME, values, strFilter, null);

                        if (newRowId == -1) {
                            Toast.makeText(diaryPage.this,
                                    "Error: Failed to save data to database",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(diaryPage.this,
                                    "Updated! Live in the moment! \nMy Friend!",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                    Intent intent = new Intent(diaryPage.this, mainPage.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
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

        contentDiary.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                Drawable[] drawables = contentDiary.getCompoundDrawables();
                if (drawables[1] != null) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            longPressEvent = MotionEvent.obtain(event);
                            break;
                        case MotionEvent.ACTION_CANCEL:
                        case MotionEvent.ACTION_UP:
                            longPressEvent = null;
                            break;
                    }
                }
                return false;
            }
        });

        contentDiary.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                Drawable[] drawables = contentDiary.getCompoundDrawables();
                if (drawables[1] != null) {
                    MotionEvent event = MotionEvent.obtain(longPressEvent);
                    float x = event.getX();
                    if (x > drawables[1].getBounds().right) {
                        // User clicked outside the drawable bounds
                        return false;
                    } else {
                        // User clicked on the drawable
                        contentDiary.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                        longPressEvent = null;
                        return true;
                    }
                }
                return false;
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
                Diary.DiaryEntry.COLUMN_SELECTED_EMOJI,
                Diary.DiaryEntry.COLUMN_PICTURE
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
                byte[] pictureData = cursor.getBlob(
                        cursor.getColumnIndexOrThrow(Diary.DiaryEntry.COLUMN_PICTURE));

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

                Bitmap originalBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, 570, 520, false);

                Bitmap frameBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.diaryframe);
                Bitmap resizedFrameBitmap = Bitmap.createScaledBitmap(frameBitmap, 600, 600, false);

                Bitmap finalBitmap = Bitmap.createBitmap(resizedFrameBitmap.getWidth(), resizedFrameBitmap.getHeight(), Bitmap.Config.ARGB_8888);

                Canvas canvas = new Canvas(finalBitmap);
                canvas.drawBitmap(resizedFrameBitmap, 0, 0, null);
                Rect rect = new Rect(115, 70, 485, 450);
                canvas.drawBitmap(resizedBitmap, null, rect, null);

                Drawable drawable = new BitmapDrawable(getResources(), finalBitmap);
                contentDiary.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);

                /*//need to handle if selection cursor is in titleDiary then will wrong
                int selectionStart = contentDiary.getSelectionStart();
                contentDiary.getText().insert(selectionStart, "\n");
                selectionStart = contentDiary.getSelectionStart();
                contentDiary.getText().insert(selectionStart, " ");
                selectionStart = contentDiary.getSelectionStart();

                ImageSpan imageSpan = new ImageSpan(this, finalBitmap, ImageSpan.ALIGN_BOTTOM);
                SpannableStringBuilder builder = new SpannableStringBuilder(contentDiary.getText());
                builder.setSpan(imageSpan, selectionStart-1, selectionStart, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                contentDiary.setText(builder);
                contentDiary.setSelection(selectionStart);*/

                // Convert the bitmap to a byte array
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                pictureData = stream.toByteArray();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}