package op27no2.fitness.thirtymm.ui.run;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
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
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdate;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.snapshotter.MapSnapshotter;
import com.mapbox.mapboxsdk.style.layers.FillExtrusionLayer;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.ColorUtils;
import com.rilixtech.materialfancybutton.MaterialFancyButton;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import op27no2.fitness.thirtymm.Database.AppDatabase;
import op27no2.fitness.thirtymm.Database.Repository;
import op27no2.fitness.thirtymm.FancyScrollView;
import op27no2.fitness.thirtymm.MyAppWidgetProvider;
import op27no2.fitness.thirtymm.R;
import op27no2.fitness.thirtymm.ui.nutrition.NutritionDay;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.mapbox.api.directions.v5.DirectionsCriteria.PROFILE_DRIVING;
import static com.mapbox.core.constants.Constants.PRECISION_6;
import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;
import static com.mapbox.mapboxsdk.style.expressions.Expression.exponential;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.expressions.Expression.interpolate;
import static com.mapbox.mapboxsdk.style.expressions.Expression.stop;
import static com.mapbox.mapboxsdk.style.expressions.Expression.zoom;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionHeight;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;


public class RunDetailFragment extends Fragment implements OnMapReadyCallback, PermissionsListener, TimerInterface {
    private String mapboxToken = "pk.eyJ1Ijoib3AyN25vMiIsImEiOiJjazJ0cmxvdGswejJ5M2NsN3g0ZzNucTMzIn0.SvSJkId0jesW0T3aM92q0Q";
    private MapView mapView;
    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private Vibrator rabbit;
    private ArrayList<Point> routeCoordinates = new ArrayList<Point>();
    private double maxalt;
    private double minalt;
    private NutritionDay mNutritionDay;

    private Style mStyle;
    private TextView timerText;
    private TextView distanceText;
    private ImageView edit;
    private long startTime;
    private TimerService timerService;
    private Intent notifyMeIntent;
    private SharedPreferences prefs;
    private SharedPreferences.Editor edt;
    private int locationRequestCode = 1000;
    private boolean DrawModeActive = false;
    private boolean trackBearing = true;
    private int cameraY = 90;
    private MaterialFancyButton startButton;
    private MaterialFancyButton lockWindowButton;
    private Boolean isPaused = false;
    private long secondHold = 0;
    private List<Feature> globalFeatureList = new ArrayList<Feature>();
    private Repository mRepository;
    private Calendar cal;
    private String formattedDate;
    private long finalTIme;
    private MapSnapshotter mapSnapshotter;

    private EditText mEditTitle;
    private EditText mEditDescription;
    private EditText mEditDuration;
    private EditText mEditDistance;
    private EditText mEditPace;
    private EditText mEditCals;

    private Integer saveTime;
    private float saveDistance;
    private int saveCals;
    private String saveDescription;
    private String saveTitle;
    private String saveDate;

    private double holdZoom;
    private ImageView zoomBar;
    private ImageView zoom1;
    private FancyScrollView scrollView;
    private CardView mCardView;
    private FrameLayout mFrame;
    private RelativeLayout mRelative;

    private TextView mText4;

    private int runWorkoutID;
    private RunWorkout mRunWorkout;
    private int initialCals;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        Mapbox.getInstance(getActivity(), mapboxToken);
        View view = inflater.inflate(R.layout.fragment_rundetail, container, false);
        rabbit = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        timerText = view.findViewById(R.id.counttime);
        distanceText = view.findViewById(R.id.distance);
        prefs = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        edt = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
        globalFeatureList = getFeatureList();
        edt.putInt("fragtag",this.getId());
        edt.commit();

        //get runworkout ID from Bundle
        runWorkoutID = getArguments().getInt("uid");
        System.out.println("id passed: "+runWorkoutID);


        mRepository = new Repository(getActivity());
        cal = Calendar.getInstance();
        Date c = cal.getTime();
        Long time = Calendar.getInstance().getTimeInMillis();
        System.out.println("Current time => " + time);
        SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d, ''yy");
        formattedDate = df.format(c);

        // saveDate is the edittext field in the save dialog, start it initially as formattedDate. User can change. Formatted date used as todays date elsewhere.
        saveDate = formattedDate;


        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        zoomBar = view.findViewById(R.id.zoom_bar);


        // TextView mText = holder.mView.findViewById(R.id.text_lift);
        mText4 = view.findViewById(R.id.date);
        mEditDuration = view.findViewById(R.id.duration_value);
        mEditDistance = view.findViewById(R.id.distance_value);
        mEditPace = view.findViewById(R.id.pace_value);
        mEditCals = view.findViewById(R.id.cals_value);
        mEditTitle = view.findViewById(R.id.workout_title);
        mEditDescription = view.findViewById(R.id.workout_description);



