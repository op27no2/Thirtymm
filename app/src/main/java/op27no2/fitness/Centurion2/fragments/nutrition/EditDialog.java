package op27no2.fitness.Centurion2.fragments.nutrition;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rilixtech.materialfancybutton.MaterialFancyButton;

import op27no2.fitness.Centurion2.R;
import op27no2.fitness.Centurion2.fragments.activities.GoalsDetail;
import op27no2.fitness.Centurion2.fragments.lifting.PickerDialogInterface;

public class EditDialog extends Dialog  {

        public Context c;

        private PickerDialogInterface mInterface;
        private Context mContext;
        private EditText mEdit;
        //passed from lift fragment adapter, keep track to  to correct row
        private Integer mValue;
        private String mName;
        private GoalsDetail mGoalsDetail;
        private int position;




    public EditDialog(@NonNull Context context, int pos, Integer value, String name, GoalsDetail goalsDetail, PickerDialogInterface dialogInterface) {
        super(context);
        c = context;
        mInterface = dialogInterface;
        mContext = context;
        this.mValue = value;
        mName = name;
        mGoalsDetail = goalsDetail;
        position = pos;
    }


    @SuppressLint("StaticFieldLeak")
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            System.out.println("dialog created");
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_edittext);
            mEdit = (EditText) findViewById(R.id.value);
            TextView titleText = findViewById(R.id.title_text);
            int type = mGoalsDetail.getGoalType();
            switch (type) {
                case 0:
                    titleText.setText("Goal:\n" +mGoalsDetail.getGoalName()+"<"+ mGoalsDetail.getGoalLimitHigh() + "/week = <"+mGoalsDetail.getGoalLimitHigh()/7+"/day" );
                    break;
                case 1:
                    titleText.setText("Goal:\n" + mGoalsDetail.getGoalLimitLow()+"<"+mGoalsDetail.getGoalName()+"<"+ mGoalsDetail.getGoalLimitHigh() + "/week = " +mGoalsDetail.getGoalLimitLow()/7+ "<"+mGoalsDetail.getGoalName()+"<"+mGoalsDetail.getGoalLimitHigh()/7+"/day" );
                    break;
                case 2:
                    titleText.setText("Goal:\n" +mGoalsDetail.getGoalName()+">"+ mGoalsDetail.getGoalLimitHigh() + "/week = >"+mGoalsDetail.getGoalLimitHigh()/7+"/day" );
                    break;
                default:
                    break;
            }





            mEdit.setText(Integer.toString(mValue));
            mEdit.selectAll();
            mEdit.requestFocus();
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        MaterialFancyButton mButton = findViewById(R.id.save);
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
            int width = metrics.widthPixels;
            getWindow().setLayout((9 * width) / 11, LinearLayout.LayoutParams.WRAP_CONTENT);

        }


    @Override
    protected void onStop(){
        mInterface.onPickerDialogDismiss(Integer.parseInt(mEdit.getText().toString()) , position);
    }

    @Override
    public void onBackPressed(){
        mInterface.onPickerDialogDismiss( Integer.parseInt(mEdit.getText().toString()),position);
        dismiss();
    }



}