package op27no2.fitness.Centurion2.fragments.nutrition;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rilixtech.materialfancybutton.MaterialFancyButton;

import java.util.ArrayList;

import op27no2.fitness.Centurion2.Database.Repository;
import op27no2.fitness.Centurion2.R;
import op27no2.fitness.Centurion2.fragments.activities.GoalsDetail;

public class DialogGoalList extends Dialog  {

        private SharedPreferences prefs;
        private SharedPreferences.Editor edt;

        private RecyclerView mRecyclerView;
        private LinearLayoutManager mLayoutManager;

        private Repository mRepository;
        private TextView titleText;
        private EditText edit1;
        private TextView text2;
        private TextView text3;
        private TextView text4;
        private EditText edit5;

        private LinearLayout proteinLayout;
        private TextView proteinText1;
        private EditText proteinEdit1;
        private TextView proteinText2;

        private Boolean stateOne;
        private Boolean stateTwo;


        private DialogGoalListInterface mInterface;
        private Context mContext;
        private ArrayList<GoalsDetail> mGoalList;
        private int selected = 0;
        private GoalSettingListAdapter mAdapter;
        private GoalsDetail mGoalSettingDetail;
        private int mPosition;



    public DialogGoalList(@NonNull Context context, int position, Repository repositoy, DialogGoalListInterface dialogInterface, GoalsDetail goalSettingDetail) {
        super(context);
        mRepository = repositoy;
        mInterface = dialogInterface;
        mContext = context;
        mPosition = position;
        mGoalSettingDetail = goalSettingDetail;
    }


    @SuppressLint("StaticFieldLeak")
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_goalchange);
        prefs = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        edt = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();

        titleText = (TextView) findViewById(R.id.my_title);
        edit1 = (EditText) findViewById(R.id.edit1);
        text2 = (TextView) findViewById(R.id.text2);
        text3 = (TextView) findViewById(R.id.text3);
        text4 = (TextView) findViewById(R.id.text4);
        edit5 = (EditText) findViewById(R.id.edit5);

        proteinText1 = (TextView) findViewById(R.id.text_protein_info1);
        proteinText2 = (TextView) findViewById(R.id.text_protein_info2);
        proteinEdit1 = (EditText) findViewById(R.id.edit_protein_info1);
        proteinLayout = (LinearLayout) findViewById(R.id.layout_protein_info);
        proteinLayout.setVisibility(View.GONE);

        text2.setText("<");
        text4.setText("<");
        text3.setText(mGoalSettingDetail.getGoalName());

        int weight = prefs.getInt("weight",150);
        final Float[] prot = {prefs.getFloat("protein", 150)};
        if (mGoalSettingDetail.getGoalName().equals("Protein")) {
            proteinLayout.setVisibility(View.VISIBLE);
            proteinText1.setText(weight+"lbs x ");
            proteinEdit1.setText(Float.toString(prot[0]));
            proteinText2.setText("g/lb = "+ (int) Math.floor(7*weight* prot[0])+" grams");
        }



        proteinEdit1.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if(!proteinEdit1.getText().toString().equals("") && proteinEdit1.getText().toString() != null) {
                    prot[0] = Float.parseFloat(proteinEdit1.getText().toString());
                    proteinText2.setText("g/lb = " + (int) Math.floor(7 * weight * prot[0]) + " grams");
                    edt.putFloat("protein", prot[0]);
                    edt.apply();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        switch(mGoalSettingDetail.getGoalType()){

            case 0:
                edit5.setText(Integer.toString(mGoalSettingDetail.getGoalLimitHigh()));
                stateOne = false;
                stateTwo = true;
                text2.setTextColor(ContextCompat.getColor(mContext, R.color.lightgrey));
                text4.setTextColor(ContextCompat.getColor(mContext, R.color.black));

                break;
            case  1:
                edit1.setText(Integer.toString(mGoalSettingDetail.getGoalLimitLow()));
                edit5.setText(Integer.toString(mGoalSettingDetail.getGoalLimitHigh()));
                stateOne = true;
                stateTwo = true;
                text2.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                text4.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                break;
            case  2:
                edit1.setText(Integer.toString(mGoalSettingDetail.getGoalLimitLow()));
                stateOne = true;
                stateTwo = false;
                text2.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                text4.setTextColor(ContextCompat.getColor(mContext, R.color.lightgrey));
                break;
            default:
                //nothing default
        }


        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mGoalSettingDetail.getGoalName().equals("Sets")) {

                    if (stateOne == true && stateTwo != false) {
                        stateOne = false;
                        text2.setTextColor(ContextCompat.getColor(mContext, R.color.lightgrey));
                    } else if (stateOne == true && stateTwo == false) {
                        stateOne = false;
                        stateTwo = true;
                        text2.setTextColor(ContextCompat.getColor(mContext, R.color.lightgrey));
                        text4.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                    } else if (stateOne == false) {
                        stateOne = true;
                        text2.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                    }
                }
            }
        });
        text4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mGoalSettingDetail.getGoalName().equals("Sets")) {
                    if (stateTwo == true && stateOne != false) {
                        stateTwo = false;
                        text4.setTextColor(ContextCompat.getColor(mContext, R.color.lightgrey));
                    } else if (stateTwo == true && stateOne == false) {
                        stateOne = true;
                        stateTwo = false;
                        text2.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                        text4.setTextColor(ContextCompat.getColor(mContext, R.color.lightgrey));
                    } else if (stateTwo == false) {
                        stateTwo = true;
                        text4.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                    }
                }
            }
        });


        MaterialFancyButton pButton = findViewById(R.id.save);
            pButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mRepository.updateGoalSetting(mGoalSettingDetail);
                    if(stateOne == true && stateTwo == true){
                        System.out.println("set goal type 1");
                        mGoalSettingDetail.setGoalType(1);
                        mGoalSettingDetail.setGoalLimitHigh(Integer.parseInt(edit5.getText().toString()));
                        mGoalSettingDetail.setGoalLimitLow(Integer.parseInt(edit1.getText().toString()));
                    }else if(stateOne == false && stateTwo == true){
                        System.out.println("set goal type 0");
                        mGoalSettingDetail.setGoalType(0);
                        mGoalSettingDetail.setGoalLimitHigh(Integer.parseInt(edit5.getText().toString()));
                    }else if(stateOne == true && stateTwo == false) {
                        System.out.println("set goal type 2");
                        mGoalSettingDetail.setGoalType(2);
                        mGoalSettingDetail.setGoalLimitLow(Integer.parseInt(edit1.getText().toString()));
                    }
                    System.out.println("update goal type: "+mGoalSettingDetail.getGoalType());
                    System.out.println("update goal low: "+mGoalSettingDetail.getGoalLimitLow());
                    System.out.println("update goal high: "+mGoalSettingDetail.getGoalLimitHigh());

                    mRepository.updateGoalSetting(mGoalSettingDetail);
                    dismiss();
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




        }



        public void finishUI(){
        System.out.println("Goal List Size"+mGoalList.size());

        //set lift data to recyclerview


        }


    @Override
    protected void onStop(){



     //   mLiftMap.setRatios(mAdapter.getRatios());
     //   mRepository.updateLiftMap(mLiftMap);
        mInterface.onGoalDialogDismiss(mPosition, mGoalSettingDetail);

    }

    @Override
    public void onBackPressed(){
        dismiss();
    }



}