package op27no2.fitness.thirtymm.ui.nutrition;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import op27no2.fitness.thirtymm.Database.AppDatabase;
import op27no2.fitness.thirtymm.Database.Repository;
import op27no2.fitness.thirtymm.Graphing.GraphView;
import op27no2.fitness.thirtymm.Graphing.GraphViewSeries;
import op27no2.fitness.thirtymm.Graphing.GraphViewStyle;
import op27no2.fitness.thirtymm.Graphing.LineGraphView;
import op27no2.fitness.thirtymm.MyAppWidgetProvider;
import op27no2.fitness.thirtymm.R;
import op27no2.fitness.thirtymm.RecyclerItemClickListener;
import op27no2.fitness.thirtymm.ui.DialogCalendar;
import op27no2.fitness.thirtymm.ui.lifting.DialogLifts;
import op27no2.fitness.thirtymm.ui.lifting.LiftingWorkout;
import op27no2.fitness.thirtymm.ui.lifting.MyDialogInterface;
import op27no2.fitness.thirtymm.ui.volume.VolumeAdapter;

import static java.lang.Integer.max;

public class NutritionFragment extends Fragment implements CalendarDialogInterface{
    private SharedPreferences prefs;
    private SharedPreferences.Editor edt;

    private String formattedDate;
    private Vibrator rabbit;
    private TextView dateText;
    private Calendar cal;
    private Repository mRepository;
    private NutritionDay mNutritionDay;
    private RecyclerView mRecyclerView;
    private TextView dateTitle;
    private LinearLayoutManager mLayoutManager;
    private NutritionAdapter mAdapter;
    private ArrayList<String> mNames = new ArrayList<String>(2);
    private ArrayList<Integer> mValues = new ArrayList<Integer>(2);
    private Integer cals;

    //graphing
    public GraphView.GraphViewData[] data;
    public GraphView.GraphViewData[] data2;
    public GraphViewSeries exampleSeries;
    public GraphViewSeries exampleSeries2;
    public LinearLayout layout;
    public LinearLayout layout2;
    public GraphView graphView;
    public GraphView graphView2;
    public ArrayList<Integer> datalist = new ArrayList<Integer>();
    public ArrayList<NutritionDay> mNutritionDays = new ArrayList<NutritionDay>();
    public SimpleDateFormat df;
    public ArrayList<String> mDates = new ArrayList<String>();

    private CalendarDialogInterface mInterface;
    private int selectedPosition = 0;


