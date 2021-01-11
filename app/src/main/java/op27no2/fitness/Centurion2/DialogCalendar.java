package op27no2.fitness.Centurion2;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;
import com.rilixtech.materialfancybutton.MaterialFancyButton;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import op27no2.fitness.Centurion2.Database.AppDatabase;
import op27no2.fitness.Centurion2.Database.Repository;
import op27no2.fitness.Centurion2.fragments.lifting.Lift;
import op27no2.fitness.Centurion2.fragments.lifting.LiftMap;
import op27no2.fitness.Centurion2.fragments.lifting.LiftingWorkout;
import op27no2.fitness.Centurion2.fragments.nutrition.CalendarDialogInterface;
import op27no2.fitness.Centurion2.fragments.nutrition.NutritionDay;
import op27no2.fitness.Centurion2.fragments.run.RunWorkout;

public class DialogCalendar extends Dialog  {

        public Context mContext;
        private CalendarDialogInterface mInterface;
        private Calendar fragCalendar;
        private Repository mRepository;
        private ArrayList<CalendarDay> bothDays = new ArrayList<CalendarDay>();
        private ArrayList<CalendarDay> liftDays = new ArrayList<CalendarDay>();
        private ArrayList<CalendarDay> runDays = new ArrayList<CalendarDay>();
        private HashMap<CalendarDay, Double[]> bothDaysH = new HashMap<CalendarDay, Double[]>();
        private HashMap<CalendarDay, Double> liftDaysH = new HashMap<CalendarDay, Double>();
        private HashMap<CalendarDay, Double> runDaysH = new HashMap<CalendarDay, Double>();

        private ArrayList<Double> runDistances = new ArrayList<Double>();
        private ArrayList<Double> liftVolumes = new ArrayList<Double>();
        private HashMap<CalendarDay, Boolean[]> flagDaysH = new HashMap<CalendarDay, Boolean[]>();

        private int MONTHS_DISPLAYED = 3;
        private ArrayList<Double> milesList = new ArrayList<Double>();
        private ArrayList<Double> setsList = new ArrayList<Double>();
        private int MONTH;
        private int MONTH_OFFSET=0;

        private MaterialCalendarView mCal;
        private Calendar copyCal;
        private Calendar copyCalTEST;
        private TextView mileText;
        private TextView setsText;
        private Double milesHold = 0.0;
        private Double setsHold = 0.0;

        private HashMap<String, Integer> mVolume = new HashMap<String, Integer>();
        private ArrayList<Map.Entry<String, Integer>> listOfEntry;
        private HashMap<String, Double> muscleVolumes = new HashMap<String, Double>();


    public DialogCalendar(@NonNull Context context, CalendarDialogInterface dialogInterface, Calendar cal) {
        super(context);
        mContext = context;
        mInterface = dialogInterface;
        fragCalendar = cal;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("StaticFieldLeak")
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_calendar);

            DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
            int width = metrics.widthPixels;
            getWindow().setLayout((8 * width) / 9, LinearLayout.LayoutParams.WRAP_CONTENT);
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            copyCal  = (Calendar)fragCalendar.clone();

            System.out.println("async dates: "+ fragCalendar.get(Calendar.DAY_OF_MONTH));
            System.out.println("loaded date: "+ copyCal.get(Calendar.DAY_OF_MONTH));

            mCal = findViewById(R.id.calendar_view);
            mileText = findViewById(R.id.text_miles);
            setsText = findViewById(R.id.text_sets);

