package op27no2.fitness.thirtymm.ui.run;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.FillExtrusionLayer;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.rilixtech.materialfancybutton.MaterialFancyButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;

import op27no2.fitness.thirtymm.R;

import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionHeight;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionOpacity;

public class RunFragment extends Fragment implements OnMapReadyCallback, PermissionsListener, TimerInterface {
    private String mapboxToken = "pk.eyJ1Ijoib3AyN25vMiIsImEiOiJjazJ0cmxvdGswejJ5M2NsN3g0ZzNucTMzIn0.SvSJkId0jesW0T3aM92q0Q";
    private MapView mapView;
    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private Vibrator rabbit;
    private ArrayList<Point> routeCoordinates = new ArrayList<Point>();
    private Style mStyle;
    private TextView timerText;
    private long startTime;
    private TimerService timerService;
    private Intent notifyMeIntent;
    private SharedPreferences prefs;
    private SharedPreferences.Editor edt;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Mapbox.getInstance(getActivity(), mapboxToken);
        View view = inflater.inflate(R.layout.fragment_run, container, false);
        rabbit = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        timerText = view.findViewById(R.id.counttime);
        prefs = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        edt = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();


        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        MaterialFancyButton mStart = view.findViewById(R.id.start_run);
        mStart.setOnClickListener(view1 -> {
            System.out.println("start click");
            startTimerService();
            //startRun();

        });


