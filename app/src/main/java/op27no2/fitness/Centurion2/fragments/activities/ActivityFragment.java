package op27no2.fitness.Centurion2.fragments.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import op27no2.fitness.Centurion2.Database.AppDatabase;
import op27no2.fitness.Centurion2.Database.Repository;
import op27no2.fitness.Centurion2.MyAdapter;
import op27no2.fitness.Centurion2.R;
import op27no2.fitness.Centurion2.fragments.run.RunWorkout;

public class ActivityFragment extends Fragment {
    private SharedPreferences prefs;
    private SharedPreferences.Editor edt;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private RunWorkout mRunWorkout;
    private ArrayList<RunWorkout> mRunWorkouts;
    private Calendar cal;
    private String formattedDate;
    private RunCardviewWorkoutAdapter mRunAdapter;
    private Bundle mState;
    private Repository mRepository;

    private TabLayout tabLayout;
    private ViewPager viewPager;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_activity, container, false);
        prefs = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        edt = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();

        tabLayout=(TabLayout) view.findViewById(R.id.tabLayout);
        viewPager=(ViewPager) view.findViewById(R.id.viewPager);

        tabLayout.addTab(tabLayout.newTab().setText("Progress"));
        tabLayout.addTab(tabLayout.newTab().setText("Activities"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final MyAdapter adapter = new MyAdapter(getActivity(),getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setCurrentItem(prefs.getInt("tab_selected",0));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                edt.putInt("tab_selected",tab.getPosition());
                edt.apply();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });






        mRepository = new Repository(getActivity());



        cal = Calendar.getInstance();
        Date c = cal.getTime();
        Long time = Calendar.getInstance().getTimeInMillis();
        System.out.println("Current time => " + time);
        SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d, ''yy");
        formattedDate = df.format(c);
        System.out.println("fomratted time => " + df.format(time));

        mState = savedInstanceState;
/*
        RelativeLayout activityLayout = view.findViewById(R.id.activity_layout);
        RelativeLayout progressLayout = view.findViewById(R.id.progress_layout);
        MaterialFancyButton activityButton = view.findViewById(R.id.activities);
        MaterialFancyButton progressButton = view.findViewById(R.id.progress);

        if(prefs.getInt("activity_selected",0) == 0) {
            activityButton.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
            progressButton.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
        }else if(prefs.getInt("activity_selected",0) == 1){
            progressButton.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
            activityButton.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
        }


        activityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityLayout.setVisibility(View.VISIBLE);
                progressLayout.setVisibility(View.GONE);
                activityButton.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
                progressButton.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
                edt.putInt("activity_selected",0);
                edt.apply();
            }
        });
        progressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressLayout.setVisibility(View.VISIBLE);
                activityLayout.setVisibility(View.GONE);
                progressButton.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
                activityButton.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
                edt.putInt("activity_selected",1);
                edt.apply();
            }
        });


        //recyclerview and layoutmanager
        mRecyclerView = view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        System.out.println("item clicked: "+position);

                        ((MainActivity)getActivity()).goToRunDetail(mRunWorkouts.get(position).getUid());


                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        System.out.println("item long clicked: "+position);

                        final Dialog dialog = new Dialog(getActivity());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_delete);

                        DisplayMetrics metrics = getResources().getDisplayMetrics();
                        int width = metrics.widthPixels;

                        dialog.getWindow().setLayout((8 * width) / 9, LinearLayout.LayoutParams.WRAP_CONTENT);
                        TextView mText = dialog.findViewById(R.id.confirm_title);

                        dialog.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mRepository.deleteRunWorkout(mRunWorkouts.get(position));
                                mRunWorkouts.remove(position);
                                mRunAdapter.notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        });
                        dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                    }
                })
        );




        getDayData();*/






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

                mRunWorkouts = (ArrayList<RunWorkout>) AppDatabase.getAppDatabase(getActivity()).rwDAO().getAll();

                return null;
            }
            protected void onPostExecute(Void unused) {
                // Post Code
                finishUI();
            }
        }.execute();

    }

    private void finishUI(){
     //   dateText.setText(formattedDate);

        //set lift data to recyclerview
        mRunAdapter = new RunCardviewWorkoutAdapter(mRunWorkouts, getActivity());
        mRecyclerView.setAdapter(mRunAdapter);
    }



}

