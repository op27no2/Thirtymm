package op27no2.fitness.Centurion2;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.rilixtech.materialfancybutton.MaterialFancyButton;

public class ValueDialog extends Dialog  {

        public Context c;

        private ValueDialogInterface mInterface;
        private Context mContext;
        private EditText mEdit;
        //passed from lift fragment adapter, keep track to  to correct row
        private float value;
        private int position;




    public ValueDialog(@NonNull Context context, int pos, float mValue, ValueDialogInterface dialogInterface) {
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
            if(position != 5) {
                mEdit.setText(Integer.toString((int) value));
            }else{
                mEdit.setText(Float.toString(value));
            }
            mEdit.requestFocus();
            mEdit.selectAll();

            //if protein, accept decimals 0.6
            if(position == 5){
                mEdit.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            }

         //   InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
         //   inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);



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
        if(position!=5) {
            mInterface.onValueDialogDismiss(Integer.parseInt(mEdit.getText().toString()), position);
        }else{
            mInterface.onValueDialogDismiss(Float.parseFloat(mEdit.getText().toString()), position);
        }

    }

    @Override
    public void onBackPressed(){
        if(position!=5) {
            mInterface.onValueDialogDismiss(Integer.parseInt(mEdit.getText().toString()), position);
        }else{
            mInterface.onValueDialogDismiss(Float.parseFloat(mEdit.getText().toString()), position);
        }
        dismiss();
    }



}