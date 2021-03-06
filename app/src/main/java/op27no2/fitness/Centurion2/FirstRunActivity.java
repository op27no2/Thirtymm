package op27no2.fitness.Centurion2;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.rilixtech.materialfancybutton.MaterialFancyButton;

import java.util.ArrayList;

import op27no2.fitness.Centurion2.Database.AppDatabase;
import op27no2.fitness.Centurion2.Database.Repository;
import op27no2.fitness.Centurion2.fragments.activities.GoalsDetail;


public class FirstRunActivity extends AppCompatActivity implements ValueDialogInterface {
    private SharedPreferences prefs;
    private SharedPreferences.Editor edt;

    private MaterialFancyButton buttonCut;
    private MaterialFancyButton buttonRecomp;
    private MaterialFancyButton buttonBulk;
    private Integer goalSelect = 99;

    private MaterialFancyButton buttonDeficit;
    private MaterialFancyButton buttonMaintenance;
    private MaterialFancyButton buttonSurplus;
    private MaterialFancyButton buttonVolume;

    private MaterialFancyButton buttonImperial;
    private MaterialFancyButton buttonMetric;
    private Integer unitSelect = 99;

    private MaterialFancyButton buttonMale;
    private MaterialFancyButton buttonFemale;
    private Integer genderSelect = 99;

    private MaterialFancyButton buttonWeight;
    private MaterialFancyButton buttonProtein;

    private TextView doneText;

    private ValueDialogInterface mInterface;

    private ArrayList<GoalsDetail> mGoalList;
    private Repository mRepository;
    private Context mContext;


    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        edt = getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
        mContext = this;
        mRepository = new Repository(mContext);

        System.out.println("FirstRun onCreate");
        setContentView(R.layout.activity_first_run);
        mInterface = this;

        buttonCut = findViewById(R.id.cut);
        buttonRecomp = findViewById(R.id.recomp);
        buttonBulk = findViewById(R.id.bulk);

        buttonDeficit = findViewById(R.id.goal_deficit);
        buttonMaintenance = findViewById(R.id.goal_recomp);
        buttonSurplus = findViewById(R.id.goal_surplus);
        buttonVolume = findViewById(R.id.goal_sets);

        buttonImperial = findViewById(R.id.pounds);
        buttonMetric = findViewById(R.id.kilograms);

        buttonMale = findViewById(R.id.male);
        buttonFemale = findViewById(R.id.female);

        buttonWeight = findViewById(R.id.weight);
        buttonProtein = findViewById(R.id.protein);

        doneText = findViewById(R.id.done_text);



