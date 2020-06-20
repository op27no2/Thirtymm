package op27no2.fitness.Centurion.ui.run;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.mapbox.geojson.Point;

import java.util.ArrayList;

import op27no2.fitness.Centurion.R;

public class TimerService extends Service  {
    private SharedPreferences prefs;
    private SharedPreferences.Editor edt;


    // Constants
    private static final int ID_SERVICE = 101;
    private TimerInterface myCallback;
    private IBinder serviceBinder = new GPSBinder();
    private long pausedTime;
    private long startTime;
    private long startPause;
    private float totalDistance = 0;
    private ArrayList<Point> routeCoordinates = new ArrayList<Point>();
    private ArrayList<TrackedPoint> trackPoints = new ArrayList<TrackedPoint>();
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private double lat;
    private double lon;
    private double alt;
    private double maxalt = 0;
    private double minalt = 1000000;
    private boolean paused = false;

    public void registerCallBack(TimerInterface myCallback){
        this.myCallback= myCallback;
    }


    public void unregisterCallBack(){
        this.myCallback= null;
    }

    public void pauseService(){
        paused = true;
        startPause = System.currentTimeMillis();
    }
    public void resumeService(){
        paused = false;
        pausedTime = pausedTime + (System.currentTimeMillis()-startPause);
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
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(200);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }else{
                    System.out.println("locationresult not null");
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        lat = location.getLatitude();
                        lon = location.getLongitude();
                        alt = location.getAltitude();
                    }else{
                        System.out.println("location null");
                    }
                }
            }
        };
        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);


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
        prefs = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        edt = getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
        //TODO manifest process currently makes this non functional...
        edt.putBoolean("service_running", true);
        edt.commit();

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
        channel.setImportance(NotificationManager.IMPORTANCE_HIGH);
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
        System.out.println("lat: "+ lat + "lon" + lon);
        if(!paused) {
            if (alt > maxalt) {
                maxalt = alt;
            }
            if (alt < minalt) {
                minalt = alt;
            }

            routeCoordinates.add(Point.fromLngLat(lon, lat, alt));
            if(routeCoordinates.size()>1){
                float[] distance = new float[2];
                Location.distanceBetween(routeCoordinates.get(routeCoordinates.size()-2).latitude(), routeCoordinates.get(routeCoordinates.size()-2).longitude(), routeCoordinates.get(routeCoordinates.size()-1).latitude(), routeCoordinates.get(routeCoordinates.size()-1).longitude(), distance);
                totalDistance = totalDistance+ distance[0];
            }
            System.out.println("total Distance: "+ totalDistance);


            //trackpoints same but with time, switching over in order to upload TCX???
            trackPoints.add(new TrackedPoint(System.currentTimeMillis(),Point.fromLngLat(lon, lat, alt), totalDistance));
            long elapsedTime = System.currentTimeMillis() - startTime - pausedTime;

            if (myCallback != null) {
                myCallback.getData(System.currentTimeMillis(),elapsedTime, routeCoordinates, minalt, maxalt, trackPoints);
            }
        }

    }

    @Override
    public void onDestroy() {
        System.out.println("service onDestroy");

        handler.removeCallbacks((runnable));
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(locationCallback);
        }
        //TODO manifest process currently makes this non functional...
        edt.putBoolean("service_running", false);
        edt.commit();
    }




}




