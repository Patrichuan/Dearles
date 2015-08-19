package dear.dearles.customclasses;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;


public class ScreenMeasurement {

    DisplayMetrics metrics;

    int ScreenHeight, ScreenWidth;
    int StatusBarHeight;
    int ToolbarHeight;

    public ScreenMeasurement(Context context) {
        // getWindowManager().getDefaultDisplay().getMetrics(metrics);
        metrics = new DisplayMetrics();
        metrics = Resources.getSystem().getDisplayMetrics();
        ScreenWidth = metrics.widthPixels;
        ScreenHeight = metrics.heightPixels;
        StatusBarHeight = (int) convertDpToPixel(24, context);
        ToolbarHeight = (int) convertDpToPixel(56, context);
    }


    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    public int getScreenHeight() {
        return ScreenHeight;
    }

    public int getScreenWidth() {
        return ScreenWidth;
    }

    public int getStatusBarHeight() {
        return StatusBarHeight;
    }

    public int getToolbarHeight() {
        return ToolbarHeight;
    }

}