package op27no2.fitness.Centurion.ui.lifting;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import op27no2.fitness.Centurion.Database.Repository;
import op27no2.fitness.Centurion.R;

/**
 * Created by CristMac on 11/3/17.
 */

//HORIZONTAL ADAPTER FOR THE REP CIRCLES on LIFT TAB

public class NamedWorkoutAdapter extends RecyclerView.Adapter<NamedWorkoutAdapter.ViewHolder>{
    private ArrayList<NamedWorkout> mWorkouts;
    private Repository mRepository;
    private LiftingWorkout mLiftingWorkout;
    private NamedWorkoutInterface mInterface;
    private SharedPreferences prefs;
    private SharedPreferences.Editor edt;




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
    //TODO THIS REFENCE IS NOT LIVE, NEED OBSERVER
    public NamedWorkoutAdapter(NamedWorkoutInterface dialogInterface, LiftingWorkout liftingWorkout, ArrayList<NamedWorkout> mNamedWorkouts, Repository repository) {
        mWorkouts = mNamedWorkouts;
        mRepository = repository;
        mLiftingWorkout = liftingWorkout;
        mInterface = dialogInterface;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public NamedWorkoutAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.named_workout_square, parent, false);
        // set the view's size, margins, paddings and layout parameters
        prefs = parent.getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        edt = parent.getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final TextView mText = holder.mView.findViewById(R.id.workout_name);
        final CardView mCard = holder.mView.findViewById(R.id.card_view);


        mText.setText(mWorkouts.get(position).getName());

        Resources res = holder.itemView.getContext().getResources();
        ResourcesCompat.getColor(res, R.color.colorPrimary, null);

        mCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("on click namedworkout");

               /* ArrayList<Lift> liftsToAdd = mWorkouts.get(position).getLifts();
                for(int i =0; i< liftsToAdd.size(); i++){
                    ArrayList<Integer> defaultRep = new ArrayList<Integer>();
                    ArrayList<Integer> defaultWeight = new ArrayList<Integer>();
                    defaultRep.add(prefs.getInt("last_weight"+liftsToAdd.get(i).getName(), 225));
                    defaultWeight.size()
                    liftsToAdd.get(i).setReps(emptyInts);
                }

                mLiftingWorkout.getMyLifts().get(position).setRepNumber(size-1,  prefs.getInt("default_reps"+mLiftingWorkout.getMyLifts().get(position).getName(),0));
*/
                ArrayList<String> liftNames = mWorkouts.get(position).getLifts();
                for(int i=0; i<liftNames.size(); i++){
                    String name =liftNames.get(i);
                    Lift mLift = new Lift(name, prefs.getInt("last_weight"+name, 225));
                    mLift.addSet();
                    mLift.setRepNumber(0, prefs.getInt("default_reps" + name, 0));
                    mLiftingWorkout.addLift(mLift);
                }

                /*ArrayList<Lift> existingLifts = mLiftingWorkout.getMyLifts();
                existingLifts.addAll(liftsToAdd);
                mLiftingWorkout.setMyLifts(existingLifts);*/

                //its not a dialog but using the method call interface here
                mInterface.onDialogDismiss(mLiftingWorkout);

            }
        });

        mCard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                System.out.println("deleting workout");
         /*       mRepository.deleteNamedWorkout(mWorkouts.get(position));
                mInterface.onDialogDismiss();*/
                return false;
            }
        });


      /*  if(position == selected){
            mFB.setBackgroundColor(ResourcesCompat.getColor(res, R.color.colorAccent, null));
        }else{
            mFB.setBackgroundColor(ResourcesCompat.getColor(res, R.color.colorPrimary, null));
        }*/


    }



/*
    public void setSelected(int position){
        selected = position;
    }
    public int getSelected(){
        return selected;
    }
*/



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mWorkouts.size();
    }

}





