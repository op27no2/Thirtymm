/*
package op27no2.fitness.Centurion2.fragments.run;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.Collator;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import op27no2.fitness.Centurion2.Database.AppDatabase;
import op27no2.fitness.Centurion2.Database.Repository;
import op27no2.fitness.Centurion2.R;
import op27no2.fitness.Centurion2.RecyclerItemClickListener;
import op27no2.fitness.Centurion2.fragments.lifting.LiftMap;
import op27no2.fitness.Centurion2.fragments.lifting.LiftingWorkout;
import op27no2.fitness.Centurion2.fragments.lifting.MyDialogAdapter;
import op27no2.fitness.Centurion2.fragments.lifting.MyDialogInterface;
import op27no2.fitness.Centurion2.fragments.lifting.SortAdapter;

public class AlertDialogChoose extends Dialog  {

        public Context c;
        private SharedPreferences.Editor edt;
        public Dialog d;
        public Button yes, no;
        private ArrayList<AlertProfile> mAlertProfiles = new ArrayList<AlertProfile>();
        private RecyclerView mRecyclerView;
        private LinearLayoutManager mLayoutManager;
        public AlertDialogAdapter mAdapter;



        private Repository mRepository;
        private LiftingWorkout mLiftingWorkout;
        private int parentposition;
        private MyDialogInterface mInterface;
        private Context mContext;
        private int selected;


    public AlertDialogChoose(@NonNull Context context, Repository repositoy, MyDialogInterface dialogInterface) {
        super(context);
        c = context;
        mRepository = repositoy;

        mInterface = dialogInterface;
        mContext = context;
    }


    @SuppressLint("StaticFieldLeak")
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_alerts);

            edt = c.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
            edt.putBoolean("dialog_opened", true);
            edt.commit();

            mRecyclerView2 = findViewById(R.id.my_recycler_view);
            mLayoutManager2 = new LinearLayoutManager(c);
            mLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView2.setLayoutManager(mLayoutManager2);
            mRecyclerView2.setNestedScrollingEnabled(false);
            mRecyclerView2.setItemAnimator(new DefaultItemAnimator());
            mAdapter2 = new AlertDialogAdapter();

            mRecyclerView2.setAdapter(mAdapter2);
            mRecyclerView2.addOnItemTouchListener(
                    new RecyclerItemClickListener(c, mRecyclerView2, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override public void onItemClick(View view, int position) {
                            System.out.println("segment item clicked: "+position);
                        }

                        @Override
                        public void onItemLongClick(View view, int position) {
                            System.out.println("segment item long clicked: "+position);
                        }
                    })
            );




            new AsyncTask<Void, Void, Void>() {
                //Get today's workout => finishUI
                //get today's workout, if it doesn't exist create it

                protected void onPreExecute() {
                    // Pre Code
                }
                protected Void doInBackground(Void... unused) {
                    System.out.println("setting base list");

                    mAlertProfiles = new ArrayList<AlertProfile>(AppDatabase.getAppDatabase(mContext).lmDAO().getAll());

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

                        alphabetize(mList);
                        mAdapter = new AlertDialogAdapter(mContext, mRepository, mAlertProfiles);
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

            System.out.println("finish ui");

            mAdapter = new MyDialogAdapter(mContext, mRepository, mLiftingWorkout, parentposition,mInterface);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter2.setSelected(1000);





        }


        private void alphabetize(ArrayList<LiftMap> mList){
            Collator collator = Collator.getInstance(Locale.US);
            if (!mList.isEmpty()) {
                Collections.sort(mList, new Comparator<LiftMap>() {
                    @Override
                    public int compare(LiftMap c1, LiftMap c2) {
                        //You should ensure that list doesn't contain null values!
                        return collator.compare(c1.getName(), c2.getName());
                    }
                });
            }
        }

}*/
