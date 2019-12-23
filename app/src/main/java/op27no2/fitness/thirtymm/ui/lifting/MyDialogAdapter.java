package op27no2.fitness.thirtymm.ui.lifting;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import op27no2.fitness.thirtymm.Database.Repository;
import op27no2.fitness.thirtymm.R;

//LIFT TAB ADAPTER for DIALOG FOR SEARCHING / ADDING LIFTS TO A WORKOUT
/**
 * Created by CristMac on 11/3/17.
 */

public class MyDialogAdapter extends RecyclerView.Adapter<MyDialogAdapter.ViewHolder>  {
    private int selected;
    private Repository mRepository;
    private ArrayList<LiftMap> liftMapArrayList;
    private LiftingWorkout mLiftingWorkout;
    private int parentPosition;
    private MyDialogInterface mListener;
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
    public MyDialogAdapter(ArrayList<LiftMap> lifts, Repository repository, LiftingWorkout lw, int p, MyDialogInterface ml) {
        liftMapArrayList = lifts;
        mRepository = repository;
        mLiftingWorkout = lw;
        parentPosition = p;
        mListener = ml;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyDialogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_view, parent, false);
        // set the view's size, margins, paddings and layout parameters



        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element


        TextView mText = holder.mView.findViewById(R.id.row_text);
        mText.setText(liftMapArrayList.get(position).getName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLiftingWorkout.getMyLifts().get(parentPosition).setName(liftMapArrayList.get(holder.getAdapterPosition()).getName());
                mRepository.updateWorkout(mLiftingWorkout);
                mListener.onDialogDismiss();
            }
        });


        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                System.out.println("lift dialog long click");
                LiftMap row = liftMapArrayList.get(holder.getAdapterPosition());
                mRepository.deleteLiftMap(row);
                liftMapArrayList.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
                return false;
            }
        });

       /* if(position == selected){
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
        return liftMapArrayList.size();
    }
}





