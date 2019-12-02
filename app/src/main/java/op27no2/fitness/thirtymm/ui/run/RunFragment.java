package op27no2.fitness.thirtymm.ui.run;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.api.matching.v5.MapboxMapMatching;
import com.mapbox.api.matching.v5.models.MapMatchingMatching;
import com.mapbox.api.matching.v5.models.MapMatchingResponse;
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
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
import com.mapbox.mapboxsdk.utils.ColorUtils;
import com.rilixtech.materialfancybutton.MaterialFancyButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import op27no2.fitness.thirtymm.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.mapbox.core.constants.Constants.PRECISION_6;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionHeight;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;
import static com.mapbox.api.directions.v5.DirectionsCriteria.PROFILE_DRIVING;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;


public class RunFragment extends Fragment implements OnMapReadyCallback, PermissionsListener, TimerInterface {
    private String mapboxToken = "pk.eyJ1Ijoib3AyN25vMiIsImEiOiJjazJ0cmxvdGswejJ5M2NsN3g0ZzNucTMzIn0.SvSJkId0jesW0T3aM92q0Q";
    private MapView mapView;
    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private Vibrator rabbit;
    private ArrayList<Point> routeCoordinates = new ArrayList<Point>();
    private Style mStyle;
    private TextView timerText;
    private TextView distanceText;
    private long startTime;
    private TimerService timerService;
    private Intent notifyMeIntent;
    private SharedPreferences prefs;
    private SharedPreferences.Editor edt;
    private int locationRequestCode = 1000;
    private boolean DrawModeActive = false;
    private int cameraY = 90;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Mapbox.getInstance(getActivity(), mapboxToken);
        View view = inflater.inflate(R.layout.fragment_run, container, false);
        rabbit = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        timerText = view.findViewById(R.id.counttime);
        distanceText = view.findViewById(R.id.distance);
        prefs = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        edt = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();


        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        MaterialFancyButton mStart = view.findViewById(R.id.start_run);
        mStart.setOnClickListener(view1 -> {
            System.out.println("start click");

            startTimerService();
            //   startRun();

        });


