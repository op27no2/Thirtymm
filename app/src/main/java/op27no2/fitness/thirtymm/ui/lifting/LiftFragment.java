package op27no2.fitness.thirtymm.ui.lifting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import op27no2.fitness.thirtymm.Database.AppDatabase;
import op27no2.fitness.thirtymm.Database.Repository;
import op27no2.fitness.thirtymm.MyApplication;
import op27no2.fitness.thirtymm.R;
import op27no2.fitness.thirtymm.RecyclerItemClickListener;

public class LiftFragment extends Fragment{
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MyLiftWorkoutAdapter mLiftAdapter;
    private LiftingWorkout mLiftingWorkout;
    private Repository mRepository;
    private CardView addCard;
    private String formattedDate;
    private Vibrator rabbit;
    private TextView dateText;
    private Calendar cal;

    @SuppressLint("StaticFieldLeak")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lift, container, false);
        mRepository = new Repository(getActivity());
        rabbit = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        cal = Calendar.getInstance();
        Date c = cal.getTime();
        Long time = Calendar.getInstance().getTimeInMillis();
        System.out.println("Current time => " + time);
        SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d, ''yy");
        formattedDate = df.format(c);
        System.out.println("fomratted time => " + df.format(time));


        dateText = view.findViewById(R.id.toolbar_date);

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

        addCard = view.findViewById(R.id.card_view);
        addCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Lift mLift = new Lift("Bench Press", 225);
                mLift.addSet();
                mLift.addSet();
                mLift.addSet();
                mLiftingWorkout.addLift(mLift);
                mRepository.updateWorkout(mLiftingWorkout);
                mLiftAdapter.notifyDataSetChanged();
            }
        });


        //recyclerview and layoutmanager
        mRecyclerView = view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        getDayData();

/*        new AsyncTask<Void, Void, Void>() {
            //Get today's workout => finishUI
            //get today's workout, if it doesn't exist create it

            protected void onPreExecute() {
                // Pre Code
            }
            protected Void doInBackground(Void... unused) {
                mLiftingWorkout = AppDatabase.getAppDatabase(getActivity()).lwDAO().findByDate(formattedDate);
                if(mLiftingWorkout == null){
                    mLiftingWorkout = new LiftingWorkout();
                    mLiftingWorkout.setWorkoutDate(formattedDate);
                    mRepository.insertWorkout(mLiftingWorkout);
                }                return null;
            }
            protected void onPostExecute(Void unused) {
                // Post Code
                finishUI();
            }
        }.execute();*/


     //recyclerview on touch events

 /*    mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        System.out.println("item clicked: "+position);

                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        System.out.println("item long clicked: "+position);


                    }
                })
        );*/


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
                mLiftingWorkout = AppDatabase.getAppDatabase(getActivity()).lwDAO().findByDate(formattedDate);
                if(mLiftingWorkout == null){
                    mLiftingWorkout = new LiftingWorkout();
                    mLiftingWorkout.setWorkoutDate(formattedDate);
                    mRepository.insertWorkout(mLiftingWorkout);
                }                return null;
            }
            protected void onPostExecute(Void unused) {
                // Post Code
                finishUI();
            }
        }.execute();

    }


    private void finishUI(){
        dateText.setText(formattedDate);

        //set lift data to recyclerview
        mLiftAdapter = new MyLiftWorkoutAdapter(mLiftingWorkout, mRepository);
        mRecyclerView.setAdapter(mLiftAdapter);

        //recyclerview on touch events
   /*     mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        System.out.println("item clicked: "+position);

                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        System.out.println("item long clicked: "+position);


                    }
                })
        );*/






    }







}