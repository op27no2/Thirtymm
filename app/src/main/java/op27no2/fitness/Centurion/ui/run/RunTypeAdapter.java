package op27no2.fitness.Centurion.ui.run;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import op27no2.fitness.Centurion.R;

//LIFT TAB ADAPTER for DIALOG FOR SEARCHING / ADDING LIFTS TO A WORKOUT

/**
 * Created by CristMac on 11/3/17.
 */

public class RunTypeAdapter extends RecyclerView.Adapter<RunTypeAdapter.ViewHolder>  {
    private SharedPreferences prefs;
    private SharedPreferences.Editor edt;
    private int selected = 1000;
    private ArrayList<String> mRunTypes;
    private Context mContext;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just  a string in this case
        public View mView;
        public ViewHolder(View v) {
            super(v);
            mView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RunTypeAdapter(ArrayList<String> types) {
        mRunTypes = types;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public RunTypeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_view, parent, false);
        // set the view's size, margins, paddings and layout parameters

        mContext = v.getContext();

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        TextView text1 = holder.mView.findViewById(R.id.row_text);


        //TextView mText = holder.mView.findViewById(R.id.workout_name);
       // CardView mCard = holder.mView.findViewById(R.id.card_view);
        text1.setText(mRunTypes.get(position));

        if(position == selected){
            holder.mView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.lightgrey));
        }else{
            holder.mView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
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
        return mRunTypes.size();
    }
}





