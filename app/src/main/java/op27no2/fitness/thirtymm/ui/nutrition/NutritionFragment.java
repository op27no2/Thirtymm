package op27no2.fitness.thirtymm.ui.nutrition;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import op27no2.fitness.thirtymm.Database.AppDatabase;
import op27no2.fitness.thirtymm.Database.Repository;
import op27no2.fitness.thirtymm.MyAppWidgetProvider;
import op27no2.fitness.thirtymm.R;
import op27no2.fitness.thirtymm.ui.lifting.LiftingWorkout;
import op27no2.fitness.thirtymm.ui.volume.VolumeAdapter;

public class NutritionFragment extends Fragment {
    private SharedPreferences prefs;
    private SharedPreferences.Editor edt;

    private String formattedDate;
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
    private Integer cals;

    @SuppressLint("StaticFieldLeak")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nutrition, container, false);
        prefs = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        edt = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();

        mRepository = new Repository(getActivity());
        rabbit = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        cal = Calendar.getInstance();
        Date c = cal.getTime();
        Long time = Calendar.getInstance().getTimeInMillis();
        System.out.println("Current time => " + time);
        SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d, ''yy");
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

        buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rabbit.vibrate(25);
                if(cals != null) {
                    cals = cals + 100;
                    mNutritionDay.setCals(cals);
                    mRepository.updateNutrition(mNutritionDay);
                    updateWidgets();
                    mValues.set(0,cals);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });



        buttonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rabbit.vibrate(25);
                if(cals != null) {
                    cals = cals - 100;
                    mNutritionDay.setCals(cals);
                    mRepository.updateNutrition(mNutritionDay);
                    updateWidgets();
                    mValues.set(0,cals);
                    mAdapter.notifyDataSetChanged();
                }


            }
        });



        getDayData();



        return view;
    }

    public void onResume() {
        getDayData();
        super.onResume();

    }


    @SuppressLint("StaticFieldLeak")
    private void getDayData(){
        new AsyncTask<Void, Void, Void>() {
            //Get today's workout => finishUI
            //get today's workout, if it doesn't exist create it

            protected void onPreExecute() {
                // Pre Code
            }
            protected Void doInBackground(Void... unused) {
                mNutritionDay = AppDatabase.getAppDatabase(getActivity()).ntDAO().findByDate(formattedDate);

                if(mNutritionDay == null){
                    mNutritionDay = new NutritionDay();
                    System.out.println("day null should create");
                    mNutritionDay.setCals(prefs.getInt("BaseCals", 2000));
                    mNutritionDay.setDate(formattedDate);
                    mRepository.insertNutrition(mNutritionDay);
                }
                cals = mNutritionDay.getCals();
                mNames.clear();
                mValues.clear();
                mNames.add("Cals");
                mNames.add("Protein");
                mValues.add(cals);
                mValues.add(0);

                return null;
            }
            protected void onPostExecute(Void unused) {
                // Post Code
                finishUI();
            }
        }.execute();

    }

    private void finishUI(){
        dateText.setText(formattedDate);
        System.out.println("finish ui");
        //set data to recyclerview
        mAdapter = new NutritionAdapter(mNames, mValues);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


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