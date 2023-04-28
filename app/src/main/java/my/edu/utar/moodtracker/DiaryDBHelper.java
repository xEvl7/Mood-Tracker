package my.edu.utar.moodtracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DiaryDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "diaryDB.db";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Diary.DiaryEntry.TABLE_NAME + " (" +
                    Diary.DiaryEntry._ID + " INTEGER PRIMARY KEY," +
                    Diary.DiaryEntry.COLUMN_SELECTED_DATE + " TEXT NOT NULL, " +
                    Diary.DiaryEntry.COLUMN_SELECTED_EMOJI + " TEXT NOT NULL, " +
                    Diary.DiaryEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                    Diary.DiaryEntry.COLUMN_CONTENT + " TEXT NOT NULL," +
                    Diary.DiaryEntry.COLUMN_PICTURE + " BLOB)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Diary.DiaryEntry.TABLE_NAME;


    public DiaryDBHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
