package op27no2.fitness.Centurion2.fragments.lifting;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import op27no2.fitness.Centurion2.Database.Repository;
import op27no2.fitness.Centurion2.R;

//LIFT TAB ADAPTER for DIALOG FOR SEARCHING / ADDING LIFTS TO A WORKOUT
/**
 * Created by CristMac on 11/3/17.
 */

public class MyDialogAdapter extends RecyclerView.Adapter<MyDialogAdapter.ViewHolder>  {
    private SharedPreferences prefs;
    private SharedPreferences.Editor edt;
    private int selected;
    private Repository mRepository;
    private ArrayList<LiftMap> liftMapArrayList;
    private LiftingWorkout mLiftingWorkout;
    private int parentPosition;
    private MyDialogInterface mListener;
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
    public MyDialogAdapter(Context context, ArrayList<LiftMap> lifts, Repository repository, LiftingWorkout lw, int p, MyDialogInterface ml) {
        liftMapArrayList = lifts;
        mRepository = repository;
        mLiftingWorkout = lw;
        parentPosition = p;
        mListener = ml;
        mContext = context;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyDialogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_view, parent, false);
        // set the view's size, margins, paddings and layout parameters

        prefs = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        edt = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();


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

                //TODO this is correct position language?
                String name = liftMapArrayList.get(holder.getAdapterPosition()).getName();
                int weight = prefs.getInt("last_weight"+name, 200);
                mLiftingWorkout.getMyLifts().get(parentPosition).setName(name);
                mLiftingWorkout.getMyLifts().get(parentPosition).setWeight(weight);
                mRepository.updateWorkout(mLiftingWorkout);
                mListener.onDialogDismiss();
            }
        });


        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                System.out.println("lift dialog long click");
                //TODO create dialog to confirm this delete code
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





