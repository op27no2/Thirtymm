package op27no2.fitness.thirtymm.ui.volume;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import op27no2.fitness.thirtymm.Database.AppDatabase;
import op27no2.fitness.thirtymm.R;
import op27no2.fitness.thirtymm.ui.history.NotificationsViewModel;
import op27no2.fitness.thirtymm.ui.lifting.Lift;
import op27no2.fitness.thirtymm.ui.lifting.LiftingWorkout;
import op27no2.fitness.thirtymm.ui.lifting.MyDialogAdapter;

public class VolumeFragment extends Fragment {
    private ArrayList<LiftingWorkout> weeksWorkouts = new ArrayList<LiftingWorkout>();
    private SimpleDateFormat df;
    private HashMap<String, Integer> mVolume = new HashMap<String, Integer>();
    private ArrayList<Map.Entry<String, Integer>> listOfEntry;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private VolumeAdapter mAdapter;

    @SuppressLint("StaticFieldLeak")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_volume, container, false);
        df= new SimpleDateFormat("EEE, MMM d, ''yy");

        mRecyclerView = view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        new AsyncTask<Void, Void, Void>() {
            protected void onPreExecute() {
                // Pre Code
            }
            protected Void doInBackground(Void... unused) {

                Calendar cal = Calendar.getInstance();
                Date date = cal.getTime();
                String formattedDate = df.format(date);
                LiftingWorkout mLiftingWorkout = AppDatabase.getAppDatabase(getActivity()).lwDAO().findByDate(formattedDate);
                weeksWorkouts.add(mLiftingWorkout);

                while(cal.get(Calendar.DAY_OF_WEEK) != 2){
                    cal.add(Calendar.DATE, -1);
                    Date mDate = cal.getTime();
                    String mformattedDate = df.format(mDate);
                    System.out.println("task dates " + mformattedDate);
                    LiftingWorkout mWorkout = AppDatabase.getAppDatabase(getActivity()).lwDAO().findByDate(mformattedDate);
                    if(mWorkout != null) {
                        weeksWorkouts.add(mWorkout);
                    }

                }


                return null;
            }
            protected void onPostExecute(Void unused) {
                // Post Code
                otherStuff();
            }
        }.execute();





        return view;
    }

    public void otherStuff(){
        for(int i=0; i<weeksWorkouts.size(); i++){
            ArrayList<Lift> mLifts = weeksWorkouts.get(i).getMyLifts();
            for(int j=0; j<mLifts.size();j++){
                String name = mLifts.get(j).getName();
                int sets = mLifts.get(j).getReps().size();
                if(mVolume.containsKey(name)){
                    int hold = mVolume.get(name);
                    hold = hold + sets;
                    mVolume.put(name, hold);
                }else{
                    mVolume.put(name, sets);
                }
            }
        }

        for (Map.Entry<String, Integer> entry : mVolume.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }

        Set<Map.Entry<String, Integer>> entrySet = mVolume.entrySet();
        listOfEntry = new ArrayList<Map.Entry<String, Integer>>(entrySet);


        //set lift data to recyclerview
        mAdapter = new VolumeAdapter(listOfEntry);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


    }



}