        MaterialFancyButton mStop = view.findViewById(R.id.stop_run);
        mStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("stop click");
                enableMapDrawing();
//            stopRun();
            }
        });


        ImageView mLocate = view.findViewById(R.id.locate);
        mLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rabbit.vibrate(50);
                if (mapboxMap.getLocationComponent().getLastKnownLocation() != null) { // Check to ensure coordinates aren't null, probably a better way of doing this...
                    mapboxMap.animateCamera(com.mapbox.mapboxsdk.camera.CameraUpdateFactory.newLatLngZoom(new LatLng(mapboxMap.getLocationComponent().getLastKnownLocation().getLatitude(), mapboxMap.getLocationComponent().getLastKnownLocation().getLongitude()), 16));
                }

                try {
                    updateLine();
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("JSON error: " + e.getMessage());
                }


            }
        });

        ImageView edit = view.findViewById(R.id.edit_map);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rabbit.vibrate(50);
                disableMapDrawing();
                // lol this feature sucks
                /*if(routeCoordinates !=null) {
                    requestMapMatched(routeCoordinates);
                }*/
            }
        });

        ImageView angle = view.findViewById(R.id.view_angle);
        angle.setOnTouchListener(handleTouch);

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

    public void updateTime() {

        long elapsedTime = System.currentTimeMillis() - startTime;
        long micro = elapsedTime / 100000;
        long elapsedSeconds = elapsedTime / 1000;
        long secondsDisplay = elapsedSeconds % 60;
        long elapsedMinutes = elapsedSeconds / 60;
        long minutesDisplay = elapsedMinutes % 60;
        if (secondsDisplay < 10) {
            timerText.setText(minutesDisplay + ":0" + secondsDisplay);
        } else {
            timerText.setText(minutesDisplay + ":" + secondsDisplay);
        }

        LocationComponent locationComponent = mapboxMap.getLocationComponent();
        Location mloc = locationComponent.getLastKnownLocation();
        routeCoordinates.add(Point.fromLngLat(mloc.getLongitude(), mloc.getLatitude(), mloc.getAltitude()));
        System.out.println(routeCoordinates.get(routeCoordinates.size() - 1));
        try {
            updateLine();
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("JSON error: " + e.getMessage());
        }
    }

    public void startRun() {
        System.out.println("start click2");


        startTime = System.currentTimeMillis();
        handler.postDelayed(runnable, 1000);
    }

    public void stopRun() {
        unbindService();

        //  handler.removeCallbacks(runnable);
    }

    private void updateLine() throws JSONException {
        //     System.out.println("update line called "+routeCoordinates);
        if (mStyle.getSource("line-source") == null) {
            mStyle.addSource(new GeoJsonSource("line-source",
                    FeatureCollection.fromFeatures(new Feature[]{Feature.fromGeometry(
                            LineString.fromLngLats(routeCoordinates)
                    )})));
        } else {
            GeoJsonSource line = mStyle.getSourceAs("line-source");
            if (line != null) {
                line.setGeoJson(FeatureCollection.fromFeature(
                        Feature.fromGeometry(LineString.fromLngLats(routeCoordinates))
                ));
            }
        }

        float total = 0f;
        for (int i = 0; i < routeCoordinates.size() - 1; i++) {
            float[] distance = new float[2];
            Location.distanceBetween(routeCoordinates.get(i).latitude(), routeCoordinates.get(i).longitude(), routeCoordinates.get(i + 1).latitude(), routeCoordinates.get(i + 1).longitude(), distance);
            total = total + distance[0];
        }
        distanceText.setText(getMiles(total));


        /*ArrayList<Feature> mFeatures = new ArrayList<Feature>();
        for(int j=0; j<routeCoordinates.size(); j++){
            Polygon.fromLngLats(getBounds(routeCoordinates.get(j)));
            mFeatures.add(new Feature.fromGeometry());
        }*/

        List<Feature> mFeatures = new ArrayList<Feature>();
   /*     if(routeCoordinates.size() != 0 && routeCoordinates != null) {
            for (int i = 0; i < routeCoordinates.size(); i++) {
                ArrayList<Point> mPoint = new ArrayList<Point>();
                mPoint.add(routeCoordinates.get(i));
                JsonObject innerObject = new JsonObject();
                innerObject.addProperty("height", 100);
                JsonObject jsonObject = new JsonObject();
                jsonObject.add("properties", innerObject);
                Feature mf = Feature.fromGeometry(Polygon.fromLngLats(getBounds(mPoint)), jsonObject);
                mFeatures.add(mf);
            }
        }*/

        if (routeCoordinates.size() != 0 && routeCoordinates != null) {
            JsonObject totalObject = new JsonObject();
            totalObject.addProperty("type", "FeatureCollection");
            JsonArray ja = new JsonArray();

            for (int i = 0; i < routeCoordinates.size(); i++) {
                ArrayList<Double[]> marray = getBox(routeCoordinates.get(i));
                ArrayList<ArrayList<Double[]>> what = new ArrayList<ArrayList<Double[]>>();
                what.add(marray);
                JsonElement result = new GsonBuilder().create().toJsonTree(what);


                JsonObject coordinateObject = new JsonObject();
                coordinateObject.addProperty("type", "Polygon");
                coordinateObject.add("coordinates", result);

                JsonObject heightObject = new JsonObject();
                heightObject.addProperty("e", Math.abs(200*Math.sin(.05*i)));

                JsonObject outerObject = new JsonObject();
                outerObject.addProperty("type", "Feature");
                outerObject.add("geometry", coordinateObject);
                outerObject.add("properties", heightObject);
                ja.add(outerObject);
            }

            totalObject.add("features", ja);


            //    add polygon extrusion layer.
            if (mStyle.getSource("extrusion") == null) {
            /*mStyle.addSource(new GeoJsonSource("extrusion",
                    FeatureCollection.fromFeatures(new Feature[]{Feature.fromGeometry(
                            Polygon.fromLngLats(getBounds(routeCoordinates))
                    )})));*/

            ;

                System.out.println("JSON: " + totalObject.toString());
                mStyle.addSource(new GeoJsonSource("extrusion", FeatureCollection.fromJson(totalObject.toString())));

                // mStyle.addSource(new GeoJsonSource("extrusion",FeatureCollection.fromFeatures(mFeatures)));

            } else {
                GeoJsonSource ex = mStyle.getSourceAs("extrusion");
                if (ex != null) {
                    System.out.println("JSON: " + totalObject.toString());
                    ex.setGeoJson(FeatureCollection.fromJson(totalObject.toString()));
                }
            }

            //add Extrusion layer, need polygons though
            if (mStyle.getLayer("course") == null) {
                // Add FillExtrusion layer to map using GeoJSON data
                System.out.println("add extrustions");
                mStyle.addLayer(new FillExtrusionLayer("course", "extrusion").withProperties(    
                        fillExtrusionColor(Color.RED),
                        fillExtrusionOpacity(0.5f),
                        fillExtrusionHeight(get("e"))));

                
            }
        }

        // The layer properties for our line. This is where we make the line dotted, set the
        // color, etc.
        if (mStyle.getLayer("linelayer") == null) {
            mStyle.addLayer(new LineLayer("linelayer", "line-source").withProperties(
                    lineWidth(5f),
                    lineColor(Color.parseColor("#e55e5e"))
            ));
        }


    }


    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        RunFragment.this.mapboxMap = mapboxMap;

        // mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/op27no2/ck2tujbra2ox21cqzxh4ql48y"),new Style.OnStyleLoaded() {
        //  mapboxMap.setStyle(Style.OUTDOORS, new Style.OnStyleLoaded() {
        mapboxMap.setStyle(new Style.Builder().fromUri("asset://mystyle.json"), new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                enableLocationComponent(style);
                mStyle = style;
                // Create the LineString from the list of coordinates and then make a GeoJSON
                // FeatureCollection so we can add the line to our map as a layer.


                //listing layers
                List<Layer> mlayers = style.getLayers();
                for (int i = 0; i < mlayers.size(); i++) {
                    System.out.println("layer: " + mlayers.get(i).getId() + ", minzoom: " + mlayers.get(i).getMinZoom() + ", maxzoom: " + mlayers.get(i).getMaxZoom());
                    //mlayers.get(i).setProperties(PropertyFactory.fillColor(Color.parseColor("#000000")));
                    String hey = mlayers.get(i).getId();
                    if (hey.equals("road-path-smooth")) {
                        System.out.println("what");
                        mlayers.get(i).setProperties(PropertyFactory.lineColor(Color.parseColor("#000000")));
                    }
                }
            }
        });

        mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
            @Override
            public boolean onMapClick(@NonNull LatLng point) {
                if (DrawModeActive) {
                    routeCoordinates.add(Point.fromLngLat(point.getLongitude(), point.getLatitude()));
                    System.out.println("map clicked: " + point.getLongitude() + " " + point.getLatitude() + " " + point.getAltitude());

                    try {
                        updateLine();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                return false;
            }
        });

        mapboxMap.addOnMoveListener(new MapboxMap.OnMoveListener() {
            @Override
            public void onMoveBegin(@NonNull MoveGestureDetector detector) {

            }

            @Override
            public void onMove(@NonNull MoveGestureDetector detector) {

            }

            @Override
            public void onMoveEnd(@NonNull MoveGestureDetector detector) {

            }
        });

    }

    @SuppressWarnings({"MissingPermission"})
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
    @SuppressWarnings({"MissingPermission"})
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
            timerService = binder.getService();
            edt.putBoolean("bound", true);
            edt.commit();
            timerService.registerCallBack(RunFragment.this); // register

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            edt.putBoolean("bound", false);
            edt.commit();
        }
    };


    private void unbindService() {
        if (prefs.getBoolean("bound", false)) {
            timerService.registerCallBack(null); // unregister
            getActivity().unbindService(serviceConnection);
            getActivity().stopService(new Intent(getActivity(), TimerService.class));
            edt.putBoolean("bound", false);
            edt.commit();
        }
    }

    private void startTimerService() {
        notifyMeIntent = new Intent(getActivity(), TimerService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getActivity().startForegroundService(notifyMeIntent);
        } else {
            getActivity().startService(notifyMeIntent);
        }
        getActivity().bindService(notifyMeIntent, serviceConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    public void getData(long elapsedTime, ArrayList<Point> rCoordinates) {
        System.out.println("elapsedTime:" + elapsedTime);
        long micro = elapsedTime / 100000;
        long elapsedSeconds = elapsedTime / 1000;
        long secondsDisplay = elapsedSeconds % 60;
        long elapsedMinutes = elapsedSeconds / 60;
        long minutesDisplay = elapsedMinutes % 60;
        if (secondsDisplay < 10) {
            timerText.setText(minutesDisplay + ":0" + secondsDisplay);
        } else {
            timerText.setText(minutesDisplay + ":" + secondsDisplay);
        }

        /*float total=0f;
        for(int i=0; i<rCoordinates.size()-1;i++) {
            float[] distance = new float[2];
            Location.distanceBetween(rCoordinates.get(i).latitude(), rCoordinates.get(i).longitude(), rCoordinates.get(i+1).latitude(), rCoordinates.get(i+1).longitude(), distance);
            total = total+distance[0];
        }*/

        //  distanceText.setText(Float.toString(total));
        routeCoordinates = rCoordinates;
        System.out.println(routeCoordinates.size());
        try {
            updateLine();
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("JSON error: " + e.getMessage());
        }


    }


    private String getMiles(float i) {
        return String.format("%.2f", i * 0.000621371192f);
    }


    private View.OnTouchListener customOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            LatLng latLngTouchCoordinate = mapboxMap.getProjection().fromScreenLocation(
                    new PointF(motionEvent.getX(), motionEvent.getY()));

            Point screenTouchPoint = Point.fromLngLat(latLngTouchCoordinate.getLongitude(),
                    latLngTouchCoordinate.getLatitude());

            // Draw the line on the map as the finger is dragged along the map
            routeCoordinates.add(screenTouchPoint);
            try {
                updateLine();
            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println("JSON error: " + e.getMessage());
            }
            //enableMapMovement();

            return true;
        }
    };

    private void enableMapMovement() {
        mapView.setOnTouchListener(null);
    }

    /**
     * Enable drawing on the map by setting the custom touch listener on the {@link MapView}
     */
    private void enableMapDrawing() {
        mapView.setOnTouchListener(customOnTouchListener);
        DrawModeActive = true;
    }


    private void disableMapDrawing() {
        mapView.setOnTouchListener(null);
        DrawModeActive = false;
    }


    private void requestMapMatched(ArrayList<Point> points) {
        System.out.println("match map");

        //  Double[] radiuses = new Double[points.size()];
    /*    for(int i=0; i<points.size()-1;i++){
            Double m = 100.0;
            radiuses[i] = m;
        }*/

        try {
            // Setup the request using a client.
            MapboxMapMatching client = MapboxMapMatching.builder()
                    .accessToken(Objects.requireNonNull(Mapbox.getAccessToken()))
                    .profile(PROFILE_DRIVING)
                    .coordinates(points)
                    .build();

            // Execute the API call and handle the response.
            client.enqueueCall(new Callback<MapMatchingResponse>() {
                @Override
                public void onResponse(@NonNull Call<MapMatchingResponse> call,
                                       @NonNull Response<MapMatchingResponse> response) {
                    if (response.code() == 200) {
                        System.out.println("match map success? " + response.body().matchings());

                        drawMapMatched(Objects.requireNonNull(response.body()).matchings());
                    } else {
                        // If the response code does not response "OK" an error has occurred.
                        Timber.e("MapboxMapMatching failed with %s", response.code());
                        System.out.println("MapboxMapMatching failed with " + response.code() + " " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<MapMatchingResponse> call, Throwable throwable) {
                    Timber.e(throwable, "MapboxMapMatching error");
                    System.out.println("MapboxMapMatching error");

                }
            });
        } catch (ServicesException servicesException) {
            Timber.e(servicesException, "MapboxMapMatching error");
            System.out.println("MapboxMapMatching error " + servicesException.getMessage());

        }
    }

    private void drawMapMatched(@NonNull List<MapMatchingMatching> matchings) {
        Style style = mapboxMap.getStyle();
        if (style != null && !matchings.isEmpty()) {
            style.addSource(new GeoJsonSource("matched-source-id", Feature.fromGeometry(LineString.fromPolyline(
                    Objects.requireNonNull(matchings.get(0).geometry()), PRECISION_6)))
            );
            style.addLayer(new LineLayer("matched-layer-id", "matched-source-id")
                    .withProperties(
                            lineColor(ColorUtils.colorToRgbaString(Color.parseColor("#3bb2d0"))),
                            lineWidth(6f))
            );
        }
    }


    private List<List<Point>> getBounds(ArrayList<Point> mPoints) {

        List<List<Point>> mBoxes = new ArrayList<List<Point>>();

        for (int i = 0; i < mPoints.size(); i++) {
            List<Point> mBox = new ArrayList<Point>();
            Point mPoint = Point.fromLngLat(mPoints.get(i).longitude() + .00001, mPoints.get(i).latitude() + .00001);
            mBox.add(mPoint);
            Point mPoint1 = Point.fromLngLat(mPoints.get(i).longitude() + .00001, mPoints.get(i).latitude() - .00001);
            mBox.add(mPoint1);
            Point mPoint2 = Point.fromLngLat(mPoints.get(i).longitude() - .00001, mPoints.get(i).latitude() + .00001);
            mBox.add(mPoint2);
            Point mPoint3 = Point.fromLngLat(mPoints.get(i).longitude() - .00001, mPoints.get(i).latitude() - .00001);
            mBox.add(mPoint3);
            mBoxes.add(mBox);
        }


        return mBoxes;
    }

    private ArrayList<Double[]> getBox(Point mPoint) {
        ArrayList<Double[]> mArray = new ArrayList<Double[]>();
        Double[] mBox = new Double[2];
        mBox[0] = mPoint.longitude() + .00001;
        mBox[1] = mPoint.latitude() + .00001;
        mArray.add(mBox);
        Double[] mBox2 = new Double[2];
        mBox2[0] = mPoint.longitude() + .00001;
        mBox2[1] = mPoint.latitude() - .00001;
        mArray.add(mBox2);
        Double[] mBox3 = new Double[2];
        mBox3[0] = mPoint.longitude() - .00001;
        mBox3[1] = mPoint.latitude() - .00001;
        mArray.add(mBox3);
        Double[] mBox4 = new Double[2];
        mBox4[0] = mPoint.longitude() - .00001;
        mBox4[1] = mPoint.latitude() + .00001;
        mArray.add(mBox4);
        Double[] mBox5 = new Double[2];
        mBox5[0] = mPoint.longitude() + .00001;
        mBox5[1] = mPoint.latitude() + .00001;
        mArray.add(mBox5);

        return mArray;
    }

    private View.OnTouchListener handleTouch = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            int x = (int) motionEvent.getX();
            int y = (int) motionEvent.getY();
            System.out.println("touch y: "+y);
            double tilt = mapboxMap.getCameraPosition().tilt;
            if(y>cameraY){
                tilt = tilt+ 5;
            }else{
                tilt = tilt - 5;
            }
            cameraY = y;

             System.out.println("new tilt"+tilt);
             CameraPosition position = new CameraPosition.Builder()
             	.tilt(tilt)
             	.build();

             mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 100);

            return true;

        }

    };



}

