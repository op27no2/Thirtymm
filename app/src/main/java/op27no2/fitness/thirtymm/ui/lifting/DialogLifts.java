package op27no2.fitness.thirtymm.ui.lifting;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

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

    public DialogLifts(@NonNull Context context, Repository repositoy, LiftingWorkout lw, int p, DialogInterface dialogInterface) {
        super(context);
        c = context;
        mRepository = repositoy;
        mLiftingWorkout = lw;
        position = p;
        mInterface = dialogInterface;
    }


    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_lift);

            ArrayList<String> mLifts = new ArrayList<String>();
            mLifts.add("Dead Lift");
            mLifts.add("Bench Press");
            mLifts.add("Pull Ups");
            mLifts.add("Upright Row");
            mLifts.add("Dumbell Curls");
            mLifts.add("Tricep Pushdowns");
            mLifts.add("Squats");

            //recyclerview and layoutmanager
            mRecyclerView = findViewById(R.id.my_recycler_view);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(c);
            mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(mLayoutManager);

            //set lift data to recyclerview
            mAdapter = new MyDialogAdapter(mLifts, mRepository, mLiftingWorkout, position,mInterface);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setNestedScrollingEnabled(false);

            mRecyclerView.setItemAnimator(new DefaultItemAnimator());



/*
            dismiss();*/

        }


}