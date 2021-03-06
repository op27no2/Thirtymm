package op27no2.fitness.Centurion2.fragments.run;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.MPPointF;
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
import com.mapbox.mapboxsdk.maps.UiSettings;
import com.mapbox.mapboxsdk.snapshotter.MapSnapshot;
import com.mapbox.mapboxsdk.snapshotter.MapSnapshotter;
import com.mapbox.mapboxsdk.style.layers.FillExtrusionLayer;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.ColorUtils;
import com.rilixtech.materialfancybutton.MaterialFancyButton;
import com.sweetzpot.stravazpot.activity.api.ActivityAPI;
import com.sweetzpot.stravazpot.activity.model.Activity;
import com.sweetzpot.stravazpot.activity.model.ActivityType;
import com.sweetzpot.stravazpot.authenticaton.api.AuthenticationAPI;
import com.sweetzpot.stravazpot.authenticaton.model.AppCredentials;
import com.sweetzpot.stravazpot.authenticaton.model.LoginResult;
import com.sweetzpot.stravazpot.common.api.AuthenticationConfig;
import com.sweetzpot.stravazpot.common.api.StravaConfig;
import com.sweetzpot.stravazpot.common.model.Distance;
import com.sweetzpot.stravazpot.common.model.Time;
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
import java.util.concurrent.ThreadLocalRandom;

import op27no2.fitness.Centurion2.Database.AppDatabase;
import op27no2.fitness.Centurion2.Database.Repository;
import op27no2.fitness.Centurion2.FancyScrollView;
import op27no2.fitness.Centurion2.MyAppWidgetProvider;
import op27no2.fitness.Centurion2.R;
import op27no2.fitness.Centurion2.fragments.nutrition.NutritionDay;
import op27no2.fitness.Centurion2.upload.TcxHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static android.graphics.Color.parseColor;
import static com.mapbox.api.directions.v5.DirectionsCriteria.PROFILE_DRIVING;
import static com.mapbox.core.constants.Constants.PRECISION_6;
import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;
import static com.mapbox.mapboxsdk.style.expressions.Expression.exponential;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.expressions.Expression.interpolate;
import static com.mapbox.mapboxsdk.style.expressions.Expression.linear;
import static com.mapbox.mapboxsdk.style.expressions.Expression.rgb;
import static com.mapbox.mapboxsdk.style.expressions.Expression.stop;
import static com.mapbox.mapboxsdk.style.expressions.Expression.zoom;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionHeight;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillExtrusionOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;


public class RunDetailFragment extends Fragment implements OnMapReadyCallback, PermissionsListener {
    private String mapboxToken = "pk.eyJ1Ijoib3AyN25vMiIsImEiOiJjazJ0cmxvdGswejJ5M2NsN3g0ZzNucTMzIn0.SvSJkId0jesW0T3aM92q0Q";
    private MapView mapView;
    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private Vibrator rabbit;
    private ArrayList<Point> routeCoordinates = new ArrayList<Point>();
    private double maxalt;
    private double minalt;
    private NutritionDay mNutritionDay;
    private static final int RQ_LOGIN = 1001;
    private static final String REDIRECT_URI = "http://op27no2.fitness/callback/";

    private Style mStyle;
    private TextView timerText;
    private TextView mapDistanceText;
    private ImageView edit;
    private long startTime;
    private RunService timerService;
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
    private long finalTime;
    private MapSnapshotter mapSnapshotter;

    private EditText mEditTitle;
    private EditText mEditDescription;
    private EditText mEditDuration;
    private EditText mEditDistance;
    private EditText mEditPace;
    private EditText mEditCals;
    private TextView mTextviewDistanceUnits;
    private TextView mTextviewPaceUnits;

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
    private Boolean hasMap = false;

    private ImageView zoomIn;
    private ImageView zoomOut;
    private EditText mEditTime1;
    private EditText mEditTime2;
    private EditText mEditTime3;
    private Spinner mSpinner;
    private ArrayList<RunType> mActivityTypes = new ArrayList<RunType>();
    private RunType saveType;
    final float[] total = new float[1];
    private CombinedChart heartRateCombinedChart;
    private PieChart heartRatePieChart;
    private PieChart pacePieChart;
    private HorizontalBarChart splitsChart;
    private LineChart paceChart;
    private LineChart elevationChart;
    private ArrayList<TrackedPoint> trackedPoints;
    ArrayList<Integer> splitsData = new ArrayList<Integer>();
    private int smoothingFactor = 5;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        Mapbox.getInstance(getActivity(), mapboxToken);
        View view = inflater.inflate(R.layout.fragment_rundetail, container, false);
        rabbit = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        timerText = view.findViewById(R.id.counttime);
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
      //  formattedDate = df.format(c);

        // saveDate is the edittext field in the save dialog, start it initially as formattedDate. User can change. Formatted date used as todays date elsewhere.
        saveDate = formattedDate;


        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

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


        mapDistanceText = view.findViewById(R.id.map_distance);
        mText4 = view.findViewById(R.id.date);
        mEditDuration = view.findViewById(R.id.duration_value);
        mEditDistance = view.findViewById(R.id.distance_value);
        mEditPace = view.findViewById(R.id.pace_value);
        mEditCals = view.findViewById(R.id.cals_value);
        mEditTitle = view.findViewById(R.id.workout_title);
        mEditDescription = view.findViewById(R.id.workout_description);
        mTextviewDistanceUnits = view.findViewById((R.id.distance_units));
        mTextviewPaceUnits = view.findViewById((R.id.pace_units));

