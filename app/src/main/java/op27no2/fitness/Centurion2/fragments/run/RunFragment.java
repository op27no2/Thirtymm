package op27no2.fitness.Centurion2.fragments.run;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
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
import android.net.Uri;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
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
import com.mapbox.mapboxsdk.snapshotter.MapSnapshot;
import com.mapbox.mapboxsdk.snapshotter.MapSnapshotter;
import com.mapbox.mapboxsdk.style.layers.FillExtrusionLayer;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.ColorUtils;
import com.rilixtech.materialfancybutton.MaterialFancyButton;
import com.sweetzpot.stravazpot.authenticaton.api.AuthenticationAPI;
import com.sweetzpot.stravazpot.authenticaton.model.AppCredentials;
import com.sweetzpot.stravazpot.authenticaton.model.LoginResult;
import com.sweetzpot.stravazpot.common.api.AuthenticationConfig;
import com.sweetzpot.stravazpot.common.api.StravaConfig;
import com.sweetzpot.stravazpot.upload.api.UploadAPI;
import com.sweetzpot.stravazpot.upload.model.DataType;
import com.sweetzpot.stravazpot.upload.model.UploadActivityType;
import com.sweetzpot.stravazpot.upload.model.UploadStatus;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import op27no2.fitness.Centurion2.Database.AppDatabase;
import op27no2.fitness.Centurion2.Database.Repository;
import op27no2.fitness.Centurion2.MainActivity;
import op27no2.fitness.Centurion2.MyAppWidgetProvider;
import op27no2.fitness.Centurion2.R;
import op27no2.fitness.Centurion2.fragments.nutrition.NutritionDay;
import op27no2.fitness.Centurion2.upload.TcxHelper;
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


public class RunFragment extends Fragment implements OnMapReadyCallback, PermissionsListener, TimerInterface {
    private String mapboxToken = "pk.eyJ1Ijoib3AyN25vMiIsImEiOiJjazJ0cmxvdGswejJ5M2NsN3g0ZzNucTMzIn0.SvSJkId0jesW0T3aM92q0Q";
    private MapView mapView;
    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private Vibrator rabbit;
    private ArrayList<Point> routeCoordinates = new ArrayList<Point>();
    private ArrayList<TrackedPoint> mPoints = new ArrayList<TrackedPoint>();
    private double maxalt;
    private double minalt;
    private NutritionDay mNutritionDay;
    private ArrayList<RunType> mActivityTypes = new ArrayList<RunType>();

    private Style mStyle;
    private TextView timerText;
    private TextView distanceText;
    private ImageView edit;
    private long startTime;
    private TimerService timerService;
    private Intent timerIntent;
    private SharedPreferences prefs;
    private SharedPreferences.Editor edt;
    private int locationRequestCode = 1000;
    private boolean DrawModeActive = false;
    private boolean trackBearing = true;
    private int cameraY = 90;
    private MaterialFancyButton startButton;
    private MaterialFancyButton stopButton;
    private Boolean isPaused = false;
    private long secondHold = 0;
    private List<Feature> globalFeatureList = new ArrayList<Feature>();
    private Repository mRepository;
    private Calendar cal;
    private String formattedDate;
    private long finalTime;
    private MapSnapshotter mapSnapshotter;
    private FrameLayout mFrame;
    private Spinner mSpinnerTop;

    private EditText mEditDuration;
    private EditText mEditDistance;
    private EditText mEditPace;
    private EditText mEditCals;
    private EditText mEditTitle;
    private TextView mTextviewDistanceUnits;
    private TextView mTextviewPaceUnits;

    private Integer saveTime;
    private float saveDistance;
    private int saveCals;
    private String saveDescription;
    private String saveTitle;
    private String saveDate;
    private RunType saveType;
    private Boolean stravaCheckBox;
    private Boolean saveMap;

    private double holdZoom;
    private ImageView zoom1;
    private ImageView zoomIn;
    private ImageView zoomOut;
    private ImageView loadOverlay;

    private ImageView settingsButton;

