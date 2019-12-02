package op27no2.fitness.thirtymm.ui.volume;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;
import com.rilixtech.materialfancybutton.MaterialFancyButton;

import java.util.ArrayList;

import op27no2.fitness.thirtymm.Database.Repository;
import op27no2.fitness.thirtymm.MyApplication;
import op27no2.fitness.thirtymm.R;
import op27no2.fitness.thirtymm.ui.lifting.DialogInterface;
import op27no2.fitness.thirtymm.ui.lifting.LiftMap;
import op27no2.fitness.thirtymm.ui.lifting.LiftingWorkout;

/**
 * Created by CristMac on 11/3/17.
 */

public class MyDialogVolumeAdapter extends RecyclerView.Adapter<MyDialogVolumeAdapter.ViewHolder>  {
    private int selected;
    private Repository mRepository;
    private ArrayList<String> mData;
    private LiftingWorkout mLiftingWorkout;
    private int parentPosition;
    private DialogInterface mListener;
    private Context mContext;
    private LiftMap mLiftMap;
    private ArrayList<Double> ratios;
    private ArrayList<String> name;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mView;
        public ViewHolder(View v) {
            super(v);
            mView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyDialogVolumeAdapter(LiftMap map, Repository repository, Context context) {
        mLiftMap = map;
        mRepository = repository;
        mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyDialogVolumeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.map_row, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ratios = mLiftMap.getRatios();
        name = mLiftMap.getMuscles();

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element


        TextView mText = holder.mView.findViewById(R.id.text1);
        mText.setText(mLiftMap.getMuscles().get(position));

        EditText mEdit = holder.mView.findViewById(R.id.edit_text);
        mEdit.setText(Double.toString(mLiftMap.getRatios().get(position)));
        mEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                //TODO THIS IS A MESS, NEED TO GRAB ALL EDIT TEXT ON-DISMISS AND UPDATE LiftMap Once. OR maybe have separate Array up top, not gauranteed to match I suppose.

                if(!s.toString().isEmpty()) {
                    System.out.println("muscle at position: "+holder.getAdapterPosition());
                    ratios.set(holder.getAdapterPosition(), Double.parseDouble(s.toString()));
                    /*mLiftMap.setRatios(ratios);
                    mRepository.updateLiftMap(mLiftMap);*/
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });



    }


    public ArrayList<Double> getRatios(){

        return ratios;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mLiftMap.getMuscles().size();
    }


}





