package my.edu.utar.moodtracker;

import android.provider.BaseColumns;

public final class Diary {

    private int id;
    private String selectedDateText;
    private String selectedEmojiText;
    private String title;
    private String content;
    private byte[] picture;
    //private String picturePosition;

    private Diary() {}

    public Diary(int id, String selectedDateText, String selectedEmojiText, String title, String content, byte[] picture) {
        this.id = id;
        this.selectedDateText = selectedDateText;
        this.selectedEmojiText = selectedEmojiText;
        this.title = title;
        this.content = content;
        this.picture = picture;
        //this.picturePosition = picturePosition;
    }

    /* Inner class that defines the table contents */
    public static class DiaryEntry implements BaseColumns {

        public static final String TABLE_NAME = "diary_entries";
        public static final String COLUMN_SELECTED_DATE = "date";
        public static final String COLUMN_SELECTED_EMOJI = "emoji";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_PICTURE = "picture";
        //public static final String COLUMN_PICTURE_POSITION = "picturePosition";

    }

}
