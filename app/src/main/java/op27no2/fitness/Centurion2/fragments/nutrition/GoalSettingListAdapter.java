package op27no2.fitness.Centurion2.fragments.nutrition;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import op27no2.fitness.Centurion2.Database.Repository;
import op27no2.fitness.Centurion2.R;
import op27no2.fitness.Centurion2.fragments.activities.GoalsDetail;
import op27no2.fitness.Centurion2.fragments.lifting.LiftMap;

/**
 * Created by CristMac on 11/3/17.
 */

public class GoalSettingListAdapter extends RecyclerView.Adapter<GoalSettingListAdapter.ViewHolder>  {
    private int selected;
    private Repository mRepository;
    private Context mContext;
    private LiftMap mLiftMap;
    private ArrayList<GoalsDetail> mGoalSettingList;

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
    public GoalSettingListAdapter(ArrayList<GoalsDetail> goalList, Repository repository, Context context) {
        mRepository = repository;
        mContext = context;
        mGoalSettingList = goalList;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public GoalSettingListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_activitysettings_goalrow, parent, false);
        // set the view's size, margins, paddings and layout parameters






        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        int pos = holder.getAdapterPosition();

        TextView mText = holder.mView.findViewById(R.id.text1);
        mText.setText(mGoalSettingList.get(position).getGoalName());


        TextView mText2 = holder.mView.findViewById(R.id.text2);
        TextView mText3 = holder.mView.findViewById(R.id.text3);
        TextView mText4 = holder.mView.findViewById(R.id.text4);
        mText2.setText("");
        mText4.setText("");


        if(mGoalSettingList.get(position).getGoalType() == 1) {
            mText2.setText(Integer.toString(mGoalSettingList.get(position).getGoalLimitLow()));
        }else if(mGoalSettingList.get(position).getGoalType() == 2){
            mText2.setText(Integer.toString(mGoalSettingList.get(position).getGoalLimitLow()));
        }

        if(mGoalSettingList.get(position).getGoalType() == 0) {
            mText3.setText("   per week <");
        }else if(mGoalSettingList.get(position).getGoalType() == 1){
            mText3.setText("< per week <");
        }else if(mGoalSettingList.get(position).getGoalType() == 2){
            mText3.setText("< per week   ");
        }

        if(mGoalSettingList.get(position).getGoalType() == 0) {
            mText4.setText(Integer.toString(mGoalSettingList.get(position).getGoalLimitHigh()));
        }else if(mGoalSettingList.get(position).getGoalType() == 1){
            mText4.setText(Integer.toString(mGoalSettingList.get(position).getGoalLimitHigh()));
        }



    }





    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mGoalSettingList.size();
    }


}





