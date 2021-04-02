package op27no2.fitness.Centurion2.fragments.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import op27no2.fitness.Centurion2.Database.AppDatabase;
import op27no2.fitness.Centurion2.Database.Repository;
import op27no2.fitness.Centurion2.R;
import op27no2.fitness.Centurion2.RecyclerItemClickListener;
import op27no2.fitness.Centurion2.ValueDialog;
import op27no2.fitness.Centurion2.ValueDialogInterface;
import op27no2.fitness.Centurion2.fragments.nutrition.DialogGoalList;
import op27no2.fitness.Centurion2.fragments.nutrition.DialogGoalListInterface;
import op27no2.fitness.Centurion2.fragments.nutrition.GoalSettingListAdapter;
import op27no2.fitness.Centurion2.fragments.nutrition.NutritionDay;


public class ActivitySettings extends Fragment implements DialogGoalListInterface, ValueDialogInterface {
    public Context mContext;
    private RecyclerView mRecyclerView;
    public LinearLayoutManager mLayoutManager;
    private GoalSettingListAdapter mAdapter;
    private SharedPreferences prefs;
    private SharedPreferences.Editor edt;
    private RelativeLayout mGoalsButton;
    private RelativeLayout mWeightRelativeLayout;
    private TextView weightText;
    private RelativeLayout mAvatarRelativeLayout;
    private RelativeLayout mBMRLayout;
    private TextView bmrText;
    private TextView avatarText;
    private Repository mRepository;
    private DialogGoalListInterface mInterface;
    private ValueDialogInterface mInterfaceValue;
    private ArrayList<GoalsDetail> mGoalSettingList;
    private NutritionDay mNutritionDay;


    @SuppressLint("StaticFieldLeak")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_activitysettings, container, false);
        mContext = getActivity();
        prefs = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        edt = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
        mRepository = new Repository(mContext);
        mInterface = this;
        mInterfaceValue = this;

        mGoalsButton = view.findViewById(R.id.goals);
        mWeightRelativeLayout = view.findViewById(R.id.weight);
        weightText = view.findViewById(R.id.weight_text);
        mBMRLayout = view.findViewById(R.id.bmr_layout);
        bmrText = view.findViewById(R.id.bmr_text);
        avatarText = view.findViewById(R.id.avatar_text);
        mAvatarRelativeLayout = view.findViewById(R.id.avatar);

  /*      mGoalsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new DialogGoalList(mContext, mRepository, mInterface);
              //  dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                dialog.show();
            }
        });*/
        Integer test = prefs.getInt("bmr",2000);
        String test2 = test.toString();
        bmrText.setText("BMR:  "+test2+" Calories");
        mBMRLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new ValueDialog(mContext, 0, prefs.getInt("bmr",2000), mInterfaceValue);
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                dialog.show();
            }
        });
        weightText.setText("Weight:  "+Integer.toString(prefs.getInt("weight",180))+" lbs");
        mWeightRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new ValueDialog(mContext, 4, prefs.getInt("weight",180), mInterfaceValue);
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                dialog.show();
            }
        });
        avatarText.setText("Avatar: "+prefs.getString("gender",""));
        mAvatarRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_avatar);

                DisplayMetrics metrics = getResources().getDisplayMetrics();
                int width = metrics.widthPixels;
                RadioGroup radio = dialog.findViewById(R.id.avatar);
                RadioButton radio1 = dialog.findViewById(R.id.prometheus);
                RadioButton radio2 = dialog.findViewById(R.id.artemis);
                if(prefs.getString("gender", "").equals("Prometheus")){
                    radio1.setChecked(true);
                }else if(prefs.getString("gender", "").equals("Artemis")){
                    radio2.setChecked(true);
                }
    //actually dont need this?
    /*            radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        View radioButton = radio.findViewById(checkedId);
                        int index = radio.indexOfChild(radioButton);
                        switch (index) {
                            case 0: // prometheus
                            System.out.println("Prometheus");
                                break;
                            case 1: // artemis
                                System.out.println("Artemis");
                                break;
                        }
                    }
                });*/


                dialog.getWindow().setLayout((6 * width) / 9, LinearLayout.LayoutParams.WRAP_CONTENT);

                dialog.findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (radio1.isChecked()) {
                            edt.putString("gender","Prometheus");
                            avatarText.setText("Avatar: Prometheus");
                        }else if(radio2.isChecked()){
                            edt.putString("gender","Artemis");
                            avatarText.setText("Avatar: Artemis");
                        }
                        edt.apply();
                        dialog.dismiss();
                    }
                });
                dialog.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
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
                        Dialog dialog = new DialogGoalList(mContext,position, mRepository, mInterface, mGoalSettingList.get(position));
                        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                        dialog.show();
                    }
                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                })
        );

        getData();


        return view;
    }

    @SuppressLint("StaticFieldLeak")
    public void getData(){
        new AsyncTask<Void, Void, Void>() {
            //Get today's workout => finishUI
            //get today's workout, if it doesn't exist create it

            protected void onPreExecute() {
                // Pre Code
            }
            protected Void doInBackground(Void... unused) {
                Calendar cal = Calendar.getInstance();
                Date d = cal.getTime();
                SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d, ''yy");
                String formattedDate = df.format(d);
                mNutritionDay = AppDatabase.getAppDatabase(mContext).ntDAO().findByDate(formattedDate);
                mGoalSettingList = new ArrayList<GoalsDetail>(AppDatabase.getAppDatabase(mContext).glDAO().getAll());
                mNutritionDay.setGoalList(mGoalSettingList);
                mRepository.updateNutrition(mNutritionDay);
                return null;
            }
            protected void onPostExecute(Void unused) {
                // Post Code

//
                finishUI();
            }
        }.execute();

    }

    public void finishUI(){
        System.out.println("Goal List Size"+mGoalSettingList.size());

        //set lift data to recyclerview

        mAdapter = new GoalSettingListAdapter(mGoalSettingList ,mRepository,mContext);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }


    @Override
    public void onGoalDialogDismiss(int position, GoalsDetail mGoalstDetail) {
        mGoalSettingList.set(position, mGoalstDetail);
        mNutritionDay.setGoalList(mGoalSettingList);
        mRepository.updateNutrition(mNutritionDay);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onValueDialogDismiss(float value, int position) {
        switch(position) {
            case 0:
                bmrText.setText("BMR:  "+Integer.toString((int) value)+" Calories");
             /* mGoalSettingList.get(getGoalIndex("Protein")).setGoalType(2);
                mGoalSettingList.get(getGoalIndex("Protein")).setGoalLimitLow((int) Math.floor((value*prefs.getFloat("protein",0.6f)*7)));
                mRepository.updateGoalSettingList(mGoalSettingList);*/
                edt.putInt("bmr",(int) value);
                edt.apply();
                break;
            case 4:
                weightText.setText("Weight:  "+Integer.toString((int) value)+" lbs");
             /* mGoalSettingList.get(getGoalIndex("Protein")).setGoalType(2);
                mGoalSettingList.get(getGoalIndex("Protein")).setGoalLimitLow((int) Math.floor((value*prefs.getFloat("protein",0.6f)*7)));
                mRepository.updateGoalSettingList(mGoalSettingList);*/
                edt.putInt("weight",(int) value);
                edt.apply();
            break;

            default:
                break;
        }


    }

    private int getGoalIndex(String name){
        int index = -1;
        for(int i=0;i<mGoalSettingList.size();i++){
            if(mGoalSettingList.get(i).getGoalName().equals(name)){
                index = i;
            }
        }
        return index;
    }


}