            MaterialFancyButton mDismiss = findViewById(R.id.dismiss_button);
            mDismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogCalendar.this.dismiss();
                }
            });

            getData();

    }

    //TODO maybe get all this data in the background on Nutrition fragment, flags the only thing to watch out for??
    @SuppressLint("StaticFieldLeak")
    private void getData(){
        new AsyncTask<Void, Void, Void>() {
            //Get today's workout => finishUI
            //get today's workout, if it doesn't exist create it

            protected void onPreExecute() {
                // Pre Code


            }
            protected Void doInBackground(Void... unused) {
                copyCal.add(Calendar.MONTH, -(MONTHS_DISPLAYED));
                copyCal.set(Calendar.DAY_OF_MONTH, 1);

                for(int j=0; j<MONTHS_DISPLAYED+1; j++) {
                    milesHold = 0.0;
                    setsHold = 0.0;
                    int monthlength = copyCal.getActualMaximum(Calendar.DAY_OF_MONTH);
                    System.out.println("monthlength: "+monthlength);

                    for (int i = 1; i < monthlength +1; i++) {

                        Date c = copyCal.getTime();
                        DateFormat df = new SimpleDateFormat("EEE, MMM d, ''yy");
                        String formattedDate = df.format(c);
                        System.out.println("caldates "+formattedDate);

                        CalendarDay theDay = CalendarDay.from(copyCal.get(Calendar.YEAR), copyCal.get(Calendar.MONTH) + 1, copyCal.get(Calendar.DAY_OF_MONTH));

                        LiftingWorkout mLiftingWorkout = AppDatabase.getAppDatabase(mContext).lwDAO().findByDate(formattedDate);
                        RunWorkout mRunWorkout = AppDatabase.getAppDatabase(mContext).rwDAO().findByDate(formattedDate);
                        List<RunWorkout> mRunWorkouts = AppDatabase.getAppDatabase(mContext).rwDAO().findAllByDate(formattedDate);
                        //TODO now use List instead of one owrkout when approp



                        NutritionDay mNutritionDay = AppDatabase.getAppDatabase(mContext).ntDAO().findByDate(formattedDate);

                        if (mLiftingWorkout != null && mLiftingWorkout.getMyLifts().size() != 0 && mRunWorkouts != null && mRunWorkouts.size() != 0) {
                            //araylist is for circles, hashmap for numbers
                            bothDays.add(theDay);
                          //  liftDays.add(theDay);
                           // runDays.add(theDay);*/
                            Double sets = getAverageSets(mLiftingWorkout);
                            Double miles = 0.0;
                            for(int k=0; k<mRunWorkouts.size(); k++) {
                                miles = miles+getMiles(mRunWorkouts.get(k).getDistance());
                            }

                            System.out.println("distances "+miles);
                            Double[] bothVals = {sets, miles};
                            bothDaysH.put(theDay,bothVals);

                            System.out.println("setshold "+setsHold);

                            /*milesHold = milesHold + getMiles(mRunWorkout.getDistance());
                            setsHold = setsHold + getAverageSets(mLiftingWorkout);*/
                            milesHold = milesHold + miles;
                            setsHold = setsHold + sets;

                        } else {
                            if (mLiftingWorkout != null && mLiftingWorkout.getMyLifts().size() != 0) {
                                liftDays.add(theDay);
                           //     liftVolumes.add(getAverageSets(mLiftingWorkout));
                                liftDaysH.put(theDay, getAverageSets(mLiftingWorkout));

                                setsHold = setsHold + getAverageSets(mLiftingWorkout);
                            }
                            if (mRunWorkouts != null && mRunWorkouts.size() != 0) {
                                runDays.add(theDay);
                                Double miles = 0.0;
                                for(int k=0; k<mRunWorkouts.size(); k++) {
                                    miles = miles+getMiles(mRunWorkouts.get(k).getDistance());
                                }

                                runDaysH.put(theDay,miles);
                                System.out.println("distances "+getMiles(mRunWorkout.getDistance()));

                                milesHold = milesHold + miles;
                            }
                        }
                        if (mNutritionDay != null && mNutritionDay.getFlags() != null && mNutritionDay.getFlags().size() != 0) {
                            Boolean[] flags = {false,false,false};
                            if (mNutritionDay.getFlags().get(0) == 1) {
                                flags[0] = true;
                            }
                            if (mNutritionDay.getFlags().get(1) == 1) {
                                flags[1] = true;
                            }
                            if (mNutritionDay.getFlags().get(2) == 1) {
                                flags[2] = true;
                            }
                            flagDaysH.put(theDay, flags);

                        }

                        copyCal.add(Calendar.DATE, 1);


                    }
                    System.out.println("setshold "+setsHold);
                    milesList.add(milesHold);
                    setsList.add(setsHold);
                 //   copyCal.add(Calendar.MONTH, 1);
                  //  copyCal.set(Calendar.DAY_OF_MONTH, 1);


                    /*for(int z=0; z<milesList.size(); z++) {
                        System.out.println("miles: "+milesList.get(z));
                    }*/

                }


                //todo possibly get workout data for calendar

                return null;
            }
            protected void onPostExecute(Void unused) {
                // Post Code
                finishUI();
            }
        }.execute();

    }


    public void finishUI(){
            //set lift data to recyclerview
        //TODO looks like switched somehwere in Async!
        //mCal view used to track month
        MONTH = mCal.getCurrentDate().getMonth();

        //set text to milesList
        System.out.println("loaded date finish ui: "+ fragCalendar.get(Calendar.DAY_OF_MONTH));
        mileText.setText("Miles: "+Double.toString(round(milesList.get(milesList.size()-1),1)));
        setsText.setText("Sets/Muscle: "+Double.toString(round(setsList.get(setsList.size()-1),1)));

        mCal = findViewById(R.id.calendar_view);
        mCal.setSelectionMode(MaterialCalendarView.SELECTION_MODE_SINGLE);

        mCal.setAllowClickDaysOutsideCurrentMonth(true);
     //   mCal.state().edit().setFirstDayOfWeek(DayOfWeek.MONDAY).commit();

        int year = fragCalendar.get(Calendar.YEAR);
        int month = fragCalendar.get(Calendar.MONTH);
        int day = fragCalendar.get(Calendar.DATE);

        mCal.setDateSelected(CalendarDay.from(year, month+1, day),true);
        System.out.println("setting date: "+year+" "+month+" "+day);
        mCal.setCurrentDate(CalendarDay.from(year, month+1, day));


        mCal.addDecorator(new MyDecoratorCirclesBackground(mContext, true,true, bothDays));
        mCal.addDecorators(new MyDecoratorCirclesBackground(mContext, true,false,liftDays));
        mCal.addDecorators(new MyDecoratorCirclesBackground(mContext, false,true,runDays));

        for (Map.Entry<CalendarDay, Double> entry : runDaysH.entrySet()) {
            Double value = entry.getValue();
            CalendarDay key = entry.getKey();
            ArrayList<CalendarDay> mList = new ArrayList<CalendarDay>();
            mList.add(key);
            mCal.addDecorators(new MyDecoratorNumbers(mContext, null, round(value,1) ,key.getDay(), mList));
        }
        for (Map.Entry<CalendarDay, Double> entry : liftDaysH.entrySet()) {
            Double value = entry.getValue();
            CalendarDay key = entry.getKey();
            ArrayList<CalendarDay> mList = new ArrayList<CalendarDay>();
            mList.add(key);
            mCal.addDecorators(new MyDecoratorNumbers(mContext,round(value,1),null , key.getDay(), mList));
        }
        for (Map.Entry<CalendarDay, Double[]> entry : bothDaysH.entrySet()) {
            Double[] values = entry.getValue();
            CalendarDay key = entry.getKey();
            ArrayList<CalendarDay> mList = new ArrayList<CalendarDay>();
            mList.add(key);
            mCal.addDecorators(new MyDecoratorNumbers(mContext, round(values[0],1), round(values[1],1),key.getDay(), mList));
        }


        for (Map.Entry<CalendarDay, Boolean[]> entry : flagDaysH.entrySet()) {
            Boolean[] values = entry.getValue();
            CalendarDay key = entry.getKey();
            ArrayList<CalendarDay> mList = new ArrayList<CalendarDay>();
            mList.add(key);
            mCal.addDecorator(new MyDecoratorDots(mContext,values[0],values[1],values[2], mList));

        }


        mCal.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                mInterface.onDialogDismiss(date);

                //    DialogCalendar.this.dismiss();
            }
        });
        mCal.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

                System.out.println("on month changed: "+date.getMonth());
                System.out.println("on month changed mcal?: "+mCal.getCurrentDate().getMonth());
                if(date.getMonth()< MONTH){
                    MONTH_OFFSET = MONTH_OFFSET -1;
                }else if(date.getMonth()> MONTH){
                    MONTH_OFFSET = MONTH_OFFSET +1;
                }
                MONTH = date.getMonth();

                //TODO to include months forward, if you want, must add milesList values forward. currently stops at current month at max arraylist place
                if(-MONTH_OFFSET<milesList.size() && MONTH_OFFSET<=0) {
                    mileText.setText("Miles: "+Double.toString(round(milesList.get(milesList.size() - 1 + MONTH_OFFSET), 1)));
                    setsText.setText("Sets/Muscle: "+ Double.toString(round(setsList.get(setsList.size() - 1 + MONTH_OFFSET), 1)));
                }

         /*       System.out.println("on month changed");
                fragCalendar.set(date.getYear(), date.getMonth()-1, date.getDay());
                System.out.println(date.getYear()+" "+date.getMonth()+" "+ date.getDay());
                */

             //   getData();
            }
        });


    }




    @Override
    protected void onStop(){

    }

    public class EventDecorator implements DayViewDecorator {

        private final int color;
        private final HashSet<CalendarDay> dates;

        public EventDecorator(int color, Collection<CalendarDay> dates) {
            this.color = color;
            this.dates = new HashSet<>(dates);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(5, color));
        }
    }



    private double getMiles(float i) {
        return  (double) i * 0.000621371192f;
    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    private double getAverageSets(LiftingWorkout mWorkout){
        mVolume.clear();
        muscleVolumes.clear();

        double avgsets = 0.0;
        if(mWorkout != null) {
            ArrayList<Lift> mLifts = mWorkout.getMyLifts();

            for (int j = 0; j < mLifts.size(); j++) {
                String name = mLifts.get(j).getName();
                int sets = mLifts.get(j).getReps().size();
                if (mVolume.containsKey(name)) {
                    int hold = mVolume.get(name);
                    hold = hold + sets;
                    mVolume.put(name, hold);
                } else {
                    mVolume.put(name, sets);
                }
            }
        }

        /*for (Map.Entry<String, Integer> entry : mVolume.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }*/
        Set<Map.Entry<String, Integer>> entrySet = mVolume.entrySet();
        listOfEntry = new ArrayList<Map.Entry<String, Integer>>(entrySet);

        for(int j=0; j<listOfEntry.size(); j++){
            LiftMap mMap = AppDatabase.getAppDatabase(mContext).lmDAO().findByName(listOfEntry.get(j).getKey());
            if (mMap != null) {
                ArrayList<String> names = mMap.getMuscles();
                ArrayList<Integer> ratios = mMap.getRatios();
                if (mMap.getRatios() != null) {
                    for (int i = 0; i < mMap.getRatios().size(); i++) {
                        //TODO probably better to just remove these from list than check during iteration
                        if(!names.get(i).equals("Chest") && !names.get(i).equals("Back")&& !names.get(i).equals("Core")&& !names.get(i).equals("Arms")&& !names.get(i).equals("Legs")){
                        if (muscleVolumes.containsKey(names.get(i))) {
                                Double hold = muscleVolumes.get(names.get(i));
                                //dividing by 10 because stored as integers
                                hold = hold + ((double) ratios.get(i) / 10) * listOfEntry.get(j).getValue();
                                muscleVolumes.put(names.get(i), hold);
                            } else {
                                //dividing by 10 because stored as integers
                                muscleVolumes.put(names.get(i), ((double) ratios.get(i) / 10) * listOfEntry.get(j).getValue());
                            }
                        }
                    }
                }
            } else {
                System.out.println("LIFT MAP NULL: " + listOfEntry.get(j).getKey());
            }
        }
        for (Double value : muscleVolumes.values()) {
            avgsets = avgsets+value;
        }
        //just to print map for debugging
 /*       for (Map.Entry<String, Double> entry : muscleVolumes.entrySet()) {
            Double value = entry.getValue();
            if(value != 0) {
                String key = entry.getKey();
                System.out.println("muscle: " + key);
                System.out.println("volume sets: " + value);
            }
            // ...
        }*/
        System.out.println("total setmuscles: "+(avgsets/38));
        return (avgsets/38);

    }






}