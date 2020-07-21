package op27no2.fitness.Centurion2;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.rilixtech.materialfancybutton.MaterialFancyButton;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        edt = getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();


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

        buttonImperial = findViewById(R.id.imperial);
        buttonMetric = findViewById(R.id.metric);

        buttonMale = findViewById(R.id.male);
        buttonFemale = findViewById(R.id.female);

        buttonWeight = findViewById(R.id.weight);
        buttonProtein = findViewById(R.id.protein);

        doneText = findViewById(R.id.done_text);

        buttonDeficit.setText("Deficit Goal\n"+"-"+Integer.toString(prefs.getInt("deficit",3500))+" Calories");
        buttonMaintenance.setText("Maintenance Goal:\n"+"Base Cals +/- "+Integer.toString(prefs.getInt("recomp",500)));
        buttonSurplus.setText("Surplus Goal:\n"+Integer.toString(prefs.getInt("bulk",2100))+"+ Calories");
        buttonVolume.setText("Volume Goal:\n(Sets/Muscle/Week):\n"+Integer.toString(prefs.getInt("volume",15)));
        buttonWeight.setText("Weight:\n"+Integer.toString(prefs.getInt("weight",180))+" lbs");
        buttonProtein.setText("Protein:\n"+Float.toString(prefs.getFloat("protein",0.6f))+"g per lb");


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
                Dialog dialog = new ValueDialog(view.getContext(), 0, prefs.getInt("deficit",-3500), mInterface);
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                dialog.show();

            }
        });
        buttonMaintenance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new ValueDialog(view.getContext(), 1, prefs.getInt("recomp",700), mInterface);
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                dialog.show();
            }
        });
        buttonSurplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new ValueDialog(view.getContext(), 2, prefs.getInt("bulk",2100), mInterface);
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                dialog.show();
            }
        });
        buttonVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int vol = 0;
                Dialog dialog = new ValueDialog(view.getContext(), 3, prefs.getInt("volume",vol), mInterface);
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
                break;
            case 1:
                buttonMetric.setBorderColor(ResourcesCompat.getColor(getResources(), R.color.darkgrey, null));
                buttonMetric.setTextColor(ResourcesCompat.getColor(getResources(), R.color.darkgrey, null));
                buttonMetric.setBorderWidth(10);
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
    public void onValueDialogDismiss(float weight, int position) {
        System.out.println("dialog position: "+position+" value: "+weight);
        switch(position){
            case 0:
                buttonDeficit.setText("Deficit Goal\n"+Integer.toString((int) weight)+" Calories");
                edt.putInt("deficit",(int) weight);
                edt.apply();
                break;
            case 1:
                buttonMaintenance.setText("Maintenance Goal:\n"+"Base Cals +/- "+Integer.toString((int) weight));
                edt.putInt("recomp",(int) weight);
                edt.apply();
                break;
            case 2:
                buttonSurplus.setText("Surplus Goal:\n"+Integer.toString((int) weight)+"+ Calories");
                edt.putInt("bulk",(int) weight);
                edt.apply();
                break;
            case 3:
                buttonVolume.setText("Volume Goal:\n(Sets/Muscle/Week):\n"+Integer.toString((int) weight));
                edt.putInt("volume",(int) weight);
                edt.apply();
                break;
            case 4:
                buttonWeight.setText("Weight:\n"+Integer.toString((int) weight)+" lbs");
                edt.putInt("weight",(int) weight);
                edt.apply();
                break;
            case 5:
                buttonProtein.setText("Protein:\n"+Float.toString(weight)+"g per lb");
                edt.putFloat("protein", weight);
                edt.apply();
                break;
            default:

                break;
        }
    }
}