        mEditTime1 = view.findViewById(R.id.duration_hours);
        mEditTime2 = view.findViewById(R.id.duration_minutes);
        mEditTime3 = view.findViewById(R.id.duration_seconds);
        mSpinner = view.findViewById(R.id.type);


        lockWindowButton = view.findViewById(R.id.lock_window);
        lockWindowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lockWindowButton.getText().toString().equals("Unlock")){
                    //LOCK THE WINDOW
                    scrollView.setScrolling(false);
                    mapboxMap.getUiSettings().setAllGesturesEnabled(true);
                    lockWindowButton.setText("Lock");
                }else{
                    //UNLOCK THE WINDOW
                    scrollView.setScrolling(true);
                    mapboxMap.getUiSettings().setAllGesturesEnabled(false);
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

        ImageView uploadButton = (ImageView) view.findViewById(R.id.upload);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rabbit.vibrate(25);
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_delete);

                DisplayMetrics metrics = getResources().getDisplayMetrics();
                int width = metrics.widthPixels;

                dialog.getWindow().setLayout((8 * width) / 9, LinearLayout.LayoutParams.WRAP_CONTENT);
                TextView mText = dialog.findViewById(R.id.confirm_title);
                Button mButton = dialog.findViewById(R.id.delete);
                mText.setText("Upload to Strava?");
                mButton.setText("Confirm");

                dialog.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        uploadToStrava();
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


        zoom1 = view.findViewById(R.id.view_zoom);
        zoom1.setOnTouchListener(handleZoom);

        scrollView = view.findViewById(R.id.scroll_view);
        mCardView = view.findViewById(R.id.card_view_map);

        //START CHART SETUP
        //START CHART SETUP
        heartRateCombinedChart = view.findViewById(R.id.heartrate_line_chart);
        heartRateCombinedChart.getDescription().setEnabled(false);
        heartRateCombinedChart.getLegend().setEnabled(false);
        heartRateCombinedChart.setBackgroundColor(Color.WHITE);
        heartRateCombinedChart.setDrawGridBackground(false);
        heartRateCombinedChart.setDrawBarShadow(false);
        heartRateCombinedChart.setHighlightFullBarEnabled(false);
        // draw bars behind lines
        heartRateCombinedChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE
        });


   /*     Legend l = heartRateCombinedChart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);*/

        YAxis rightAxis = heartRateCombinedChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(100f); // this replaces setStartAtZero(true)
        rightAxis.setAxisMaximum(200f); // this replaces setStartAtZero(true)

        YAxis leftAxis = heartRateCombinedChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(100f); // this replaces setStartAtZero(true)
        leftAxis.setAxisMaximum(200f); // this replaces setStartAtZero(true)

        XAxis xAxis = heartRateCombinedChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        //TODO what does this do looks deprecated
    /*    xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return months[(int) value % months.length];

            }
        });
*/



        heartRatePieChart = view.findViewById(R.id.heartrate_pie_chart);
        heartRatePieChart.getDescription().setEnabled(false);
        //heartRatePieChart.setRotationAngle(180);
        //heartRatePieChart.setMaxAngle(180f);
        heartRatePieChart.animateY(1400, Easing.EaseInOutQuad);
        heartRatePieChart.setEntryLabelColor(Color.WHITE);
        heartRatePieChart.setEntryLabelTextSize(12f);
        //heartRatePieChart.setExtraOffsets(0,0,0,-100);
        heartRatePieChart.invalidate();

        pacePieChart = view.findViewById(R.id.pace_pie_chart);
        pacePieChart.getDescription().setEnabled(false);
        pacePieChart.animateY(1400, Easing.EaseInOutQuad);
        pacePieChart.setEntryLabelColor(Color.WHITE);
        pacePieChart.setEntryLabelTextSize(12f);
        pacePieChart.invalidate();

        splitsChart = view.findViewById(R.id.splits_chart);
        splitsChart.setDrawBarShadow(false);
        splitsChart.setDrawValueAboveBar(false);
        splitsChart.getDescription().setEnabled(false);
        splitsChart.setPinchZoom(false);
        splitsChart.setDrawGridBackground(false);
        splitsChart.animateX(1400, Easing.EaseInOutQuad);

        XAxis xl = splitsChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        xl.setGranularity(1f);


        YAxis yl = splitsChart.getAxisLeft();
        yl.setDrawAxisLine(true);
        yl.setDrawLabels(true);
        yl.setDrawTopYLabelEntry(true);
        yl.setDrawGridLines(true);
        yl.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        splitsChart.setFitBars(true);


        paceChart = view.findViewById(R.id.pace_chart);
        paceChart.setBackgroundColor(Color.WHITE);
        paceChart.setGridBackgroundColor(Color.WHITE);
        paceChart.setDrawGridBackground(true);
        paceChart.setDrawBorders(true);
        paceChart.getDescription().setEnabled(false);
        paceChart.setPinchZoom(false);
        paceChart.animateX(3000, Easing.EaseInOutQuad);


        Legend l = paceChart.getLegend();
        l.setEnabled(false);

        XAxis mxAxis = paceChart.getXAxis();

        YAxis mleftAxis = paceChart.getAxisLeft();
        mleftAxis.setAxisMaximum(6f);
        mleftAxis.setAxisMinimum(0f);
        mleftAxis.setDrawAxisLine(false);
        mleftAxis.setDrawZeroLine(false);
        mleftAxis.setDrawGridLines(false);

        paceChart.getAxisRight().setEnabled(false);




        elevationChart = view.findViewById(R.id.elevation_chart);
        elevationChart.setBackgroundColor(Color.WHITE);
        elevationChart.setGridBackgroundColor(Color.WHITE);
        elevationChart.setDrawGridBackground(true);
        elevationChart.setDrawBorders(true);
        elevationChart.getDescription().setEnabled(false);
        elevationChart.setPinchZoom(false);

        Legend l2 = elevationChart.getLegend();
        l2.setEnabled(false);

        XAxis mxAxis2 = elevationChart.getXAxis();
        YAxis mleftAxis2 = elevationChart.getAxisLeft();
        mleftAxis2.setDrawAxisLine(false);
        mleftAxis2.setDrawZeroLine(false);
        mleftAxis2.setDrawGridLines(false);

        elevationChart.getAxisRight().setEnabled(false);




        //END CHART SETUP
        //END CHART SETUP

        getRunWorkoutData();
        return view;
    }

    private int getRandom(int min, int max){
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
    private BarData generateSplitsData() {

            splitsData = new ArrayList<Integer>();

            int index = 0;
            int intervalStart=0;
            long time = 0;
            double distance = 0;
            for(int i=0;i<trackedPoints.size();i++){
                if((trackedPoints.get(i).getDistance()*0.000621371192f) >= (index+1)){
                    time = (trackedPoints.get(i).getTimestamp() - trackedPoints.get(intervalStart).getTimestamp())/1000;
                    splitsData.add((int) time);
                    ++index;
                    intervalStart = i;
                }
                //last iteration, last point
                if((i == trackedPoints.size()-1) && (i-intervalStart>30)){
                    time = (trackedPoints.get(i).getTimestamp() - trackedPoints.get(intervalStart).getTimestamp())/1000;
                    distance = (trackedPoints.get(i).getDistance() - trackedPoints.get(intervalStart).getDistance());
                    splitsData.add((int) Math.floor((double)time/(distance*0.000621371192f)));
                }

            }

            //transform splits data into relative bar paces
            int max = 0;
            int min = 99999999;
            for(int j=0;j<splitsData.size();j++){
                System.out.println("splits data: "+splitsData.get(j));
                if(splitsData.get(j)>max){
                    max = splitsData.get(j);
                }
                if(splitsData.get(j)<min){
                    min = splitsData.get(j);
                }
            }
            int lowValue = min - 15;
            //int highValue = max+30;

        ArrayList<BarEntry> entries2 = new ArrayList<>();
        for(int k=0;k<splitsData.size();k++){
            entries2.add(new BarEntry(k, splitsData.get(k)));
        }

        BarDataSet set1 = new BarDataSet(entries2, "");
        set1.setColors(Color.argb(100,0, 0, 240));
        set1.setDrawValues(true);
        /*set1.setValueTextColor(Color.rgb(61, 165, 255));
        set1.setValueTextSize(10f);*/
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);


        float barSpace = 0.2f; // x2 dataset
        float barWidth = 0.8f; // x2 dataset

        BarData d = new BarData(set1);
        d.setBarWidth(barWidth);
        splitsChart.getAxisLeft().setAxisMinimum(lowValue);

        return d;
    }
    private LineData generatePaceData() {



        LineData d = new LineData();
        ArrayList<Entry> entries = new ArrayList<>();
        for (int index = 30; index < trackedPoints.size(); index++) {
            //TODO may want this an average instead of instantaneous
            long deltaTime =  trackedPoints.get(index).getTimestamp()- trackedPoints.get(index-29).getTimestamp();
            System.out.println("deltaTime0: "+ trackedPoints.get(index).getTimestamp());
            System.out.println("deltaTimediff: "+deltaTime);
            float deltaMeters = (float) trackedPoints.get(index).getDistance()-(float) trackedPoints.get(index-29).getDistance();
            System.out.println("deltaMeters: "+deltaMeters);
            System.out.println("heart?: "+ trackedPoints.get(index).getHeartRate());

            float pace = (deltaMeters/deltaTime)*1000f;
            System.out.println("pace: "+pace);


            entries.add(new Entry(index + 0.5f,pace));
        }
        LineDataSet set = new LineDataSet(entries, "Line DataSet");
        set.setColor(Color.rgb(0, 0, 0));
        set.setDrawCircles(false);
        set.setLineWidth(1.5f);
        set.setDrawFilled(true);
  /*      if (Utils.getSDKInt() >= 18) {
            // fill drawable only supported on api level 18 and above
            Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.fade_blue);
            set.setFillDrawable(drawable);
        }
        else {*/
            set.setFillColor(Color.argb(200,159, 193, 255));
     //   }
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(false);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return d;
    }
    private LineData generateHeartRateLineData() {

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<>();

        for (int index = 0; index < trackedPoints.size(); index++) {
            entries.add(new Entry(index + 0.5f, (float) trackedPoints.get(index).getHeartRate()));
            System.out.println("heart data: "+ trackedPoints.get(index).getHeartRate());
        }
        LineDataSet set = new LineDataSet(entries, "Line DataSet");
        set.setColor(Color.rgb(0, 0, 0));
        set.setDrawCircles(false);
        set.setLineWidth(1.5f);
        set.setFillColor(Color.rgb(255, 255, 255));
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(false);




        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return d;
    }

    private BarData generateHeartRateBarData() {

        ArrayList<BarEntry> entries2 = new ArrayList<>();

        for (int index = 0; index < trackedPoints.size(); index++) {
            // stacked
            entries2.add(new BarEntry(index, new float[]{130, 20, 20, 30}));
        }


        BarDataSet set1 = new BarDataSet(entries2, "");
        set1.setStackLabels(new String[]{"Stack 1", "Stack 2"});
        set1.setColors(Color.argb(77,18, 196, 0), Color.argb(77,252, 251, 87), Color.argb(77,255, 186, 33), Color.argb(77,255, 0, 0));
        set1.setDrawValues(false);
        /*set1.setValueTextColor(Color.rgb(61, 165, 255));
        set1.setValueTextSize(10f);*/
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        float groupSpace = 0.06f;
        float barSpace = 0.02f; // x2 dataset
        float barWidth = 1f; // x2 dataset

        BarData d = new BarData(set1);
        d.setBarWidth(barWidth);

        return d;
    }
    private PieData generatePieData() {
        PieData d = new PieData();
        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        double HR4 = 0;
        double HR3 = 0;
        double HR2 = 0;
        double HR1 = 0;

        for(int i=0;i<trackedPoints.size()-1;i++){
            int heartRate = (int) trackedPoints.get(i).getHeartRate();
            double time = trackedPoints.get(i+1).getTimestamp() - trackedPoints.get(i).getTimestamp();
            if(heartRate >= 170){
                HR4 = HR4 + time;
            }else if(150 <= heartRate && heartRate< 170){
                HR3 = HR3 + time;
            }else if(130 <= heartRate && heartRate< 150){
                HR2 = HR2 + time;
            }else if(heartRate< 130){
                HR1 = HR1 + time;
            }
        }
        HR1 = HR1/1000;
        HR2 = HR2/1000;
        HR3 = HR3/1000;
        HR4 = HR4/1000;

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry((float) HR1,"Zone "+(1)));
        entries.add(new PieEntry((float) HR2,"Zone "+(2)));
        entries.add(new PieEntry((float) HR3,"Zone "+(3)));
        entries.add(new PieEntry((float) HR4,"Zone "+(4)));

        PieDataSet dataSet = new PieDataSet(entries,"Pace");
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);
        dataSet.setColors(Color.argb(200,18, 196, 0), Color.argb(200,252, 251, 87), Color.argb(200, 255, 186, 33), Color.argb(200,255, 0, 0));

        d.setValueFormatter(new PercentFormatter());
        d.setValueTextSize(11f);
        d.setValueTextColor(Color.WHITE);
        d.addDataSet(dataSet);
        return d;
    }
    private PieData generatePacePieData() {
        PieData d = new PieData();
        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.

        double PZ4 = 0;
        double PZ3 = 0;
        double PZ2 = 0;
        double PZ1 = 0;

        for(int i=0;i<trackedPoints.size()-1;i++){
            double time = trackedPoints.get(i+1).getTimestamp() - trackedPoints.get(i).getTimestamp();
            double distance = trackedPoints.get(i+1).getDistance() - trackedPoints.get(i).getDistance();
            double pace = 1000*(distance/time);
            System.out.println("pace values pie"+pace);
            if(pace >= 3){
                PZ4 = PZ4 + time;
            }else if(2.5 <= pace && pace< 3){
                PZ3 = PZ3 + time;
            }else if(2 <= pace && pace< 2.5){
                PZ2 = PZ2 + time;
            }else if(pace< 2){
                PZ1 = PZ1 + time;
            }
        }
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry((float) PZ1,"Zone "+(1)));
        entries.add(new PieEntry((float) PZ2,"Zone "+(2)));
        entries.add(new PieEntry((float) PZ3,"Zone "+(3)));
        entries.add(new PieEntry((float) PZ4,"Zone "+(4)));


        PieDataSet dataSet = new PieDataSet(entries,"HR");
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);
        dataSet.setColors(Color.argb(200,159, 193, 255), Color.argb(200,116, 165, 255), Color.argb(200, 48, 121, 255), Color.argb(200,0, 90, 255));

        d.setValueFormatter(new PercentFormatter());
        d.setValueTextSize(11f);
        d.setValueTextColor(Color.WHITE);
        d.addDataSet(dataSet);
        return d;
    }

    private LineData generateElevationData() {
        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<>();

        for (int index = 0; index < trackedPoints.size(); index++) {
            //if(index<30) {
                entries.add(new Entry(index + 0.5f, (float) trackedPoints.get(index).getPoint().altitude()));
            //}
            //v1
            /*else{
                entries.add(new Entry(index + 0.5f, ((float) routeTrackedPoints.get(index).getPoint().altitude()+(float) routeTrackedPoints.get(index-30).getPoint().altitude() )/2 ));
            }*/
            //v2
            /*else{
                float avg= 0;
                for(int j=0; j<30; j++){
                    avg = avg+(float) routeTrackedPoints.get(index-j).getPoint().altitude();
                }
                avg = avg/30f;
                entries.add(new Entry(index + 0.5f, avg));
            }*/
        }
        LineDataSet set = new LineDataSet(entries, "Line DataSet");
        set.setColor(Color.rgb(255, 0, 0));
        set.setDrawCircles(false);
        set.setLineWidth(1f);
        set.setDrawFilled(false);

    /*    if (Utils.getSDKInt() >= 18) {
            // fill drawable only supported on api level 18 and above
            Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.fade_gray);
            set.setFillDrawable(drawable);
        }
        else {*/
            set.setFillColor(Color.argb(77,18, 196, 0));
        //}
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(false);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        ArrayList<Entry> entries2 = new ArrayList<>();

        for (int index2 = 0; index2 < trackedPoints.size(); index2++) {
            if(index2<smoothingFactor) {
                entries2.add(new Entry(index2 + 0.5f, (float) trackedPoints.get(index2).getPressureAltitude()));
            }else{
                float avg= 0;
                for(int j=0; j<smoothingFactor; j++){
                    avg = avg+trackedPoints.get(index2-j).getPressureAltitude();
                }
                avg = avg/smoothingFactor;
                entries2.add(new Entry(index2 + 0.5f, avg));
            }
        }
        LineDataSet set2 = new LineDataSet(entries2, "Line DataSet");
        set2.setColor(Color.rgb(0, 0, 0));
        set2.setDrawCircles(false);
        set2.setLineWidth(2.5f);
        set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set2.setDrawValues(true);
        set2.setDrawFilled(true);
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);
        set2.setFillColor(Color.argb(77,18, 196, 0));



        d.addDataSet(set2);
        d.addDataSet(set);

        return d;
    }





    private void updateLine() throws JSONException {

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
        //TODO DECIDE IF WE WANT DISTANCE TEXT HERE
        mapDistanceText.setText(getMiles(total));


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
    public void onMapReady(@NonNull final MapboxMap mBoxMap) {
        RunDetailFragment.this.mapboxMap = mBoxMap;

        mapboxMap.getUiSettings().setAllGesturesEnabled(false);

        // mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/op27no2/ck2tujbra2ox21cqzxh4ql48y"),new Style.OnStyleLoaded() {
        //  mapboxMap.setStyle(Style.OUTDOORS, new Style.OnStyleLoaded() {
        mBoxMap.setStyle(new Style.Builder().fromUri("asset://mystyle.json"), new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                enableLocationComponent(style);
                mStyle = style;

                //snapshotter
                try {
                    updateLine();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                UiSettings uiSettings = mapboxMap.getUiSettings();
                uiSettings.setCompassEnabled(false);

                //listing layers
                List<Layer> mlayers = style.getLayers();
                for (int i = 0; i < mlayers.size(); i++) {
                 //   System.out.println("layer: " + mlayers.get(i).getId() + ", minzoom: " + mlayers.get(i).getMinZoom() + ", maxzoom: " + mlayers.get(i).getMaxZoom());
                    //mlayers.get(i).setProperties(PropertyFactory.fillColor(Color.parseColor("#000000")));
                    String hey = mlayers.get(i).getId();
                    if (hey.equals("road-path-smooth")) {
                        System.out.println("what");
                        mlayers.get(i).setProperties(PropertyFactory.lineColor(parseColor("#000000")));
                    }
                }

            //adds all frozen trails
             addTrails();

            }
        });

        mBoxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
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

        mBoxMap.addOnMoveListener(new MapboxMap.OnMoveListener() {
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
 /*       if(prefs.getBoolean("service_running", false) == true){
            bindTimerService();
        }*/

        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        /*if(prefs.getBoolean("service_running", false) == true){
            unbindTimerService();
        }*/
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
/*

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
    public void getData(long timestamp, long elapsedTime, ArrayList<Point> rCoordinates, double min, double max, ArrayList<TrackedPoint> mPoints) {

        System.out.println("elapsedTime:" + elapsedTime);
        finalTime = elapsedTime;
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

        *//*float total=0f;
        for(int i=0; i<rCoordinates.size()-1;i++) {
            float[] distance = new float[2];
            Location.distanceBetween(rCoordinates.get(i).latitude(), rCoordinates.get(i).longitude(), rCoordinates.get(i+1).latitude(), rCoordinates.get(i+1).longitude(), distance);
            total = total+distance[0];
        }*//*

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


    }*/

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
                            lineColor(ColorUtils.colorToRgbaString(parseColor("#3bb2d0"))),
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
        double zoom = mapboxMap.getCameraPosition().zoom;
        double offset = 0.0001;
        if(zoom < 12){
            offset = 0.0005;
        }
        if(zoom < 10){
            offset = 0.001;
        }
        if(zoom < 8){
            offset = 0.005;
        }
        if(zoom <4){
            offset = 0.01;
        }


        ArrayList<Double[]> mArray = new ArrayList<Double[]>();
        Double[] mBox = new Double[2];
        mBox[0] = mPoint.longitude() + offset;
        mBox[1] = mPoint.latitude() + offset;
        mArray.add(mBox);
        Double[] mBox2 = new Double[2];
        mBox2[0] = mPoint.longitude() + offset;
        mBox2[1] = mPoint.latitude() - offset;
        mArray.add(mBox2);
        Double[] mBox3 = new Double[2];
        mBox3[0] = mPoint.longitude() - offset;
        mBox3[1] = mPoint.latitude() - offset;
        mArray.add(mBox3);
        Double[] mBox4 = new Double[2];
        mBox4[0] = mPoint.longitude() - offset;
        mBox4[1] = mPoint.latitude() + offset;
        mArray.add(mBox4);
        Double[] mBox5 = new Double[2];
        mBox5[0] = mPoint.longitude() + offset;
        mBox5[1] = mPoint.latitude() + offset;
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

        if (trackedPoints.size() != 0 && trackedPoints != null) {

            //get min alt
            minalt = 1000000;
            for (int i = 0; i < trackedPoints.size(); i++) {
                float alt = trackedPoints.get(i).getPressureAltitude();
                if(alt < minalt) {
                    if(alt >0) {
                        minalt = alt;
                    }
                }
            }

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
                        //double height = (routeCoordinates.get(i).altitude() - minalt) * 50;
                        float mAlt=0;
                        double mPace=0;
                        if(i<=smoothingFactor){
                            mAlt = trackedPoints.get(i).getPressureAltitude();
                        }else{
                            for(int j=0;j<smoothingFactor;j++) {
                                mAlt = mAlt+trackedPoints.get(i - j).getPressureAltitude();
                            }
                            mAlt=mAlt/smoothingFactor;
                        }

                        if(i>smoothingFactor){
                        mPace = ((trackedPoints.get(i).getDistance()-trackedPoints.get(i-smoothingFactor).getDistance())/((trackedPoints.get(i).getTimestamp()-trackedPoints.get(i-smoothingFactor).getTimestamp())/1000));
                        }

                        double factor = ((((22-mapboxMap.getCameraPosition().zoom)*(22-mapboxMap.getCameraPosition().zoom)*(22-mapboxMap.getCameraPosition().zoom)*30)/22)*0.06);
                        double height = (mAlt - minalt) * factor;
                        JsonObject heightObject = new JsonObject();
                        heightObject.addProperty("height", height);
                        heightObject.addProperty("hr", trackedPoints.get(i).getHeartRate());
                        heightObject.addProperty("pace", mPace);
                        System.out.println("coord values: " + routeCoordinates.get(i).altitude());
                        System.out.println("height values: " + mAlt);


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
                        fillExtrusionColor(interpolate(
                                linear(), get("hr"),
                                stop(0, rgb(18,196,0)),
                                stop(130, rgb(252,251,87)),
                                stop(150, rgb(255,186,33)),
                                stop(170, rgb(255,0,0)),
                                stop(195, rgb(50,0,0))
                        )),
                        fillExtrusionOpacity(0.5f),
                        fillExtrusionHeight(get("height"))
         /*
                        fillExtrusionColor(step((get("hr")), rgb(0,0,0),
                                stop(0, rgb(18,196,0)),
                                stop(130, rgb(252,251,87)),
                                stop(150, rgb(255,186,33)),
                                stop(170, rgb(255,0,0))
                        )),
                        fillExtrusionOpacity(0.5f),
                        fillExtrusionHeight(get("height"))*/
                ));


/*            if (mStyle.getLayer("course2") == null) {
                mStyle.addLayer(new FillExtrusionLayer("course2", "extrusion").withProperties(
                        fillExtrusionColor(step((get("hr")), rgb(0,0,0),
                                stop(0, rgb(18,196,0)),
                                stop(130, rgb(252,251,87)),
                                stop(150, rgb(255,186,33)),
                                stop(170, rgb(255,0,0))
                        )),
                        fillExtrusionOpacity(1f),
                        fillExtrusionBase(get("height")),
                        fillExtrusionHeight(Expression.sum(literal(10), get("height")))
                ));
            }*/



            }
        }

    }

    private void clearExtrusion(){
        mStyle.removeLayer("course");
        //mStyle.removeLayer("course2");
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
                            lineColor(parseColor("#c919ff")));

                    mStyle.addLayer(mlayer);
                }

            }
        }

    }


