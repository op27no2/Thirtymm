package op27no2.fitness.Centurion2.fragments.nutrition;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.rilixtech.materialfancybutton.MaterialFancyButton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import op27no2.fitness.Centurion2.Database.AppDatabase;
import op27no2.fitness.Centurion2.Database.Repository;
import op27no2.fitness.Centurion2.DialogCalendar;
import op27no2.fitness.Centurion2.Graphing.GraphView;
import op27no2.fitness.Centurion2.Graphing.GraphViewSeries;
import op27no2.fitness.Centurion2.Graphing.GraphViewStyle;
import op27no2.fitness.Centurion2.Graphing.LineGraphView;
import op27no2.fitness.Centurion2.MyAppWidgetProvider;
import op27no2.fitness.Centurion2.R;
import op27no2.fitness.Centurion2.RecyclerItemClickListener;
import op27no2.fitness.Centurion2.fragments.lifting.PickerDialogInterface;

public class NutritionFragment extends Fragment implements CalendarDialogInterface, PickerDialogInterface {
    private SharedPreferences prefs;
    private SharedPreferences.Editor edt;

    private String formattedDate;
    private String todayDate;
    private Vibrator rabbit;
    private TextView dateText;
    private Calendar cal;
    private Repository mRepository;
    private NutritionDay mNutritionDay;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private NutritionAdapter mAdapter;
    private ArrayList<String> mNames = new ArrayList<String>(2);
    private ArrayList<Integer> mValues = new ArrayList<Integer>(2);
    private HashMap<String, Double> mGoalMap = new  HashMap<String, Double>();
    private int mGoalType;
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
    int checkThis = 0;

    private CalendarDialogInterface mInterface;
    private PickerDialogInterface mPickerInterface;
    private int selectedPosition = 0;

    private Boolean flag1True = false;
    private Boolean flag2True = false;
    private Boolean flag3True = false;

    ImageView flag1;
    ImageView flag2;
    ImageView flag3;

