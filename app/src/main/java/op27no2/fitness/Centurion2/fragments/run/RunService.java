package op27no2.fitness.Centurion2.fragments.run;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
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
import java.util.List;
import java.util.Locale;

import op27no2.fitness.Centurion2.Bluetooth.BluetoothHandler;
import op27no2.fitness.Centurion2.Bluetooth.HeartRateMeasurement;
import op27no2.fitness.Centurion2.R;
import timber.log.Timber;

import static androidx.core.app.ActivityCompat.requestPermissions;
import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class RunService extends Service  {
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
    private float acc;
    private float accuracyThreshold = 50;
    private double metersPerSecond = .1;
    private double maxalt = 0;
    private double minalt = 1000000;
    private boolean paused = false;
    private boolean smoothInput = true;
    private static final int ACCESS_LOCATION_REQUEST = 2;
    private int heartrate = 0;


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

        public RunService getService(){
            return RunService.this;
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
                        acc   = location.getAccuracy();
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

        registerReceiver(heartRateDataReceiver, new IntentFilter( BluetoothHandler.MEASUREMENT_HEARTRATE ));

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
        System.out.println("lat: "+ lat + " lon:" + lon+" acc"+acc);
        if(!paused) {
            if (alt > maxalt) {
                maxalt = alt;
            }
            if (alt < minalt) {
                minalt = alt;
            }

            if(routeCoordinates.size()>1){
                float[] distance = new float[2];
                //distance in meters
             //   Location.distanceBetween(routeCoordinates.get(routeCoordinates.size()-2).latitude(), routeCoordinates.get(routeCoordinates.size()-2).longitude(), routeCoordinates.get(routeCoordinates.size()-1).latitude(), routeCoordinates.get(routeCoordinates.size()-1).longitude(), distance);
                Location.distanceBetween(routeCoordinates.get(routeCoordinates.size()-1).latitude(), routeCoordinates.get(routeCoordinates.size()-1).longitude(), lat, lon, distance);

                //gets rid of large GPS jumps, must be feasible to have moved that distance, i.e. for running <25 mph =~ 11 mps
                long previousTimeMillis = trackPoints.get(trackPoints.size()-1).getTimestamp();
                System.out.println("x previousTime:" + previousTimeMillis);
                System.out.println("x currentTime:" + System.currentTimeMillis());
                double timeDif = ((double) System.currentTimeMillis() - (double) previousTimeMillis)/1000.0;
                System.out.println("x timeDif:" + timeDif);
                System.out.println("x mps:" + distance[0]/(timeDif));

                //if distance to last tracked point and proposed point in meters divided by previous timestamp > 11, its a jump and ignore, must be <11
                // will keep checking as distance is accrued, so will eventually log and hopefully GPS is fixed., could maybe check if its near a road too...

                //distance in mps is greater than 0.1, i.e. remove drift when stopped...
                if(distance[0]/(timeDif)>metersPerSecond && acc<accuracyThreshold) {
                    System.out.println("z x distance: "+distance[0] +" seconds:"+ timeDif);
                    System.out.println("z x mps: "+distance[0]/timeDif);
                    totalDistance = totalDistance+ distance[0];
                    routeCoordinates.add(Point.fromLngLat(lon, lat, alt));

                    //trackedpoints same as coords but with time, total distance, and HR, switching over in order to upload TCX, redundant at the moment!
                    //Also different from 'trackpoints' which are the formal structure we upload in TCX helper. This makes that conversion easier
                    trackPoints.add(new TrackedPoint(System.currentTimeMillis(),Point.fromLngLat(lon, lat, alt), totalDistance, heartrate));
                }

            }else {
                routeCoordinates.add(Point.fromLngLat(lon, lat, alt));
                //trackedpoints same as coords but with time, total distance, and HR, switching over in order to upload TCX, redundant at the moment!
                trackPoints.add(new TrackedPoint(System.currentTimeMillis(),Point.fromLngLat(lon, lat, alt), totalDistance, heartrate));
            }

            System.out.println("total Distance: "+ totalDistance);


            long runningTime = System.currentTimeMillis() - startTime - pausedTime;

            if (myCallback != null) {
                myCallback.getData(System.currentTimeMillis(),runningTime, routeCoordinates, minalt, maxalt, trackPoints);
            }
        }

    }

    @Override
    public void onDestroy() {
        System.out.println("service onDestroy");
        this.unregisterReceiver(heartRateDataReceiver);

        handler.removeCallbacks((runnable));
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(locationCallback);
        }
        //TODO manifest process currently makes this non functional...
        edt.putBoolean("service_running", false);
        edt.commit();
    }










    //BLUETOOTH ADDITIONS START
    private final BroadcastReceiver heartRateDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            HeartRateMeasurement measurement = (HeartRateMeasurement) intent.getSerializableExtra(BluetoothHandler.MEASUREMENT_HEARTRATE_EXTRA);
            if (measurement == null) return;
            heartrate =    measurement.pulse;
            System.out.println("service HR measurement: "+ String.format(Locale.ENGLISH, "%d", measurement.pulse));
        }
    };

/*
    private void initBluetoothHandler()
    {
        System.out.println("Bluetooth Handler has been called");
        BluetoothHandler.getInstance(getApplicationContext());
    }
*/




    //BLUETOOTH ADDITIONS END









}




