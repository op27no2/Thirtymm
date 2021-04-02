package op27no2.fitness.Centurion2.fragments.activities;

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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import op27no2.fitness.Centurion2.Database.AppDatabase;
import op27no2.fitness.Centurion2.Database.Repository;
import op27no2.fitness.Centurion2.R;
import op27no2.fitness.Centurion2.RecyclerItemClickListener;
import op27no2.fitness.Centurion2.fragments.lifting.Lift;
import op27no2.fitness.Centurion2.fragments.lifting.LiftMap;
import op27no2.fitness.Centurion2.fragments.lifting.LiftingWorkout;
import op27no2.fitness.Centurion2.fragments.nutrition.NutritionDay;


public class ChildProgressFragment extends Fragment {
    private SharedPreferences prefs;
    private SharedPreferences.Editor edt;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private Repository mRepository;
    private ProgressAdapter mProgressAdapter;
    private ArrayList<NutritionDay> mDays = new ArrayList<NutritionDay>();
    private ArrayList<LiftingWorkout> mWorkouts = new ArrayList<LiftingWorkout>();
    private ArrayList<ArrayList<GoalsDetail>> mBigGoalsDetail = new ArrayList<ArrayList<GoalsDetail>>();
    private ArrayList<Integer> goalChangePositions = new ArrayList<Integer>();
    private ArrayList<String> calendarWeeks = new ArrayList<String>();
    private Context mContext;
    private Date today;
    private GridView grid;
    private HashMap<String, Integer> mVolume = new HashMap<String, Integer>();
    private ArrayList<Map.Entry<String, Integer>> listOfLiftNamesAndSets;
    private HashMap<String, Double> muscleVolumes = new HashMap<String, Double>();
    private ArrayList<GoalsDetail> mGoalList = new ArrayList<GoalsDetail>();


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
        grid = view.findViewById(R.id.grid_view);


        Resources res = mContext.getResources();
        ResourcesCompat.getColor(res, R.color.colorPrimary, null);



        //currently the top title Row for goals uses GridAdapterHeader, below that progress adapter has data for each row, but within progressadapter each row uses GridAdapter to display contents...
/*
        ArrayList<Double> headerGrid = new ArrayList<Double>();
        headerGrid.add((double) prefs.getInt("volume",0));
        headerGrid.add((double) prefs.getInt("deficit", 0));
        double x = ((double) prefs.getInt("weight", 0))*((double) prefs.getFloat("protein", 0));
        double xx = (double) Math.floor(x);
        headerGrid.add(xx);*/

        Calendar mCal1 = Calendar.getInstance();
        today = mCal1.getTime();


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

        mProgressAdapter = new ProgressAdapter(mBigGoalsDetail, calendarWeeks, goalChangePositions, getActivity());
        mRecyclerView.setAdapter(mProgressAdapter);






