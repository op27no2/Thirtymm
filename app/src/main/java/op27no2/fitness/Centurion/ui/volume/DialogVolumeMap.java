package op27no2.fitness.Centurion.ui.volume;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rilixtech.materialfancybutton.MaterialFancyButton;

import java.util.ArrayList;

import op27no2.fitness.Centurion.Database.AppDatabase;
import op27no2.fitness.Centurion.Database.Repository;
import op27no2.fitness.Centurion.R;
import op27no2.fitness.Centurion.RecyclerItemClickListener;
import op27no2.fitness.Centurion.ui.lifting.LiftMap;

public class DialogVolumeMap extends Dialog  {

        public Context c;

        private RecyclerView mRecyclerView;
        private LinearLayoutManager mLayoutManager;
        private MyDialogVolumeAdapter mAdapter;
        private Repository mRepository;
        private TextView titleText;

        private DialogVolumeMapnterface mInterface;
        private Context mContext;
        private LiftMap mLiftMap;
        private String liftName;
        private int selected = 0;




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

        titleText = (TextView) findViewById(R.id.my_title);


        MaterialFancyButton pButton = findViewById(R.id.plus);
            pButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArrayList<Integer> mRats = mLiftMap.getRatios();
                    System.out.println("selected = "+selected);
                    if(selected == 0) {
                        for(int i=0; i<13; i++) {
                            Integer hold = mRats.get(i);
                            if (hold == 0) {
                                hold = 10;
                            } else {
                                hold = hold + 1;
                            }
                            mRats.set(i, hold);
                        }
                    }else if(selected == 13) {
                        for (int i = 13; i < 16; i++) {
                            Integer hold = mRats.get(i);
                            if (hold == 0) {
                                hold = 10;
                            } else {
                                hold = hold + 1;
                            }
                            mRats.set(i, hold);
                        }
                    }else if(selected == 16) {
                        for (int i = 16; i < 25; i++) {
                            Integer hold = mRats.get(i);
                            if (hold == 0) {
                                hold = 10;
                            } else {
                                hold = hold + 1;
                            }
                            mRats.set(i, hold);
                        }
                    }else if(selected == 25) {
                        for (int i = 25; i < 29; i++) {
                            Integer hold = mRats.get(i);
                            if (hold == 0) {
                                hold = 10;
                            } else {
                                hold = hold + 1;
                            }
                            mRats.set(i, hold);
                        }
                    }else if(selected == 29) {
                        for (int i = 29; i < 43; i++) {
                            Integer hold = mRats.get(i);
                            if (hold == 0) {
                                hold = 10;
                            } else {
                                hold = hold + 1;
                            }
                            mRats.set(i, hold);
                        }
                    }
                    else{
                        Integer hold = mRats.get(selected);
                        if (hold == 0) {
                            hold = 10;
                        } else {
                            hold = hold + 1;
                        }
                        mRats.set(selected, hold);
                    }


                    mLiftMap.setRatios(mRats);
                    mAdapter.notifyDataSetChanged();
                    mRepository.updateLiftMap(mLiftMap);
                }
            });

            MaterialFancyButton mButton = findViewById(R.id.minus);
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArrayList<Integer> mRats = mLiftMap.getRatios();
                    if(selected == 0) {
                        for (int i = 0; i < 13; i++) {
                            Integer hold = mRats.get(i);
                            hold = hold - 1;
                            if(hold<0){
                                hold = 0;
                            }
                            mRats.set(i, hold);
                        }
                    }else if(selected == 13) {
                        for (int i = 13; i < 16; i++) {
                            Integer hold = mRats.get(i);
                            hold = hold - 1;
                            if(hold<0){
                                hold = 0;
                            }
                            mRats.set(i, hold);
                        }
                    }else if(selected == 16) {
                        for (int i = 16; i < 25; i++) {
                            Integer hold = mRats.get(i);
                            hold = hold - 1;
                            if(hold<0){
                                hold = 0;
                            }
                            mRats.set(i, hold);
                        }
                    }else if(selected == 25) {
                        for (int i = 25; i <29; i++) {
                            Integer hold = mRats.get(i);
                            hold = hold - 1;
                            if(hold<0){
                                hold = 0;
                            }
                            mRats.set(i, hold);
                        }
                    }else if(selected == 29) {
                        for (int i = 29; i < 43; i++) {
                            Integer hold = mRats.get(i);
                            hold = hold - 1;
                            if(hold<0){
                                hold = 0;
                            }
                            mRats.set(i, hold);
                        }
                    }else{
                        Integer hold = mRats.get(selected);
                        hold = hold - 1;
                        if(hold<0){
                            hold = 0;
                        }
                        mRats.set(selected, hold);
                    }
                    mLiftMap.setRatios(mRats);
                    mAdapter.notifyDataSetChanged();
                    mRepository.updateLiftMap(mLiftMap);
                }
            });

        MaterialFancyButton mButton3 = findViewById(R.id.dismiss);
        mButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });



            DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
            int width = metrics.widthPixels;
            getWindow().setLayout((10 * width) / 11, LinearLayout.LayoutParams.WRAP_CONTENT);



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
                    if(mLiftMap == null){
                        System.out.println("MAP NULL");
                    }else{
                        System.out.println("MAP NOT NULL");
                    }

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
            mRecyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(getContext(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            System.out.println("item clicked: "+position);
                            selected = mAdapter.getRealPosition(position);

                            //This code will collapse/expand the headers on click
                            //I've decided we don't need this at the moment

/*
                            if(mAdapter.getLine(position).equals("Arms")){
                                for(int i=12; i>0; i--){
                                    mAdapter.toggleVisibilty(i);
                                }
                            }
                            if(mAdapter.getLine(position).equals("Chest")){
                                for(int i=15; i>13; i--){
                                    mAdapter.toggleVisibilty(i);
                                }
                            }
                            if(mAdapter.getLine(position).equals("Back")){
                                for(int i=24; i>16; i--){
                                    mAdapter.toggleVisibilty(i);
                                }
                            }
                            if(mAdapter.getLine(position).equals("Core")){
                                for(int i=28; i>25; i--){
                                    mAdapter.toggleVisibilty(i);
                                }
                            }
                            if(mAdapter.getLine(position).equals("Legs")){
                                for(int i=42; i>29; i--){
                                    mAdapter.toggleVisibilty(i);
                                }
                            }
*/

                            mAdapter.setSelected(position);
                            mAdapter.notifyDataSetChanged();

                        }
                        @Override
                        public void onItemLongClick(View view, int position) {

                        }
                    })
            );

        }



        public void finishUI(){
        System.out.println("lift map "+mLiftMap.getMuscles().size());
        System.out.println("lift map r "+mLiftMap.getRatios().size());
            ArrayList<Integer> mlist = new ArrayList<Integer>();
            for(int i=0; i<mLiftMap.getMuscles().size(); i++){
                mlist.add(i);
            }

            //set lift data to recyclerview
            mAdapter = new MyDialogVolumeAdapter(mLiftMap, mRepository, mContext,mlist);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            titleText.setText(mLiftMap.getName());

        }


    @Override
    protected void onStop(){

        System.out.println("ratios on-stop: "+mAdapter.getRatios());


     //   mLiftMap.setRatios(mAdapter.getRatios());
        mRepository.updateLiftMap(mLiftMap);
        mInterface.onDialogDismiss();

    }

    @Override
    public void onBackPressed(){
        dismiss();
    }



}