        lockWindowButton = view.findViewById(R.id.lock_window);
        lockWindowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lockWindowButton.getText().toString().equals("Unlock")){
                    //LOCK THE WINDOW
                    scrollView.setScrolling(false);
                    lockWindowButton.setText("Lock");
                }else{
                    //UNLOCK THE WINDOW
                    scrollView.setScrolling(true);
                    lockWindowButton.setText("Unlock");
                }
            }
        });

        ImageView mExtrude = view.findViewById(R.id.extrude_button);
        mExtrude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rabbit.vibrate(30);
                extrudeRoute();
            }
        });
        ImageView eraseButton = view.findViewById(R.id.erase_button);
        eraseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                routeCoordinates.clear();
                clearExtrusion();
                try {
                    updateLine();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        ImageView mLocate = view.findViewById(R.id.locate);
        mLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rabbit.vibrate(50);
                trackBearing = true;
                if (mapboxMap.getLocationComponent().getLastKnownLocation() != null) { // Check to ensure coordinates aren't null, probably a better way of doing this...
                    mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mapboxMap.getLocationComponent().getLastKnownLocation().getLatitude(), mapboxMap.getLocationComponent().getLastKnownLocation().getLongitude()), 16));
                }

                try {
                    updateLine();
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("JSON error: " + e.getMessage());
                }


                double height = zoom1.getMeasuredHeight();
                System.out.println("height: "+height);

                float bar = (float) zoom1.getY();
                System.out.println("barr: "+bar);

                float add = (float) (height*(1f - ((16f-13f)/7f)));
                System.out.println("add: "+add);

                zoomBar.setY(bar+add);
                //zoomBar.setY((float) (bar+(height*(16/22))));

            }
        });

        edit = view.findViewById(R.id.edit_map);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rabbit.vibrate(50);
                if(!DrawModeActive) {
                    enableMapDrawing();
                }else {
                    disableMapDrawing();
                }
                // lol this route snap feature sucks?
                /*if(routeCoordinates !=null) {
                    requestMapMatched(routeCoordinates);
                }*/
            }
        });

        ImageView saveButton = (ImageView) view.findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("run detail save");

                rabbit.vibrate(30);
                startSnapShot();
            }
        });

        ImageView angle = view.findViewById(R.id.view_angle);
        angle.setOnTouchListener(handleTouch);

        ImageView rotation = view.findViewById(R.id.view_rotation);
        rotation.setOnTouchListener(handle360left);

        ImageView rotation2 = view.findViewById(R.id.view_rotation2);
        rotation2.setOnTouchListener(handle360);

        zoom1 = view.findViewById(R.id.view_zoom);
        zoom1.setOnTouchListener(handleZoom);

        scrollView = view.findViewById(R.id.scroll_view);
        mCardView = view.findViewById(R.id.card_view);


        getNutritionDayData();
        getRunWorkoutData();




        return view;
    }


/*
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            updateTime();
            handler.postDelayed(this, 100);
        }
    };
*/

