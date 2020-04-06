package op27no2.fitness.thirtymm.ui.volume;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Map;

import op27no2.fitness.thirtymm.Database.Repository;
import op27no2.fitness.thirtymm.R;

/**
 * Created by CristMac on 11/3/17.
 */

public class VolumeAdapter extends RecyclerView.Adapter<VolumeAdapter.ViewHolder>  {
    private ArrayList<Map.Entry<String, Integer>> mData;
    private DialogVolumeMapnterface passThisInterface;
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
    public VolumeAdapter(ArrayList<Map.Entry<String, Integer>> volume, Repository repository, DialogVolumeMapnterface pass) {
        mData = volume;
        passThisInterface = pass;
        mRepository = repository;
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

        mText1.setText(mData.get(position).getKey());
        mText2.setText(Integer.toString(mData.get(position).getValue()));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new DialogVolumeMap(view.getContext(), mRepository, mData.get(position).getKey(), passThisInterface);

                dialog.show();

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
        return mData.size();
    }
}





