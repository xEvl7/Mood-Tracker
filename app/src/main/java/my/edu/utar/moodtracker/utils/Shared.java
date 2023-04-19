package my.edu.utar.moodtracker.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.Typeface;

public final class Shared {

    private static ContextWrapper instance;
    private static SharedPreferences pref;

    public static Typeface fontRegular;
    public static Typeface fontBold;
    public static Typeface fontLight;

    public static void initialize(Context base) {

        instance = new ContextWrapper(base);
        pref = instance.getSharedPreferences("com.example.moodtracker", Context.MODE_PRIVATE);

        fontRegular = Typeface.createFromAsset(instance.getAssets(),"fonts/SourceSansPro-Regular.ttf");
        fontBold = Typeface.createFromAsset(instance.getAssets(),"fonts/SourceSansPro-Bold.ttf");
        fontLight = Typeface.createFromAsset(instance.getAssets(),"fonts/SourceSansPro-Light.ttf");
    }

}
