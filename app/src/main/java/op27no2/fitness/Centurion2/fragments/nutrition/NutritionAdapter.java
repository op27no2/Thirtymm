package op27no2.fitness.Centurion2.fragments.nutrition;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import op27no2.fitness.Centurion2.Database.Repository;
import op27no2.fitness.Centurion2.R;
import op27no2.fitness.Centurion2.fragments.activities.GoalsDetail;
import op27no2.fitness.Centurion2.fragments.lifting.PickerDialogInterface;

/**
 * Created by CristMac on 11/3/17.
 */

public class NutritionAdapter extends RecyclerView.Adapter<NutritionAdapter.ViewHolder>  {
    private ArrayList<String> mNames;
    private ArrayList<Integer> mValues;
    private ArrayList<GoalsDetail> mGoalTopList;
    private int selected;
    private Repository mRepository;
    private PickerDialogInterface passThisInterface;


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
    public NutritionAdapter(ArrayList<String> names, ArrayList<Integer> values, ArrayList<GoalsDetail> goalTopList, PickerDialogInterface mInterface ) {
        mNames = names;
        mValues = values;
        mGoalTopList = goalTopList;
        passThisInterface = mInterface;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NutritionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.volume_row, parent, false);
        // set the view's size, margins, paddings and layout parameters
        mRepository = new Repository(v.getContext());


        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        int pos = holder.getAdapterPosition();

        TextView mText1 = holder.mView.findViewById(R.id.volume_lift);
        TextView mText2 = holder.mView.findViewById(R.id.volume_sets);


        mText1.setText(mNames.get(pos));
        mText2.setText(Integer.toString(mValues.get(pos)));

        mText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("heyhey");
              /*  Dialog dialog = new EditDialog(view.getContext(),position, mValues.get(position), passThisInterface);
                dialog.show();*/
            }
        });


        if(pos == selected){
            holder.mView.setBackgroundColor(ColorUtils.setAlphaComponent(ContextCompat.getColor(holder.mView.getContext(), R.color.lightgrey),200));
        }else{
            holder.mView.setBackgroundColor(ContextCompat.getColor(holder.mView.getContext(), R.color.white));
        }

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
        return mNames.size();
    }



    //meh I didn't like adding goals on the list
    private int getGoalIndex(String name){
        int index = -1;
        for(int i=0;i<mGoalTopList.size();i++){
            if(mGoalTopList.get(i).getGoalName().equals(name)){
                index = i;
            }
        }
        return index;
    }

    private String getGoalString(String name) {
        String mGoal = "";
        int type = mGoalTopList.get(getGoalIndex(name)).getGoalType();
        switch (type) {
            case 0:
                mGoal = "[goal: <" + mGoalTopList.get(getGoalIndex(name)).getGoalLimitHigh() + "/week";
                return mGoal;
            case 1:
                mGoal = "[goal: " + mGoalTopList.get(getGoalIndex(name)).getGoalLimitLow() + "</week<" + mGoalTopList.get(getGoalIndex(name)).getGoalLimitHigh();
                return mGoal;
            case 2:
                mGoal = "[goal: >" + mGoalTopList.get(getGoalIndex(name)).getGoalLimitLow() + "/week";
                return mGoal;
            default:
                return mGoal;
        }
    }



}





