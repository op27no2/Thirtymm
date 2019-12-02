package op27no2.fitness.thirtymm.ui.volume;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import op27no2.fitness.thirtymm.Database.AppDatabase;
import op27no2.fitness.thirtymm.Database.Repository;
import op27no2.fitness.thirtymm.R;
import op27no2.fitness.thirtymm.ui.lifting.LiftMap;
import op27no2.fitness.thirtymm.ui.lifting.LiftingWorkout;
import op27no2.fitness.thirtymm.ui.lifting.MyDialogAdapter;

public class DialogVolumeMap extends Dialog  {

        public Context c;
        private RecyclerView mRecyclerView;
        private LinearLayoutManager mLayoutManager;
        private MyDialogVolumeAdapter mAdapter;
        private Repository mRepository;


        private DialogVolumeMapnterface mInterface;
        private Context mContext;
        private LiftMap mLiftMap;
        private String liftName;



    public DialogVolumeMap(@NonNull Context context, Repository repositoy, String lift,  DialogVolumeMapnterface dialogInterface) {
        super(context);
        c = context;
        mRepository = repositoy;
        mInterface = dialogInterface;
        mContext = context;
        liftName = lift;
    }


    @SuppressLint("StaticFieldLeak")
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_volumemap);


            new AsyncTask<Void, Void, Void>() {
                //Get today's workout => finishUI
                //get today's workout, if it doesn't exist create it

                protected void onPreExecute() {
                    // Pre Code
                }
                protected Void doInBackground(Void... unused) {
                    mLiftMap = AppDatabase.getAppDatabase(mContext).lmDAO().findByName(liftName);


                    return null;
                }
                protected void onPostExecute(Void unused) {
                    // Post Code
                    finishUI();
                }
            }.execute();




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
            mAdapter = new MyDialogVolumeAdapter(mLiftMap, mRepository, mContext);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

        }


    @Override
    protected void onStop(){

        System.out.println(mAdapter.getRatios());
        mLiftMap.setRatios(mAdapter.getRatios());
        mRepository.updateLiftMap(mLiftMap);

        mInterface.onDialogDismiss();

    }



}