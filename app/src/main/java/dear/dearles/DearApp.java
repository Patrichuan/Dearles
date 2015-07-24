package dear.dearles;

import android.app.Application;
import android.content.Context;

import dear.dearles.parse.ParseHelper;

public class DearApp extends Application {

    private static final String TAG = DearApp.class.getSimpleName();
    private static DearApp instance;

    private ParseHelper DB;



    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        InitializeParse();
    }


    public void InitializeParse () {
        DB = new ParseHelper(getContext());
        DB.Initialize();
        DB.Test();
    }



    public static Context getContext() {
        return instance.getApplicationContext();
    }


}