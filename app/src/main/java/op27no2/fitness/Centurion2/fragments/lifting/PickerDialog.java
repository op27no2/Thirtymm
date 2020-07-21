package op27no2.fitness.Centurion2.fragments.lifting;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import op27no2.fitness.Centurion2.Database.Repository;
import op27no2.fitness.Centurion2.R;
import op27no2.fitness.Centurion2.RecyclerItemClickListener;

public class PickerDialog extends Dialog  {

        public Context c;

        private RecyclerView mRecyclerView;
        private GridLayoutManager mLayoutManager;
        private PickerDialogAdapter mAdapter;
        private Repository mRepository;
        private TextView titleText;

        private PickerDialogInterface mInterface;
        private Context mContext;
        private LiftMap mLiftMap;
        private int selected = 0;

        //passed from lift fragment adapter, keep track to  to correct row
        private int position;

        private ArrayList<Integer> mData = new ArrayList<Integer>();




    public PickerDialog(@NonNull Context context, int mposition, Repository repositoy, PickerDialogInterface dialogInterface) {
        super(context);
        c = context;
        mRepository = repositoy;
        mInterface = dialogInterface;
        mContext = context;
        position = mposition;
    }


    @SuppressLint("StaticFieldLeak")
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_picker);

            titleText = (TextView) findViewById(R.id.my_title);


            DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
            int width = metrics.widthPixels;
            getWindow().setLayout((10 * width) / 11, LinearLayout.LayoutParams.WRAP_CONTENT);

            for(int i=0 ; i<70; i++){
                mData.add(i*5);
            }

            mAdapter = new PickerDialogAdapter(mData);
            mRecyclerView = findViewById(R.id.my_recycler_view);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new GridLayoutManager(mContext, 5);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(getContext(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            System.out.println("item clicked: "+position);
                            selected = position;
                            mAdapter.setSelected(selected);
                            mAdapter.notifyDataSetChanged();
                            onBackPressed();

                        }
                        @Override
                        public void onItemLongClick(View view, int position) {

                        }
                    })
            );


        }




    @Override
    protected void onStop(){
        mInterface.onPickerDialogDismiss(mData.get(selected), position);
    }

    @Override
    public void onBackPressed(){
        mInterface.onPickerDialogDismiss(mData.get(selected), position);
        dismiss();
    }



}