/*
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
        long pace = (long) (finalTime/(total[0] *0.000621371192f));

        mEditDuration.setText(getDuration(finalTime));
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
                System.out.println("save Cals = "+ave);
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
    }*/


    private void setDialogText(RunType activity, float[] total){

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
            float kilometers = total[0] / 1000f;
            float five = total[0] / 500f;
            float one = total[0]/100f;

            //will downfactor calculations in pounds to kg
            double factorUnit = prefs.getInt("units",0) == 0 ? 1 : (1/2.2);
            double factorCal = activity.getCalBurnValue();
            int weight = prefs.getInt("weight",180);

            int calValue = 0;
            switch (activity.getCalBurnUnit()) {
                case 0:
                    //Per Minute
                    calValue = (int) Math.floor((finalTime/1000f/60f)*weight*factorUnit*factorCal);
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
                    calValue = (int) Math.floor((finalTime/1000f/60f/60f)*weight*factorUnit*factorCal);
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
        //TODO MAKE SURE WE ARE GETTING ALL THESE LIKE RUN IS
        saveDate = mText4.getText().toString();
       // saveTime = getIntFromDuration(mEditDuration.getText().toString());
        saveTime = 1000*((Integer.parseInt(mEditTime1.getText().toString())*60*60)+(Integer.parseInt(mEditTime2.getText().toString())*60)+(Integer.parseInt(mEditTime3.getText().toString())));
        //saveDistance = (int) Math.floor(Float.parseFloat(mEditDistance.getText().toString()) * 1609.34400);
        saveDistance = total[0];

        saveCals = Integer.parseInt(mEditCals.getText().toString());
        saveDescription = mEditDescription.getText().toString();
        saveTitle = mEditTitle.getText().toString();

        mRunWorkout.setWorkoutDate(saveDate);
        mRunWorkout.setDuration(saveTime);
        mRunWorkout.setDistance((int) saveDistance);
        mRunWorkout.setCalories(saveCals);
        mRunWorkout.setDescription(saveDescription);
        mRunWorkout.setTitle(saveTitle);
        mRunWorkout.setWorkoutType(saveType);

        int cals = mNutritionDay.getValues().get(0);
        ArrayList<Integer> mvals = mNutritionDay.getValues();
        cals = cals - (saveCals-initialCals);

        if((saveCals-initialCals)>0) {
            Toast.makeText(getActivity(), formattedDate + " Calories Updated: +" + (saveCals - initialCals), Toast.LENGTH_LONG).show();
        }
        if((saveCals-initialCals)<0) {
            Toast.makeText(getActivity(), formattedDate + " Calories Updated: -" + (saveCals - initialCals), Toast.LENGTH_LONG).show();
        }
        initialCals = saveCals;
        mvals.set(0, cals);
        mNutritionDay.setValues(mvals);
        mRepository.updateNutrition(mNutritionDay);

        if(hasMap) {
            String bmp = saveBitmap(bMap, UUID.randomUUID().toString());
            System.out.println("bmp string: " + bmp);
            String coords = saveCoordinates(routeCoordinates, UUID.randomUUID().toString());
            mRunWorkout.setImage(bmp);
            mRunWorkout.setCoordinates(coords);
        }

        mRepository.updateRunWorkout(mRunWorkout);
        Toast.makeText(getActivity(), "Workout Updated", Toast.LENGTH_LONG).show();
        updateWidgets();
    }




/*
    private void startSnapShot() {
        MapboxMap.SnapshotReadyCallback snapCallback = new MapboxMap.SnapshotReadyCallback() {
            @Override
            public void onSnapshotReady(@NonNull Bitmap snapshot) {
                saveRun(snapshot);
            }
        };
        mapboxMap.snapshot(snapCallback);
    }
*/

    private void startSnapShot() {
        if(hasMap) {
            Style.Builder builder1 = new Style.Builder()
                    .fromUri(mapboxMap.getStyle().getUri())
                    .withSource(new GeoJsonSource("line-source",
                            FeatureCollection.fromFeatures(new Feature[]{Feature.fromGeometry(
                                    LineString.fromLngLats(routeCoordinates)
                            )})))
                    .withLayer(new LineLayer("linelayer", "line-source").withProperties(
                            lineWidth(5f),
                            lineColor(parseColor("#e55e5e"))
                    ));

            MapSnapshotter.Options snapShotOptions = new MapSnapshotter.Options(500, 500)
                    .withCameraPosition(mapboxMap.getCameraPosition())
                    .withRegion(mapboxMap.getProjection().getVisibleRegion().latLngBounds)
                    .withStyleBuilder(builder1);

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
        }else{
            //does not have Map associated
            saveRun(null);
        }



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
            /*Gson gson = new Gson();
            String json = gson.toJson(coords);
            edt.putString(uid, json);
            edt.commit();*/
        TrackedPoints savePoints = new TrackedPoints(trackedPoints);
        for(int i=0;i<routeCoordinates.size();i++){
            if(savePoints.getTrackedPoints().size() > i){
                savePoints.getTrackedPoints().get(i).setPoint(routeCoordinates.get(i));
            }else{
                savePoints.getTrackedPoints().add(new TrackedPoint(0,routeCoordinates.get(i),0,0,0));
            }
        }
        long id = mRepository.insertTrackedPoints(savePoints);
        String mID = Long.toString(id);
        return mID;
    }

    public ArrayList<Point> getCoordinates(String uid){
    //    String json = prefs.getString(uid,"");
     //   System.out.println("RETRIEVE COORDS JSON: " + json);
      //  System.out.println("RETRIEVE COORDS JSON: " + json);

   /*     Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Point>>() {}.getType();
        ArrayList<Point> coords = gson.fromJson(json, type);
        if(coords != null) {
            System.out.println("RETRIEVE COORDS SIZE: " + coords.size());
        }else{
            System.out.println("RETRIEVE COORDS NULL: ");
        }*/
        TrackedPoints testPoints = null;
        ArrayList<Point> coords = new ArrayList<Point>();
        if(uid!=null) {
            testPoints = mRepository.getTrackedPoints(uid);
        }
        if(testPoints !=null) {
            for (int i = 0; i < testPoints.getTrackedPoints().size(); i++) {
                coords.add(testPoints.getTrackedPoints().get(i).getPoint());
            }
            trackedPoints = testPoints.getTrackedPoints();
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
                    Calendar mcal = Calendar.getInstance();
                    mNutritionDay.setDateMillis(mcal.getTimeInMillis());

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
                finishUI(mRunWorkout);

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
                formattedDate = mRunWorkout.getWorkoutDate();
                mActivityTypes = new ArrayList<RunType>(AppDatabase.getAppDatabase(getActivity()).rtDAO().getAllActive());
                hasMap = mRunWorkout.getSaveMap();

                System.out.println("check run exec1");


                return null;
            }

            protected void onPostExecute(Void unused) {
                // Post Code
                getNutritionDayData();


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




    private void finishUI(RunWorkout mRunWorkout){
        if(mRunWorkout != null) {
            Integer duration = mRunWorkout.getDuration();
            Integer distance = mRunWorkout.getDistance();
            routeCoordinates = getCoordinates(mRunWorkout.getCoordinates());

            long pace = (long) (duration / (distance * 0.000621371192f));

            RunType mActivityType = mRunWorkout.getWorkoutType();
            total[0] = distance;

            setDialogText(mActivityType,total);
            setPaceAndCalText(mActivityType, total);
            finalTime = duration;
            System.out.println("FINAL base duration: "+duration);
            System.out.println("FINAL TIME: "+finalTime);

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


            mEditTitle.setText(mRunWorkout.getTitle());
            mText4.setText(mRunWorkout.getWorkoutDate());
            initialCals = mRunWorkout.getCalories();
            mEditDescription.setText(mRunWorkout.getDescription());
            ArrayAdapter<RunType> adapter =
                    new ArrayAdapter<RunType>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, mActivityTypes);
            adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
            mSpinner.setAdapter(adapter);
            RunType mType = mRunWorkout.getWorkoutType();

            saveType = mActivityTypes.get(mSpinner.getSelectedItemPosition());
            setDialogText(mActivityTypes.get(prefs.getInt("default_activity",0)), total);
            setPaceAndCalText(mActivityTypes.get(prefs.getInt("default_activity",0)),total);


            final int[] check = {0};
            mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    if(++check[0] > 1) {
                        setDialogText(mActivityTypes.get(position), total);
                        setPaceAndCalText(mActivityTypes.get(position), total);
                        System.out.println("is this called spinner");
                        saveType = mActivityTypes.get(position);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {

                }

            });
            boolean flag = true;
            for(int i=0; i<mActivityTypes.size(); i++){
                if(mActivityTypes.get(i).getName().equals(mType.getName())){
                    mSpinner.setSelection(i);
                    flag = false;
                }
            }
            if(flag){
                mSpinner.setSelection(0);
            }

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

            if(hasMap) {
                mapView.getMapAsync(this);
                mCardView.setVisibility(View.VISIBLE);
            }

            //Still set calls after pace and caltext code, as user-set cals should take precedence, but setPaceandCal gets called on edit so will change if they change distance etc
            System.out.println("is this called timing test");
            mEditCals.setText(Integer.toString(mRunWorkout.getCalories()));

            //TODO do I still need this, now that getMapAsync is moved to just above isntead of at start of code
    /*        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        updateLine();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        System.out.println("line update error");
                    }
                }
            }, 300);*/

            if(trackedPoints != null) {
                //CHART STUFF
                CombinedData data = new CombinedData();
                data.setData(generateHeartRateLineData());
                data.setData(generateHeartRateBarData());
                XAxis xAxis = heartRateCombinedChart.getXAxis();
                xAxis.setAxisMaximum(data.getXMax() + 0.25f);
                heartRateCombinedChart.animateY(1000, Easing.EaseInOutQuad);
                heartRateCombinedChart.setData(data);
                heartRateCombinedChart.invalidate();

                // add data
                paceChart.setData(generatePaceData());
                paceChart.invalidate();

                //splits data
                splitsChart.setData(generateSplitsData());
                splitsChart.getAxisLeft().setValueFormatter(new MyFormatterYAxisSplitsBar());
                splitsChart.invalidate();

                // add data
                elevationChart.setData(generateElevationData());
                YAxis mleftAxis2 = elevationChart.getAxisLeft();
                mleftAxis2.setAxisMaximum(elevationChart.getData().getYMax() + 10f);
                mleftAxis2.setAxisMinimum(elevationChart.getData().getYMin() - 10f);
                elevationChart.invalidate();

                heartRatePieChart.setData(generatePieData());
                pacePieChart.setData(generatePacePieData());
            }

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


    public void uploadToStrava(){


        if(!prefs.getBoolean("strava9", false)) {
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

                saveTime = 1000*((Integer.parseInt(mEditTime1.getText().toString())*60*60)+(Integer.parseInt(mEditTime2.getText().toString())*60)+(Integer.parseInt(mEditTime3.getText().toString())));
                saveDistance = total[0];
                saveCals = Integer.parseInt(mEditCals.getText().toString());
                saveDescription = mEditDescription.getText().toString();
                saveTitle = mEditTitle.getText().toString();

                if (ActivityType.fromString(saveType.getName()) != null && (trackedPoints == null || trackedPoints.size()==0)){
                    ActivityAPI activityAPI = new ActivityAPI(sconfig);
                    Activity activity = activityAPI.createActivity(saveTitle)
                            .ofType(ActivityType.fromString(saveType.getName()))
                            .startingOn(Calendar.getInstance().getTime())
                            .withElapsedTime(Time.seconds(saveTime/1000))
                            .withDescription(saveDescription)
                            .withDistance(Distance.meters(saveDistance))
                            .isPrivate(false)
                            .withTrainer(false)
                            .withCommute(false)
                            .execute();
                } else if(trackedPoints == null || trackedPoints.size()==0) {
                    ActivityAPI activityAPI = new ActivityAPI(sconfig);
                    Activity activity = activityAPI.createActivity(saveTitle)
                            .ofType(ActivityType.WORKOUT)
                            .startingOn(Calendar.getInstance().getTime())
                            .withElapsedTime(Time.seconds(saveTime/1000))
                            .withDescription(saveDescription)
                            .withDistance(Distance.meters(saveDistance))
                            .isPrivate(false)
                            .withTrainer(false)
                            .withCommute(false)
                            .execute();

                }else{
                    TcxHelper mHelper = new TcxHelper();
                    String mPath = mHelper.createTCX("testfile" + System.currentTimeMillis(), trackedPoints, (int) saveDistance, saveCals, saveTime);

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

                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Strava Upload Status: " + uploadStatus.getStatus(), Toast.LENGTH_LONG).show();
                        }
                    }, 15000);
                }

                return null;
            }

            protected void onPostExecute(Void unused) {
            }

        }.execute();



    }

}

