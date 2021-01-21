package op27no2.fitness.Centurion2.fragments.nutrition;

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

import op27no2.fitness.Centurion2.Database.AppDatabase;
import op27no2.fitness.Centurion2.Database.Repository;
import op27no2.fitness.Centurion2.R;
import op27no2.fitness.Centurion2.RecyclerItemClickListener;
import op27no2.fitness.Centurion2.fragments.activities.GoalsDetail;
import op27no2.fitness.Centurion2.fragments.volume.MyDialogVolumeAdapter;

public class DialogGoalList extends Dialog  {

        public Context c;

        private RecyclerView mRecyclerView;
        private LinearLayoutManager mLayoutManager;
        private MyDialogVolumeAdapter mAdapter;
        private Repository mRepository;
        private TextView titleText;

        private DialogGoalListInterface mInterface;
        private Context mContext;
        private ArrayList<GoalsDetail> mGoalList;
        private int selected = 0;




    public DialogGoalList(@NonNull Context context, Repository repositoy, DialogGoalListInterface dialogInterface) {
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
            setContentView(R.layout.dialog_volumemap);

        titleText = (TextView) findViewById(R.id.my_title);


        MaterialFancyButton pButton = findViewById(R.id.save);
            pButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                 /*   ArrayList<Integer> mRats = mLiftMap.getRatios();

                    mLiftMap.setRatios(mRats);
                    mAdapter.notifyDataSetChanged();
                    mRepository.updateLiftMap(mLiftMap);*/
                }
            });

            MaterialFancyButton mButton = findViewById(R.id.dismiss);
            mButton.setOnClickListener(new View.OnClickListener() {
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
                    mGoalList = new ArrayList<GoalsDetail>(AppDatabase.getAppDatabase(mContext).glDAO().getAll());
                    if(mGoalList == null){
                        //really this should be done on first run instead of prefs.
                        System.out.println("goals null should create");
                        GoalsDetail mDetail = new GoalsDetail( "Cals", 0,  -300, 0);
                        mGoalList.add(mDetail);
                        GoalsDetail mDetail4 = new GoalsDetail( "Protein", 2, 0,  (int) Math.floor(0.6f * 150));
                        mGoalList.add(mDetail4);
                        GoalsDetail mDetail5 = new GoalsDetail( "Sets", 2, 0,   15);
                        mGoalList.add(mDetail5);
                        AppDatabase.getAppDatabase(mContext).glDAO().insertAll(mGoalList);
                    }

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
            mRecyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(getContext(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            System.out.println("item clicked: "+position);
                            selected = mAdapter.getRealPosition(position);

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
        System.out.println("Goal List Size"+mGoalList.size());

        //set lift data to recyclerview

    /*    mAdapter = new MyDialogVolumeAdapter(mLiftMap, mRepository, mContext,mlist);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        titleText.setText(mLiftMap.getName());
*/
        }


    @Override
    protected void onStop(){

        System.out.println("ratios on-stop: "+mAdapter.getRatios());


     //   mLiftMap.setRatios(mAdapter.getRatios());
     //   mRepository.updateLiftMap(mLiftMap);
        mInterface.onDialogDismiss();

    }

    @Override
    public void onBackPressed(){
        dismiss();
    }



}