        MaterialFancyButton mStop = view.findViewById(R.id.stop_run);
        mStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            System.out.println("stop click");
            stopRun();
            }
        });


        ImageView mLocate = view.findViewById(R.id.locate);
        mLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rabbit.vibrate(50);
                if(mapboxMap.getLocationComponent().getLastKnownLocation() != null) { // Check to ensure coordinates aren't null, probably a better way of doing this...
                    mapboxMap.animateCamera(com.mapbox.mapboxsdk.camera.CameraUpdateFactory.newLatLngZoom(new LatLng(mapboxMap.getLocationComponent().getLastKnownLocation().getLatitude(),mapboxMap.getLocationComponent().getLastKnownLocation().getLongitude()), 16));
                }

                updateLine();


            }
        });

        return view;
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
        long micro = elapsedTime / 100000;
        long elapsedSeconds = elapsedTime / 1000;
        long secondsDisplay = elapsedSeconds % 60;
        long elapsedMinutes = elapsedSeconds / 60;
        long minutesDisplay = elapsedMinutes % 60;
        if(secondsDisplay<10) {
            timerText.setText(minutesDisplay + ":0" + secondsDisplay);
        }else{
            timerText.setText(minutesDisplay + ":" + secondsDisplay);
        }

        LocationComponent locationComponent = mapboxMap.getLocationComponent();
        Location mloc = locationComponent.getLastKnownLocation();
        routeCoordinates.add(Point.fromLngLat(mloc.getLongitude(), mloc.getLatitude(), mloc.getAltitude()));
        System.out.println(routeCoordinates.get(routeCoordinates.size()-1));
        updateLine();
    }
    public void startRun(){
        System.out.println("start click2");



        startTime = System.currentTimeMillis();
    //    handler.postDelayed(runnable, 1000);
    }

    public void stopRun(){
      unbindService();

      //  handler.removeCallbacks(runnable);
    }

    private void updateLine(){

        if(mStyle.getSource("line-source") == null) {
            mStyle.addSource(new GeoJsonSource("line-source",
                    FeatureCollection.fromFeatures(new Feature[]{Feature.fromGeometry(
                            LineString.fromLngLats(routeCoordinates)
                    )})));
        }else {
            GeoJsonSource line = mStyle.getSourceAs("line-source");
            if (line != null) {
                line.setGeoJson(FeatureCollection.fromFeature(
                        Feature.fromGeometry(LineString.fromLngLats(routeCoordinates))
                ));
            }
        }

        // add polygon extrusion layer.
/*        if(mStyle.getSource("extrusion") == null) {
            mStyle.addSource(new GeoJsonSource("extrusion",
                    FeatureCollection.fromFeatures(new Feature[]{Feature.fromGeometry(
                            LineString.fromLngLats(routeCoordinates)
                    )})));
        }else {
            GeoJsonSource ex = mStyle.getSourceAs("extrusion");
            if (ex != null) {
                ex.setGeoJson(FeatureCollection.fromFeature(
                        Feature.fromGeometry(LineString.fromLngLats(routeCoordinates))
                ));
            }
        }*/

        // The layer properties for our line. This is where we make the line dotted, set the
        // color, etc.
        if(mStyle.getLayer("linelayer") == null) {
            mStyle.addLayer(new LineLayer("linelayer", "line-source").withProperties(
                    PropertyFactory.lineDasharray(new Float[]{0.01f, 2f}),
                    PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                    PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                    PropertyFactory.lineWidth(5f),
                    PropertyFactory.lineColor(Color.parseColor("#e55e5e"))
            ));
        }

        //add Extrusion layer, need polygons though
/*        if(mStyle.getLayer("course") == null) {
            // Add FillExtrusion layer to map using GeoJSON data
            mStyle.addLayer(new FillExtrusionLayer("course", "extrusion").withProperties(
                    fillExtrusionColor(Color.YELLOW),
                    fillExtrusionOpacity(0.7f),
                    fillExtrusionHeight((float) new Random().nextInt(100))));
        }*/

    }


    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        RunFragment.this.mapboxMap = mapboxMap;

       // mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/op27no2/ck2tujbra2ox21cqzxh4ql48y"),new Style.OnStyleLoaded() {
      //  mapboxMap.setStyle(Style.OUTDOORS, new Style.OnStyleLoaded() {
          mapboxMap.setStyle(new Style.Builder().fromUri("asset://mystyle.json"),new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        enableLocationComponent(style);
                        mStyle = style;
                        // Create the LineString from the list of coordinates and then make a GeoJSON
                        // FeatureCollection so we can add the line to our map as a layer.


                        //listing layers
                        List<Layer> mlayers = style.getLayers();
                        for(int i=0; i<mlayers.size();i++){
                            System.out.println("layer: "+mlayers.get(i).getId()+", minzoom: "+mlayers.get(i).getMinZoom()+", maxzoom: "+mlayers.get(i).getMaxZoom());
                            //mlayers.get(i).setProperties(PropertyFactory.fillColor(Color.parseColor("#000000")));
                        }
                    }
                });

        mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
              @Override
              public boolean onMapClick(@NonNull LatLng point) {
                  routeCoordinates.add(Point.fromLngLat(point.getLongitude(), point.getLatitude()));
                  System.out.println("map clicked: "+ point.getLongitude()+" "+point.getLatitude()+" "+point.getAltitude());

                  return false;
              }
          });
    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(getActivity())) {

        // Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

        // Activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(getActivity(), loadedMapStyle).build());

        // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

        // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING, 200, 16.0, null, 40.0, null);

        // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);



        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(getActivity(), "Location Permission not Granted, Location Cannot Load", Toast.LENGTH_LONG).show();
           // getActivity().finish();
        }
    }

    @Override
    @SuppressWarnings( {"MissingPermission"})
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void onDestroyView() {
        handler.removeCallbacks(runnable);
        mapView.onDestroy();

        super.onDestroyView();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(getActivity(), "location permission is needed for gps feautres", Toast.LENGTH_LONG).show();
    }




    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {

            TimerService.GPSBinder binder = (TimerService.GPSBinder) service;
            timerService= binder.getService();
            edt.putBoolean("bound",true);
            edt.commit();
            timerService.registerCallBack(RunFragment.this); // register

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            edt.putBoolean("bound",false);
            edt.commit();        }
    };


    private void unbindService(){
        if (prefs.getBoolean("bound",false)) {
            timerService.registerCallBack(null); // unregister
            getActivity().unbindService(serviceConnection);
            getActivity().stopService(new Intent(getActivity(), TimerService.class));
            edt.putBoolean("bound",false);
            edt.commit();
        }
    }
    private void startTimerService(){
        notifyMeIntent = new Intent(getActivity(), TimerService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getActivity().startForegroundService(notifyMeIntent);
        }else{
            getActivity().startService(notifyMeIntent);
        }
        getActivity().bindService(notifyMeIntent, serviceConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    public void getData(long elapsedTime) {
        System.out.println("elapsedTime:"+ elapsedTime);


    }
}