    private static final int RQ_LOGIN = 1001;
    private static final String REDIRECT_URI = "http://op27no2.fitness/callback/";
    private Boolean mapReady = false;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mapbox.getInstance(getActivity(), mapboxToken);
        View view = inflater.inflate(R.layout.fragment_run, container, false);
        rabbit = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        timerText = view.findViewById(R.id.counttime);
        distanceText = view.findViewById(R.id.distance);
        prefs = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        edt = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
        globalFeatureList = getFeatureList();
        mFrame = view.findViewById(R.id.frame);
        edt.putInt("fragtag",this.getId());
        edt.commit();


        mRepository = new Repository(getActivity());
        cal = Calendar.getInstance();
        Date c = cal.getTime();
        Long time = Calendar.getInstance().getTimeInMillis();
        System.out.println("Current time => " + time);
        SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d, ''yy");
        formattedDate = df.format(c);

        // saveDate is the edittext field in the save dialog, start it initially as formattedDate. User can change. Formatted date used as todays date elsewhere.
        saveDate = formattedDate;

        settingsButton = (ImageView) view.findViewById(R.id.settings);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).goToRunSettings();
            }
        });

        System.out.println("default selection top: "+prefs.getInt("default_activity", 0));


        mSpinnerTop = view.findViewById(R.id.top_type);
        mSpinnerTop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                edt.putInt("default_activity",position);
                edt.apply();
                System.out.println("default selection set: "+prefs.getInt("default_activity", 0));
                saveType = mActivityTypes.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        startButton = view.findViewById(R.id.start_run);
        startButton.setOnClickListener(view1 -> {
            System.out.println("start click");

            //not started, start and set text to pausable
            if(timerService == null){
                startRun();
                startButton.setText("Pause");
            }else {
                if(isPaused){
                    timerService.resumeService();
                    startButton.setText("Pause");
                    isPaused = false;
                }else{
                    timerService.pauseService();
                    startButton.setText("Resume");
                    isPaused = true;
                }
            }

        });


        ImageView mFreeze = view.findViewById(R.id.freeze);
        mFreeze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rabbit.vibrate(30);
                addTrails();
            }
        });
        stopButton = view.findViewById(R.id.stop_run);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  stopRun();
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
                    if(timerService != null){
                        final Dialog dialog = new Dialog(getActivity());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_delete);
                        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
                        int width = metrics.widthPixels;
                        dialog.getWindow().setLayout((8 * width) / 9, LinearLayout.LayoutParams.WRAP_CONTENT);
                        TextView mText = dialog.findViewById(R.id.confirm_title);
                        mText.setText("Are you sure you want to clear the run path?");

                        dialog.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                routeCoordinates.clear();
                                mPoints.clear();
                                clearExtrusion();
                                try {
                                    updateLine();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                dialog.dismiss();
                            }
                        });
                        dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();

                }
                else{
                    routeCoordinates.clear();
                    mPoints.clear();
                    clearExtrusion();
                    try {
                        updateLine();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
                    mapboxMap.animateCamera(com.mapbox.mapboxsdk.camera.CameraUpdateFactory.newLatLngZoom(new LatLng(mapboxMap.getLocationComponent().getLastKnownLocation().getLatitude(), mapboxMap.getLocationComponent().getLastKnownLocation().getLongitude()), 16));
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

        ImageView angle = view.findViewById(R.id.view_angle);
        angle.setOnTouchListener(handleTouch);

        ImageView rotation = view.findViewById(R.id.view_rotation);
        rotation.setOnTouchListener(handle360left);

        ImageView rotation2 = view.findViewById(R.id.view_rotation2);
        rotation2.setOnTouchListener(handle360);

        zoom1 = view.findViewById(R.id.view_zoom);
        zoom1.setOnTouchListener(handleZoom);

        zoomIn = view.findViewById(R.id.view_zoom_in);
        zoomIn.setOnTouchListener(handleZoomIn);

        zoomOut = view.findViewById(R.id.view_zoom_out);
        zoomOut.setOnTouchListener(handleZoomOut);

        // loadOverlay = view.findViewById(R.id.load_overlay);

        getNutritionDayData();





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
        timerIntent = new Intent(getActivity(), TimerService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            System.out.println("start foreground service (act)");
            getActivity().startForegroundService(timerIntent);
        } else {
            System.out.println("start service (act)");
            getActivity().startService(timerIntent);
        }
        getActivity().bindService(timerIntent, serviceConnection, Context.BIND_AUTO_CREATE);

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

    //updates bearing and follows target
    private void updateBearing(){
        if(trackBearing) {
            int j = routeCoordinates.size() - 1;
            float[] distance2 = new float[2];
            if (j > 10) {
                System.out.println("update bearing called");
                Location.distanceBetween(routeCoordinates.get(j - 10).latitude(), routeCoordinates.get(j - 10).longitude(), routeCoordinates.get(j).latitude(), routeCoordinates.get(j).longitude(), distance2);
                if (!DrawModeActive && distance2[0] > 1) {
                    double bearing = getDirection(routeCoordinates.get(j - 1).latitude(), routeCoordinates.get(j - 1).longitude(), routeCoordinates.get(j).latitude(), routeCoordinates.get(j).longitude());
                    CameraPosition position = new CameraPosition.Builder()
                            .bearing(bearing)
                            .target(new LatLng(routeCoordinates.get(j).latitude(), routeCoordinates.get(j).longitude()))
                            .build();
                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 1000);
                }
            }
        }
    }


    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        RunFragment.this.mapboxMap = mapboxMap;
      //  loadOverlay.setVisibility(View.GONE);
        mapReady = true;

        // mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/op27no2/ck2tujbra2ox21cqzxh4ql48y"),new Style.OnStyleLoaded() {
        //  mapboxMap.setStyle(Style.OUTDOORS, new Style.OnStyleLoaded() {
        mapboxMap.setStyle(new Style.Builder().fromUri("asset://mystyle.json"), new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                System.out.println("map ready style loaded");
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
                        mapboxMap.animateCamera(com.mapbox.mapboxsdk.camera.CameraUpdateFactory.newLatLngZoom(new LatLng(mapboxMap.getLocationComponent().getLastKnownLocation().getLatitude(), mapboxMap.getLocationComponent().getLastKnownLocation().getLongitude()), 16));
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
                                        mapboxMap.animateCamera(com.mapbox.mapboxsdk.camera.CameraUpdateFactory.newLatLngZoom(new LatLng(mapboxMap.getLocationComponent().getLastKnownLocation().getLatitude(), mapboxMap.getLocationComponent().getLastKnownLocation().getLongitude()), 16));
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
    public void onStop() {
        super.onStop();
        System.out.println("run fragment stopped");

        mapView.onStop();
    }



    @Override
    public void onResume() {
        super.onResume();
        System.out.println("run fragment onResume");
        if(isMyServiceRunning(TimerService.class)){
            bindTimerService();
        }
        mapView.onResume();
        if(RunFragment.this.mapboxMap == null) {
            mapView.getMapAsync(this);
        }

    }


    @Override
    public void onPause() {
        super.onPause();
        System.out.println("run fragment paused");
    //    loadOverlay.setVisibility(View.VISIBLE);

        if(isMyServiceRunning(TimerService.class)){
            unbindTimerService();
        }
        mapView.onPause();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        System.out.println("run fragment onDestroy");
        super.onDestroy();

    }

    public void onDestroyView() {
        //handler.removeCallbacks(runnable);'
        System.out.println("run fragment onDestroyviewed");

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
        timerIntent = new Intent(getActivity(), TimerService.class);
        getActivity().bindService(timerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }


    @Override
    public void getData(long timestamp, long elapsedTime, ArrayList<Point> rCoordinates, double min, double max, ArrayList<TrackedPoint> points) {
        System.out.println("elapsedTime:" + elapsedTime);

        finalTime = elapsedTime;
        long micro = elapsedTime / 100000;
        long elapsedSeconds = elapsedTime / 1000;
        long secondsDisplay = elapsedSeconds % 60;
        long elapsedMinutes = elapsedSeconds / 60;
        long minutesDisplay = elapsedMinutes % 60;
        int hoursDisplay = (int) Math.floor(elapsedMinutes / 60);
        
        if (secondsDisplay < 10) {
            timerText.setText(minutesDisplay + ":0" + secondsDisplay);
        } else if(hoursDisplay == 0){
            timerText.setText(minutesDisplay + ":" + secondsDisplay);
        }         else{
            timerText.setText(hoursDisplay+":"+minutesDisplay + ":" + secondsDisplay);
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
        mPoints = points;

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


            double zoom = (1 - y/height)*7+13;

            System.out.println("new tilt"+zoom);
            CameraPosition position = new CameraPosition.Builder()
                    .zoom(zoom)
                    .build();

            mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 100);

            return true;
        }
    };

    private View.OnTouchListener handleZoomIn = new View.OnTouchListener() {
        Handler handler = new Handler();

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch(motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    int delay = 10; //milliseconds

                    handler.postDelayed(new Runnable(){
                        public void run(){

                            double mapzoom = mapboxMap.getCameraPosition().zoom;
                            mapzoom = mapzoom*1.01;
                            System.out.println("new zoom"+mapzoom);
                            CameraPosition position = new CameraPosition.Builder()
                                    .zoom(mapzoom)
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


    private View.OnTouchListener handleZoomOut = new View.OnTouchListener() {
        Handler handler = new Handler();

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch(motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    int delay = 10; //milliseconds
                    handler.postDelayed(new Runnable(){
                        public void run(){
                            double mapzoom = mapboxMap.getCameraPosition().zoom;
                            mapzoom = mapzoom*.99;
                            System.out.println("new zoom"+mapzoom);
                            CameraPosition position = new CameraPosition.Builder()
                                    .zoom(mapzoom)
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
                        double height = (routeCoordinates.get(i).altitude() - minalt) * 50;
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

        Spinner mSpinner = dialog.findViewById(R.id.type);
        ArrayAdapter<RunType> adapter =
                new ArrayAdapter<RunType>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, mActivityTypes);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);

        mEditDistance = dialog.findViewById(R.id.distance_value);
        mEditPace = dialog.findViewById(R.id.pace_value);
        mEditCals = dialog.findViewById(R.id.cals_value);
        mEditTitle = dialog.findViewById(R.id.workout_title);
        mTextviewDistanceUnits = dialog.findViewById((R.id.distance_units));
        mTextviewPaceUnits = dialog.findViewById((R.id.pace_units));
        EditText mEditDescription = dialog.findViewById(R.id.workout_description);
        CheckBox mStravaCheck = (CheckBox) dialog.findViewById(R.id.strava_checkbox);
        CheckBox mMapCheck = (CheckBox) dialog.findViewById(R.id.map_checkbox);


        //set initial texts, can edit then save

        //calc total distance and pace
        final float[] total = {0f};
        for (int i = 0; i < routeCoordinates.size() - 1; i++) {
            float[] distance = new float[2];
            Location.distanceBetween(routeCoordinates.get(i).latitude(), routeCoordinates.get(i).longitude(), routeCoordinates.get(i + 1).latitude(), routeCoordinates.get(i + 1).longitude(), distance);
            total[0] = total[0] + distance[0];
        }
        long pace = (long) (finalTime /(total[0] *0.000621371192f));
        System.out.println("initial total: "+total[0]);


        mSpinner.setSelection(prefs.getInt("default_activity",mActivityTypes.size()-1));
        saveType = mActivityTypes.get(mSpinner.getSelectedItemPosition());

        setDialogText(mActivityTypes.get(prefs.getInt("default_activity",0)), total);
        setPaceAndCalText(mActivityTypes.get(prefs.getInt("default_activity",0)),total);


        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                setDialogText(mActivityTypes.get(position), total);
                setPaceAndCalText(mActivityTypes.get(position), total);
                saveType = mActivityTypes.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

      //  mEditPace.setText(getDuration(pace)+" /mi");

        //TODO edit cals calc, setCals method?
      //  mEditCals.setText(Integer.toString(((int) Math.floor(total[0] *0.000621371192f*prefs.getInt("weight",215)*0.63))));
        mEditCals.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    View view = getActivity().getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
                return false;
            }
        });


        mEditDistance.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                //total[0] is equal to a number of miles or kilometers,
                float newTotal = 0;
                if(!mEditDistance.getText().toString().equals("") && !mEditDistance.getText().toString().equals(".")) {

                    double factor = (mActivityTypes.get(mSpinner.getSelectedItemPosition()).getDistanceUnits() == 0) ? .000621371192f : .001;
                    newTotal = Float.parseFloat(mEditDistance.getText().toString());
                    //have to adjust total since you edited distance
                    total[0] = (float) Math.floor(newTotal / factor);
                    setPaceAndCalText(mActivityTypes.get(mSpinner.getSelectedItemPosition()), total);
                }else{
                    System.out.println("string empty savecals 0");
                    saveCals = 0;
                    mEditCals.setText(Integer.toString(saveCals));
                    setPaceAndCalText(mActivityTypes.get(mSpinner.getSelectedItemPosition()), total);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });
        mEditDistance.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    System.out.println("event test");
                    mEditCals.requestFocus();
                }
                return false;
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

        EditText mEditTime1 = dialog.findViewById(R.id.duration_hours);
        EditText mEditTime2 = dialog.findViewById(R.id.duration_minutes);
        EditText mEditTime3 = dialog.findViewById(R.id.duration_seconds);

        long elapsedSeconds = finalTime / 1000;
        long secondsDisplay = elapsedSeconds % 60;
        long elapsedMinutes = elapsedSeconds / 60;
        long minutesDisplay = elapsedMinutes % 60;
        long hoursDisplay = elapsedMinutes / 60;

        int hours = (int)  Math.floor(hoursDisplay);
        int minutes = (int)  Math.floor(minutesDisplay);
        int seconds = (int)  Math.floor(secondsDisplay);

     //   if(hours>0) {
            mEditTime1.setText(Integer.toString(hours));
     //   }
        if(minutes<10) {
            mEditTime2.setText("0"+Integer.toString(minutes));
        }
        else if (minutes>0){
            mEditTime2.setText(Integer.toString(minutes));
        }

        if(seconds<10) {
            mEditTime3.setText("0"+Integer.toString(seconds));
        }else{
            mEditTime3.setText(Integer.toString(seconds));
        }

        mEditTime1.addTextChangedListener(new TextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==1){
                    mEditTime2.requestFocus();
                    finalTime = ((Integer.parseInt(mEditTime1.getText().toString())*60*60)+(Integer.parseInt(mEditTime2.getText().toString())*60)+(Integer.parseInt(mEditTime3.getText().toString())))*1000;

                    // setDialogText(mActivityTypes.get(mSpinner.getSelectedItemPosition()), total);
                    setPaceAndCalText(mActivityTypes.get(mSpinner.getSelectedItemPosition()), total);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
        mEditTime2.addTextChangedListener(new TextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==2) {
                    mEditTime3.requestFocus();
                }
                    finalTime = ((Integer.parseInt(mEditTime1.getText().toString())*60*60)+(Integer.parseInt(mEditTime2.getText().toString())*60)+(Integer.parseInt(mEditTime3.getText().toString())))*1000;
                    //  setDialogText(mActivityTypes.get(mSpinner.getSelectedItemPosition()), total);
                    setPaceAndCalText(mActivityTypes.get(mSpinner.getSelectedItemPosition()), total);

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        mEditTime3.addTextChangedListener(new TextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==2) {
                    mEditDistance.requestFocus();
                }
                finalTime = ((Integer.parseInt(mEditTime1.getText().toString())*60*60)+(Integer.parseInt(mEditTime2.getText().toString())*60)+(Integer.parseInt(mEditTime3.getText().toString())))*1000;
                //  setDialogText(mActivityTypes.get(mSpinner.getSelectedItemPosition()), total);
                setPaceAndCalText(mActivityTypes.get(mSpinner.getSelectedItemPosition()), total);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });





        dialog.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check time is formatted correctly

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

                    //
                    //saveDistance = (int) Math.floor(Float.parseFloat(mEditDistance.getText().toString()) * 1609.34400);
                    saveDistance = total[0];
                    saveCals = Integer.parseInt(mEditCals.getText().toString());
                    saveTime = 1000*((Integer.parseInt(mEditTime1.getText().toString())*60*60)+(Integer.parseInt(mEditTime2.getText().toString())*60)+(Integer.parseInt(mEditTime3.getText().toString())));
                    saveDescription = mEditDescription.getText().toString();
                    saveTitle = mEditTitle.getText().toString();
                    stravaCheckBox = mStravaCheck.isChecked();
                    saveMap = mMapCheck.isChecked();

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
              //  }
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

    private void setDialogText(RunType activity, float[] total){
        mEditTitle.setText("Daily "+activity.getName());
        switch(activity.getDistanceUnits()){
            case 0:
                //Miles
                mEditDistance.setText(getMiles(total[0]));
                mTextviewDistanceUnits.setText("miles");
                break;
            case 1:
                //Kilometers
                double Kilometers = total[0]/1000;
                mEditDistance.setText(String.format("%.2f",Kilometers));
                mTextviewDistanceUnits.setText("km");
                break;

        }
    }

    //this uses total[0]
    private void setPaceAndCalText(RunType activity, float[] total){
        System.out.println("pace total: "+total[0]);

        if(!mEditDistance.getText().toString().equals("") || !mEditDistance.getText().toString().equals(".")  || Integer.parseInt(mEditDistance.getText().toString()) != 0) {
            long pace = 0;
            float miles = total[0] * 0.000621371192f;
            float kilometers = total[0] / 1000;
            float five = total[0] / 500;
            float one = total[0]/100;

            //will downfactor calculations in pounds to kg
            double factorUnit = prefs.getInt("units",0) == 0 ? 1 : (1/2.2);
            double factorCal = activity.getCalBurnValue();
            int weight = prefs.getInt("weight",180);

            int calValue = 0;
            switch (activity.getCalBurnUnit()) {
                case 0:
                    //Per Minute
                    calValue = (int) Math.floor((finalTime/1000/60)*weight*factorUnit*factorCal);
                    break;
                case 1:
                    //Per Mile
                    System.out.println("miles: "+miles+" weight: "+weight+" funit: "+factorUnit+" fcal: "+factorCal);
                    calValue = (int) Math.floor(miles*weight*factorUnit*factorCal);
                    break;
                case 2:
                    //Per Kilometer
                    System.out.println("kilo: "+kilometers+" weight: "+weight+" funit: "+factorUnit+" fcal: "+factorCal);
                    calValue = (int) Math.floor(kilometers*weight*factorUnit*factorCal);
                    break;
                case 3:
                    //Per Hour
                    calValue = (int) Math.floor((finalTime/1000/60/60)*weight*factorUnit*factorCal);
                    break;
            }
            mEditCals.setText(Integer.toString(calValue));

            switch (activity.getPaceUnits()) {
                case 0:
                    //minutes per mile
                    if(miles!=0) {
                        pace = (long) (finalTime / (miles));
                    }
                    mEditPace.setText(getDuration(pace));
                    mTextviewPaceUnits.setText("/mi");
                    break;
                case 1:
                    //per kilometer
                    if(kilometers!=0) {
                        pace = (long) (finalTime / (kilometers));
                    }
                    mEditPace.setText(getDuration(pace));
                    mTextviewPaceUnits.setText("/km");
                    break;
                case 2:
                    //per 500m

                    if(five!=0) {
                        pace = (long) (finalTime / (five));
                    }
                    mEditPace.setText(getDuration(pace));
                    mTextviewPaceUnits.setText("/500m");
                    break;
                case 3:
                    //per 100m
                    if(one!=0) {
                        pace = (long) (finalTime / (one));
                    }
                    mEditPace.setText(getDuration(pace));
                    mTextviewPaceUnits.setText("/100m");
                    break;
                case 4:
                    //miles per hour
                    float mpace = 0;
                    if(miles!=0) {
                        mpace = (float) ((miles ) / ((float) finalTime / 3600000f));
                    }
                    mEditPace.setText(String.format("%.2f", mpace));
                    mTextviewPaceUnits.setText("mph");
                    break;
            }
        }
    }



    public void saveRun(Bitmap bMap){
        RunWorkout mRunWorkout = new RunWorkout();
        mRunWorkout.setWorkoutDate(saveDate);
        mRunWorkout.setWorkoutType(saveType);
        mRunWorkout.setDuration(saveTime);
        System.out.println("save duration: " + saveTime);
        mRunWorkout.setDistance((int) saveDistance);
        mRunWorkout.setCalories(saveCals);
        mRunWorkout.setDescription(saveDescription);
        mRunWorkout.setTitle(saveTitle);
        mRunWorkout.setSaveMap(saveMap);


        int cals = mNutritionDay.getValues().get(0);

        ArrayList<Integer> mvals = mNutritionDay.getValues();
        cals = cals - saveCals;
        mvals.set(0, cals);
        mNutritionDay.setValues(mvals);
        mRepository.updateNutrition(mNutritionDay);

        //store name of bitmap file to retrieve, file stored on system.
        if(saveMap) {
            String bmp = saveBitmap(bMap, UUID.randomUUID().toString());
            System.out.println("bmp string: " + bmp);
            String coords = saveCoordinates(routeCoordinates, UUID.randomUUID().toString());
            mRunWorkout.setImage(bmp);
            mRunWorkout.setCoordinates(coords);
        }

        //TODO add checkbox for social upload
        if(stravaCheckBox) {
            uploadToStrava();
        }

        mRepository.insertRunWorkout(mRunWorkout);
        updateWidgets();
    }




    private void startSnapShot() {
        MapSnapshotter.Options snapShotOptions = new MapSnapshotter.Options(500, 500);

        snapShotOptions.withRegion(mapboxMap.getProjection().getVisibleRegion().latLngBounds);

        snapShotOptions.withStyle(mapboxMap.getStyle().getUri());

        mapSnapshotter = new MapSnapshotter(getActivity(), snapShotOptions);
        mapSnapshotter.start(new MapSnapshotter.SnapshotReadyCallback() {
            @Override
            public void onSnapshotReady(MapSnapshot snapshot) {

                // Display, share, or use bitmap image how you'd like
                System.out.println("ss ready");
                Bitmap bitmapImage = snapshot.getBitmap();
                saveRun(bitmapImage);


            }
        });

        //TODO map snapshot not working?
       /*
        MapboxMap.SnapshotReadyCallback snapCallback = new MapboxMap.SnapshotReadyCallback() {
            @Override
            public void onSnapshotReady(@NonNull Bitmap snapshot) {
//
                saveRun(snapshot);
                //
            }
        };
        mapboxMap.snapshot(snapCallback);*/
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


    //TODO STORE COORDS ELSEWHERE THIS IS KILLING THE GODDAMN APP BY SLOWING SHAREDPREFERENCES
    public String saveCoordinates(ArrayList<Point> coords, String uid){
            Gson gson = new Gson();
            String json = gson.toJson(coords);
        System.out.println("SAVE COORDS JSON: " + json);

            edt.putString(uid, json);
            edt.commit();
            return uid;

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
                mActivityTypes = new ArrayList<RunType>(AppDatabase.getAppDatabase(getActivity()).rtDAO().getAllActive());

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
                finishUI();
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
        //input is milliseconds
        long micro = elapsedTime / 100000;
        long elapsedSeconds = elapsedTime / 1000;
        long secondsDisplay = elapsedSeconds % 60;
        long elapsedMinutes = elapsedSeconds / 60;
        long minutesDisplay = elapsedMinutes % 60;
        long hoursDisplay = (int) Math.floor(elapsedMinutes / 60);
        if(hoursDisplay>999){
            hoursDisplay = 999;
        }

        if(hoursDisplay>0) {
            if (secondsDisplay < 10) {
                return ((hoursDisplay + ":" + minutesDisplay + ":0" + secondsDisplay));
            } else {
                return ((hoursDisplay + ":" + minutesDisplay + ":" + secondsDisplay));
            }
        }else{
            if (secondsDisplay < 10) {
                return ((minutesDisplay + ":0" + secondsDisplay));
            } else {
                return ((minutesDisplay + ":" + secondsDisplay));
            }
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


    public void uploadToStrava(){
        System.out.println("trackedpoints size1 "+mPoints.size());

        if(!prefs.getBoolean("strava8", false)) {
            Uri intentUri = Uri.parse("https://www.strava.com/oauth/mobile/authorize")
                    .buildUpon()
                    .appendQueryParameter("client_id", "43815")
                    .appendQueryParameter("redirect_uri", REDIRECT_URI)
                    .appendQueryParameter("response_type", "code")
                    .appendQueryParameter("approval_prompt", "auto")
                    .appendQueryParameter("scope", "activity:write,read")
                    .build();
            edt.putString("upload_fragment", "Run");
            edt.commit();
            Intent intent = new Intent(Intent.ACTION_VIEW, intentUri);
            intent.putExtra("key", 999);
            startActivityForResult(intent, RQ_LOGIN);
        }else{
            finishStrava();
        }

    }

    @SuppressLint("StaticFieldLeak")
    public void finishStrava() {
        System.out.println("trackedpoints size2 "+mPoints.size());

        new AsyncTask<Void, Void, Void>() {

            protected void onPreExecute() {
            }

            protected Void doInBackground(Void... unused) {
                //nutritionDay and formatted day change with selected day up top, used to edit values
                AuthenticationConfig config = AuthenticationConfig.create()
                        .debug()
                        .build();
                AuthenticationAPI api = new AuthenticationAPI(config);
                String mToken = null;
                String mToken2 = null;

                if(prefs.getBoolean("first_refresh", true) == true) {
                    LoginResult result = api.getTokenForApp(AppCredentials.with(43815, "87571a766af016d9949d28929316f894bbc57938"))
                            .withCode(prefs.getString("code", "")) //original response token placed here as well.
                            .execute();
                    mToken = result.getAccessToken();
                    mToken2 = result.getRefreshToken();
                    edt.putBoolean("first_refresh",false );
                    edt.putString("refresh_token",mToken2 );
                    edt.apply();
                }else {
                    AuthenticationConfig config2 = AuthenticationConfig.create()
                            .debug()
                            .build();
                    AuthenticationAPI api2 = new AuthenticationAPI(config);
                    LoginResult loginResult = api2.refreshTokenForApp(AppCredentials.with(43815, "87571a766af016d9949d28929316f894bbc57938"))
                            .withRefreshToken(prefs.getString("refresh_token", ""))
                            .refreshToken();
                    mToken = loginResult.getAccessToken();
                    mToken2 = loginResult.getRefreshToken();
                    edt.putString("refresh_token", mToken2);
                    edt.apply();
                }

                StravaConfig sconfig = StravaConfig.withToken(mToken)
                        .debug()
                        .build();

               /* ActivityAPI activityAPI = new ActivityAPI(sconfig);
                Activity activity = activityAPI.createActivity("test upload")
                        .ofType(ActivityType.RUN)
                        .startingOn(Calendar.getInstance().getTime())
                        .withElapsedTime(Time.seconds(333))
                        .withDescription("test description")
                        .withDistance(Distance.meters((int) 33333))
                        .isPrivate(true)
                        .withTrainer(true)
                        .withCommute(false)
                        .execute();*/

                TcxHelper mHelper = new TcxHelper();
                String mPath = mHelper.createTCX("testfile"+System.currentTimeMillis(), mPoints, (int) saveDistance, saveCals, saveTime);

                UploadAPI uploadAPI = new UploadAPI(sconfig);
                UploadStatus uploadStatus = uploadAPI.uploadFile(new File(mPath))
                        .withDataType(DataType.TCX)
                        .withActivityType(UploadActivityType.RUN)
                        .withName(saveTitle)
                        .withDescription(saveDescription)
                        .isPrivate(false)
                        .hasTrainer(false)
                        .isCommute(false)
                        .withExternalID("test4")
                        .execute();




                return null;
            }

            protected void onPostExecute(Void unused) {
                // Post Code
            }
        }.execute();



    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void finishUI(){
        ArrayAdapter<RunType> adapter =
                new ArrayAdapter<RunType>(getApplicationContext(), R.layout.spinner_color, mActivityTypes);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        mSpinnerTop.setAdapter(adapter);
        System.out.println("default selection loaded: "+prefs.getInt("default_activity", 0));

        int spinnerSize= 0;
        for(int i=0; i<mActivityTypes.size();i++) {
            if(mActivityTypes.get(0).getActive()){
                spinnerSize++;
            }
        }
        if(prefs.getInt("default_activity",0) >= spinnerSize){
            edt.putInt("default_activity", 0);
            edt.commit();
        }
        mSpinnerTop.setSelection(prefs.getInt("default_activity", 0));


    }


}

