package op27no2.fitness.Centurion;

import android.content.Context;

import java.io.IOException;


public class MyApplication extends android.app.Application {
    private static Context context;
    private static MyApplication sInstance = null;


    public MyApplication() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.context = op27no2.fitness.Centurion.MyApplication.getAppContext();
        sInstance = this;

        System.out.println("APPLICATION CALLED");

        int pid = android.os.Process.myPid();
        String whiteList = "logcat -P '" + pid + "'";
        try {
            Runtime.getRuntime().exec(whiteList).waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static Context getAppContext() {
        return MyApplication.context;
    }
    public static MyApplication getInstance() {
        return sInstance ;
    }
}
