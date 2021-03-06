package op27no2.fitness.Centurion2.fragments.lifting;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import op27no2.fitness.Centurion2.Database.Repository;
import op27no2.fitness.Centurion2.R;

/**
 * Created by CristMac on 11/3/17.
 */

//HORIZONTAL ADAPTER FOR THE REP CIRCLES on LIFT TAB

public class MyRepAdapter extends RecyclerView.Adapter<MyRepAdapter.ViewHolder>{
    private LiftingWorkout mLiftingWorkout;
    private int selected;
    private int parentPosition;
    private Repository mRepository;
    private LiftCardviewWorkoutAdapter parentAdapter;
    private SharedPreferences prefs;
    private SharedPreferences.Editor edt;
    private Vibrator rabbit;





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
    public MyRepAdapter(LiftCardviewWorkoutAdapter madapter, LiftingWorkout lift, int position, Repository repository) {
        mLiftingWorkout = lift;
        parentPosition = position;
        mRepository = repository;
        parentAdapter = madapter;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyRepAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.number_square, parent, false);
        // set the view's size, margins, paddings and layout parameters
        prefs = parent.getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        edt = parent.getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
        rabbit = (Vibrator) v.getContext().getSystemService(Context.VIBRATOR_SERVICE);



        SharedPreferences prefs = v.getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = v.getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final TextView mText = holder.mView.findViewById(R.id.circle_number);
        final LinearLayout mText2 = holder.mView.findViewById(R.id.circle_lin);
        final TextView mWeightText = holder.mView.findViewById(R.id.weight);
        if(mLiftingWorkout.getMyLifts().get(parentPosition).getReps().get(position) == 0) {
            mWeightText.setVisibility(View.GONE);
        }else{
            mWeightText.setVisibility(View.VISIBLE);
        }

        mText.setText(Integer.toString(mLiftingWorkout.getMyLifts().get(parentPosition).getReps().get(position)));
        mWeightText.setText(Integer.toString(mLiftingWorkout.getMyLifts().get(parentPosition).getRepWeights().get(position)));

/*
        mText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mLiftingWorkout.getMyLifts().get(parentPosition).getDirection()) {
                    mLiftingWorkout.getMyLifts().get(parentPosition).plusRep(position);
                }else{
                    mLiftingWorkout.getMyLifts().get(parentPosition).minueRep(position);
                }
                edt.putInt("default_reps"+mLiftingWorkout.getMyLifts().get(parentPosition).getName(), mLiftingWorkout.getMyLifts().get(parentPosition).getRepNumber(position));
                edt.commit();
                mLiftingWorkout.getMyLifts().get(parentPosition).setRepWeight(position, mLiftingWorkout.getMyLifts().get(parentPosition).getWeight());
                mRepository.updateWorkout(mLiftingWorkout);
                notifyDataSetChanged();
            }
        });
*/


        mText2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP){
                    if(event.getY() > (v.getHeight()/2)){
                        rabbit.vibrate(5);
                        System.out.println("bottom half");
                        mLiftingWorkout.getMyLifts().get(parentPosition).minueRep(position);
                    }else if(event.getY() < (v.getHeight()/2)){
                        rabbit.vibrate(15);
                        System.out.println("top half");
                        mLiftingWorkout.getMyLifts().get(parentPosition).plusRep(position);

                    }

                    edt.putInt("default_reps"+mLiftingWorkout.getMyLifts().get(parentPosition).getName(), mLiftingWorkout.getMyLifts().get(parentPosition).getRepNumber(position));
                    edt.apply();
                    mLiftingWorkout.getMyLifts().get(parentPosition).setRepWeight(position, mLiftingWorkout.getMyLifts().get(parentPosition).getWeight());
                    mRepository.updateWorkout(mLiftingWorkout);
                    notifyDataSetChanged();
                }

                return false;
            }
        });

        mText2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final Dialog dialog = new Dialog(view.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_delete);
                DisplayMetrics metrics = view.getContext().getResources().getDisplayMetrics();
                int width = metrics.widthPixels;
                dialog.getWindow().setLayout((8 * width) / 9, LinearLayout.LayoutParams.WRAP_CONTENT);
                TextView mText = dialog.findViewById(R.id.confirm_title);
                mText.setText("Delete?");

                dialog.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mLiftingWorkout.getMyLifts().get(parentPosition).removeSet(position);
                        mRepository.updateWorkout(mLiftingWorkout);

                        notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                return false;
            }
        });

      /*  MaterialFancyButton mFB = holder.mView.findViewById(R.id.segment);

        if(position != (mDataset.size()-1)) {
            mFB.setText("Segment " + (position+1));
        }else{
            mFB.setText("  +  ");
        }*/

        Resources res = holder.itemView.getContext().getResources();
        ResourcesCompat.getColor(res, R.color.colorPrimary, null);

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
        return mLiftingWorkout.getMyLifts().get(parentPosition).getReps().size();
    }
}