/*    public void updateTime() {

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
    }*/

    public void startRun() {
        //used for bearing updates
        isPaused = false;

        secondHold = 0;
        System.out.println("start click2");
        startButton.setText("Pause");
        startTime = System.currentTimeMillis();

        //start timer service
        notifyMeIntent = new Intent(getActivity(), TimerService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            System.out.println("start foreground service (act)");
            getActivity().startForegroundService(notifyMeIntent);
        } else {
            System.out.println("start service (act)");
            getActivity().startService(notifyMeIntent);
        }
        getActivity().bindService(notifyMeIntent, serviceConnection, Context.BIND_AUTO_CREATE);

    }

    public void pauseRun() {
        isPaused = true;
        startButton.setText("Resume");

    }

    public void continueRun() {
        isPaused = false;


    }


    public void stopRun() {
        if(timerService != null) {
            if (!isPaused) {
                isPaused = true;
                timerService.pauseService();
                startButton.setText("Resume");
            }

        }
        saveDialog();


    }

    private void updateLine() throws JSONException {
        //     System.out.println("update line called "+routeCoordinates);
        if(mStyle != null && mStyle.isFullyLoaded()) {
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

            // The layer properties for our line. This is where we make the line dotted, set the
            // color, etc.
            if (mStyle.getLayer("linelayer") == null) {
                mStyle.addLayer(new LineLayer("linelayer", "line-source").withProperties(
                        lineWidth(5f),
                        lineColor(Color.parseColor("#e55e5e"))
                ));
            }



        float total = 0f;
        for (int i = 0; i < routeCoordinates.size() - 1; i++) {
            float[] distance = new float[2];
            Location.distanceBetween(routeCoordinates.get(i).latitude(), routeCoordinates.get(i).longitude(), routeCoordinates.get(i + 1).latitude(), routeCoordinates.get(i + 1).longitude(), distance);

            total = total + distance[0];
        }
        distanceText.setText(getMiles(total));



        }
    }


    private void updateBearing(){
        if(trackBearing) {
            int j = routeCoordinates.size() - 1;
            float[] distance2 = new float[2];
            if (j > 1) {
                System.out.println("update bearing called");
                Location.distanceBetween(routeCoordinates.get(j - 1).latitude(), routeCoordinates.get(j - 1).longitude(), routeCoordinates.get(j).latitude(), routeCoordinates.get(j).longitude(), distance2);
                if (!DrawModeActive && distance2[0] > 1) {
                    double bearing = getDirection(routeCoordinates.get(j - 1).latitude(), routeCoordinates.get(j - 1).longitude(), routeCoordinates.get(j).latitude(), routeCoordinates.get(j).longitude());
                    CameraPosition position = new CameraPosition.Builder()
                            .bearing(bearing)
                            .build();
                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 1000);
                }
            }
        }
    }


    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        RunDetailFragment.this.mapboxMap = mapboxMap;


        // mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/op27no2/ck2tujbra2ox21cqzxh4ql48y"),new Style.OnStyleLoaded() {
        //  mapboxMap.setStyle(Style.OUTDOORS, new Style.OnStyleLoaded() {
        mapboxMap.setStyle(new Style.Builder().fromUri("asset://mystyle.json"), new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                enableLocationComponent(style);
                mStyle = style;

                //snapshotter




                //listing layers
                List<Layer> mlayers = style.getLayers();
                for (int i = 0; i < mlayers.size(); i++) {
                 //   System.out.println("layer: " + mlayers.get(i).getId() + ", minzoom: " + mlayers.get(i).getMinZoom() + ", maxzoom: " + mlayers.get(i).getMaxZoom());
                    //mlayers.get(i).setProperties(PropertyFactory.fillColor(Color.parseColor("#000000")));
                    String hey = mlayers.get(i).getId();
                    if (hey.equals("road-path-smooth")) {
                        System.out.println("what");
                        mlayers.get(i).setProperties(PropertyFactory.lineColor(Color.parseColor("#000000")));
                    }
                }

            //adds all frozen trails
             addTrails();

            }
        });

        mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
            @Override
            public boolean onMapClick(@NonNull LatLng point) {
                trackBearing = false;
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
                trackBearing = false;

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
            System.out.println("enable location component called");

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

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    //set to location
                    if (mapboxMap.getLocationComponent().getLastKnownLocation() != null) { // Check to ensure coordinates aren't null, probably a better way of doing this...
                        System.out.println("set location");
                        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mapboxMap.getLocationComponent().getLastKnownLocation().getLatitude(), mapboxMap.getLocationComponent().getLastKnownLocation().getLongitude()), 16));
                    }                }
            }, 200);


        } else {
            System.out.println("request permission");
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }
    }

    public void enableLocationComponentAfterResult(){

        mapboxMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                System.out.println("permission granted recalling enable location component");

                        // Check if permissions are enabled and if not request
                        if (PermissionsManager.areLocationPermissionsGranted(getActivity())) {
                            System.out.println("enable location component called");

                            // Get an instance of the component
                            LocationComponent locationComponent = mapboxMap.getLocationComponent();

                            // Activate with options
                            locationComponent.activateLocationComponent(
                                    LocationComponentActivationOptions.builder(getActivity(), style).build());

                            // Enable to make component visible
                            locationComponent.setLocationComponentEnabled(true);

                            // Set the component's camera mode
                            locationComponent.setCameraMode(CameraMode.TRACKING, 200, 16.0, null, 40.0, null);

                            // Set the component's render mode
                            locationComponent.setRenderMode(RenderMode.COMPASS);

                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //set to location
                                    if (mapboxMap.getLocationComponent().getLastKnownLocation() != null) { // Check to ensure coordinates aren't null, probably a better way of doing this...
                                        System.out.println("set location");
                                        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mapboxMap.getLocationComponent().getLastKnownLocation().getLatitude(), mapboxMap.getLocationComponent().getLastKnownLocation().getLongitude()), 16));
                                    }                }
                            }, 200);


                        } else {
                            System.out.println("still need to request permission");
                        }



            }
        });

    }


    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            System.out.println("permission granted");

            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    System.out.println("permission granted recalling enable location component");

                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(getActivity(), "Location Permission not Granted, Location Cannot Load", Toast.LENGTH_LONG).show();
            System.out.println("permission NOT granted");
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
        if(prefs.getBoolean("service_running", false) == true){
            bindTimerService();
        }

        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(prefs.getBoolean("service_running", false) == true){
            unbindTimerService();
        }
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
        //handler.removeCallbacks(runnable);
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
            timerService.registerCallBack(RunDetailFragment.this); // register

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {

            timerService.unregisterCallBack();
            edt.putBoolean("bound", false);
            edt.commit();
        }
    };


    private void stopTimerService(){
        //getActivity().stopService(new Intent(getActivity(), TimerService.class));
        timerService.stopSelf();
    }

    private void unbindTimerService() {
        if (prefs.getBoolean("bound", false) && timerService !=null) {
            timerService.registerCallBack(null); // unregister
            getActivity().unbindService(serviceConnection);
            edt.putBoolean("bound", false);
            edt.commit();
        }
    }

    private void bindTimerService() {
        notifyMeIntent = new Intent(getActivity(), TimerService.class);
        getActivity().bindService(notifyMeIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }


    @Override
    public void getData(long elapsedTime, ArrayList<Point> rCoordinates, double min, double max) {
        System.out.println("elapsedTime:" + elapsedTime);
        finalTIme = elapsedTime;
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

        maxalt = max;
        minalt = min;

        /*float total=0f;
        for(int i=0; i<rCoordinates.size()-1;i++) {
            float[] distance = new float[2];
            Location.distanceBetween(rCoordinates.get(i).latitude(), rCoordinates.get(i).longitude(), rCoordinates.get(i+1).latitude(), rCoordinates.get(i+1).longitude(), distance);
            total = total+distance[0];
        }*/

        //  distanceText.setText(Float.toString(total));
        routeCoordinates = rCoordinates;

        try {
            updateLine();
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("JSON error: " + e.getMessage());
        }

        //update bearing at most 1 per second
        if(elapsedSeconds > secondHold){
            updateBearing();
            secondHold = elapsedSeconds;
        }


    }

    private View.OnTouchListener customOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            trackBearing = false;

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
        edit.setBackgroundResource(R.drawable.circle_red_plain);
        DrawModeActive = true;
    }


    private void disableMapDrawing() {
        mapView.setOnTouchListener(null);
        edit.setBackgroundResource(R.drawable.circle_white_plain);
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

    private double getDirection(double lat1, double lon1, double lat2, double lon2){
            double longitude1 = lon1;
            double longitude2 = lon2;
            double latitude1 = Math.toRadians(lat1);
            double latitude2 = Math.toRadians(lat2);
            double longDiff= Math.toRadians(longitude2-longitude1);
            double y= Math.sin(longDiff)*Math.cos(latitude2);
            double x=Math.cos(latitude1)*Math.sin(latitude2)-Math.sin(latitude1)*Math.cos(latitude2)*Math.cos(longDiff);

            return (Math.toDegrees(Math.atan2(y, x))+360)%360;
    }

    private ArrayList<Double[]> getBox(Point mPoint) {
        ArrayList<Double[]> mArray = new ArrayList<Double[]>();
        Double[] mBox = new Double[2];
        mBox[0] = mPoint.longitude() + .0001;
        mBox[1] = mPoint.latitude() + .0001;
        mArray.add(mBox);
        Double[] mBox2 = new Double[2];
        mBox2[0] = mPoint.longitude() + .0001;
        mBox2[1] = mPoint.latitude() - .0001;
        mArray.add(mBox2);
        Double[] mBox3 = new Double[2];
        mBox3[0] = mPoint.longitude() - .0001;
        mBox3[1] = mPoint.latitude() - .0001;
        mArray.add(mBox3);
        Double[] mBox4 = new Double[2];
        mBox4[0] = mPoint.longitude() - .0001;
        mBox4[1] = mPoint.latitude() + .0001;
        mArray.add(mBox4);
        Double[] mBox5 = new Double[2];
        mBox5[0] = mPoint.longitude() + .0001;
        mBox5[1] = mPoint.latitude() + .0001;
        mArray.add(mBox5);

        return mArray;
    }

    private View.OnTouchListener handleTouch = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            double height = view.getMeasuredHeight();
            System.out.println("hieght: "+height);


            int x = (int) motionEvent.getX();
            double y = (double) motionEvent.getY();
            System.out.println("touch y: "+y);
            double tilt = mapboxMap.getCameraPosition().tilt;

            /*if(y>cameraY){
                tilt = tilt+ 5;
            }else{
                tilt = tilt - 5;
            }

            cameraY = y;*/

            //tilt is zero to 60
            tilt = (y/height)*60;

            System.out.println("new tilt"+tilt);
             CameraPosition position = new CameraPosition.Builder()
             	.tilt(tilt)
             	.build();

             mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 100);

            return true;

        }

    };

    private View.OnTouchListener handle360 = new View.OnTouchListener() {
        Handler handler = new Handler();

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch(motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    int delay = 10; //milliseconds

                    handler.postDelayed(new Runnable(){
                        public void run(){
                            //do something

                            double mapbearing = mapboxMap.getCameraPosition().bearing;
                            mapbearing = mapbearing+ 5;
                            System.out.println("new bearing"+mapbearing);
                            CameraPosition position = new CameraPosition.Builder()
                                    .bearing(mapbearing)
                                    .build();

                            mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 100);


                            handler.postDelayed(this, delay);
                        }
                    }, delay);



                    break;

                case MotionEvent.ACTION_MOVE:
                    // touch move code
                    break;

                case MotionEvent.ACTION_UP:
                    // touch up code
                    handler.removeCallbacksAndMessages(null);
                    break;
            }


            return true;
        }
    };
    private View.OnTouchListener handle360left = new View.OnTouchListener() {
        Handler handler = new Handler();

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch(motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    int delay = 10; //milliseconds

                    handler.postDelayed(new Runnable(){
                        public void run(){
                            //do something

                            double mapbearing = mapboxMap.getCameraPosition().bearing;
                            mapbearing = mapbearing - 5;
                            System.out.println("new bearing"+mapbearing);
                            CameraPosition position = new CameraPosition.Builder()
                                    .bearing(mapbearing)
                                    .build();

                            mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 100);


                            handler.postDelayed(this, delay);
                        }
                    }, delay);



                    break;

                case MotionEvent.ACTION_MOVE:
                    // touch move code
                    break;

                case MotionEvent.ACTION_UP:
                    // touch up code
                    handler.removeCallbacksAndMessages(null);
                    break;
            }


            return true;
        }
    };


    private View.OnTouchListener handleZoom = new View.OnTouchListener() {


        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            double height = view.getMeasuredHeight();
            double y = (double) motionEvent.getY();
            float bar = (float) y+view.getY();
            if(bar<view.getY()){
                bar = view.getY();
            }else if(bar > view.getY()+height){
                bar = (float) (view.getY()+height);
            }

            zoomBar.setY(bar);

            double zoom = (1 - y/height)*7+13;

            System.out.println("new tilt"+zoom);
            CameraPosition position = new CameraPosition.Builder()
                    .zoom(zoom)
                    .build();


            mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 100);



            return true;
        }
    };




    private void extrudeRoute(){

        if (routeCoordinates.size() != 0 && routeCoordinates != null) {

            //add Json objects for extrusion, move to
            JsonObject totalObject = new JsonObject();
            totalObject.addProperty("type", "FeatureCollection");
            JsonArray ja = new JsonArray();

            for (int i = 0; i < routeCoordinates.size(); i++) {
                float[] distance2 = new float[2];
                if (i > 1) {
                    System.out.println("update bearing called");
                    Location.distanceBetween(routeCoordinates.get(i - 1).latitude(), routeCoordinates.get(i - 1).longitude(), routeCoordinates.get(i).latitude(), routeCoordinates.get(i).longitude(), distance2);
                    //TODO extrusion to not overdraw points
                    if ( distance2[0] > 0) {


                        ArrayList<Double[]> marray = getBox(routeCoordinates.get(i));
                        ArrayList<ArrayList<Double[]>> what = new ArrayList<ArrayList<Double[]>>();
                        what.add(marray);
                        JsonElement result = new GsonBuilder().create().toJsonTree(what);


                        JsonObject coordinateObject = new JsonObject();
                        coordinateObject.addProperty("type", "Polygon");
                        coordinateObject.add("coordinates", result);


                        //adjust altitude data to min
                        //TODO futher adjust based on max too?
                       // double height = (routeCoordinates.get(i).altitude() - minalt) * 50;
                        double height = (routeCoordinates.get(i).altitude() - minalt) * (22-mapboxMap.getCameraPosition().zoom)*(22-mapboxMap.getCameraPosition().zoom)*.1;
                        JsonObject heightObject = new JsonObject();
                        heightObject.addProperty("e", height);
                        System.out.println("coord values: " + routeCoordinates.get(i).altitude());
                        System.out.println("height values: " + height);


                        JsonObject outerObject = new JsonObject();
                        outerObject.addProperty("type", "Feature");
                        outerObject.add("geometry", coordinateObject);
                        outerObject.add("properties", heightObject);
                        ja.add(outerObject);


                    }}
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

    }

    private void clearExtrusion(){
        mStyle.removeLayer("course");
    }

    public void saveFeatureList(List<Feature> mList){
        for(int i=0; i<mList.size();i++) {
            edt.putString("feature_list" + i, mList.get(i).toJson());
        }
        edt.putInt("feature_list_size", mList.size());
        edt.commit();
    }

    public List<Feature> getFeatureList() {
        int listsize = prefs.getInt("feature_list_size",0);
        List<Feature> mlist = new ArrayList<Feature>();
        for(int i=0; i<listsize; i++){
            String json = prefs.getString("feature_list"+i, "");
            if(!json.equals("")) {
                Feature mfeat = Feature.fromJson(json);
                mlist.add(mfeat);
            }
        }

        return mlist;
    }

    public void addTrails(){

        //highlight feature testing
        System.out.println("got here1");
        RectF rectF = new RectF(
                mapView.getLeft(),
                mapView.getTop(),
                mapView.getRight(),
                mapView.getBottom()
        );
        List<Feature> featureList = mapboxMap.queryRenderedFeatures(rectF, "road-path-smooth");
        globalFeatureList.addAll(featureList);
        saveFeatureList(globalFeatureList);

        //appears to work too
        if (globalFeatureList.size() > 0) {
            System.out.println("got here2");
            if(mStyle.isFullyLoaded()) {

                if (mStyle.getSource("myline-source") == null) {
                    mStyle.addSource(new GeoJsonSource("myline-source", FeatureCollection.fromFeatures(globalFeatureList)));

                } else {
                    GeoJsonSource line = mStyle.getSourceAs("myline-source");
                    if (line != null) {
                        line.setGeoJson(FeatureCollection.fromFeatures(globalFeatureList));
                    }
                }
                // The layer properties for our line. This is where we make the line dotted, set the
                // color, etc.
                if (mStyle.getLayer("mylinelayer") == null) {
                    System.out.println("adding line layer");
                    LineLayer mlayer = new LineLayer("mylinelayer", "myline-source").withProperties(
                            lineWidth(interpolate(exponential(1f), zoom(),
                                    stop(1f,0.5 ),
                                    stop(8.5f,0.5),
                                    stop(18f, 2),
                                    stop(21f, 6))),
                            lineColor(Color.parseColor("#c919ff")));

                    mStyle.addLayer(mlayer);
                }

            }
        }

    }



    public void saveDialog(){

        final Dialog dialog = new Dialog(getActivity());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_save_run);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;

        dialog.getWindow().setLayout((8 * width) / 9, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView mText = dialog.findViewById(R.id.confirm_title);
        TextView mTextDate = dialog.findViewById(R.id.date);
        mTextDate.setText(formattedDate);

        mEditDuration = dialog.findViewById(R.id.duration_value);
        mEditDistance = dialog.findViewById(R.id.distance_value);
        mEditPace = dialog.findViewById(R.id.pace_value);
        mEditCals = dialog.findViewById(R.id.cals_value);
        mEditTitle = dialog.findViewById(R.id.workout_title);
        mEditDescription = dialog.findViewById(R.id.workout_description);
        //set initial texts, can edit then save
        final float[] total = {0f};
        for (int i = 0; i < routeCoordinates.size() - 1; i++) {
            float[] distance = new float[2];
            Location.distanceBetween(routeCoordinates.get(i).latitude(), routeCoordinates.get(i).longitude(), routeCoordinates.get(i + 1).latitude(), routeCoordinates.get(i + 1).longitude(), distance);
            total[0] = total[0] + distance[0];
        }
        long pace = (long) (finalTIme/(total[0] *0.000621371192f));

        mEditDuration.setText(getDuration(finalTIme));
        mEditDistance.setText(getMiles(total[0]));
        mEditPace.setText(getDuration(pace)+" /mi");
        mEditCals.setText(Integer.toString(((int) Math.floor(total[0] *0.000621371192f*prefs.getInt("weight",215)*0.63))));
        mEditDistance.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                total[0] = Float.parseFloat(s.toString());
                System.out.println("total = "+total[0]);
                saveCals = (((int) Math.floor(total[0]*prefs.getInt("weight",215)*0.63f)));
                System.out.println("save Cals = "+saveCals);
                mEditCals.setText(Integer.toString(saveCals));
            }
        });


        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d, ''yy");
                Date c = myCalendar.getTime();
                saveDate = df.format(c);
                mTextDate.setText(saveDate);
            }

        };
        mTextDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        dialog.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check time is formatted correctly
                String timefield = mEditDuration.getText().toString();
                int pos = timefield.indexOf(":");
                String before = timefield.substring(0,pos);
                String after = timefield.substring(pos+1,timefield.length());

                if(timefield.contains(":") && before.matches("[0-9]+") && after.matches("[0-9]+")) {


                    //stop service set screen miles back to 0
                    startButton.setText("Start");
                    if (timerService != null) {
                        unbindTimerService();
                        stopTimerService();
                    }
                    timerService = null;
                    timerText.setText("0:00");
                    distanceText.setText("0");

                    //get fields from EditTexts

                    saveDistance = (int) Math.floor(Float.parseFloat(mEditDistance.getText().toString()) * 1609.34400);
                    saveCals = Integer.parseInt(mEditCals.getText().toString());
                    saveTime = getIntFromDuration(mEditDuration.getText().toString());
                    saveDescription = mEditDescription.getText().toString();
                    saveTitle = mEditTitle.getText().toString();

                    //starts save process, gets image then callback finishes and calls saveRun(bitmap)
                    moveToBounds();
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something here
                            startSnapShot();

                        }
                    }, 200);


                    dialog.dismiss();
                }
            }
        });

        dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPaused && timerService != null) {
                    timerService.resumeService();
                }
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.discard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButton.setText("Start");
                if(timerService != null) {
                    unbindTimerService();
                    stopTimerService();
                }
                timerService = null;
                timerText.setText("0:00");
                distanceText.setText("0");
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    public void saveRun(Bitmap bMap){

        saveDate = mText4.getText().toString();
        saveTime = getIntFromDuration(mEditDuration.getText().toString());
        saveDistance = (int) Math.floor(Float.parseFloat(mEditDistance.getText().toString()) * 1609.34400);
        saveCals = Integer.parseInt(mEditCals.getText().toString());
        saveDescription = mEditDescription.getText().toString();
        saveTitle = mEditTitle.getText().toString();

        mRunWorkout.setWorkoutDate(saveDate);
        mRunWorkout.setDuration(saveTime);
        mRunWorkout.setDistance((int) saveDistance);
        mRunWorkout.setCalories(saveCals);
        mRunWorkout.setDescription(saveDescription);
        mRunWorkout.setTitle(saveTitle);


        int cals = mNutritionDay.getValues().get(0);
        ArrayList<Integer> mvals = mNutritionDay.getValues();
        cals = cals - (saveCals-initialCals);
        mvals.set(0, cals);
        mNutritionDay.setValues(mvals);
        mRepository.updateNutrition(mNutritionDay);


        String bmp = saveBitmap(bMap,UUID.randomUUID().toString());
        System.out.println("bmp string: "+bmp);
        String coords = saveCoordinates(routeCoordinates,UUID.randomUUID().toString());
        mRunWorkout.setImage(bmp);
        mRunWorkout.setCoordinates(coords);

        mRepository.updateRunWorkout(mRunWorkout);
        Toast.makeText(getActivity(), "Workout Updated", Toast.LENGTH_LONG).show();
        updateWidgets();
    }




    private void startSnapShot() {
        MapboxMap.SnapshotReadyCallback snapCallback = new MapboxMap.SnapshotReadyCallback() {
            @Override
            public void onSnapshotReady(@NonNull Bitmap snapshot) {
                saveRun(snapshot);
            }
        };
        mapboxMap.snapshot(snapCallback);
    }


    public String saveBitmap(Bitmap bitmapImage, String filename){
            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            // path to /data/data/yourapp/app_data/imageDir
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            // Create imageDir
            File mypath=new File(directory,filename+".jpg");

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(mypath);
                // Use the compress method on the BitMap object to write image to the OutputStream
                bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return mypath.getAbsolutePath();
        }

    public String saveCoordinates(ArrayList<Point> coords, String uid){
            Gson gson = new Gson();
            String json = gson.toJson(coords);
            edt.putString(uid, json);
            edt.commit();
            return uid;
    }

    public ArrayList<Point> getCoordinates(String uid){
        String json = prefs.getString(uid,"");
        System.out.println("RETRIEVE COORDS JSON: " + json);

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Point>>() {}.getType();
        ArrayList<Point> coords = gson.fromJson(json, type);
        if(coords != null) {
            System.out.println("RETRIEVE COORDS SIZE: " + coords.size());
        }else{
            System.out.println("RETRIEVE COORDS NULL: ");
        }
        return coords;
    }


    private void moveToBounds(){
        if(routeCoordinates != null && routeCoordinates.size() >2) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (int i = 0; i < routeCoordinates.size(); i++) {
                LatLng l = new LatLng(routeCoordinates.get(i).latitude(), routeCoordinates.get(i).longitude());
                builder.include(l);
            }
            LatLngBounds bounds = builder.build();
            System.out.println("N: " + bounds.getLatNorth());
            System.out.println("S: " + bounds.getLatSouth());
            System.out.println("Span: " + bounds.getLatitudeSpan());

            LatLngBounds.Builder builder2 = new LatLngBounds.Builder();
            if (bounds.getLatitudeSpan() > bounds.getLongitudeSpan() / 2) {
                //TODO error latitude must be between -90 and 90
                LatLng l = new LatLng(bounds.getLatNorth() + bounds.getLatitudeSpan() * 1.5, bounds.getLonEast());
                LatLng l2 = new LatLng(bounds.getLatSouth() - bounds.getLatitudeSpan() * 1.5, bounds.getLonEast());
                builder2.include(l);
                builder2.include(l2);
                LatLngBounds bounds2 = builder2.build();
                bounds = bounds.union(bounds2);
            }


            int padding = 100; // offset from edges of the map in pixels

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            mapboxMap.animateCamera(cu);
        }
    }


    @SuppressLint("StaticFieldLeak")
    private void getNutritionDayData() {
        new AsyncTask<Void, Void, Void>() {
            //Get today's workout => finishUI
            //get today's workout, if it doesn't exist create it

            protected void onPreExecute() {

            }

            protected Void doInBackground(Void... unused) {
                //nutritionDay and formatted day change with selected day up top, used to edit values
                mNutritionDay = AppDatabase.getAppDatabase(getActivity()).ntDAO().findByDate(formattedDate);

                if (mNutritionDay == null) {
                    mNutritionDay = new NutritionDay();
                    System.out.println("day null should create");
                    ArrayList<String> mNames = new ArrayList<String>(2);
                    ArrayList<Integer> mValues = new ArrayList<Integer>(2);
                    mNames.add("Cals");
                    mValues.add(prefs.getInt("BaseCals", -2000));
                    mNames.add("Protein");
                    mValues.add(0);
                    mNutritionDay.setNames(mNames);
                    mNutritionDay.setValues(mValues);
                    mNutritionDay.setDate(formattedDate);
                    mRepository.insertNutrition(mNutritionDay);
                } else {
                    System.out.println("found Day get names/values");

                }

                //populate a list with all days up to current that shouldn't change like mNutritionDay and formattedDate, this async is called onResume and on date change
                //TODO will this get updated appropriately with databse operations saving edits?
                System.out.println("check exec1");


                return null;
            }

            protected void onPostExecute(Void unused) {
                // Post Code
            }
        }.execute();
    }
    @SuppressLint("StaticFieldLeak")
    private void getRunWorkoutData() {
        new AsyncTask<Void, Void, Void>() {
            //Get today's workout => finishUI
            //get today's workout, if it doesn't exist create it

            protected void onPreExecute() {

            }

            protected Void doInBackground(Void... unused) {
                //nutritionDay and formatted day change with selected day up top, used to edit values
                mRunWorkout = AppDatabase.getAppDatabase(getActivity()).rwDAO().findById(runWorkoutID);


                System.out.println("check run exec1");


                return null;
            }

            protected void onPostExecute(Void unused) {
                // Post Code
                finishUI(mRunWorkout);

            }
        }.execute();
    }




    private String getMiles(float i) {
        return String.format("%.2f", i * 0.000621371192f);
    }



    private Integer getIntFromDuration(String text){
        Integer time = null;
        int pos = text.indexOf(":");
        String before = text.substring(0,pos);
        String after = text.substring(pos+1,text.length());
        int msecond = Integer.parseInt(before)*60*1000;
        msecond = msecond+Integer.parseInt(after)*1000;

        return msecond;
    }

    private String getDuration(long elapsedTime){
        long micro = elapsedTime / 100000;
        long elapsedSeconds = elapsedTime / 1000;
        long secondsDisplay = elapsedSeconds % 60;
        long elapsedMinutes = elapsedSeconds / 60;
        long minutesDisplay = elapsedMinutes % 60;
        if (secondsDisplay < 10) {
            return((minutesDisplay + ":0" + secondsDisplay));
        } else {
            return((minutesDisplay + ":" + secondsDisplay));
        }
    }



    private void finishUI(RunWorkout mRunWorkout){
        if(mRunWorkout != null) {
            Integer duration = mRunWorkout.getDuration();
            Integer distance = mRunWorkout.getDistance();

            long pace = (long) (duration / (distance * 0.000621371192f));

            mEditTitle.setText(mRunWorkout.getTitle());
            mEditDistance.setText(getMiles(distance));
            mEditDuration.setText(getDuration(duration));
            mEditPace.setText(getDuration(pace) + " /mi");
            mText4.setText(mRunWorkout.getWorkoutDate());
            mEditCals.setText(Integer.toString(mRunWorkout.getCalories()));
            initialCals = mRunWorkout.getCalories();
            mEditDescription.setText(mRunWorkout.getDescription());

            routeCoordinates = getCoordinates(mRunWorkout.getCoordinates());
            System.out.println("cood uid"+mRunWorkout.getCoordinates());
            System.out.println("retrieve coords"+routeCoordinates);

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        updateLine();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        System.out.println("line update error");
                    }
                }
            }, 300);



        }else{
            System.out.println("run workout null");
        }

    }

    private void updateWidgets(){
        Intent intent = new Intent(getActivity(), MyAppWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        // Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
        // since it seems the onUpdate() is only fired on that:
        AppWidgetManager manager= AppWidgetManager.getInstance(getActivity().getApplication());
        int[] ids = manager.getAppWidgetIds(new ComponentName(getActivity().getApplication(), MyAppWidgetProvider.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        getActivity().sendBroadcast(intent);
    }



}

