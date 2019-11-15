package op27no2.fitness.thirtymm.ui.run;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.location.LocationComponent;

import op27no2.fitness.thirtymm.R;

public class TimerService extends Service {

    // Constants
    private static final int ID_SERVICE = 101;
    private TimerInterface myCallback;
    private IBinder serviceBinder = new GPSBinder();
    private long startTime;

    public void registerCallBack(TimerInterface myCallback){
        this.myCallback= myCallback;
    }

    public class GPSBinder extends Binder {

        public TimerService getService(){
            return TimerService.this;
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        System.out.println("service onStartCommand");
        startTime =  System.currentTimeMillis();
        handler.postDelayed(runnable, 1000);

        return START_STICKY;
    }




    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return serviceBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // do stuff like register for BroadcastReceiver, etc.
        System.out.println("service onCreate");

        // Create the Foreground Service
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? createNotificationChannel(notificationManager) : "";
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(0)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .build();

        startForeground(ID_SERVICE, notification);


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel(NotificationManager notificationManager){
        String channelId = "my_service_channelid";
        String channelName = "My Foreground Service";
        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
        // omitted the LED color
        channel.setImportance(NotificationManager.IMPORTANCE_NONE);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        notificationManager.createNotificationChannel(channel);
        return channelId;
    }


    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            updateTime();
            handler.postDelayed(this, 100);
        }
    };
    public void updateTime(){

        long elapsedTime = System.currentTimeMillis() - startTime;
        myCallback.getData(elapsedTime);


    }

    @Override
    public void onDestroy() {
       handler.removeCallbacks((runnable));

    }


}




