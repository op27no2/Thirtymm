package op27no2.fitness.Centurion2.fragments.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import op27no2.fitness.Centurion2.Database.Repository;
import op27no2.fitness.Centurion2.R;
import op27no2.fitness.Centurion2.fragments.lifting.DialogLifts;
import op27no2.fitness.Centurion2.fragments.lifting.MyDialogInterface;
import op27no2.fitness.Centurion2.fragments.run.RunType;
import op27no2.fitness.Centurion2.fragments.run.RunWorkout;

/**
 * Created by CristMac on 11/3/17.
 */

//TOP LEVEL ADAPTER FOR LIFT TAB THE HOLDS THE LIFT CARDS

public class RunCardviewWorkoutAdapter extends RecyclerView.Adapter<RunCardviewWorkoutAdapter.ViewHolder>  implements MyDialogInterface {
    private ArrayList<RunWorkout> mWorkouts;
    private SharedPreferences prefs;
    private SharedPreferences.Editor edt;
    private Repository mRepository;
    private int selected;
    private DialogLifts dialog;
    MyDialogInterface mInterface;
    private Boolean direction = true;
    private ImageView mapView;
    private Context mContext;

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
    public RunCardviewWorkoutAdapter(ArrayList<RunWorkout> workouts, Context context) {
        mWorkouts = workouts;
        mContext = context;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public RunCardviewWorkoutAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view


        View view = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.run_cards_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        mInterface = this;

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

       // TextView mText = holder.mView.findViewById(R.id.text_lift);
        TextView mTextTitle = holder.mView.findViewById(R.id.title);
        TextView mTextDescription = holder.mView.findViewById(R.id.description);
        TextView mTextDate = holder.mView.findViewById(R.id.date);
        TextView mTextDistance = holder.mView.findViewById(R.id.distance_value);
        TextView mTextDistanceUnits = holder.mView.findViewById(R.id.distance_units);
        TextView mTextDuration = holder.mView.findViewById(R.id.duration_value);
        TextView mTextPace = holder.mView.findViewById(R.id.pace_value);
        TextView mTextPaceUnits = holder.mView.findViewById(R.id.pace_units);
        TextView mTextCals = holder.mView.findViewById(R.id.cals_value);
        ImageView mImage = holder.mView.findViewById(R.id.mapView);

        Integer duration = mWorkouts.get(holder.getAdapterPosition()).getDuration();
        Integer distance = mWorkouts.get(holder.getAdapterPosition()).getDistance();
        System.out.println("duration: "+duration);
        System.out.println("distance: "+distance);

        mTextTitle.setText(mWorkouts.get(holder.getAdapterPosition()).getTitle());
        mTextDescription.setText(mWorkouts.get(holder.getAdapterPosition()).getDescription());
        mTextDate.setText(mWorkouts.get(holder.getAdapterPosition()).getWorkoutDate());
        mTextDuration.setText(getDuration(duration));

        RunType mActivityType = mWorkouts.get(holder.getAdapterPosition()).getWorkoutType();
        if(mWorkouts.get(holder.getAdapterPosition()).getWorkoutType() != null) {
            switch (mActivityType.getDistanceUnits()) {
                case 0:
                    //Miles
                    mTextDistance.setText(getMiles(distance));
                    mTextDistanceUnits.setText(" miles");
                    break;
                case 1:
                    //Kilometers
                    double Kilometers = distance / 1000d;
                    System.out.println("after calc "+distance);
                    System.out.println("after calc K "+Kilometers);
                    mTextDistance.setText(String.format("%.3f", Kilometers));
                    mTextDistanceUnits.setText(" km");
                    break;
            }
        }

        if(mActivityType != null){
        long pace = 0;
        float miles = distance * 0.000621371192f;
        float kilometers = distance / 1000f;
        float five = distance / 500f;
        float one = distance/100f;

        //will downfactor calculations in pounds to kg
        double factorUnit = prefs.getInt("units",0) == 0 ? 1 : (1/2.2);
        double factorCal = mActivityType.getCalBurnValue();
        int weight = prefs.getInt("weight",180);

        int calValue = 0;
        switch (mActivityType.getCalBurnUnit()) {
            case 0:
                //Per Minute
                calValue = (int) Math.floor((duration/1000f/60f)*weight*factorUnit*factorCal);
                break;
            case 1:
                //Per Mile
                System.out.println("miles: "+miles+" weight: "+weight+" funit: "+factorUnit+" fcal: "+factorCal);
                calValue = (int) Math.floor(miles*weight*factorUnit*factorCal);
                break;
            case 2:
                //Per Kilometer
                System.out.println("kilo: "+kilometers+" weight: "+weight+" funit: "+factorUnit+" fcal: "+factorCal);
                calValue = (int) Math.floor(kilometers*weight*factorUnit*factorCal);
                break;
            case 3:
                //Per Hour
                calValue = (int) Math.floor((duration/1000f/60f/60f)*weight*factorUnit*factorCal);
                break;
        }

        switch (mActivityType.getPaceUnits()) {
            case 0:
                //minutes per mile
                if (miles != 0) {
                    pace = (long) (duration / (miles));
                }
                mTextPace.setText(getDuration(pace));
                mTextPaceUnits.setText("/mi");
                break;
            case 1:
                //per kilometer
                if (kilometers != 0) {
                    pace = (long) (duration / (kilometers));
                }
                mTextPace.setText(getDuration(pace));
                mTextPaceUnits.setText("/km");
                break;
            case 2:
                //per 500m

                if (five != 0) {
                    pace = (long) (duration / (five));
                }
                mTextPace.setText(getDuration(pace));
                mTextPaceUnits.setText("/500m");
                break;
            case 3:
                //per 100m
                if (one != 0) {
                    pace = (long) (duration / (one));
                }
                mTextPace.setText(getDuration(pace));
                mTextPaceUnits.setText("/100m");
                break;
            case 4:
                //miles per hour
                float mpace = 0;
                if (miles != 0) {
                    mpace = (float) ((miles) / ((float) duration / 3600000f));
                }
                mTextPace.setText(String.format("%.2f", mpace));
                mTextPaceUnits.setText("mph");
                break;
            }
        }

        mTextCals.setText(Integer.toString(mWorkouts.get(holder.getAdapterPosition()).getCalories()));

        if(mWorkouts.get(holder.getAdapterPosition()).getImage() != null && mWorkouts.get(holder.getAdapterPosition()).getSaveMap() == true){
            Glide.with(mContext).load(mWorkouts.get(holder.getAdapterPosition()).getImage()).centerCrop().into(mImage);
        }else{
            mImage.setVisibility(View.GONE);
        }

        CardView mCard = holder.mView.findViewById(R.id.card_view);
        mCard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                System.out.println(" long click");

                return false;
            }
        });



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
        return mWorkouts.size();
    }


    private String getMiles(float i) {
        return String.format("%.2f", i * 0.000621371192f);
    }

