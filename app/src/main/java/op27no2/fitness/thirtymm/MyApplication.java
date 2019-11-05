package op27no2.fitness.thirtymm;

import android.content.Context;


public class MyApplication extends android.app.Application {
    private static Context context;

    public MyApplication() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.context = op27no2.fitness.thirtymm.MyApplication.getAppContext();
        System.out.println("APPLICATION CALLED");

    }
    public static Context getAppContext() {
        return MyApplication.context;
    }
}
