package op27no2.fitness.thirtymm.ui.lifting;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import op27no2.fitness.thirtymm.Database.AppDatabase;
import op27no2.fitness.thirtymm.Database.Repository;
import op27no2.fitness.thirtymm.R;

public class DialogLifts extends Dialog  {

        public Context c;
        public Dialog d;
        public Button yes, no;
        private RecyclerView mRecyclerView;
        private LinearLayoutManager mLayoutManager;
        private MyDialogAdapter mAdapter;
        private Repository mRepository;
        private LiftingWorkout mLiftingWorkout;
        private int position;
        private DialogInterface mInterface;
        private Context mContext;
        private ArrayList<LiftMap> mList = new ArrayList<LiftMap>();
        private ArrayList<String> allMuscles = new ArrayList<String>();
        private ArrayList<Double> allRatios = new ArrayList<Double>();



    public DialogLifts(@NonNull Context context, Repository repositoy, LiftingWorkout lw, int p, DialogInterface dialogInterface) {
        super(context);
        c = context;
        mRepository = repositoy;
        mLiftingWorkout = lw;
        position = p;
        mInterface = dialogInterface;
        mContext = context;
    }


    @SuppressLint("StaticFieldLeak")
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_lift);
            allMuscles = new ArrayList(Arrays.asList((mContext.getResources().getStringArray(R.array.full_muscle_list))));
            for(int i=0; i<allMuscles.size(); i++){
                allRatios.add(0.0);
            }

            new AsyncTask<Void, Void, Void>() {
                //Get today's workout => finishUI
                //get today's workout, if it doesn't exist create it

                protected void onPreExecute() {
                    // Pre Code
                }
                protected Void doInBackground(Void... unused) {
                    mList = new ArrayList<LiftMap>(AppDatabase.getAppDatabase(mContext).lmDAO().getAll());
                    if(mList == null || mList.size()==0){
                        LiftMap mLiftMap = new LiftMap("Bench Press");
                        mLiftMap.setMuscles(allMuscles);
                        mLiftMap.setRatios(allRatios);
                        LiftMap mLiftMap2 = new LiftMap("Squat");
                        mLiftMap2.setMuscles(allMuscles);
                        mLiftMap2.setRatios(allRatios);
                        LiftMap mLiftMap3 = new LiftMap("Pull Ups");
                        mLiftMap3.setMuscles(allMuscles);
                        mLiftMap3.setRatios(allRatios);
                        mList.add(mLiftMap);
                        mList.add(mLiftMap2);
                        mList.add(mLiftMap3);
                        mRepository.insertLiftMap(mLiftMap);
                        mRepository.insertLiftMap(mLiftMap2);
                        mRepository.insertLiftMap(mLiftMap3);
                        mAdapter.notifyDataSetChanged();
                    }                return null;
                }
                protected void onPostExecute(Void unused) {
                    // Post Code
                    finishUI();
                }
            }.execute();


            EditText mEdit = findViewById(R.id.edit_text);
            ImageView addLift = findViewById(R.id.circle_add);
            addLift.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LiftMap mMap = new LiftMap(mEdit.getText().toString());
                    mMap.setMuscles(allMuscles);
                    mMap.setRatios(allRatios);
                    mList.add(mMap);
                    mRepository.insertLiftMap(mMap);
                    mEdit.setText("");
                    mAdapter.notifyDataSetChanged();
                }
            });

            //recyclerview and layoutmanager
            mRecyclerView = findViewById(R.id.my_recycler_view);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(c);
            mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setNestedScrollingEnabled(false);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());



        }


        public void finishUI(){
            //set lift data to recyclerview
            mAdapter = new MyDialogAdapter(mList, mRepository, mLiftingWorkout, position,mInterface);
            mRecyclerView.setAdapter(mAdapter);


        }

}