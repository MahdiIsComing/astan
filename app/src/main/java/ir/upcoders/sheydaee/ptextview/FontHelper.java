package ir.upcoders.sheydaee.ptextview;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by mahdi on 12/19/16.
 */
public class FontHelper {
    private static FontHelper instance;
    private static Typeface persianTypeface;

    private FontHelper(Context context) {
        persianTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/XM Yekan.ttf");
    }

    public static synchronized FontHelper getInstance(Context context) {
        if (instance == null)
            instance = new FontHelper(context);
        return instance;
    }

    public Typeface getPersianTextTypeface() {
        return persianTypeface;
    }
}