        return view;
    }


    public void onResume(){
        super.onResume();
        mBigGoalsDetail.clear();
        calendarWeeks.clear();
        goalChangePositions.clear();

        getDayData();
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
             //this works now!
             NutritionDay minDate = AppDatabase.getAppDatabase(getActivity()).ntDAO().findMinimumDate();
             System.out.println("minimum date found: "+ minDate.getDate());
             System.out.println("minimum date found mils: "+ minDate.getDateMillis());
             Date dateZero = null;
             try { dateZero = df.parse(minDate.getDate());
             }catch(ParseException e){ e.printStackTrace();
             }

             //so for new algorithm, we are storing the weeks goals on NutrtionDay, so once Sunday is reached, Goal will be locked in
            //not collecting GoalsDetail every day backwards, just goal data to add to the gaolsdetail weektotal, goaldetail stays Sundays, or first day.
             //look to Sundays for the weeks goal, and total weeks values to see if they were met
             //not necessarily opened every week, so keep goal until new goal on Sunday detected.
             Calendar mCal = Calendar.getInstance();
             mCal.setTime(today);
             ArrayList<Integer> weekTotals = new ArrayList<Integer>(2);
             for(int i=0; i<2;i++){ //change to i<mvalues.size()
                 weekTotals.add(0);
             }

                System.out.println("date Zero: "+ dateZero);
                System.out.println("date today: "+ today);

                SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");

                Boolean goalsForWeekFound = false;

                //loop backwards till we get to day zero
                while(!fmt.format(mCal.getTime()).equals(fmt.format(dateZero))){

                    //TODO change this to mDay.getvalues.Size()?
                    if(weekTotals.size() == 0){
                        for(int i=0; i<3;i++){ //change to i<mvalues.size()
                            weekTotals.add(0);
                        }
                    }


                    //get nutritionday for that day
                    String mDate = df.format(mCal.getTime());
                    System.out.println("date: "+ mDate);
                    NutritionDay mDay = AppDatabase.getAppDatabase(getActivity()).ntDAO().findByDate(mDate);

                    //get Lifting info for that day
                    LiftingWorkout mLiftDay = AppDatabase.getAppDatabase(getActivity()).lwDAO().findByDate(mDate);

                    if(mLiftDay != null) {
                        ArrayList<Lift> mLifts = mLiftDay.getMyLifts();
                        //add to weekly total for Volume
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



                    //if today isn't sunday first loop needs goals from today, not Sunday.

                    //WE are counting backwards from the day. Each day may not have goals if app was not logged in on that day.
                    //The first day counting backward for that week should be the goals for that week
                    //That is, if Sunday has goals, those were the last in the week and will be set
                    //If Sunday did not have goals, nor saturday, but Friday did, we would take those for the week isntead.
                    //on Monday, total everything up and then reset

                    //If no goals for the week, no DATA and will be grey?


                    Calendar mcal2 = Calendar.getInstance();
                    mcal2.setTime(today);


                    if(goalsForWeekFound == false){

                        //should be today on first loop, Sundays thereafter.
                        mGoalList = mDay.getGoalList();
                        System.out.println("iterate day test: "+ mDay.getDate());
                        if(mGoalList != null) {
                            System.out.println("goal list not null");
                            System.out.println("goal list size: "+mGoalList.size());
                            goalsForWeekFound = true;
                        }else{
                            System.out.println("goal list null");
                        }
                    //else if it is Sunday moving backwards, we are getting goals to count backward for the week.
                    }


                //whatever day it is, we are adding the day's data to the week totals.
                 if(mDay !=null && mDay.getValues().size() != 0 && mDay.getNames().size() != 0) {
                     if (mDay.getValues().get(0) != null) {
                         Integer newvalue = weekTotals.get(0) + mDay.getValues().get(0);
                         weekTotals.set(0, newvalue);
                     }
                     if (mDay.getValues().get(1) != null) {
                         Integer newvalue = weekTotals.get(1) + mDay.getValues().get(1);
                         weekTotals.set(1, newvalue);
                     }
                 }



                //if its Monday, add the week string to the labels, and save week totals since we are moving backwards.
                    if(mCal.get(Calendar.DAY_OF_WEEK)  == Calendar.MONDAY) {
                        calendarWeeks.add(shortFormat.format(mCal.getTime()));
                        System.out.println("should add week = "+mCal.getTime());

                        convertVolumeToWeekGoal(mVolume);

                        for (int i = 0; i < mGoalList.size(); i++) {
                            //go through each goal and evaluate, store data for the row. can look back to the last Monday
                            //now just going by position, but can eventually search for name and match to nutritiondays.getNames()? or better solution
                            String name = mGoalList.get(i).getGoalName();
                            // day has 2 arrays, names i.e. cals, protein, and values. Goals has names, find the matching index
                            int index = mDay.getNames().indexOf(name);
                            if(index>=0) {
                                mGoalList.get(i).setWeekTotal((float) weekTotals.get(index));
                                System.out.println("nametest setting week total= "+weekTotals.get(index));
                            }
                            System.out.println("nametest = "+name);
                            System.out.println("nametest index= "+index);
                            System.out.println("nametest week get= "+mGoalList.get(i).getWeekTotal());


                        }

                        ArrayList<GoalsDetail> toAdd = new ArrayList<GoalsDetail>(mGoalList);
                        mBigGoalsDetail.add(toAdd);

                        //update adapter and clear stuff for next week counting backwards starting Sunday.
                        mRecyclerView.post(new Runnable(){
                            @Override
                            public void run() {
                                 mProgressAdapter.notifyDataSetChanged();

                            }
                        });


                        mGoalList.clear();
                        weekTotals.clear();
                        mVolume.clear();
                        listOfLiftNamesAndSets.clear();
                        muscleVolumes.clear();
                        goalsForWeekFound = false;
                    }





                System.out.println("Day Processed, current Big Goal List Size = "+mBigGoalsDetail.size());
                if(mBigGoalsDetail.size()!=0) {
                    System.out.println("size of each goal list entry");
                    for(int i=0; i< mBigGoalsDetail.size(); i++) {
                        System.out.println("i="+i+" :"+mBigGoalsDetail.get(i).size());
                    }
                }

                //TODO this is just reporting for testin gplease delete
                    for(int i=0; i< mBigGoalsDetail.size(); i++){
                        for(int j=0; j< mBigGoalsDetail.get(i).size(); j++){
                            System.out.println("week "+i+" goal name: "+mBigGoalsDetail.get(i).get(j).getGoalName());
                            System.out.println("week "+i+" goal total: " +mBigGoalsDetail.get(i).get(j).getWeekTotal());
                        }
                    }



                 mCal.add(Calendar.DATE, -1);
                 //TODO wouldn't loop again if below true, so check here and do any final things, or maybe loop to include one more day above?
                 if(fmt.format(mCal.getTime()).equals(fmt.format(dateZero))){
                     System.out.println("thats last day yo");
                     break;
                     //do something on today to show this week so far, this is last iteration
                 }
             }


            for(int i=0; i<mBigGoalsDetail.size()-1;i++){
                if(!mBigGoalsDetail.get(i+1).equals(mBigGoalsDetail.get(i))){
                    if(mBigGoalsDetail.get(i).size() == mBigGoalsDetail.get(i+1).size()){
                        System.out.println("Goals Size Same");
                        boolean checkSame = true;
                       for(int j=0; j<mBigGoalsDetail.get(i).size();j++){
                        /*   System.out.println("goal 1 name:"+mBigGoalsDetail.get(i).get(j).getGoalName());
                           System.out.println("goal 2 name:"+mBigGoalsDetail.get(i+1).get(j).getGoalName());
                           if(mBigGoalsDetail.get(i).get(j).getGoalName().equals(mBigGoalsDetail.get(i+1).get(j).getGoalName())){
                               System.out.println("true");
                           }else{
                               System.out.println("false");
                           }
                           System.out.println("goal 1 type:"+mBigGoalsDetail.get(i).get(j).getGoalType());
                           System.out.println("goal 2 type:"+mBigGoalsDetail.get(i+1).get(j).getGoalType());
                           if(mBigGoalsDetail.get(i).get(j).getGoalType().equals(mBigGoalsDetail.get(i+1).get(j).getGoalType())){
                               System.out.println("true");
                           }else{
                               System.out.println("false");
                           }
                           System.out.println("goal 1 low:"+mBigGoalsDetail.get(i).get(j).getGoalLimitLow());
                           System.out.println("goal 2 low:"+mBigGoalsDetail.get(i+1).get(j).getGoalLimitLow());
                           if(mBigGoalsDetail.get(i).get(j).getGoalLimitLow().equals(mBigGoalsDetail.get(i+1).get(j).getGoalLimitLow())){
                               System.out.println("true");
                           }else{
                               System.out.println("false");
                           }
                           System.out.println("goal 1 high:"+mBigGoalsDetail.get(i).get(j).getGoalLimitHigh());
                           System.out.println("goal 2 high:"+mBigGoalsDetail.get(i+1).get(j).getGoalLimitHigh());
                           if(mBigGoalsDetail.get(i).get(j).getGoalLimitHigh().equals(mBigGoalsDetail.get(i+1).get(j).getGoalLimitHigh())){
                               System.out.println("true");
                           }else{
                               System.out.println("false");
                           }*/


                           if(mBigGoalsDetail.get(i).get(j).getGoalName().equals(mBigGoalsDetail.get(i+1).get(j).getGoalName()) && mBigGoalsDetail.get(i).get(j).getGoalType().equals(mBigGoalsDetail.get(i+1).get(j).getGoalType()) &&
                                   mBigGoalsDetail.get(i).get(j).getGoalLimitLow().equals(mBigGoalsDetail.get(i+1).get(j).getGoalLimitLow()) && mBigGoalsDetail.get(i).get(j).getGoalLimitHigh().equals(mBigGoalsDetail.get(i+1).get(j).getGoalLimitHigh())){
                           //the are the same do nothing
                           }else{
                               checkSame = false;
                               System.out.println("Goals Diff");
                           }
                       }
                        if(checkSame == false) {
                            //goals for the two weeks are not the same, log number to make sure you display header row
                            goalChangePositions.add(i + 1);
                        }


                    }


                }
            }


                //old algorithm


 /*           Float holdSets = 0f;
            int holdCals = 0;
            int holdProtein = 0;

            Boolean loopStarted = false;

            while(dateZero.before(mCal.getTime())){
                //loop backwards one day at a time from today

                int day = mCal.get(Calendar.DAY_OF_WEEK);

                if(day == Calendar.MONDAY) {
                   // System.out.println("day formatting" +df.format(mCal.getTime()));
                    //counts back to monday, adds the monday string to a string array, collects all the monday strings
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
            }*/


                return null;
            }
            protected void onPostExecute(Void unused) {


                finishUI();
            }
        }.execute();

    }

    private void finishUI(){

        //TODO getting most recent row for now
        ArrayList<String> titles = new ArrayList<String>();
        Resources res = mContext.getResources();
        int j = mBigGoalsDetail.size();
        if(j>=1) {
            for (int i = 0; i < mBigGoalsDetail.get(j - 1).size(); i++) {
                titles.add(mBigGoalsDetail.get(j - 1).get(i).getGoalName());
            }
        }else{
            titles.add("test");
            titles.add("test2");
            titles.add("test3");
        }
            MyGridHeaderAdapter gridAdapter = new MyGridHeaderAdapter(mContext, titles, res);
            grid.setNumColumns(titles.size());
            grid.setAdapter(gridAdapter);

        mProgressAdapter.notifyDataSetChanged();

    }

    //returns a float based on success. greater than 1 should indicate success, <1 is a percent failure for color map
    private float checkSuccess(int value, GoalsDetail detail){
        float result = 0f;
        if(detail.getGoalType() == 0){
            result = detail.getGoalLimitLow()/value; //e.g  100/50 will be 2 for a deficit goal. if you go over, 100/150, you are below 1 and we can colorize, this isn't really linear currently, hard to get low on scale
        }
        if(detail.getGoalType() == 1){
            if(value > detail.getGoalLimitLow() && value<detail.getGoalLimitHigh()){
                result = 1;
            }else if(value < detail.getGoalLimitLow()){
                result = value/detail.getGoalLimitLow();                  //50/100
            }else if(value > detail.getGoalLimitHigh()){
                result = detail.getGoalLimitHigh()/value;    //e.g. again 100/150 gives low numbers if you go over
            }
        }
        if(detail.getGoalType() == 2) {
            result = value/detail.getGoalLimitHigh();    //want to go over, so under is<1, i.e. 50/100 if you don't get enough
        }
        return result;
    }


    private int convertVolumeToWeekGoal(HashMap<String, Integer> VolumeFromLifts){
        int result = 0;
        for (Map.Entry<String, Integer> entry : mVolume.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
        Set<Map.Entry<String, Integer>> entrySet = mVolume.entrySet();
        listOfLiftNamesAndSets = new ArrayList<Map.Entry<String, Integer>>(entrySet);
        //get all info and set colors
        for(int j = 0; j< listOfLiftNamesAndSets.size(); j++){
            String liftName = listOfLiftNamesAndSets.get(j).getKey();
            Integer numSets = listOfLiftNamesAndSets.get(j).getValue();
            LiftMap mMap = AppDatabase.getAppDatabase(getActivity()).lmDAO().findByName(liftName);
            if(mMap != null) {
                ArrayList<String> names = mMap.getMuscles();
                ArrayList<Integer> ratios = mMap.getRatios();
                if (mMap.getRatios() != null) {
                    for (int i = 0; i < mMap.getRatios().size(); i++) {
                        //TODO probably better to just remove these from list than check during iteration
                        if(!names.get(i).equals("Chest") && !names.get(i).equals("Back")&& !names.get(i).equals("Core")&& !names.get(i).equals("Arms")&& !names.get(i).equals("Legs")) {
                            if (muscleVolumes.containsKey(names.get(i))) {
                                Double hold = muscleVolumes.get(names.get(i));
                                //dividing by 10 because stored as integers
                                hold = hold + ((double) ratios.get(i) / 10) * numSets;
                                muscleVolumes.put(names.get(i), hold);
                            } else {
                                //dividing by 10 because stored as integers
                                muscleVolumes.put(names.get(i), ((double) ratios.get(i) / 10) * numSets);
                            }
                        }
                    }
                }
            }else{
                System.out.println("LIFT MAP NULL: "+liftName);
            }
        }

        Double avgsets = 0.0;
        for (Double value : muscleVolumes.values()) {
            avgsets = avgsets+value;
        }
        float totalWeekSets = (float) (avgsets/38);

        mGoalList.get(2).setWeekTotal(totalWeekSets);


        return result;
    }



}