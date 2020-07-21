package op27no2.fitness.Centurion;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
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

import org.threeten.bp.DayOfWeek;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import op27no2.fitness.Centurion.Database.AppDatabase;
import op27no2.fitness.Centurion.Database.Repository;
import op27no2.fitness.Centurion.ui.lifting.LiftingWorkout;
import op27no2.fitness.Centurion.ui.nutrition.CalendarDialogInterface;
import op27no2.fitness.Centurion.ui.nutrition.NutritionDay;
import op27no2.fitness.Centurion.ui.run.RunWorkout;

public class DialogCalendar extends Dialog  {

        public Context mContext;
        private CalendarDialogInterface mInterface;
        private Calendar fragCalendar;
        private Repository mRepository;
        private ArrayList<CalendarDay> liftDays = new ArrayList<CalendarDay>();
        private ArrayList<CalendarDay> runDays = new ArrayList<CalendarDay>();
        private ArrayList<Double> runDistances = new ArrayList<Double>();
        private ArrayList<CalendarDay> bothDays = new ArrayList<CalendarDay>();
        private ArrayList<CalendarDay> flagOneDays = new ArrayList<CalendarDay>();
        private ArrayList<CalendarDay> flagTwoDays = new ArrayList<CalendarDay>();
        private ArrayList<CalendarDay> flagThreeDays = new ArrayList<CalendarDay>();
        private int MONTHS_DISPLAYED = 3;
        private ArrayList<Double> milesList = new ArrayList<Double>();
        private int MONTH;
        private int MONTH_OFFSET=0;

        private MaterialCalendarView mCal;
        private Calendar copyCal;
        private TextView mileText;
        private Double milesHold = 0.0;


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
            mileText.setText("Test");

