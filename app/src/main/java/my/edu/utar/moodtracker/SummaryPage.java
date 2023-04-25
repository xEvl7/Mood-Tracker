package my.edu.utar.moodtracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SummaryPage extends AppCompatActivity {

    private static final String TAG = "DataSummaryActivity";
    private List<String> data = new ArrayList<>();
    private DiaryDBHelper dbHelper;
    private SQLiteDatabase database;
    LineChart weekLineChart;
    private ArrayList<Entry> diarydata = null;
    int[] colorClassArray = new int[]{Color.LTGRAY,Color.BLUE,Color.CYAN,Color.DKGRAY,Color.GREEN,Color.MAGENTA,Color.RED};

    PieChart totalEmojiChart;
    private ArrayList<PieEntry> numEmoji = null;

    private ImageView backSettingsButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_page);

        /*//backNavigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        backSettingsButton = findViewById(R.id.backSettings);

        backSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), mainPage.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        dbHelper = new DiaryDBHelper(this);
        database = dbHelper.getWritableDatabase();

        getWeekSum();
        getMonthEmojiSum();
    }


    private void getWeekSum(){
        weekLineChart = findViewById(R.id.line_chart);
        weekLineChart.setBackgroundColor(Color.WHITE);
        //listView = findViewById(R.id.listView);
        dbHelper = new DiaryDBHelper(this);
        database = dbHelper.getWritableDatabase();
        String[] columns = new String[]{Diary.DiaryEntry.COLUMN_SELECTED_DATE, Diary.DiaryEntry.COLUMN_SELECTED_EMOJI};
        String sortOrder = Diary.DiaryEntry.COLUMN_SELECTED_DATE + " DESC";

        Cursor cursor1 = database.rawQuery("SELECT COUNT(*) FROM " + Diary.DiaryEntry.TABLE_NAME, null);
        cursor1.moveToFirst();
        int count = cursor1.getInt(0);
        if (count == 0) {
            // handle empty database
            return;
        }
        cursor1.close();


        Cursor cursor = database.query(Diary.DiaryEntry.TABLE_NAME, columns, null, null, null, null, sortOrder,"7");
        diarydata = new ArrayList<Entry>();
        data.add(" ");
        int i = 1;
        if(cursor.moveToLast()) {
            do {
                int id = i++;
                @SuppressLint("Range") String dateStr = cursor.getString(cursor.getColumnIndex(Diary.DiaryEntry.COLUMN_SELECTED_DATE));
                DateFormat inputFormat = new SimpleDateFormat("yyyy/MM/dd");
                DateFormat outputFormat = new SimpleDateFormat("dd/MM");
                Date date = null;
                try {
                    date = inputFormat.parse(dateStr);
                } catch (ParseException e) {
                    Log.e(TAG, "Error parsing date: " + dateStr, e);
                    continue;
                }
                String forDate = outputFormat.format(date);
                @SuppressLint("Range") String emoji = cursor.getString(cursor.getColumnIndex(Diary.DiaryEntry.COLUMN_SELECTED_EMOJI));
                diarydata.add(new Entry(id, getEmojiValue(emoji)));
                data.add(forDate);
            }while (cursor.moveToPrevious());
        }
        cursor.close();

        LineDataSet lineDataSet = new LineDataSet(diarydata,"");
        //linedataset pattern
        lineDataSet.setColor(Color.BLUE);
        lineDataSet.setLineWidth(4f);
        lineDataSet.setCircleRadius(8f);
        lineDataSet.setCircleHoleRadius(6f);
        lineDataSet.setValueTextSize(0);
        lineDataSet.enableDashedLine(5,3,0);
        lineDataSet.setCircleColor(Color.BLUE);


        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);


        //Graph pattern
        weekLineChart.setNoDataText("No Data");
        weekLineChart.setDrawGridBackground(true);
        weekLineChart.setDrawBorders(true);
        weekLineChart.setBorderWidth(2);
        weekLineChart.setBorderColor(Color.DKGRAY);

        weekLineChart.getDescription().setEnabled(false);

        weekLineChart.getLegend().setEnabled(true);
        weekLineChart.getLegend().setFormSize(0);

        XAxis xAxis = weekLineChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(data));
        xAxis.setTextSize(12f);

        //Axis setting
        weekLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        weekLineChart.getXAxis().setAxisMinimum(0);
        weekLineChart.getXAxis().setAxisMaximum(8);
        weekLineChart.getAxisRight().setEnabled(false);
        weekLineChart.getAxisLeft().setEnabled(true);
        weekLineChart.getAxisLeft().setValueFormatter(new IndexAxisValueFormatter(new String[]{"","Speechless","Sad","Meh","Confuse","Anxious","Angry","Happy"}));
        weekLineChart.getAxisLeft().setTextSize(15f);
        weekLineChart.getAxisLeft().setAxisMinimum(0);
        weekLineChart.getAxisLeft().setAxisMaximum(8);

        LineData data = new LineData(dataSets);
        weekLineChart.setData(data);
        weekLineChart.invalidate();
    }

    private int getEmojiValue(String emoji) {
        switch (emoji) {
            case "happy":
                return 7;
            case "angry":
                return 6;
            case "anxious":
                return 5;
            case "confuse":
                return 4;
            case "meh":
                return 3;
            case "sad":
                return 2;
            case "speechless":
                return 1;
            default:
                return 0;
        }
    }

    private void getMonthEmojiSum() {
        totalEmojiChart = findViewById(R.id.piechart);
        totalEmojiChart.setBackgroundColor(Color.WHITE);
        totalEmojiChart.setNoDataText("No Data");
        String[] pieCol = new String[]{Diary.DiaryEntry.COLUMN_SELECTED_EMOJI};
        String sortOrder = Diary.DiaryEntry.COLUMN_SELECTED_DATE +" DESC";

        Cursor cursor1 = database.rawQuery("SELECT COUNT(*) FROM " + Diary.DiaryEntry.TABLE_NAME, null);
        cursor1.moveToFirst();
        int count = cursor1.getInt(0);
        if (count == 0) {
            // handle empty database
            return;
        }
        cursor1.close();

        Cursor cursor = database.query(Diary.DiaryEntry.TABLE_NAME,pieCol,null,null,null,null,sortOrder,"30");
        numEmoji = new ArrayList<PieEntry>();

        int happy = 0, angry = 0, anxious = 0, confuse = 0, meh = 0, sad = 0, speechless = 0;
        if(cursor.moveToLast()){
            do {
                @SuppressLint("Range") String emoji = cursor.getString(cursor.getColumnIndex(Diary.DiaryEntry.COLUMN_SELECTED_EMOJI));
                if(emoji.equals("happy")){
                    happy++;
                }else if(emoji.equals("angry")){
                    angry++;
                }else if(emoji.equals("anxious")){
                    anxious++;
                }else if(emoji.equals("confuse")){
                    confuse++;
                }else if(emoji.equals("meh")){
                    meh++;
                }else if(emoji.equals("sad")){
                    sad++;
                }else if(emoji.equals("speechless")){
                    speechless++;
                }

            }while (cursor.moveToPrevious());
            numEmoji.add(new PieEntry(happy,"Happy"));
            numEmoji.add(new PieEntry(angry,"Angry"));
            numEmoji.add(new PieEntry(anxious,"Anxious"));
            numEmoji.add(new PieEntry(confuse,"Confuse"));
            numEmoji.add(new PieEntry(meh,"Meh"));
            numEmoji.add(new PieEntry(sad,"Sad"));
            numEmoji.add(new PieEntry(speechless,"Speechless"));

        }
        cursor.close();

        PieDataSet totalEmojiDataSet = new PieDataSet(numEmoji," ");
        totalEmojiDataSet.setColors(colorClassArray);

        totalEmojiDataSet.setValueLinePart1OffsetPercentage(80f);
        totalEmojiDataSet.setValueLineColor(Color.LTGRAY);
        totalEmojiDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        totalEmojiDataSet.setSliceSpace(2f);
        totalEmojiDataSet.setHighlightEnabled(true);

        PieData pieData = new PieData(totalEmojiDataSet);

        totalEmojiChart.getDescription().setEnabled(false);
        totalEmojiChart.setExtraOffsets(20,5,20,5);

        //set legend
        totalEmojiChart.getLegend().setEnabled(true);
        totalEmojiChart.getLegend().setWordWrapEnabled(true);
        totalEmojiChart.getLegend().setXEntrySpace(20);
        totalEmojiChart.getLegend().setTextSize(15);
        totalEmojiChart.getLegend().setFormSize(20);
        totalEmojiChart.getLegend().setFormToTextSpace(10);

        //set design
        totalEmojiChart.setDrawEntryLabels(true);
        totalEmojiChart.setUsePercentValues(true);
        totalEmojiChart.setCenterText("30 DAY EMOJIS");
        totalEmojiChart.setCenterTextSize(20);
        totalEmojiChart.setCenterTextRadiusPercent(50);
        totalEmojiChart.setRotationEnabled(false);
        pieData.setValueTextSize(12);
        pieData.setValueFormatter(new PercentFormatter());




        totalEmojiChart.setData(pieData);
        totalEmojiChart.invalidate();
    }
}