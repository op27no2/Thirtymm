package op27no2.fitness.thirtymm.ui.lifting;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import op27no2.fitness.thirtymm.Database.Repository;
import op27no2.fitness.thirtymm.R;

/**
 * Created by CristMac on 11/3/17.
 */

public class MyRepAdapter extends RecyclerView.Adapter<MyRepAdapter.ViewHolder>{
    private LiftingWorkout mLiftingWorkout;
    private int selected;
    private int parentPosition;
    private Repository mRepository;





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
    public MyRepAdapter(LiftingWorkout lift, int position, Repository repository) {
        mLiftingWorkout = lift;
        parentPosition = position;
        mRepository = repository;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyRepAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.number_circle, parent, false);
        // set the view's size, margins, paddings and layout parameters


        SharedPreferences prefs = v.getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = v.getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final TextView mText = holder.mView.findViewById(R.id.circle_number);
        final TextView mWeightText = holder.mView.findViewById(R.id.weight);
        if(mLiftingWorkout.getMyLifts().get(parentPosition).getReps().get(position) == 0) {
            mWeightText.setVisibility(View.GONE);
        }else{
            mWeightText.setVisibility(View.VISIBLE);
        }

        mText.setText(Integer.toString(mLiftingWorkout.getMyLifts().get(parentPosition).getReps().get(position)));
        mWeightText.setText(Integer.toString(mLiftingWorkout.getMyLifts().get(parentPosition).getRepWeights().get(position)));

        mText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLiftingWorkout.getMyLifts().get(parentPosition).plusRep(position);
           //     mText.setText(Integer.toString(mLift.getReps().get(position)));
           //     mWeightText.setVisibility(View.VISIBLE);
                mLiftingWorkout.getMyLifts().get(parentPosition).setRepWeight(position, mLiftingWorkout.getMyLifts().get(parentPosition).getWeight());
                mRepository.updateWorkout(mLiftingWorkout);
                notifyDataSetChanged();
            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                System.out.println("lift long click");
                mLiftingWorkout.getMyLifts().get(parentPosition).removeSet(position);
                mRepository.updateWorkout(mLiftingWorkout);
                notifyDataSetChanged();
                return false;
            }
        });

      /*  MaterialFancyButton mFB = holder.mView.findViewById(R.id.segment);

        if(position != (mDataset.size()-1)) {
            mFB.setText("Segment " + (position+1));
        }else{
            mFB.setText("  +  ");
        }*/

        Resources res = holder.itemView.getContext().getResources();
        ResourcesCompat.getColor(res, R.color.colorPrimary, null);

      /*  if(position == selected){
            mFB.setBackgroundColor(ResourcesCompat.getColor(res, R.color.colorAccent, null));
        }else{
            mFB.setBackgroundColor(ResourcesCompat.getColor(res, R.color.colorPrimary, null));
        }*/


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
        return mLiftingWorkout.getMyLifts().get(parentPosition).getReps().size();
    }
}