/*    private String getDuration(long elapsedTime){
        long micro = elapsedTime / 100000;
        long elapsedSeconds = elapsedTime / 1000;
        long secondsDisplay = elapsedSeconds % 60;
        long elapsedMinutes = elapsedSeconds / 60;
        long minutesDisplay = elapsedMinutes % 60;
        if (secondsDisplay < 10) {
            return((minutesDisplay + ":0" + secondsDisplay));
        } else {
            return((minutesDisplay + ":" + secondsDisplay));
        }
    }*/
    private String getDuration(long elapsedTime){
        long elapsedSeconds = elapsedTime/1000;
        long secondsDisplay = elapsedSeconds % 60;
        long elapsedMinutes = elapsedSeconds / 60;
        long minutesDisplay = elapsedMinutes % 60;
        long hoursDisplay =   elapsedMinutes / 60;
        int hours = (int)  Math.floor(hoursDisplay);
        int minutes = (int)  Math.floor(minutesDisplay);
        int seconds = (int)  Math.floor(secondsDisplay);

        String mhours = "";
        String mminutes = "";
        String mseconds = "";
        //   }
        if(hours>0) {
            mhours = Integer.toString(hours) + ":";

            if (minutes < 10) {
                mminutes = "0" + Integer.toString(minutes);
            } else if (minutes > 0) {
                mminutes = Integer.toString(minutes);
            }
            if (seconds < 10) {
                mseconds = "0" + Integer.toString(seconds);
            } else {
                mseconds = (Integer.toString(seconds));
            }
            return mhours + mminutes + ":" + mseconds;
        }else{

            mminutes = Integer.toString(minutes);
            if (seconds < 10) {
                mseconds = "0" + Integer.toString(seconds);
            } else {
                mseconds = (Integer.toString(seconds));
            }
            return mhours + mminutes + ":" + mseconds;
        }




    }




}