            getData();

    }

    @SuppressLint("StaticFieldLeak")
    private void getData(){
        new AsyncTask<Void, Void, Void>() {
            //Get today's workout => finishUI
            //get today's workout, if it doesn't exist create it

            protected void onPreExecute() {
                // Pre Code
                System.out.println("async dates0: "+ copyCal.get(Calendar.DAY_OF_MONTH));


                System.out.println("async dates1: "+ copyCal.get(Calendar.DAY_OF_MONTH));


            }
            protected Void doInBackground(Void... unused) {
                copyCal.add(Calendar.MONTH, -(MONTHS_DISPLAYED-1));
                copyCal.set(Calendar.DAY_OF_MONTH, 1);

                for(int j=0; j<MONTHS_DISPLAYED; j++) {
                    milesHold = 0.0;

                    for (int i = 1; i < copyCal.getActualMaximum(Calendar.DAY_OF_MONTH)-1; i++) {
                        System.out.println("async dates2: " + copyCal.get(Calendar.DAY_OF_MONTH));

                        Date c = copyCal.getTime();
                        DateFormat df = new SimpleDateFormat("EEE, MMM d, ''yy");
                        String formattedDate = df.format(c);

                        CalendarDay theDay = CalendarDay.from(copyCal.get(Calendar.YEAR), copyCal.get(Calendar.MONTH) + 1, copyCal.get(Calendar.DAY_OF_MONTH));

                        LiftingWorkout mLiftingWorkout = AppDatabase.getAppDatabase(mContext).lwDAO().findByDate(formattedDate);
                        RunWorkout mRunWorkout = AppDatabase.getAppDatabase(mContext).rwDAO().findByDate(formattedDate);
                        NutritionDay mNutritionDay = AppDatabase.getAppDatabase(mContext).ntDAO().findByDate(formattedDate);

                        if (mLiftingWorkout != null && mLiftingWorkout.getMyLifts().size() != 0 && mRunWorkout != null && mRunWorkout.getDistance() != 0) {
                            bothDays.add(theDay);

                                milesHold = milesHold + getMiles(mRunWorkout.getDistance());

                        } else {
                            if (mLiftingWorkout != null && mLiftingWorkout.getMyLifts().size() != 0) {
                                liftDays.add(theDay);
                            }
                            if (mRunWorkout != null && mRunWorkout.getDistance() != 0) {
                                runDays.add(theDay);
                                runDistances.add(getMiles(mRunWorkout.getDistance()));

                                milesHold = milesHold + getMiles(mRunWorkout.getDistance());


                            }
                        }
                        if (mNutritionDay != null && mNutritionDay.getFlags() != null && mNutritionDay.getFlags().size() != 0) {
                            if (mNutritionDay.getFlags().get(0) == 1) {
                                flagOneDays.add(theDay);
                            }
                            if (mNutritionDay.getFlags().get(1) == 1) {
                                flagTwoDays.add(theDay);
                            }
                            if (mNutritionDay.getFlags().get(2) == 1) {
                                flagThreeDays.add(theDay);
                            }

                        }
                        System.out.println("async dates3: " + copyCal.get(Calendar.DAY_OF_MONTH));

                        copyCal.add(Calendar.DATE, 1);

                        System.out.println("async dates4: " + copyCal.get(Calendar.DAY_OF_MONTH));

                    }
                    milesList.add(milesHold);
                    copyCal.set(Calendar.DAY_OF_MONTH, 1);
                    copyCal.add(Calendar.MONTH, 1);
                    for(int z=0; z<milesList.size(); z++) {
                        System.out.println("miles: "+milesList.get(z));
                    }

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
        mileText.setText(Double.toString(round(milesList.get(milesList.size()-1),1)));

        mCal = findViewById(R.id.calendar_view);
        mCal.setSelectionMode(MaterialCalendarView.SELECTION_MODE_SINGLE);
        mCal.setAllowClickDaysOutsideCurrentMonth(true);
        mCal.state().edit().setFirstDayOfWeek(DayOfWeek.MONDAY).commit();

        int year = fragCalendar.get(Calendar.YEAR);
        int month = fragCalendar.get(Calendar.MONTH);
        int day = fragCalendar.get(Calendar.DATE);

        mCal.setDateSelected(CalendarDay.from(year, month+1, day),true);
        System.out.println("setting date: "+year+" "+month+" "+day);
        mCal.setCurrentDate(CalendarDay.from(year, month+1, day));

        ArrayList<CalendarDay> days = new ArrayList<CalendarDay>();
        days.add( CalendarDay.from(year, month, day));
        // days.add( CalendarDay.from(year, month, day-2));

        //TODO THROWS ERROR at DAY 1 of MONTH
        //days.add( CalendarDay.from(year, month, day-4));

        ArrayList<CalendarDay> days2 = new ArrayList<CalendarDay>();
        // days2.add( CalendarDay.from(year, month, day-3));

        ArrayList<CalendarDay> days3 = new ArrayList<CalendarDay>();
        //  days3.add( CalendarDay.from(year, month, day-5));


      //  mCal.addDecorator(new EventDecorator(Color.BLACK, flagOneDays));
        mCal.addDecorator(new MyDecoratorDots(mContext,true,true,true, flagOneDays));

        mCal.addDecorators(new EventDecorator(Color.GREEN, flagTwoDays));
        mCal.addDecorator(new EventDecorator(Color.RED, flagThreeDays));

        mCal.addDecorator(new MyDecoratorCirclesBackground(mContext, true,true, bothDays));
        mCal.addDecorators(new MyDecoratorCirclesBackground(mContext, true,false,liftDays));
        mCal.addDecorators(new MyDecoratorCirclesBackground(mContext, false,true,runDays));

        for(int i=0; i<runDays.size(); i++) {
            //have to move just one to new arraylist because decorator constructor wasn't taking arraylist.get(i)
            ArrayList<CalendarDay> mList = new ArrayList<CalendarDay>();
            mList.add(runDays.get(i));
            mCal.addDecorators(new MyDecoratorNumbers(mContext, round(runDistances.get(i),1), mList));
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
                    mileText.setText(Double.toString(round(milesList.get(milesList.size() - 1 + MONTH_OFFSET), 1)));
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

}