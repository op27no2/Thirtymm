package op27no2.fitness.Centurion2.fragments.nutrition;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

import op27no2.fitness.Centurion2.Database.Repository;
import op27no2.fitness.Centurion2.R;
import op27no2.fitness.Centurion2.fragments.lifting.LiftMap;
import op27no2.fitness.Centurion2.fragments.lifting.LiftingWorkout;
import op27no2.fitness.Centurion2.fragments.lifting.MyDialogInterface;

/**
 * Created by CristMac on 11/3/17.
 */

public class DialogGoalListAdapter extends RecyclerView.Adapter<DialogGoalListAdapter.ViewHolder>  {
    private int selected;
    private Repository mRepository;
    private ArrayList<String> mData;
    private LiftingWorkout mLiftingWorkout;
    private int parentPosition;
    private MyDialogInterface mListener;
    private Context mContext;
    private LiftMap mLiftMap;
    private ArrayList<Integer> ratios;
    private ArrayList<String> name;
    private ArrayList<Integer> mVisibilities;

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
    public DialogGoalListAdapter(LiftMap map, Repository repository, Context context, ArrayList<Integer> visibilities) {
        mLiftMap = map;
        mRepository = repository;
        mContext = context;
        mVisibilities = visibilities;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public DialogGoalListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
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
        //mText.setText(mLiftMap.getMuscles().get(position));
        mText.setText(mLiftMap.getMuscles().get(mVisibilities.get(position)));
        String muscle = mLiftMap.getMuscles().get(mVisibilities.get(position));
        if(muscle.equals("Arms")||muscle.equals("Chest")||muscle.equals("Back")||muscle.equals("Core")||muscle.equals("Legs")){
            mText.setTextColor(Color.BLACK);
            mText.setTextSize(30);
        }else{
            mText.setTextColor(Color.GRAY);
            mText.setTextSize(20);
        }

        TextView mText2 = holder.mView.findViewById(R.id.text2);
        Double rat = (double) mLiftMap.getRatios().get(mVisibilities.get(position))/10;
        mText2.setText(Double.toString(rat));

        if(mVisibilities.get(position) == selected){
            holder.mView.setBackgroundColor(ColorUtils.setAlphaComponent(ContextCompat.getColor(holder.mView.getContext(), R.color.lightgrey),200));
        }else{
            holder.mView.setBackgroundColor(ContextCompat.getColor(holder.mView.getContext(), R.color.white));
        }



 /*       EditText mEdit = holder.mView.findViewById(R.id.edit_text);
        mEdit.setText(Double.toString(mLiftMap.getRatios().get(position)));
        mEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                //TODO THIS IS A MESS, NEED TO GRAB ALL EDIT TEXT ON-DISMISS AND UPDATE LiftMap Once. OR maybe have separate Array up top, not gauranteed to match I suppose.

                if(!s.toString().isEmpty()) {
                    System.out.println("muscle at position: "+holder.getAdapterPosition());
                    ratios.set(holder.getAdapterPosition(), Double.parseDouble(s.toString()));
                    *//*mLiftMap.setRatios(ratios);
                    mRepository.updateLiftMap(mLiftMap);*//*
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });*/



    }


    public ArrayList<Integer> getRatios(){

        return ratios;
    }

    public void setSelected(int position){
        selected = mVisibilities.get(position);
    }
    public int getSelected(){
        return selected;
    }

    public String getLine(int position){
        return mLiftMap.getMuscles().get(mVisibilities.get(position));
    }
    public int getRealPosition(int position){
        return (mVisibilities.get(position));
    }


    public void toggleVisibilty(int position){
        if(mVisibilities.contains(position)){
            mVisibilities.remove(mVisibilities.indexOf(position));
        }else{
            mVisibilities.add(position);
        }
        Collections.sort(mVisibilities);
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        int size = 38;
        if(mVisibilities.size() != 0){
             size = mVisibilities.size();
        }

        return size;
    }


}





