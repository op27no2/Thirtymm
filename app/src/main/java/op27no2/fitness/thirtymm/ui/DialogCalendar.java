package op27no2.fitness.thirtymm.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.Calendar;
import java.util.Date;

import op27no2.fitness.thirtymm.Database.AppDatabase;
import op27no2.fitness.thirtymm.Database.Repository;
import op27no2.fitness.thirtymm.R;
import op27no2.fitness.thirtymm.ui.lifting.LiftMap;
import op27no2.fitness.thirtymm.ui.lifting.MyDialogInterface;
import op27no2.fitness.thirtymm.ui.nutrition.CalendarDialogInterface;
import op27no2.fitness.thirtymm.ui.volume.DialogVolumeMapnterface;
import op27no2.fitness.thirtymm.ui.volume.MyDialogVolumeAdapter;
import org.threeten.bp.LocalDate;

public class DialogCalendar extends Dialog  {

        public Context mContext;
        private CalendarDialogInterface mInterface;
        private Calendar fragCalendar;

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

            new AsyncTask<Void, Void, Void>() {
                //Get today's workout => finishUI
                //get today's workout, if it doesn't exist create it

                protected void onPreExecute() {
                    // Pre Code
                }
                protected Void doInBackground(Void... unused) {
                 //   mLiftMap = AppDatabase.getAppDatabase(mContext).lmDAO().findByName(liftName);

                    //todo possibly get workout data for calendar

                    return null;
                }
                protected void onPostExecute(Void unused) {
                    // Post Code
                    finishUI();
                }
            }.execute();

        MaterialCalendarView mCal = findViewById(R.id.calendar_view);
        int year = fragCalendar.get(Calendar.YEAR);
        int month = fragCalendar.get(Calendar.MONTH);
        month = month+1;
        int day = fragCalendar.get(Calendar.DAY_OF_MONTH);
        mCal.setDateSelected(CalendarDay.from(year, month, day),true);
        mCal.setCurrentDate(CalendarDay.from(year, month, day));

        mCal.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                mInterface.onDialogDismiss(date);
                DialogCalendar.this.dismiss();
            }
        });



    }



        public void finishUI(){
            //set lift data to recyclerview



        }


    @Override
    protected void onStop(){

    }



}