        //PROGRAM GOAL CUT VS RECOMP VS BULK
        buttonCut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goalSelect = 0;
                updateGoalButtons();
            }
        });
        buttonRecomp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goalSelect = 1;
                updateGoalButtons();
            }
        });
        buttonBulk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goalSelect = 2;
                updateGoalButtons();
            }
        });


        buttonDeficit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   Dialog dialog = new ValueDialog(view.getContext(), 0, prefs.getInt("deficit",-3500), mInterface);
                Dialog dialog = new ValueDialog(view.getContext(), 0, mGoalList.get(getGoalIndex("Cals")).getGoalLimitLow()/7, mInterface);
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                dialog.show();

            }
        });
        buttonMaintenance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //    Dialog dialog = new ValueDialog(view.getContext(), 1, prefs.getInt("recomp",700), mInterface);
                  Dialog dialog = new ValueDialog(view.getContext(), 1, mGoalList.get(getGoalIndex("Cals")).getGoalLimitHigh()/7, mInterface);
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                dialog.show();
            }
        });
        buttonSurplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //    Dialog dialog = new ValueDialog(view.getContext(), 2, prefs.getInt("bulk",2100), mInterface);
                Dialog dialog = new ValueDialog(view.getContext(), 2, mGoalList.get(getGoalIndex("Cals")).getGoalLimitHigh()/7, mInterface);
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                dialog.show();
            }
        });
        buttonVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int vol = 0;
                //Dialog dialog = new ValueDialog(view.getContext(), 3, prefs.getInt("volume",vol), mInterface);Dialog dialog = new ValueDialog(view.getContext(), 0, mGoalList.get(getGoalIndex("Cals")).getGoalLimitLow(), mInterface);
                Dialog dialog = new ValueDialog(view.getContext(), 3, mGoalList.get(getGoalIndex("Sets")).getGoalLimitHigh(), mInterface);
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

                dialog.show();
            }
        });


        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //IMPERIAL VS METRIC
        buttonImperial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unitSelect = 0;
                edt.putInt("units", 0);
                edt.apply();
                updateUnitButtons();
            }
        });
        buttonMetric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unitSelect = 1;
                edt.putInt("units", 1);
                edt.apply();
                updateUnitButtons();
            }
        });

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //MALE VS FEMALE
        buttonMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genderSelect = 0;
                edt.putString("gender", "Prometheus");
                edt.apply();
                updateGenderButtons();
            }
        });
        buttonFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genderSelect = 1;
                edt.putString("gender", "Artemis");
                edt.apply();
                updateGenderButtons();
            }
        });

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //SET WEIGHT
        buttonWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new ValueDialog(view.getContext(), 4, prefs.getInt("weight",180), mInterface);
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                dialog.show();
            }
        });
        buttonProtein.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new ValueDialog(view.getContext(), 5, prefs.getFloat("protein", 0.6f), mInterface);
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                dialog.show();
            }
        });


        doneText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        new AsyncTask<Void, Void, Void>() {
            //Get today's workout => finishUI
            //get today's workout, if it doesn't exist create it

            protected void onPreExecute() {
                // Pre Code
            }
            protected Void doInBackground(Void... unused) {
                mGoalList = new ArrayList<GoalsDetail>(AppDatabase.getAppDatabase(mContext).glDAO().getAll());
                if(mGoalList == null || mGoalList.size() == 0){
                    //really this should be done on first run instead of prefs.
                    System.out.println("goals null should create");
                    GoalsDetail mDetail = new GoalsDetail( "Cals", 0,  -300, 0);
                    mGoalList.add(mDetail);
                    GoalsDetail mDetail4 = new GoalsDetail( "Protein", 2, 0,  (int) Math.floor(0.6f * 150));
                    mGoalList.add(mDetail4);
                    GoalsDetail mDetail5 = new GoalsDetail( "Sets", 2, 0,   15);
                    mGoalList.add(mDetail5);
                    AppDatabase.getAppDatabase(mContext).glDAO().insertAll(mGoalList);
                }else{
                    System.out.println("First run goallist null or size zero");
                }

                return null;
            }
            protected void onPostExecute(Void unused) {
                // Post Code


                finishUI();
            }
        }.execute();



    }


    private void updateGoalButtons(){
        buttonCut.setBorderColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
        buttonCut.setBorderWidth(1);
        buttonCut.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
        buttonRecomp.setBorderColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
        buttonRecomp.setBorderWidth(1);
        buttonRecomp.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
        buttonBulk.setBorderColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
        buttonBulk.setBorderWidth(1);
        buttonBulk.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));

        buttonVolume.setVisibility(View.VISIBLE);
        buttonDeficit.setVisibility(View.GONE);
        buttonMaintenance.setVisibility(View.GONE);
        buttonSurplus.setVisibility(View.GONE);

        switch(goalSelect){
            case 0:
                buttonCut.setBorderColor(ResourcesCompat.getColor(getResources(), R.color.darkgrey, null));
                buttonCut.setTextColor(ResourcesCompat.getColor(getResources(), R.color.darkgrey, null));
                buttonCut.setBorderWidth(10);
                buttonDeficit.setVisibility(View.VISIBLE);
                break;
            case 1:
                buttonRecomp.setBorderColor(ResourcesCompat.getColor(getResources(), R.color.darkgrey, null));
                buttonRecomp.setTextColor(ResourcesCompat.getColor(getResources(), R.color.darkgrey, null));
                buttonRecomp.setBorderWidth(10);
                buttonMaintenance.setVisibility(View.VISIBLE);
                break;
            case 2:
                buttonBulk.setBorderColor(ResourcesCompat.getColor(getResources(), R.color.darkgrey, null));
                buttonBulk.setTextColor(ResourcesCompat.getColor(getResources(), R.color.darkgrey, null));
                buttonBulk.setBorderWidth(10);
                buttonSurplus.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void updateUnitButtons(){
        buttonImperial.setBorderColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
        buttonImperial.setBorderWidth(1);
        buttonImperial.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
        buttonMetric.setBorderColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
        buttonMetric.setBorderWidth(1);
        buttonMetric.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));

        switch(unitSelect){
            case 0:
                buttonImperial.setBorderColor(ResourcesCompat.getColor(getResources(), R.color.darkgrey, null));
                buttonImperial.setTextColor(ResourcesCompat.getColor(getResources(), R.color.darkgrey, null));
                buttonImperial.setBorderWidth(10);
                buttonWeight.setText("Weight:\n180 lbs");
                buttonProtein.setText("Protein:\n0.6g per lb");
                edt.putInt("weight", 180);
                edt.putFloat("protein", 0.6f);
                edt.commit();
                break;
            case 1:
                buttonMetric.setBorderColor(ResourcesCompat.getColor(getResources(), R.color.darkgrey, null));
                buttonMetric.setTextColor(ResourcesCompat.getColor(getResources(), R.color.darkgrey, null));
                buttonMetric.setBorderWidth(10);
                buttonWeight.setText("Weight:\n80 kgs");
                buttonProtein.setText("Protein:\n1g per kg");
                edt.putInt("weight", 80);
                edt.putFloat("protein", 1.0f);
                edt.commit();
                break;
        }
    }

    private void updateGenderButtons(){
        buttonMale.setBorderColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
        buttonMale.setBorderWidth(1);
        buttonMale.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
        buttonFemale.setBorderColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
        buttonFemale.setBorderWidth(1);
        buttonFemale.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));

        switch(genderSelect){
            case 0:
                buttonMale.setBorderColor(ResourcesCompat.getColor(getResources(), R.color.darkgrey, null));
                buttonMale.setTextColor(ResourcesCompat.getColor(getResources(), R.color.darkgrey, null));
                buttonMale.setBorderWidth(10);
                break;
            case 1:
                buttonFemale.setBorderColor(ResourcesCompat.getColor(getResources(), R.color.darkgrey, null));
                buttonFemale.setTextColor(ResourcesCompat.getColor(getResources(), R.color.darkgrey, null));
                buttonFemale.setBorderWidth(10);
                break;
        }
    }




    //weight is just a float variable returned from the dialogs to get values. Most are cast to int, but protein needs decimal so stays float for display, stored as int multipled with bodyweight
    @Override
    public void onValueDialogDismiss(float value, int position) {
        System.out.println("dialog position: "+position+" value: "+value);
        String unit = prefs.getInt("units",0) == 0 ? "lb" : "kg";
        switch(position){
            case 0:
                int myValue = (int) -(Math.abs(value));
                buttonDeficit.setText("Deficit Goal\n"+Integer.toString(myValue)+" Calories per Day\n (= "+Integer.toString(myValue*7)+ " per Week)");
                mGoalList.get(getGoalIndex("Cals")).setGoalType(0);
                mGoalList.get(getGoalIndex("Cals")).setGoalLimitHigh(myValue*7);
                mRepository.updateGoalSettingList(mGoalList);

                //TODO remove prefs we dont need once we confirm its working, but not for weight, gender, units
                edt.putInt("deficit",(int) value);
                edt.putInt("goaltype", 0);
                edt.apply();
                break;
            case 1:
                buttonMaintenance.setText("Maintenance Goal:\n"+"Base Cals +/- "+Integer.toString((int) value) +" per Day\n"+ "(= +/-" +Integer.toString((int) value*7) +" per Week");
                mGoalList.get(getGoalIndex("Cals")).setGoalType(1);
                mGoalList.get(getGoalIndex("Cals")).setGoalLimitLow((int) value*7);
                mGoalList.get(getGoalIndex("Cals")).setGoalLimitHigh((int) value*7);
                mRepository.updateGoalSettingList(mGoalList);

                edt.putInt("recomp",(int) value);
                edt.putInt("goaltype", 1);
                edt.apply();
                break;
            case 2:
                buttonSurplus.setText("Surplus Goal:\n"+Integer.toString((int) value)+"+ Calories per Day\n (= "+Integer.toString((int) value*7)+ " per Week)");
                mGoalList.get(getGoalIndex("Cals")).setGoalType(2);
                mGoalList.get(getGoalIndex("Cals")).setGoalLimitLow((int) value*7);
                mRepository.updateGoalSettingList(mGoalList);

                edt.putInt("bulk",(int) value);
                edt.putInt("goaltype", 2);
                edt.apply();
                break;
            case 3:
                buttonVolume.setText("Volume Goal:\n(Sets per Muscle per Week):\n"+Integer.toString((int) value));
                mGoalList.get(getGoalIndex("Sets")).setGoalType(2);
                mGoalList.get(getGoalIndex("Sets")).setGoalLimitLow((int) value);
                mRepository.updateGoalSettingList(mGoalList);

                edt.putInt("volume",(int) value);
                edt.apply();
                break;
            case 4:
                buttonWeight.setText("Weight:\n"+Integer.toString((int) value)+unit+"s");
                mGoalList.get(getGoalIndex("Protein")).setGoalType(2);
                mGoalList.get(getGoalIndex("Protein")).setGoalLimitLow((int) Math.floor((value*prefs.getFloat("protein",0.6f)*7)));
                mRepository.updateGoalSettingList(mGoalList);
                edt.putInt("weight",(int) value);
                Integer bmr = (int) Math.floor(value*11.363);
                if(bmr<1300){bmr = 1300;}
                edt.putInt("bmr",bmr);
                edt.apply();
                break;
            case 5:
                buttonProtein.setText("Protein:\n"+Float.toString(value)+"g per "+unit);
                mGoalList.get(getGoalIndex("Protein")).setGoalType(2);
                mGoalList.get(getGoalIndex("Protein")).setGoalLimitLow((int) value*prefs.getInt("weight",150)*7);
                mRepository.updateGoalSettingList(mGoalList);
                edt.putFloat("protein", value);
                edt.apply();
                break;
            default:

                break;
        }
    }

    private void finishUI(){
 /*       buttonDeficit.setText("Deficit Goal\n"+"-"+Integer.toString(prefs.getInt("deficit",3500))+" Calories");
        buttonMaintenance.setText("Maintenance Goal:\n"+"Base Cals +/- "+Integer.toString(prefs.getInt("recomp",500)));
        buttonSurplus.setText("Surplus Goal:\n"+Integer.toString(prefs.getInt("bulk",2100))+"+ Calories");
        buttonVolume.setText("Volume Goal:\n(Sets/Muscle/Week):\n"+Integer.toString(prefs.getInt("volume",15)));
        buttonWeight.setText("Weight:\n"+Integer.toString(prefs.getInt("weight",180))+" lbs");
        buttonProtein.setText("Protein:\n"+Float.toString(prefs.getFloat("protein",0.6f))+"g per lb");*/
        String unit = prefs.getInt("units",0) == 0 ? "lb" : "kg";

        buttonDeficit.setText("Deficit Goal\n"+mGoalList.get(getGoalIndex("Cals")).getGoalLimitHigh()/7+" Calories per Day\n (= "+mGoalList.get(getGoalIndex("Cals")).getGoalLimitHigh()+" per Week)");
        buttonMaintenance.setText("Maintenance Goal:\n"+"Base Cals +/- "+mGoalList.get(getGoalIndex("Cals")).getGoalLimitLow()/7 +" per Day\n"+ "(= +/-" +mGoalList.get(getGoalIndex("Cals")).getGoalLimitLow() +" per Week");
        buttonSurplus.setText("Surplus Goal:\n"+mGoalList.get(getGoalIndex("Cals")).getGoalLimitLow()/7+"+ Calories per Day\n (= "+mGoalList.get(getGoalIndex("Cals")).getGoalLimitLow()+ "per Week)");
        buttonVolume.setText("Volume Goal:\n(Sets per Muscle per Week):\n"+mGoalList.get(getGoalIndex("Sets")).getGoalLimitLow());
        buttonWeight.setText("Weight:\n"+Integer.toString(prefs.getInt("weight",180))+" "+unit+"s");
        buttonProtein.setText("Protein:\n"+Float.toString(prefs.getFloat("protein",0.6f))+"g per "+unit);

    }

    private int getGoalIndex(String name){
        int index = -1;
        for(int i=0;i<mGoalList.size();i++){
            if(mGoalList.get(i).getGoalName().equals(name)){
                index = i;
            }
        }
        return index;
    }
}

