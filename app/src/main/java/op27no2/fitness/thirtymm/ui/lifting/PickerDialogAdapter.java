package op27no2.fitness.thirtymm.ui.lifting;

import android.content.Context;
import android.icu.text.DecimalFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import op27no2.fitness.thirtymm.Database.Repository;
import op27no2.fitness.thirtymm.R;

/**
 * Created by CristMac on 11/3/17.
 */

public class PickerDialogAdapter extends RecyclerView.Adapter<PickerDialogAdapter.ViewHolder>  {
    private int selected;
    private Repository mRepository;
    private ArrayList<Integer> mData;
    private LiftingWorkout mLiftingWorkout;
    private int parentPosition;
    private MyDialogInterface mListener;
    private Context mContext;
    private LiftMap mLiftMap;
    private ArrayList<Integer> ratios;
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
    public PickerDialogAdapter(ArrayList<Integer> data) {
        mData = data;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PickerDialogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.number_grid_row, parent, false);
        // set the view's size, margins, paddings and layout parameters


        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element


        TextView mText = holder.mView.findViewById(R.id.text1);


        mText.setText(Integer.toString(mData.get(position)));

        if(position == selected){
            holder.mView.setBackgroundColor(ColorUtils.setAlphaComponent(ContextCompat.getColor(holder.mView.getContext(), R.color.lightgrey),200));
        }else{
            holder.mView.setBackgroundColor(ContextCompat.getColor(holder.mView.getContext(), R.color.white));
        }


    }


    public ArrayList<Integer> getRatios(){

        return ratios;
    }

    public void setSelected(int position){
        selected = position;
    }
    public int getSelected(){
        return selected;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mData.size();
    }


}