    //TODO need to fix for future values

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("StaticFieldLeak")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nutrition, container, false);
        prefs = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        edt = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
        mInterface = (CalendarDialogInterface) this;

        //TODO just setting here to test grpah
        cals=-2200;

        mRepository = new Repository(getActivity());
        rabbit = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        layout = (LinearLayout) view.findViewById(R.id.graph1);
        layout2 = (LinearLayout) view.findViewById(R.id.graph2);

        dateTitle = view.findViewById(R.id.toolbar_date);

        cal = Calendar.getInstance();
        Date c = cal.getTime();
        Long time = Calendar.getInstance().getTimeInMillis();
        System.out.println("Current time => " + time);
       // SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d, ''yy");
        df = new SimpleDateFormat("EEE, MMM d, ''yy");

        formattedDate = df.format(c);
        System.out.println("fomratted time => " + df.format(time));

        mRecyclerView = view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);


        dateText = view.findViewById(R.id.toolbar_date);
        dateText.setText(formattedDate);
        ImageView arrowLeft = (ImageView) view.findViewById(R.id.arrow_left);
        ImageView arrowRight = (ImageView) view.findViewById(R.id.arrow_right);
        arrowLeft.setAlpha(0.8f);
        arrowRight.setAlpha(0.8f);
        arrowLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rabbit.vibrate(25);
                cal.add(Calendar.DATE, -1);
                formattedDate = df.format(cal.getTime());
                getDayData();
            }
        });
        dateTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Dialog dialog = new DialogCalendar(view.getContext(), mInterface, cal);
              dialog.show();
              //mRepository.deleteAllNutrition();

            }
        });
        arrowRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rabbit.vibrate(25);
                cal.add(Calendar.DATE, +1);
                formattedDate = df.format(cal.getTime());
                getDayData();
            }
        });

        ImageView buttonPlus = (ImageView) view.findViewById(R.id.button_plus);
        ImageView buttonMinus = (ImageView) view.findViewById(R.id.button_minus);
        ImageView buttonPlusSmall = (ImageView) view.findViewById(R.id.button_plus_small);
        ImageView buttonMinusSmall = (ImageView) view.findViewById(R.id.button_minus_small);

        buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rabbit.vibrate(25);
      /*          if(cals != null) {
                    cals = cals + 100;
                    mValues.set(0,cals);
                    mNutritionDay.setValues(mValues);
                    mRepository.updateNutrition(mNutritionDay);
                    updateWidgets();
                    mAdapter.notifyDataSetChanged();
                    updateGraphView(formattedDate, cals);

                }*/
                int hold = mValues.get(selectedPosition);
                if(selectedPosition == 0){
                    hold = hold +100;
                }else{
                    hold = hold +10;
                }
                mValues.set(selectedPosition,hold);
                mNutritionDay.setValues(mValues);
                mRepository.updateNutrition(mNutritionDay);
                updateWidgets();
                mAdapter.notifyDataSetChanged();
                updateGraphView(formattedDate, hold, selectedPosition);

            }
        });
        buttonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rabbit.vibrate(25);
               /* if(cals != null) {
                    cals = cals - 100;
                    mValues.set(0,cals);
                    mNutritionDay.setValues(mValues);
                    mRepository.updateNutrition(mNutritionDay);
                    updateWidgets();
                    mAdapter.notifyDataSetChanged();
                    updateGraphView(formattedDate, cals);
                }*/
                int hold = mValues.get(selectedPosition);
                if(selectedPosition == 0){
                    hold = hold -100;
                }else{
                    hold = hold -10;
                }
                mValues.set(selectedPosition,hold);
                mNutritionDay.setValues(mValues);
                mRepository.updateNutrition(mNutritionDay);
                updateWidgets();
                mAdapter.notifyDataSetChanged();
                updateGraphView(formattedDate, hold, selectedPosition);
            }
        });
        buttonPlusSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rabbit.vibrate(25);
      /*          if(cals != null) {
                    cals = cals + 100;
                    mValues.set(0,cals);
                    mNutritionDay.setValues(mValues);
                    mRepository.updateNutrition(mNutritionDay);
                    updateWidgets();
                    mAdapter.notifyDataSetChanged();
                    updateGraphView(formattedDate, cals);

                }*/
                int hold = mValues.get(selectedPosition);
                if(selectedPosition == 0){
                    hold = hold +10;
                }else{
                    hold = hold +1;
                }
                mValues.set(selectedPosition,hold);
                mNutritionDay.setValues(mValues);
                mRepository.updateNutrition(mNutritionDay);
                updateWidgets();
                mAdapter.notifyDataSetChanged();
                updateGraphView(formattedDate, hold, selectedPosition);

            }
        });



        buttonMinusSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rabbit.vibrate(25);
               /* if(cals != null) {
                    cals = cals - 100;
                    mValues.set(0,cals);
                    mNutritionDay.setValues(mValues);
                    mRepository.updateNutrition(mNutritionDay);
                    updateWidgets();
                    mAdapter.notifyDataSetChanged();
                    updateGraphView(formattedDate, cals);
                }*/
                int hold = mValues.get(selectedPosition);
                if(selectedPosition == 0){
                    hold = hold -10;
                }else{
                    hold = hold -1;
                }
                mValues.set(selectedPosition,hold);
                mNutritionDay.setValues(mValues);
                mRepository.updateNutrition(mNutritionDay);
                updateWidgets();
                mAdapter.notifyDataSetChanged();
                updateGraphView(formattedDate, hold, selectedPosition);
            }
        });

        getDayData();

        System.out.println("check1");

        data = new GraphView.GraphViewData[1];
        data2 = new GraphView.GraphViewData[1];
        // set up graph view with dummy data point before populating
        data[0]=  new GraphView.GraphViewData(0, 0);
        data2[0]=  new GraphView.GraphViewData(0, 0);

        GraphViewSeries.GraphViewSeriesStyle style = new GraphViewSeries.GraphViewSeriesStyle(Color.rgb(0, 255, 21),4, true);
        GraphViewSeries.GraphViewSeriesStyle style2 = new GraphViewSeries.GraphViewSeriesStyle(Color.rgb(0, 0, 255),4, false);
        exampleSeries = new GraphViewSeries("",style, data);
        exampleSeries2 = new GraphViewSeries("",style2, data2);

        graphView = new LineGraphView(getActivity(),""); // context, "" // heading);
        graphView.setScrollable(true);
        graphView.setScalable(true);

        graphView2 = new LineGraphView(getActivity(),""); // context, "" // heading);
        graphView2.setScrollable(true);
        graphView2.setScalable(true);

        GraphViewStyle mstyle = graphView.getGraphViewStyle();
        mstyle.setTextSize(50);
        graphView.setGraphViewStyle(mstyle);

        GraphViewStyle mstyle2 = graphView2.getGraphViewStyle();
        mstyle2.setTextSize(50);
        graphView2.setGraphViewStyle(mstyle2);
        System.out.println("check2");

        for(int i=0;i<data.length;i++) {
            System.out.println("data pre-viewport "+i+": " + data[i].valueY);
        }

        if (data.length > 8){
            graphView.setViewPort(data.length-7,6);
            int maxY = getMax(data, data.length-8, data.length-1);
            graphView.setManualYAxisBounds(maxY*1.1,-maxY*1.1);
        }

        else if(data.length == 2){
            graphView.setViewPort(0,1);
            int maxY = getMax(data, 0, data.length-1);
            maxY = 2200;
            graphView.setManualYAxisBounds(maxY*1.1,-maxY*1.1);
        }

        else{
            graphView.setViewPort(0,data.length-1);
            int maxY = getMax(data, 0, data.length-1);
            graphView.setManualYAxisBounds(maxY*1.1,-maxY*1.1);
        }


        //data2
        for(int i=0;i<data2.length;i++) {
            System.out.println("data pre-viewport "+i+": " + data2[i].valueY);
        }

        if (data2.length > 8){
            graphView2.setViewPort(data2.length-7,6);
            int maxY = getMax(data2,data2.length-8, data2.length-1);
            graphView2.setManualYAxisBounds(maxY*1.1,-maxY*1.1);
        }

        else if(data2.length == 2){
            graphView2.setViewPort(0,1);
            int maxY = getMax(data2,0, data.length-1);
           // maxY = 2200;
            graphView2.setManualYAxisBounds(maxY*1.1,-maxY*1.1);
        }

        else{
            graphView2.setViewPort(0,data2.length-1);
            int maxY = getMax(data2,0, data2.length-1);
            graphView2.setManualYAxisBounds(maxY*1.1,-maxY*1.1);
        }

        System.out.println("check3");

        graphView.addSeries(exampleSeries); // data
        graphView2.addSeries(exampleSeries2); // data
        // graphView.addSeries(exampleSeries3);
        graphView.getGraphViewStyle().setNumVerticalLabels(5);
        graphView.getGraphViewStyle().setNumHorizontalLabels(1);
        graphView.getGraphViewStyle().setVerticalLabelsWidth(0);

        graphView2.getGraphViewStyle().setNumVerticalLabels(5);
        graphView2.getGraphViewStyle().setNumHorizontalLabels(1);
        graphView2.getGraphViewStyle().setVerticalLabelsWidth(0);

        ((LineGraphView) graphView).setDrawBackground(true);
        // ((LineGraphView) graphView).setBackgroundColor(Color.argb(55, 0, 255, 21));
        // graphView.getGraphViewStyle().setGridColor(Color.DKGRAY);
        ((LineGraphView) graphView).setDataPointsRadius(4f);

      //  ((LineGraphView) graphView2).setDrawDataPoints(true);
        ((LineGraphView) graphView2).setDrawBackground(false);
        ((LineGraphView) graphView2).setDataPointsRadius(4f);


        layout.addView(graphView);
        layout2.addView(graphView2);

        System.out.println("check4");


        return view;
    }

    public void onResume() {
     //   getDayData();
        super.onResume();

    }


    @SuppressLint("StaticFieldLeak")
    private void getDayData(){
        new AsyncTask<Void, Void, Void>() {
            //Get today's workout => finishUI
            //get today's workout, if it doesn't exist create it

            protected void onPreExecute() {
                // Pre Code
                mValues.clear();
                mNames.clear();
                mDates.clear();
                mNutritionDays.clear();

            }
            protected Void doInBackground(Void... unused) {
                //nutritionDay and formatted day change with selected day up top, used to edit values
                mNutritionDay = AppDatabase.getAppDatabase(getActivity()).ntDAO().findByDate(formattedDate);
                ArrayList<NutritionDay> mmDays = new ArrayList<NutritionDay>();
                ArrayList<String> mmDates = new ArrayList<String>();

                if(mNutritionDay == null){
                    mNutritionDay = new NutritionDay();
                    System.out.println("day null should create");
                    mNames.add("Cals");
                    mValues.add(prefs.getInt("BaseCals", -2000));
                    mNames.add("Protein");
                    mValues.add(0);
                    mNutritionDay.setNames(mNames);
                    mNutritionDay.setValues(mValues);
                    mNutritionDay.setDate(formattedDate);
                    mRepository.insertNutrition(mNutritionDay);
                    cals = mValues.get(0);
                }else{
                    System.out.println("found Day get names/values");
                    mNames = mNutritionDay.getNames();
                    mValues = mNutritionDay.getValues();
                    cals = mValues.get(0);
                }

                //populate a list with all days up to current that shouldn't change like mNutritionDay and formattedDate, this async is called onResume and on date change
                //TODO will this get updated appropriately with databse operations saving edits?
                System.out.println("check exec1");

                Calendar mCalendar = Calendar.getInstance();
                for(int i = 30; i>-1; i--) {
                    mCalendar = Calendar.getInstance();
                    Date c = mCalendar.getTime();
                    String mDate = null;
                    mCalendar.add(Calendar.DATE, -i);
                    mDate = df.format(mCalendar.getTime());
                    System.out.println("list dates: "+mDate);
                    NutritionDay mDay = AppDatabase.getAppDatabase(getActivity()).ntDAO().findByDate(mDate);
                    mmDates.add(mDate);
                    mmDays.add(mDay);
                }

                //if this async runs twice, don't want to be filling arraylist directly, just replace with inline array after done accumulating, so final run replaces
                // with final values

                mNutritionDays = mmDays;
                mDates = mmDates;

                System.out.println("check exec2");

                return null;
            }
            protected void onPostExecute(Void unused) {
                // Post Code
                System.out.println("check postexec");

                finishUI();
                setupGraphView();

            }
        }.execute();

    }

    private void finishUI(){
        System.out.println("check finishui");

        dateText.setText(formattedDate);
        //set data to recyclerview
        mAdapter = new NutritionAdapter(mNames, mValues);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        System.out.println("item clicked: "+position);
                        selectedPosition = position;
                    }
                    @Override
                    public void onItemLongClick(View view, int position) {
                    }
                })
        );
        System.out.println("nutrition days size:" +mNutritionDays.size());

    }

    private void setupGraphView(){
        System.out.println("check setup graph");
        // set up graph view
        data = new GraphView.GraphViewData[mNutritionDays.size()];

        for(int i=0; i<mNutritionDays.size(); i++){
            if(mNutritionDays.get(i) !=null && mNutritionDays.get(i).getValues().size() != 0) {
                data[i] = new GraphView.GraphViewData(i, mNutritionDays.get(i).getValues().get(0));
            }else{
                data[i]=  new GraphView.GraphViewData(i, 0);
            }
        }

        if (data.length > 8){
            graphView.setViewPort(data.length-7,6);
            int maxY = getMax(data,data.length-8, data.length-1);
            graphView.setManualYAxisBounds(maxY*1.1,-maxY*1.1);
        }
        else if(data.length == 2){
            graphView.setViewPort(0,1);
            int maxY = getMax(data,0, data.length-1);
            maxY = 2200;
            graphView.setManualYAxisBounds(maxY*1.1,-maxY*1.1);
        }
        else{
            graphView.setViewPort(0,data.length-1);
            int maxY = getMax(data,0, data.length-1);
            graphView.setManualYAxisBounds(maxY*1.1,-maxY*1.1);
        }
        exampleSeries.resetData(data);


        //graph2
        data2 = new GraphView.GraphViewData[mNutritionDays.size()];

        for(int i=0; i<mNutritionDays.size(); i++){
            if(mNutritionDays.get(i) !=null && mNutritionDays.get(i).getValues().size() != 0) {
                data2[i] = new GraphView.GraphViewData(i, mNutritionDays.get(i).getValues().get(1));
            }else{
                data2[i]=  new GraphView.GraphViewData(i, 0);
            }
        }
        if (data2.length > 8){
            graphView2.setViewPort(data2.length-7,6);
            int maxY = getMax(data2,data2.length-8, data2.length-1);
            graphView2.setManualYAxisBounds(maxY*1.1,-maxY*1.1);
        }
        else if(data2.length == 2){
            graphView2.setViewPort(0,1);
            int maxY = getMax(data2,0, data2.length-1);
            maxY = 2200;
            graphView2.setManualYAxisBounds(maxY*1.1,-maxY*1.1);
        }
        else{
            graphView2.setViewPort(0,data2.length-1);
            int maxY = getMax(data2,0, data2.length-1);
            graphView2.setManualYAxisBounds(maxY*1.1,-maxY*1.1);
        }
        exampleSeries2.resetData(data2);
      /*  GraphViewSeries.GraphViewSeriesStyle style = new GraphViewSeries.GraphViewSeriesStyle(Color.rgb(0, 255, 21),4);
        exampleSeries = new GraphViewSeries("",style, data);
        graphView = new LineGraphView(getActivity(),""); // context, "" // heading);
        graphView.setScrollable(true);
        graphView.setScalable(true);
        GraphViewStyle mstyle = graphView.getGraphViewStyle();
        mstyle.setTextSize(50);
        graphView.setGraphViewStyle(mstyle);

        for(int i=0;i<data.length;i++) {
            System.out.println("data pre-viewport "+i+": " + data[i].valueY);
        }

        if (data.length > 8){
            graphView.setViewPort(data.length-7,6);
            int maxY = getMax(data.length-8, data.length-1);
            graphView.setManualYAxisBounds(maxY*1.1,-maxY*1.1);
        }

        else if(data.length == 2){
            graphView.setViewPort(0,1);
            int maxY = getMax(0, data.length-1);
            maxY = 2200;
            graphView.setManualYAxisBounds(maxY*1.1,-maxY*1.1);
        }

        else{
            graphView.setViewPort(0,data.length-1);
            int maxY = getMax(0, data.length-1);
            graphView.setManualYAxisBounds(maxY*1.1,-maxY*1.1);
        }

        graphView.addSeries(exampleSeries); // data
        // graphView.addSeries(exampleSeries3);
        graphView.getGraphViewStyle().setNumVerticalLabels(5);
        graphView.getGraphViewStyle().setNumHorizontalLabels(1);
        graphView.getGraphViewStyle().setVerticalLabelsWidth(0);
        ((LineGraphView) graphView).setDrawBackground(true);
        // ((LineGraphView) graphView).setBackgroundColor(Color.argb(55, 0, 255, 21));
        // graphView.getGraphViewStyle().setGridColor(Color.DKGRAY);
        ((LineGraphView) graphView).setDrawDataPoints(true);
        ((LineGraphView) graphView).setDataPointsRadius(4f);


        layout.addView(graphView);*/
    }


    private void updateGraphView(String date, Integer calories, int pos) {
        int position = mDates.indexOf(date);
        System.out.println("update data: "+ position+" "+calories+" "+date);
        if(pos == 0) {
            data[position] = new GraphView.GraphViewData(position, calories);
            exampleSeries.resetData(data);
        }
        if(pos ==1){
            data2[position] = new GraphView.GraphViewData(position, calories);
            exampleSeries2.resetData(data2);
        }

        yFit();
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

    //x and y refer to position in the array, checks the max between these indecies
    public int getMax(GraphView.GraphViewData[] data, int x, int y){
        int result = 0;
        for(int i=x; i<y+1; i++){
            if(Math.abs(data[i].getY())>result){
                result = (int) Math.abs(data[i].getY());
            }
        }


     /*   if(data.length >2){
            if( y == data.length-1){
                for (int i = x; i < y; i++)
                    if (Math.abs(data[i].valueY) > result){
                        result = (int) Math.abs(data[i].getY());
                    }
                if (Math.abs(cals) > result){
                    result = Math.abs(cals);
                }
            }
            else if(y != data.length-1){

                for (int i = x; i < y+1; i++){
                    if (Math.abs(data[i].getY()) > result){
                        result = (int) Math.abs(data[i].getY());
                    }
                }
            }

        }
        if(data.length <3){
            result = Math.abs(cals);
        }*/


        return result;
    }


    @Override
    public void onDialogDismiss(String m) {
        System.out.println("received date: "+m);
    }

    public void yFit(){
        int low = (int) Math.floor(graphView.getMinX(false));
        System.out.println("low1 "+low);

        int high = (int) Math.floor(graphView.getMaxX(false));
        System.out.println("high1 "+high);

        int maxY = getMax(data, low, high);
        System.out.println("maxy "+maxY);

        graphView.setManualYAxisBounds(maxY*1.1,-maxY*1.1);
        exampleSeries.resetData(data);

        int low2 = (int) Math.floor(graphView2.getMinX(false));
        System.out.println("low2 "+low2);

        int high2 = (int) Math.floor(graphView2.getMaxX(false));
        System.out.println("high2 "+high2);

        int maxY2 = getMax(data2, low2, high2);
        System.out.println("maxy2 "+maxY2);

        graphView2.setManualYAxisBounds(maxY2*1.1,-maxY2*1.1);
        exampleSeries2.resetData(data2);
    }


}