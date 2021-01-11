package op27no2.fitness.Centurion2.fragments.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import op27no2.fitness.Centurion2.Database.Repository;
import op27no2.fitness.Centurion2.R;
import op27no2.fitness.Centurion2.fragments.lifting.DialogLifts;
import op27no2.fitness.Centurion2.fragments.lifting.MyDialogInterface;

/**
 * Created by CristMac on 11/3/17.
 */

//TOP LEVEL ADAPTER FOR LIFT TAB THE HOLDS THE LIFT CARDS

public class ProgressAdapter extends RecyclerView.Adapter<ProgressAdapter.ViewHolder>  implements MyDialogInterface {
    private SharedPreferences prefs;
    private SharedPreferences.Editor edt;
    private Repository mRepository;
    private int selected;
    private DialogLifts dialog;
    MyDialogInterface mInterface;
    private Boolean direction = true;
    private ImageView mapView;
    private Context mContext;
    private int offset = 0;
    ArrayList<ArrayList<GoalsDetail>> goalWeekData = new ArrayList<ArrayList<GoalsDetail>>();
    ArrayList<String> calendarWeekData = new ArrayList<String>();
    ArrayList<Integer> goalChangePositions = new ArrayList<Integer>();

    @Override
    public void onDialogDismiss() {
        dialog.dismiss();
        notifyDataSetChanged();
    }


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
    public ProgressAdapter(ArrayList<ArrayList<GoalsDetail>> mGoals, ArrayList<String> weeks, Context context) {
        calendarWeekData = weeks;
        goalWeekData = mGoals;
        mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ProgressAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view

        View view = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_progress_line, parent, false);
        // set the view's size, margins, paddings and layout parameters
        mInterface = this;
        System.out.println("Progress Adapter data0: "+goalWeekData.size());


        prefs = parent.getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        edt = parent.getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();

        mapView = view.findViewById(R.id.mapView);



        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Resources res = holder.itemView.getContext().getResources();
        ResourcesCompat.getColor(res, R.color.colorPrimary, null);

        TextView mText = holder.mView.findViewById(R.id.week_text);
        GridView grid = holder.mView.findViewById(R.id.grid_view);
        GridView gridTitle = holder.mView.findViewById(R.id.grid_view_title);


        int pos = holder.getAdapterPosition();


        MyGridEachRowAdapter gridAdapter = new MyGridEachRowAdapter(mContext, goalWeekData.get(pos) , res, false);
      //  grid.setNumColumns(goalWeekData.size());
        grid.setNumColumns(goalWeekData.get(pos).size());
        grid.setAdapter(gridAdapter);

        if(pos==0){
            gridTitle.setVisibility(View.VISIBLE);
            MyGridEachRowAdapter gridAdapter2 = new MyGridEachRowAdapter(mContext, goalWeekData.get(pos) , res, true);
            //  grid.setNumColumns(goalWeekData.size());
            gridTitle.setNumColumns(goalWeekData.get(pos).size());
            gridTitle.setAdapter(gridAdapter2);

        }

        mText.setText(calendarWeekData.get(pos));





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
        return goalWeekData.size();
    }


    //returns a float based on success. greater thanor= 1 should indicate success, <1 is a percent failure for color map
    private float checkSuccess(int value, GoalsDetail detail){
        float result = 0f;
        if(detail.getGoalType() == 0){
            result = detail.getGoalLimitLow()/value; //e.g  100/50 will be 2 for a deficit goal. if you go over, 100/150, you are below 1 and we can colorize, this isn't really linear currently, hard to get low on scale
        }
        if(detail.getGoalType() == 1){
            if(value > detail.getGoalLimitLow() && value<detail.getGoalLimitHigh()){
                result = 1;
            }else if(value < detail.getGoalLimitLow()){
                result = value/detail.getGoalLimitLow();                  //50/100
            }else if(value > detail.getGoalLimitHigh()){
                result = detail.getGoalLimitHigh()/value;    //e.g. again 100/150 gives low numbers if you go over
            }
        }
        if(detail.getGoalType() == 2) {
            result = value/detail.getGoalLimitHigh();    //want to go over, so under is<1, i.e. 50/100 if you don't get enough
        }
        return result;
    }


}



