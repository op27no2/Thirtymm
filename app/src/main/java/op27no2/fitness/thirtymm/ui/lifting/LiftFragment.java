package op27no2.fitness.thirtymm.ui.lifting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import op27no2.fitness.thirtymm.Database.AppDatabase;
import op27no2.fitness.thirtymm.R;
import op27no2.fitness.thirtymm.RecyclerItemClickListener;

public class LiftFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MyLiftAdapter mLiftAdapter;
    private LiftingWorkout mLiftingWorkout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lift, container, false);

        //Get today's workout => finishUI
        //get today's workout, if it doesn't exist create it
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            mLiftingWorkout = AppDatabase.getAppDatabase(getActivity()).lwDAO().findByDate("today motherfucker");
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
        mLiftAdapter = new MyLiftAdapter(mLiftDataset[0]);
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
        mLiftAdapter = new MyLiftAdapter(mLiftDataset[0]);
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
                mWorkout.addLift("Dead Lift");
                mLiftDataset[0] = mWorkout.getLifts();
                mLiftAdapter.notifyDataSetChanged();
            }
        });





    }



}