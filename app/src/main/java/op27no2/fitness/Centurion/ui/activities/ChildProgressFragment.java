package op27no2.fitness.Centurion.ui.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import op27no2.fitness.Centurion.Database.AppDatabase;
import op27no2.fitness.Centurion.Database.Repository;
import op27no2.fitness.Centurion.R;
import op27no2.fitness.Centurion.RecyclerItemClickListener;
import op27no2.fitness.Centurion.ui.lifting.LiftingWorkout;
import op27no2.fitness.Centurion.ui.nutrition.NutritionDay;


public class ChildProgressFragment extends Fragment {
    private SharedPreferences prefs;
    private SharedPreferences.Editor edt;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private Repository mRepository;
    private ProgressAdapter mProgressAdapter;
    private ArrayList<NutritionDay> mDays = new ArrayList<NutritionDay>();
    private ArrayList<LiftingWorkout> mWorkouts = new ArrayList<LiftingWorkout>();
    private ArrayList<Float> setWeekData = new ArrayList<Float>();
    private ArrayList<Integer> calWeekData = new ArrayList<Integer>();
    private ArrayList<Integer> proteinWeekData = new ArrayList<Integer>();
    private ArrayList<String> calendarWeeks = new ArrayList<String>();
    private TextView volumeText;
    private TextView calorieText;
    private TextView proteinText;
    private Context mContext;

    public ChildProgressFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activity_child_progress, container, false);
        prefs = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        edt = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
        mRepository = new Repository(getActivity());
        mContext = getActivity();

        Resources res = mContext.getResources();
        ResourcesCompat.getColor(res, R.color.colorPrimary, null);

        GridView grid = view.findViewById(R.id.grid_view);
        ArrayList<Double> headerGrid = new ArrayList<Double>();

        headerGrid.add((double) prefs.getInt("volume",0));
        headerGrid.add((double) prefs.getInt("deficit", 0));
        double x = ((double) prefs.getInt("weight", 0))*((double) prefs.getFloat("protein", 0));
        double xx = (double) Math.floor(x);
        headerGrid.add(xx);
        MyGridAdapter gridAdapter = new MyGridAdapter(mContext, headerGrid, res);
        grid.setNumColumns(headerGrid.size());
        grid.setAdapter(gridAdapter);

/*
        //Header (switch to recyclerview at some point)
        volumeText = view.findViewById(R.id.volume_text);
        volumeText.setText("Volume:\n"+ Integer.toString(prefs.getInt("volume",15))+" sets");
        calorieText = view.findViewById(R.id.calorie_text);
        calorieText.setText("Calories:\n"+Integer.toString(prefs.getInt("deficit",-3500)));
        proteinText = view.findViewById(R.id.protein_text);
        proteinText.setText("Protein:\n"+(int) Math.floor(prefs.getInt("weight",0)*(prefs.getFloat("protein",120)))+"g");
*/


        //recyclerview and layoutmanager
        mRecyclerView = view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
       // mLayoutManager.setReverseLayout(true);
       // mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        System.out.println("item clicked: "+position);
                       // ((MainActivity)getActivity()).goToRunDetail(mRunWorkouts.get(position).getUid());
                    }
                    @Override
                    public void onItemLongClick(View view, int position) {
                        System.out.println("item long clicked: "+position);
                    }
                })
        );

        mProgressAdapter = new ProgressAdapter(setWeekData, calWeekData, proteinWeekData,calendarWeeks, getActivity());
        mRecyclerView.setAdapter(mProgressAdapter);

        getDayData();

        return view;
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
            SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d, ''yy");
            SimpleDateFormat shortFormat = new SimpleDateFormat("M/d");

             mDays = (ArrayList<NutritionDay>) AppDatabase.getAppDatabase(getActivity()).ntDAO().getAll();
             //this block should return oldest day from mDays
             Date dateZero = null;
                try {
                    dateZero = df.parse(mDays.get(0).getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                for(int i=1; i<mDays.size();i++){
                    Date dateCompare = null;
                    try {
                        dateCompare = df.parse(mDays.get(i).getDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                if (dateCompare.before(dateZero)) {
                    //sets dateZero to older date to be compared again
                    dateZero = dateCompare;
                 }
             }

             mWorkouts = (ArrayList<LiftingWorkout>) AppDatabase.getAppDatabase(getActivity()).lwDAO().getAll();
            //and this block should return oldest day from mWorkouts
            Date dateZero2 = null;
            try {
                dateZero2 = df.parse(mWorkouts.get(0).getWorkoutDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            for(int i=1; i<mWorkouts.size();i++){
                Date dateCompare = null;
                try {
                    dateCompare = df.parse(mWorkouts.get(i).getWorkoutDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (dateCompare.before(dateZero2)) {
                    //sets dateZero to older date to be compared again
                    dateZero2 = dateCompare;
                }
            }
            //now after this block dateZero should be oldest formatted date, iterate to that date when making goal data
                if (dateZero2.before(dateZero)) {
                //sets dateZero to older date
                dateZero = dateZero2;
            }
            System.out.println("Oldest Date: "+dateZero);



            Calendar mCal = Calendar.getInstance();
            Float holdSets = 0f;
            int holdCals = 0;
            int holdProtein = 0;

            Boolean loopStarted = false;

            while(dateZero.before(mCal.getTime())){
                //loop backwards one day at a time from today

                int day = mCal.get(Calendar.DAY_OF_WEEK);

                if(day == Calendar.MONDAY) {
                   // System.out.println("day formatting" +df.format(mCal.getTime()));
                    calendarWeeks.add(shortFormat.format(mCal.getTime()));
                }

                    //Loop backwards and start collecting weeks on end week day == Sunday default
                if(day == Calendar.SUNDAY) {
                    //if sunday save previous week and reset looping totals
                    if(loopStarted) {
                        calWeekData.add(holdCals);
                        System.out.println("adding holdCals "+holdCals);
                        proteinWeekData.add(holdProtein);
                        mRecyclerView.post(new Runnable(){
                            @Override
                            public void run() {
                                mProgressAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                    loopStarted=true;

                    holdSets = 0f;
                    holdCals = 0;
                    holdProtein = 0;

                }
                if(loopStarted){
                    String mDate = df.format(mCal.getTime());
                    NutritionDay mDay = AppDatabase.getAppDatabase(getActivity()).ntDAO().findByDate(mDate);
                    if(mDay !=null) {
                        if (mDay.getValues().get(0) != null) {
                            holdCals = holdCals + mDay.getValues().get(0);
                        }
                        if (mDay.getValues().get(1) != null) {
                            holdProtein = holdProtein + mDay.getValues().get(1);
                        }
                    }
                }

                mCal.add(Calendar.DATE, -1);

            }


                return null;
            }
            protected void onPostExecute(Void unused) {
                // Post Code
                for(int i=0; i<calWeekData.size();i++){
                    System.out.println("grid data cals:" +calWeekData.get(i));
                }
                for(int i=0; i<proteinWeekData.size();i++){
                    System.out.println("grid data protein:" +proteinWeekData.get(i));
                }


                finishUI();
            }
        }.execute();

    }

    private void finishUI(){



    }

}