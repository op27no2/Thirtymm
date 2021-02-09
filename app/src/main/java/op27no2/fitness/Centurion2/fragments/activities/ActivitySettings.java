package op27no2.fitness.Centurion2.fragments.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import op27no2.fitness.Centurion2.Database.AppDatabase;
import op27no2.fitness.Centurion2.Database.Repository;
import op27no2.fitness.Centurion2.R;
import op27no2.fitness.Centurion2.RecyclerItemClickListener;
import op27no2.fitness.Centurion2.fragments.nutrition.DialogGoalListInterface;
import op27no2.fitness.Centurion2.fragments.nutrition.GoalSettingListAdapter;


public class ActivitySettings extends Fragment implements DialogGoalListInterface {
    public Context mContext;
    private RecyclerView mRecyclerView;
    public LinearLayoutManager mLayoutManager;
    private GoalSettingListAdapter mAdapter;
    private SharedPreferences prefs;
    private SharedPreferences.Editor edt;
    private RelativeLayout mGoalsButton;
    private RelativeLayout mWeightButton;
    private RelativeLayout mAvatarButton;

    private Repository mRepository;
    private DialogGoalListInterface mInterface;
    private ArrayList<GoalsDetail> mGoalSettingList;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_activitysettings, container, false);
        mContext = getActivity();
        prefs = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        edt = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
        mRepository = new Repository(mContext);

        mGoalsButton = view.findViewById(R.id.goals);
        mWeightButton = view.findViewById(R.id.weight);
        mAvatarButton = view.findViewById(R.id.avatar);

  /*      mGoalsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new DialogGoalList(mContext, mRepository, mInterface);
              //  dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                dialog.show();
            }
        });*/
        mWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mAvatarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        System.out.println("item clicked: "+position);
                         /*   selected = mAdapter.getRealPosition(position);

                            mAdapter.setSelected(position);
                            mAdapter.notifyDataSetChanged();*/

                    }
                    @Override
                    public void onItemLongClick(View view, int position) {

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
                mGoalSettingList = new ArrayList<GoalsDetail>(AppDatabase.getAppDatabase(mContext).glDAO().getAll());
                return null;
            }
            protected void onPostExecute(Void unused) {
                // Post Code


                finishUI();
            }
        }.execute();



        return view;
    }

    public void finishUI(){
        System.out.println("Goal List Size"+mGoalSettingList.size());

        //set lift data to recyclerview

        mAdapter = new GoalSettingListAdapter(mGoalSettingList ,mRepository,mContext);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }


    @Override
    public void onDialogDismiss() {

    }


}

