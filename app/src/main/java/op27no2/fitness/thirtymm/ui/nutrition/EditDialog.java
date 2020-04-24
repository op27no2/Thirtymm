package op27no2.fitness.thirtymm.ui.nutrition;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rilixtech.materialfancybutton.MaterialFancyButton;

import java.util.ArrayList;

import op27no2.fitness.thirtymm.Database.Repository;
import op27no2.fitness.thirtymm.R;
import op27no2.fitness.thirtymm.RecyclerItemClickListener;
import op27no2.fitness.thirtymm.ui.lifting.LiftMap;
import op27no2.fitness.thirtymm.ui.lifting.PickerDialogAdapter;
import op27no2.fitness.thirtymm.ui.lifting.PickerDialogInterface;

public class EditDialog extends Dialog  {

        public Context c;

        private PickerDialogInterface mInterface;
        private Context mContext;
        private EditText mEdit;
        //passed from lift fragment adapter, keep track to  to correct row
        private int value;
        private int position;




    public EditDialog(@NonNull Context context,int pos, int mValue, PickerDialogInterface dialogInterface) {
        super(context);
        c = context;
        mInterface = dialogInterface;
        mContext = context;
        value = mValue;
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
            mEdit.setText(Integer.toString(value));
            MaterialFancyButton mButton = findViewById(R.id.save);
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
            int width = metrics.widthPixels;
            getWindow().setLayout((7 * width) / 11, LinearLayout.LayoutParams.WRAP_CONTENT);




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