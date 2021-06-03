package op27no2.fitness.Centurion2.fragments.volume;

import android.app.Dialog;
import android.content.Context;
import android.icu.text.DecimalFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import op27no2.fitness.Centurion2.Database.Repository;
import op27no2.fitness.Centurion2.R;

/**
 * Created by CristMac on 11/3/17.
 */

public class VolumeAdapter extends RecyclerView.Adapter<VolumeAdapter.ViewHolder>  {
    private ArrayList<Map.Entry<String, Integer>> mData;
    private HashMap<String, Double> muscleVolumes;
    private ArrayList<String> muscleNames;
    private DialogVolumeMapnterface passThisInterface;
    private Repository mRepository;
    private int mState;
    private Context mContext;

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
    public VolumeAdapter(Context context, ArrayList<Map.Entry<String, Integer>> volume, Repository repository, DialogVolumeMapnterface pass, HashMap<String, Double> muscVolumes, ArrayList<String> muscNames, int recyclerState) {
        mData = volume;
        passThisInterface = pass;
        mRepository = repository;
        muscleVolumes = muscVolumes;
        mContext = context;
        //0 = list of lifts, 1 = list of muscles
        mState = recyclerState;
        muscleNames = muscNames;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public VolumeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.volume_row, parent, false);
        // set the view's size, margins, paddings and layout parameters



        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element


        TextView mText1 = holder.mView.findViewById(R.id.volume_lift);
        TextView mText2 = holder.mView.findViewById(R.id.volume_sets);
        int pos = holder.getAdapterPosition();

        if(mState == 0) {
            mText1.setText(mData.get(pos).getKey());
            mText2.setText(Integer.toString(mData.get(pos).getValue()));
        }else if(mState ==1){
            mText1.setText(muscleNames.get(pos));
           // mText2.setText(Double.toString(muscleVolumes.get(muscleNames.get(pos))));
            if(muscleVolumes.get(muscleNames.get(pos)) != null) {
                mText2.setText(new DecimalFormat("#.##").format(muscleVolumes.get(muscleNames.get(pos))));
            }
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mState == 0 ) {
                    Dialog dialog = new DialogVolumeMap(view.getContext(), mRepository, mData.get(position).getKey(), passThisInterface);
                    dialog.show();
                }
            }
        });


    }



 /*   public void setSelected(int position){
        selected = position;
    }
    public int getSelected(){
        return selected;
    }
*/
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        int size = 0;
        if(mState == 0){
            size = mData.size();
        }else{
            size = muscleNames.size();
        }

        return size;
    }
}