    //TODO need to fix for future values

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("StaticFieldLeak")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nutrition, container, false);
        prefs = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        edt = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
        mGoalType = prefs.getInt("goaltype", 0);
        mInterface = (CalendarDialogInterface) this;
        mPickerInterface = this;

        //TODO just setting here to test grpah
        cals=-2200;

        mRepository = new Repository(getActivity());
        rabbit = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        layout = (LinearLayout) view.findViewById(R.id.graph1);
        layout2 = (LinearLayout) view.findViewById(R.id.graph2);

        cal = Calendar.getInstance();
        Date c = cal.getTime();
        Long time = Calendar.getInstance().getTimeInMillis();
        System.out.println("Current time => " + time);
       // SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d, ''yy");
        df = new SimpleDateFormat("EEE, MMM d, ''yy");

        formattedDate = df.format(c);
        todayDate = formattedDate;
        System.out.println("fomratted time => " + df.format(time));


        Calendar cal2 = Calendar.getInstance();
        DateFormat df2= new SimpleDateFormat("EEE, M/d");
        Date c2 = cal2.getTime();
        String day1 = df2.format(c2);
        dateText = view.findViewById(R.id.toolbar_date);
        dateText.setText(day1);


        mRecyclerView = view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        System.out.println("item clicked: " + position);
                        selectedPosition = position;
                        mAdapter.setSelected(selectedPosition);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        System.out.println("long click triggered??: ");
                        Dialog dialog = new EditDialog(view.getContext(), position, mValues.get(position), mPickerInterface);
                        dialog.show();
                    }
                })
        );


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
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("pre dialog date: "+cal.get(Calendar.DAY_OF_MONTH));
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

        MaterialFancyButton buttonPlus = (MaterialFancyButton) view.findViewById(R.id.button_plus);
        MaterialFancyButton buttonMinus = (MaterialFancyButton) view.findViewById(R.id.button_minus);
        MaterialFancyButton buttonPlusSmall = (MaterialFancyButton) view.findViewById(R.id.button_plus_small);
        MaterialFancyButton buttonMinusSmall = (MaterialFancyButton) view.findViewById(R.id.button_minus_small);
        MaterialFancyButton monday = (MaterialFancyButton) view.findViewById(R.id.monday);
        MaterialFancyButton seven = (MaterialFancyButton) view.findViewById(R.id.seven);
        flag1 = (ImageView) view.findViewById(R.id.flag_1);
        flag2 = (ImageView) view.findViewById(R.id.flag_2);
        flag3 = (ImageView) view.findViewById(R.id.flag_3);

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

        monday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int day = cal.get(Calendar.DAY_OF_WEEK);
                int subtract = 0;
                switch(day) {
                    case Calendar.MONDAY:
                        subtract= 1;
                        graphView.setViewPort(data.length-1.5,1);
                        graphView2.setViewPort(data.length-1.5,1);
                    break;
                    case Calendar.TUESDAY:
                        subtract= 2;
                        graphView.setViewPort(data.length-subtract-0.25, subtract-0.5);
                        graphView2.setViewPort(data.length-subtract-0.25,subtract-0.5);
                        break;
                    case Calendar.WEDNESDAY:
                        subtract= 3;
                        graphView.setViewPort(data.length-subtract-0.25, subtract-0.5);
                        graphView2.setViewPort(data.length-subtract-0.25,subtract-0.5);
                        break;
                    case Calendar.THURSDAY:
                        subtract= 4;
                        graphView.setViewPort(data.length-subtract-0.25, subtract-0.5);
                        graphView2.setViewPort(data.length-subtract-0.25,subtract-0.5);
                        break;
                    case Calendar.FRIDAY:
                        subtract= 5;
                        graphView.setViewPort(data.length-subtract-0.25, subtract-0.5);
                        graphView2.setViewPort(data.length-subtract-0.25,subtract-0.5);
                        break;
                    case Calendar.SATURDAY:
                        subtract= 6;
                        graphView.setViewPort(data.length-subtract-0.25, subtract-0.5);
                        graphView2.setViewPort(data.length-subtract-0.25,subtract-0.5);
                        break;
                    case Calendar.SUNDAY:
                        subtract= 7;
                        graphView.setViewPort(data.length-subtract-0.25, subtract-0.5);
                        graphView2.setViewPort(data.length-subtract-0.25,subtract-0.5);
                        break;
                }


                exampleSeries.resetData(data);
                exampleSeries2.resetData(data2);


            }
        });

        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                graphView.setViewPort(data.length-7.25,6+0.5);
                graphView2.setViewPort(data.length-7.25,6+0.5);
                exampleSeries.resetData(data);
                exampleSeries2.resetData(data2);
            }
        });


        flag1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Integer> valueHold = new ArrayList<Integer>(3);
                valueHold.add(0);
                valueHold.add(0);
                valueHold.add(0);
                if(mNutritionDay.getFlags() != null && mNutritionDay.getFlags().size()!=0) {
                    valueHold = mNutritionDay.getFlags();
                }
                if(flag1True){
                    flag1True = false;
                    valueHold.set(0,0);
                    flag1.setColorFilter(ContextCompat.getColor(getActivity(), R.color.black), PorterDuff.Mode.SRC_ATOP);
                }else{
                    flag1True = true;
                    valueHold.set(0,1);
                    flag1.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccentLight), PorterDuff.Mode.SRC_ATOP);
                }
                mNutritionDay.setFlags(valueHold);
                mRepository.updateNutrition(mNutritionDay);

            }
        });
        flag2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Integer> valueHold = new ArrayList<Integer>();
                valueHold.add(0);
                valueHold.add(0);
                valueHold.add(0);
                if(mNutritionDay.getFlags() != null) {
                    valueHold = mNutritionDay.getFlags();
                }
                if(flag2True){
                    flag2True = false;
                    valueHold.set(1,0);
                    flag2.setColorFilter(ContextCompat.getColor(getActivity(), R.color.black), PorterDuff.Mode.SRC_ATOP);
                }else{
                    flag2True = true;
                    valueHold.set(1,1);
                    flag2.setColorFilter(ContextCompat.getColor(getActivity(), R.color.lightgreen), PorterDuff.Mode.SRC_ATOP);
                }
                mNutritionDay.setFlags(valueHold);
                mRepository.updateNutrition(mNutritionDay);
            }
        });
        flag3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO can probably just use the class variables instead of fetching things here
                ArrayList<Integer> valueHold = new ArrayList<Integer>();
                valueHold.add(0);
                valueHold.add(0);
                valueHold.add(0);
                if(mNutritionDay.getFlags() != null) {
                    valueHold = mNutritionDay.getFlags();
                }
                if(flag3True){
                    flag3True = false;
                    valueHold.set(2,0);
                    flag3.setColorFilter(ContextCompat.getColor(getActivity(), R.color.black), PorterDuff.Mode.SRC_ATOP);
                }else{
                    flag3True = true;
                    valueHold.set(2,1);
                    flag3.setColorFilter(ContextCompat.getColor(getActivity(), R.color.blue), PorterDuff.Mode.SRC_ATOP);
                }
                mNutritionDay.setFlags(valueHold);
                mRepository.updateNutrition(mNutritionDay);
            }
        });






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

        // Third field is active title, setting to cals give cal behavior in LineGraphView, Gram grams behavior, etc.
        graphView = new LineGraphView(getActivity(),"", "Cals"); // context, "" // heading);
        graphView.setScrollable(true);
        graphView.setScalable(true);
        graphView.setResolution(500, true,-1500,1500);

        graphView2 = new LineGraphView(getActivity(),"", "Grams"); // context, "" // heading);
        graphView2.setScrollable(true);
        graphView2.setScalable(true);
        graphView2.setResolution(50, false,0,200);


        GraphViewStyle mstyle = graphView.getGraphViewStyle();
        mstyle.setTextSize(40);
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
            //graphView.setManualYAxisBounds(maxY*1.1,-maxY*1.1);
            graphView.setManualYAxisBounds(1500,-1500);
        }

        else if(data.length == 2){
            graphView.setViewPort(0,1);
            int maxY = getMax(data, 0, data.length-1);
            maxY = 2200;
            //graphView.setManualYAxisBounds(maxY*1.1,-maxY*1.1);
            graphView.setManualYAxisBounds(1500,-1500);
        }

        else{
            graphView.setViewPort(0,data.length-1);
            int maxY = getMax(data, 0, data.length-1);
            //graphView.setManualYAxisBounds(maxY*1.1,-maxY*1.1);
            graphView.setManualYAxisBounds(1500,-1500);
        }


        //data2
        for(int i=0;i<data2.length;i++) {
            System.out.println("data pre-viewport "+i+": " + data2[i].valueY);
        }

        if (data2.length > 8){
            graphView2.setViewPort(data2.length-7,6);
            int maxY = getMax(data2,data2.length-8, data2.length-1);
           // graphView2.setManualYAxisBounds(maxY*1.1,-maxY*1.1);
            graphView.setManualYAxisBounds(1500,-1500);

        }

        else if(data2.length == 2){
            graphView2.setViewPort(0,1);
            int maxY = getMax(data2,0, data.length-1);
           // maxY = 2200;
           // graphView2.setManualYAxisBounds(maxY*1.1,-maxY*1.1);
            graphView.setManualYAxisBounds(1500,-1500);

        }

        else{
            graphView2.setViewPort(0,data2.length-1);
            int maxY = getMax(data2,0, data2.length-1);
           // graphView2.setManualYAxisBounds(maxY*1.1,-maxY*1.1);
            graphView.setManualYAxisBounds(1500,-1500);

        }

        System.out.println("check3");

        graphView.addSeries(exampleSeries); // data
        graphView2.addSeries(exampleSeries2); // data
        // graphView.addSeries(exampleSeries3);
        graphView.getGraphViewStyle().setNumVerticalLabels(7);
        graphView.getGraphViewStyle().setNumHorizontalLabels(1);
        graphView.getGraphViewStyle().setVerticalLabelsWidth(0);

        graphView2.getGraphViewStyle().setNumVerticalLabels(5);
        graphView2.getGraphViewStyle().setNumHorizontalLabels(1);
        graphView2.getGraphViewStyle().setVerticalLabelsWidth(0);
        graphView2.setManualYAxisBounds(200,0);



        ((LineGraphView) graphView).setDrawBackground(true);
        ((LineGraphView) graphView).setDrawDataPoints(true);
        // ((LineGraphView) graphView).setBackgroundColor(Color.argb(55, 0, 255, 21));
        graphView.getGraphViewStyle().setGridColor(Color.LTGRAY);
        ((LineGraphView) graphView).setDataPointsRadius(4f);

        ((LineGraphView) graphView2).setDrawDataPoints(true);
        ((LineGraphView) graphView2).setDrawBackground(false);
        graphView2.getGraphViewStyle().setGridColor(Color.LTGRAY);
        ((LineGraphView) graphView2).setDataPointsRadius(4f);




        layout.addView(graphView);
        layout2.addView(graphView2);




        return view;
    }

    public void onResume() {
     //   getDayData();
        super.onResume();
        //TODO Update ON Resume because widget could change it!
        getDayData();
        if(mAdapter !=null) {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    System.out.println("selected position " + selectedPosition);
                    mAdapter.setSelected(selectedPosition);
                    mAdapter.notifyDataSetChanged();
                }
            }, 200);

        }

    }

    @SuppressLint("StaticFieldLeak")
    private void getDayData(){
        new AsyncTask<Void, Void, Void>() {
            //Get today's workout => finishUI
            //get today's workout, if it doesn't exist create it

            protected void onPreExecute() {
                System.out.println("NUTRITION GET DAY DATA CALLED");
                // Pre Code
                mValues.clear();
                mNames.clear();
                mDates.clear();
                mGoalMap.clear();
                mNutritionDays.clear();

            }
            protected Void doInBackground(Void... unused) {
                //nutritionDay and formatted day change with selected day up top, used to edit values
                mNutritionDay = AppDatabase.getAppDatabase(getActivity()).ntDAO().findByDate(formattedDate);
                ArrayList<NutritionDay> mmDays = new ArrayList<NutritionDay>();
                ArrayList<String> mmDates = new ArrayList<String>();

                if(mNutritionDay == null){
                    mNutritionDay = new NutritionDay();
                    System.out.println("nutrition day null create uid: "+mNutritionDay.getUid());

                    mNames.add("Cals");
                    mValues.add(Integer.parseInt(prefs.getString("bmr", "-2000")));
                    mNames.add("Protein");
                    mValues.add(0);

                    mNutritionDay.setNames(mNames);
                    mNutritionDay.setValues(mValues);
                    mNutritionDay.setDate(formattedDate);
                    mNutritionDay.setDateMillis(cal.getTimeInMillis());
                    System.out.println("min set time millis: "+cal.getTimeInMillis());


                    //Will only edit goal if it is Today, won't change goals if looking back/ahead. Some days may therefore not have goals if map is opened
                    //should be fine as we can look back and just change for progress list when goals change.
                    //TODO display goalstodayDate
                    if(formattedDate == todayDate) {
                        switch (mGoalType) {
                            case 0:
                                mGoalMap.put("Cals", (double) prefs.getInt("deficit", 300));
                                break;
                            case 1:
                                mGoalMap.put("Cals", (double) prefs.getInt("recomp", 300));
                                break;
                            case 2:
                                mGoalMap.put("Cals", (double) prefs.getInt("bulk", 300));
                                break;
                            default:
                                break;
                        }
                        mGoalMap.put("Protein", (double) prefs.getFloat("protein", 0.6f) * prefs.getInt("weight", (int) 150));
                        mGoalMap.put("Sets", (double) prefs.getInt("volume", 15));
                        mNutritionDay.setGoalMap(mGoalMap);
                    }

                    //TODO can I really get rid of this??
                  //  mRepository.insertNutrition(mNutritionDay);

                    //TODO and do I really need this?, widget doesn't have
                    long id = AppDatabase.getAppDatabase(getActivity()).ntDAO().insert(mNutritionDay);
                    mNutritionDay = AppDatabase.getAppDatabase(getActivity()).ntDAO().findById((int) id);

                    System.out.println("nutrition day after insertion uid: "+mNutritionDay.getUid());

                    cals = mValues.get(0);

                }else{
                    mNames = mNutritionDay.getNames();
                    mValues = mNutritionDay.getValues();

                    if(mNames.size() == 0 || mValues.size() == 0){
                        System.out.println("nutrition day found get names/values uid: "+mNutritionDay.getUid());
                        mNames.add("Cals");
                        mValues.add(Integer.parseInt(prefs.getString("bmr", "-2000")));
                        mNames.add("Protein");
                        mValues.add(0);
                        mNutritionDay.setNames(mNames);
                        mNutritionDay.setValues(mValues);
                        System.out.println(mNames.get(0)+" "+mValues.get(1));
                    }else {
                        System.out.println("mNames size not zero "+mNames.get(0)+" "+mValues.get(0));
                    }

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
        SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d, ''yy");
        DateFormat df2= new SimpleDateFormat("EEE, M/d");
        try {
            Date date = df.parse(formattedDate);
            dateText.setText(df2.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        //set data to recyclerview
        mAdapter = new NutritionAdapter(mNames, mValues, mPickerInterface);
        mRecyclerView.setAdapter(mAdapter);

        ArrayList<Integer> setFlags = new ArrayList<Integer>();
        setFlags.add(0);
        setFlags.add(0);
        setFlags.add(0);

    if(mNutritionDay.getFlags() != null && mNutritionDay.getFlags().size() != 0) {
            setFlags = mNutritionDay.getFlags();
            if(setFlags.get(0) ==1){
                flag1True = true;
            }
            if(setFlags.get(1) ==1){
                flag2True = true;
            }
            if(setFlags.get(2) ==1){
                flag3True = true;
            }
        }
        if(setFlags.get(0)==1) {
            flag1.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccentLight), PorterDuff.Mode.SRC_ATOP);
        }else{
            flag1.setColorFilter(ContextCompat.getColor(getActivity(), R.color.black), PorterDuff.Mode.SRC_ATOP);
        }

        if(setFlags.get(1)==1) {
            flag2.setColorFilter(ContextCompat.getColor(getActivity(), R.color.lightgreen), PorterDuff.Mode.SRC_ATOP);
        }else{
            flag2.setColorFilter(ContextCompat.getColor(getActivity(), R.color.black), PorterDuff.Mode.SRC_ATOP);
        }

        if(setFlags.get(2)==1) {
            flag3.setColorFilter(ContextCompat.getColor(getActivity(), R.color.blue), PorterDuff.Mode.SRC_ATOP);
        }else{
            flag3.setColorFilter(ContextCompat.getColor(getActivity(), R.color.black), PorterDuff.Mode.SRC_ATOP);
        }



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
            //graphView.setManualYAxisBounds(maxY*1.1,-maxY*1.1);
            graphView.setManualYAxisBounds(1500,-1500);
        }
        else if(data.length == 2){
            graphView.setViewPort(0,1);
            int maxY = getMax(data,0, data.length-1);
            maxY = 2200;
           // graphView.setManualYAxisBounds(maxY*1.1,-maxY*1.1);
            graphView.setManualYAxisBounds(1500,-1500);
        }
        else{
            graphView.setViewPort(0,data.length-1);
            int maxY = getMax(data,0, data.length-1);
           // graphView.setManualYAxisBounds(maxY*1.1,-maxY*1.1);
            graphView.setManualYAxisBounds(1500,-1500);

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
            //graphView2.setManualYAxisBounds(maxY*1.1,-maxY*1.1);
            graphView.setManualYAxisBounds(1500,-1500);
        }
        else if(data2.length == 2){
            graphView2.setViewPort(0,1);
            int maxY = getMax(data2,0, data2.length-1);
            maxY = 2200;
            //graphView2.setManualYAxisBounds(maxY*1.1,-maxY*1.1);
            graphView.setManualYAxisBounds(1500,-1500);

        }
        else{
            graphView2.setViewPort(0,data2.length-1);
            int maxY = getMax(data2,0, data2.length-1);
            //graphView2.setManualYAxisBounds(maxY*1.1,-maxY*1.1);
            graphView.setManualYAxisBounds(1500,-1500);

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



    public void yFit(){
        int low = (int) Math.floor(graphView.getMinX(false));
        System.out.println("low1 "+low);

        int high = (int) Math.floor(graphView.getMaxX(false));
        System.out.println("high1 "+high);

        int maxY = getMax(data, low, high);
        System.out.println("maxy "+maxY);

      //  graphView.setManualYAxisBounds(maxY*1.1,-maxY*1.1);
        graphView.setManualYAxisBounds(1500,-1500);

        exampleSeries.resetData(data);

        int low2 = (int) Math.floor(graphView2.getMinX(false));
        System.out.println("low2 "+low2);

        int high2 = (int) Math.floor(graphView2.getMaxX(false));
        System.out.println("high2 "+high2);

        int maxY2 = getMax(data2, low2, high2);
        System.out.println("maxy2 "+maxY2);

       // graphView2.setManualYAxisBounds(maxY2*1.1,-maxY2*1.1);
        graphView.setManualYAxisBounds(1500,-1500);

        exampleSeries2.resetData(data2);
    }


    @Override
    public void onDialogDismiss(CalendarDay m) {

        cal.set(m.getYear(), m.getMonth() - 1, m.getDay());
        Date d = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d, ''yy");
        formattedDate = df.format(d);
        System.out.println("formdate:" + formattedDate);

        getDayData();

    }

    @Override
    public void onPickerDialogDismiss(int weight, int position) {
        System.out.println("test interface: "+position+" : "+weight);
        mValues.set(position,weight);
        mNutritionDay.setValues(mValues);
        mRepository.updateNutrition(mNutritionDay);
        updateWidgets();
        mAdapter.notifyDataSetChanged();
        updateGraphView(formattedDate, weight, selectedPosition);

    }
}