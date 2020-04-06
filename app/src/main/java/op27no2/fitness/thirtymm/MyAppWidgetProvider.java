package op27no2.fitness.thirtymm;

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
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import op27no2.fitness.thirtymm.Database.AppDatabase;
import op27no2.fitness.thirtymm.Database.Repository;
import op27no2.fitness.thirtymm.ui.lifting.LiftingWorkout;
import op27no2.fitness.thirtymm.ui.nutrition.NutritionDay;

public class MyAppWidgetProvider extends AppWidgetProvider {
    private Repository mRepository;
    NutritionDay mDay;
    private SharedPreferences prefs;
    private SharedPreferences.Editor edt;
    private Integer cals;
    private ArrayList<String> mNames = new ArrayList<String>(2);
    private ArrayList<Integer> mValues = new ArrayList<Integer>(2);

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

            Long time = Calendar.getInstance().getTimeInMillis();
            SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d, ''yy");
            String date = df.format(time);

            new AsyncTask<Void, Void, Void>() {
                protected void onPreExecute() {
                    // Pre Code
                }
                protected Void doInBackground(Void... unused) {
                    mDay = AppDatabase.getAppDatabase(context).ntDAO().findByDate(date);
                    if(mDay == null) {
                        mDay = new NutritionDay();
                        System.out.println("day null should create");
                        mNames.add("Cals");
                        mValues.add(prefs.getInt("BaseCals", -2000));
                        mNames.add("Protein");
                        mValues.add(0);
                        mDay.setNames(mNames);
                        mDay.setValues(mValues);
                        mDay.setDate(date);
                        mRepository.insertNutrition(mDay);
                    }
                    return null;
                }
                protected void onPostExecute(Void unused) {
                finishUI(views, appWidgetManager, watchWidget);
                }
            }.execute();



            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(watchWidget, views);
        }
    }

    public void finishUI(RemoteViews views, AppWidgetManager mgr,ComponentName watchWidget){
        if(mDay.getValues() != null) {
            cals = mDay.getValues().get(0);
            System.out.println("cals is "+cals);
            views.setTextViewText(R.id.text1,Integer.toString(cals));
            if(cals<0){
                views.setTextColor(R.id.text1, Color.RED);
            }else{
                views.setTextColor(R.id.text1, Color.GREEN);
            }
        }
        mgr.updateAppWidget(watchWidget, views);

    }


    @SuppressLint("StaticFieldLeak")
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        //TODO test this code
    if (intent.getAction().equals("HOME_BUTTON")) {
        Intent i = new Intent(context, MainActivity.class);
        i.putExtra("frgToLoad", "Nutrition");
        context.startActivity(i);

    }else{


        prefs = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        edt = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();

        System.out.println("onreceive called");
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.appwidget);
        ComponentName watchWidget = new ComponentName(context, MyAppWidgetProvider.class);
        mRepository = new Repository(context);

            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
           // int viewIndex = intent.getIntExtra(EXTRA_ITEM, 0);
            System.out.println("widget pressed plus");


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

                 //   if(mDay == null || mDay.getValues() == null || mDay.getValues().size() == 0){
                    if(mDay == null){
                        mDay = new NutritionDay();
                        System.out.println("day null should create");
                        mNames.add("Cals");
                        mValues.add(Integer.parseInt(prefs.getString("bmr", "-2000")));
                        mNames.add("Protein");
                        mValues.add(0);
                        mDay.setNames(mNames);
                        mDay.setValues(mValues);
                        mDay.setDate(date);
                        mRepository.insertNutrition(mDay);

                        long id = AppDatabase.getAppDatabase(context).ntDAO().insert(mDay);
                        mDay = AppDatabase.getAppDatabase(context).ntDAO().findById((int) id);


                    }else{
                        mNames = mDay.getNames();
                        mValues = mDay.getValues();
                    }

                    cals = mValues.get(0);



                    return null;
                }
                protected void onPostExecute(Void unused) {
                    if(mDay != null && cals !=null) {
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
                        mValues.set(0,cals);
                        mDay.setValues(mValues);
                        mRepository.updateNutrition(mDay);
                        finishUI(remoteViews, mgr, watchWidget);
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
}