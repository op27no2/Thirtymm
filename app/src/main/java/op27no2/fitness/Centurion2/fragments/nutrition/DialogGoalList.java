package op27no2.fitness.Centurion2.fragments.nutrition;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rilixtech.materialfancybutton.MaterialFancyButton;

import java.util.ArrayList;

import op27no2.fitness.Centurion2.Database.Repository;
import op27no2.fitness.Centurion2.R;
import op27no2.fitness.Centurion2.fragments.activities.GoalsDetail;

public class DialogGoalList extends Dialog  {

        public Context c;

        private RecyclerView mRecyclerView;
        private LinearLayoutManager mLayoutManager;

        private Repository mRepository;
        private TextView titleText;

        private DialogGoalListInterface mInterface;
        private Context mContext;
        private ArrayList<GoalsDetail> mGoalList;
        private int selected = 0;
        private GoalSettingListAdapter mAdapter;
        private GoalsDetail mGoalSettingDetail;




    public DialogGoalList(@NonNull Context context, Repository repositoy, DialogGoalListInterface dialogInterface, GoalsDetail goalSettingDetail) {
        super(context);
        c = context;
        mRepository = repositoy;
        mInterface = dialogInterface;
        mContext = context;
        mGoalSettingDetail = goalSettingDetail;
    }


    @SuppressLint("StaticFieldLeak")
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_goalchange);

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




        }



        public void finishUI(){
        System.out.println("Goal List Size"+mGoalList.size());

        //set lift data to recyclerview


        }


    @Override
    protected void onStop(){



     //   mLiftMap.setRatios(mAdapter.getRatios());
     //   mRepository.updateLiftMap(mLiftMap);
        mInterface.onDialogDismiss();

    }

    @Override
    public void onBackPressed(){
        dismiss();
    }



}