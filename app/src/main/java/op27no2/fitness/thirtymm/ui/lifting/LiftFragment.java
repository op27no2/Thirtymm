package op27no2.fitness.thirtymm.ui.lifting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class LiftFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MyLiftWorkoutAdapter mLiftAdapter;
    private LiftingWorkout mLiftingWorkout;
    private Repository mRepository;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lift, container, false);
        mRepository = new Repository(getActivity());

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d, ''yy");
        String formattedDate = df.format(c);


        //Get today's workout => finishUI
        //get today's workout, if it doesn't exist create it
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            mLiftingWorkout = AppDatabase.getAppDatabase(getActivity()).lwDAO().findByDate(formattedDate);
            if(mLiftingWorkout == null){
                mLiftingWorkout = new LiftingWorkout();
                mLiftingWorkout.setWorkoutDate(formattedDate);
                mRepository.insertWorkout(mLiftingWorkout);
            }
            finishUI(view);
        });


/*        final WorkoutLift mWorkout2 = new WorkoutLift();
        mWorkout2.addLift("Bench Press");
        mWorkout2.addLift("Pull Ups");
        mWorkout2.addLift("Dead Lift");*/

      //  final ArrayList<WorkoutLift.Lift>[] mLiftDataset = new ArrayList[]{mWorkout.getLifts()};


        //recyclerview and layoutmanager
       /* mRecyclerView = view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //set lift data to recyclerview
        mLiftAdapter = new MyLiftWorkoutAdapter(mLiftDataset[0]);
        mRecyclerView.setAdapter(mLiftAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //recyclerview on touch events
   *//*     mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        System.out.println("item clicked: "+position);

                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        System.out.println("item long clicked: "+position);


                    }
                })
        );*//*


        CardView addCard = view.findViewById(R.id.card_view);
        addCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWorkout.addLift("Dead Lift");
                mLiftDataset[0] = mWorkout.getLifts();
                mLiftAdapter.notifyDataSetChanged();
            }
        });*/



        return view;
    }


    private void finishUI(View view){
        final ArrayList<Lift>[] mLiftDataset = new ArrayList[]{mLiftingWorkout.getMyLifts()};

        //recyclerview and layoutmanager
        mRecyclerView = view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //set lift data to recyclerview
        mLiftAdapter = new MyLiftWorkoutAdapter(mLiftingWorkout, mRepository);
        mRecyclerView.setAdapter(mLiftAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

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

        CardView addCard = view.findViewById(R.id.card_view);
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





    }



}