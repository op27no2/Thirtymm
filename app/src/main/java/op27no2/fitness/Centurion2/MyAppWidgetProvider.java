package op27no2.fitness.Centurion2;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioAttributes;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.TypedValue;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import op27no2.fitness.Centurion2.Database.AppDatabase;
import op27no2.fitness.Centurion2.Database.Repository;
import op27no2.fitness.Centurion2.fragments.activities.GoalsDetail;
import op27no2.fitness.Centurion2.fragments.nutrition.NutritionDay;

public class MyAppWidgetProvider extends AppWidgetProvider {
    private Repository mRepository;
    NutritionDay mDay;
    private SharedPreferences prefs;
    private SharedPreferences.Editor edt;
    private Integer cals;
    private Integer protein;
    private ArrayList<String> mNames = new ArrayList<String>(2);
    private ArrayList<Integer> mValues = new ArrayList<Integer>(2);
    private Vibrator rabbit;
    private ArrayList<GoalsDetail> mGoalTopList = new  ArrayList<GoalsDetail>(2);
    private boolean holding = true;


    @SuppressLint("StaticFieldLeak")
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        prefs = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        edt = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();


        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appwidget);
            ComponentName watchWidget = new ComponentName(context, MyAppWidgetProvider.class);


            views.setOnClickPendingIntent(R.id.button_plus, buildButtonPendingIntent(context, appWidgetId, "PLUS_BUTTON"));
            views.setOnClickPendingIntent(R.id.button_minus, buildButtonPendingIntent(context, appWidgetId, "MINUS_BUTTON"));
            views.setOnClickPendingIntent(R.id.button_plus_small, buildButtonPendingIntent(context, appWidgetId, "PLUS_BUTTON_SMALL"));
            views.setOnClickPendingIntent(R.id.button_minus_small, buildButtonPendingIntent(context, appWidgetId, "MINUS_BUTTON_SMALL"));
            views.setOnClickPendingIntent(R.id.top, buildButtonPendingIntent(context, appWidgetId, "HOME_BUTTON"));
            mRepository = new Repository(context);

            System.out.println("test configure widget id: "+Integer.toString(appWidgetIds[i]));
            System.out.println("test configure widget id value: "+prefs.getString(Integer.toString(appWidgetIds[i]),""));




            Long time = Calendar.getInstance().getTimeInMillis();
            SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d, ''yy");
            String date = df.format(time);

            new AsyncTask<Void, Void, Void>() {
                protected void onPreExecute() {
                    // Pre Code
                }
                protected Void doInBackground(Void... unused) {
                    mDay = AppDatabase.getAppDatabase(context).ntDAO().findByDate(date);

                    if(mDay == null){
                        mDay = new NutritionDay();
                        System.out.println("nutrition day null create uid: "+mDay.getUid());
                        mNames.add("Cals");
                        mValues.add(-(prefs.getInt("bmr", 2000)));
                        mNames.add("Protein");
                        mValues.add(0);
                        mDay.setNames(mNames);
                        mDay.setValues(mValues);
                        mDay.setDate(date);
                       // mRepository.insertNutrition(mDay);

                       long id = AppDatabase.getAppDatabase(context).ntDAO().insert(mDay);
                       mDay = AppDatabase.getAppDatabase(context).ntDAO().findById((int) id);
                       // System.out.println("nutrition day after insertion uid: "+mDay.getUid());

                    }else{
                        mNames = mDay.getNames();
                        mValues = mDay.getValues();

                        if(mNames.size() == 0 || mValues.size() == 0){
                            System.out.println("nutrition day found get names/values uid: "+mDay.getUid());
                            mNames.add("Cals");
                            mValues.add(-(prefs.getInt("bmr", 2000)));
                            mNames.add("Protein");
                            mValues.add(0);
                            mDay.setNames(mNames);
                            mDay.setValues(mValues);
                        }else {
                            System.out.println("mNames size not zero "+mNames.get(0)+" "+mValues.get(0));
                        }

                    }



                    return null;
                }
                protected void onPostExecute(Void unused) {
                finishUI(views, appWidgetManager, watchWidget, appWidgetId);
                }
            }.execute();



            // Tell the AppWidgetManager to perform an update on the current app widget
      //      appWidgetManager.updateAppWidget(watchWidget, views);
        }
    }

    public void finishUI(RemoteViews views, AppWidgetManager mgr,ComponentName watchWidget, int appWidgetId){
        if(mDay.getValues() != null) {
            cals = mDay.getValues().get(0);
            protein = mDay.getValues().get(1);

            if(prefs.getString(Integer.toString(appWidgetId),"").equals("Protein")) {
                System.out.println("protein settext"+protein);
                views.setTextViewText(R.id.top, "Protein");
                views.setTextViewText(R.id.text1,Integer.toString(protein));
                views.setTextColor(R.id.text1, Color.BLUE);
            }
            if(prefs.getString(Integer.toString(appWidgetId),"").equals("Calories")) {
                System.out.println("cal settext"+cals);

                views.setTextViewText(R.id.top, "Calories");
                views.setTextViewText(R.id.text1,Integer.toString(cals));
                if(cals<0){
                    views.setTextColor(R.id.text1, Color.RED);
                }else{
                    views.setTextColor(R.id.text1, Color.GREEN);
                }
            }
        }
        //FIRST ONE WITH APP COMPONENT DIDN"T SEEM TO WORK, SWITCHED TO THIS SEEMS TO BE WORKING, Can get rid of Watch Widget
     //   mgr.updateAppWidget(watchWidget, views);
        mgr.updateAppWidget(appWidgetId, views);

    }


    @SuppressLint("StaticFieldLeak")
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        rabbit = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        //TODO test this code
    if (intent.getAction().equals("HOME_BUTTON")) {
        //REMOVING HOME BUTTON FOR NOW
     /*   Intent i = new Intent(context, MainActivity.class);
        i.putExtra("frgToLoad", "Nutrition");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        vib(40);*/

    }else if(intent.getAction().equals("PLUS_BUTTON") || intent.getAction().equals("MINUS_BUTTON") || intent.getAction().equals("PLUS_BUTTON_SMALL")|| intent.getAction().equals("MINUS_BUTTON_SMALL")){
        prefs = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        edt = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
        vib(30);

        System.out.println("onreceive called");
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.appwidget);
        ComponentName watchWidget = new ComponentName(context, MyAppWidgetProvider.class);
        mRepository = new Repository(context);

        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        // int viewIndex = intent.getIntExtra(EXTRA_ITEM, 0);


        System.out.println("widget pressed "+appWidgetId);


        Long time = Calendar.getInstance().getTimeInMillis();
        SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d, ''yy");
        String date = df.format(time);
        System.out.println("widget date: "+date);

        new AsyncTask<Void, Void, Void>() {
                protected void onPreExecute() {
                    // Pre Code
                }
                protected Void doInBackground(Void... unused) {

                        mDay = AppDatabase.getAppDatabase(context).ntDAO().findByDate(date);
                        mGoalTopList = new ArrayList<GoalsDetail>(AppDatabase.getAppDatabase(context).glDAO().getAll());

                        Calendar cal = Calendar.getInstance();

                        //   if(mDay == null || mDay.getValues() == null || mDay.getValues().size() == 0){
                        if (mDay == null) {
                            mDay = new NutritionDay();
                            System.out.println("day null should create");
                            mNames.clear();
                            mValues.clear();
                            mNames.add("Cals");
                            mValues.add(-(prefs.getInt("bmr", 2000)));
                            mNames.add("Protein");
                            mValues.add(0);
                            mDay.setNames(mNames);
                            mDay.setValues(mValues);
                            mDay.setDate(date);
                            mDay.setDateMillis(cal.getTimeInMillis());
                            //mRepository.insertNutrition(mDay);
                            mDay.setGoalList(mGoalTopList);

                            long id = AppDatabase.getAppDatabase(context).ntDAO().insert(mDay);
                            mDay = AppDatabase.getAppDatabase(context).ntDAO().findById((int) id);

                            System.out.println("creating goals");


                        } else {
                            mNames = mDay.getNames();
                            mValues = mDay.getValues();

                            if (mNames.size() == 0 || mValues.size() == 0) {
                                System.out.println("nutrition day found get names/values uid: " + mDay.getUid());
                                mNames.clear();
                                mValues.clear();
                                mNames.add("Cals");
                                mValues.add(-(prefs.getInt("bmr", 2000)));
                                mNames.add("Protein");
                                mValues.add(0);
                                mDay.setNames(mNames);
                                mDay.setValues(mValues);
                                System.out.println(mNames.get(0) + " " + mValues.get(1));
                            } else {
                                System.out.println("mNames size not zero " + mNames.get(0) + " " + mValues.get(0));
                            }

                            if (mDay.getGoalList() == null || mDay.getGoalList().size() == 0) {
                                System.out.println("need to create goals if its today");
                                mDay.setGoalList(mGoalTopList);
                            }
                            mRepository.updateNutrition(mDay);
                        }

                        //TODO change to search for index
                        cals = mValues.get(0);
                        protein = mValues.get(1);


                    return null;
                }
                protected void onPostExecute(Void unused) {
                    if(mDay != null && cals !=null) {
                    if(prefs.getString(Integer.toString(appWidgetId),"").equals("Calories")) {
                        System.out.println("cal buttons"+cals);

                        if (intent.getAction().equals("PLUS_BUTTON")) {
                            cals = cals + 100;
                        }
                        if (intent.getAction().equals("MINUS_BUTTON")) {
                            cals = cals - 100;
                        }
                        if (intent.getAction().equals("PLUS_BUTTON_SMALL")) {
                            cals = cals + 10;
                        }
                        if (intent.getAction().equals("MINUS_BUTTON_SMALL")) {
                            cals = cals - 10;
                        }
                        mValues.set(0, cals);
                        mDay.setValues(mValues);
                    }
                    if(prefs.getString(Integer.toString(appWidgetId),"").equals("Protein")) {
                        System.out.println("protein buttons"+protein);

                        if (intent.getAction().equals("PLUS_BUTTON")) {
                            protein = protein + 10;
                        }
                        if (intent.getAction().equals("MINUS_BUTTON")) {
                            protein = protein - 10;
                        }
                        if (intent.getAction().equals("PLUS_BUTTON_SMALL")) {
                            protein = protein + 1;
                        }
                        if (intent.getAction().equals("MINUS_BUTTON_SMALL")) {
                            protein = protein - 1;
                        }
                        mValues.set(1, protein);
                        mDay.setValues(mValues);
                    }


                    finishUI(remoteViews, mgr, watchWidget, appWidgetId);

                    mRepository.updateNutrition(mDay);


                    }
                }
            }.execute();

        }
    }








    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appwidget);

        Toast.makeText(context,"updateAppWidget",Toast.LENGTH_LONG).show();
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }





    //send actions PLUS_BUTTON, MINUS_BUTTON,
    public static PendingIntent buildButtonPendingIntent(Context context, int appWidgetId, String action) {
        Intent intent = new Intent();
        intent.setClass(context, MyAppWidgetProvider.class);
        intent.setAction(action);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                appWidgetId);
        return PendingIntent.getBroadcast(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }



    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions){

        super.onAppWidgetOptionsChanged(context,appWidgetManager,appWidgetId,newOptions);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            int maxWidth = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH);
            int maxHeight = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT);

            int size = Math.min(maxHeight/2,maxWidth/2);

            int pxSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,size,context.getResources().getDisplayMetrics());

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.appwidget);
            float newSize = refitText("2000",pxSize);

            remoteViews.setTextViewTextSize(R.id.text1, TypedValue.COMPLEX_UNIT_PX,newSize);

            appWidgetManager.updateAppWidget(appWidgetId,remoteViews);
        }

    }

    private float refitText(String text, int textWidth)
    {
        if (textWidth <= 0)
            return 0;

        float hi = 1000;
        float lo = 2;
        final float threshold = 0.5f; // How close we have to be

        Paint testPaint = new Paint();

        while((hi - lo) > threshold) {
            float size = (hi+lo)/2;
            testPaint.setTextSize(size);
            if(testPaint.measureText(text) >= textWidth)
                hi = size; // too big
            else
                lo = size; // too small
        }
        // Use lo so that we undershoot rather than overshoot
        return lo;
    }

    private void vib(int duration){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();
            VibrationEffect ve = null;
            ve = VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE);
            rabbit.vibrate(ve, audioAttributes);
        }else{
            rabbit.vibrate(duration);
        }
    }


}