package op27no2.fitness.thirtymm;

import android.content.Context;

import java.io.IOException;


public class MyApplication extends android.app.Application {
    private static Context context;

    public MyApplication() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.context = op27no2.fitness.thirtymm.MyApplication.getAppContext();
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
}
