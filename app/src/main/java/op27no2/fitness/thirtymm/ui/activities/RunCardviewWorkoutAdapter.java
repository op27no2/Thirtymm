package op27no2.fitness.thirtymm.ui.activities;

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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.rilixtech.materialfancybutton.MaterialFancyButton;

import java.util.ArrayList;

import op27no2.fitness.thirtymm.Database.Repository;
import op27no2.fitness.thirtymm.R;
import op27no2.fitness.thirtymm.ui.lifting.DialogLifts;
import op27no2.fitness.thirtymm.ui.lifting.LiftingWorkout;
import op27no2.fitness.thirtymm.ui.lifting.MyDialogInterface;
import op27no2.fitness.thirtymm.ui.lifting.MyRepAdapter;
import op27no2.fitness.thirtymm.ui.run.RunWorkout;

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
        TextView mText1 = holder.mView.findViewById(R.id.distance_value);
        TextView mText2 = holder.mView.findViewById(R.id.duration_value);
        TextView mText3 = holder.mView.findViewById(R.id.pace_value);
        TextView mText4 = holder.mView.findViewById(R.id.date);
        TextView mText5 = holder.mView.findViewById(R.id.cals_value);
        ImageView mImage = holder.mView.findViewById(R.id.mapView);

        Integer duration = mWorkouts.get(position).getDuration();
        Integer distance = mWorkouts.get(position).getDistance();

        long pace = (long) (duration / (distance*0.000621371192f));

        mText1.setText(getMiles(distance));
        mText2.setText(getDuration(duration));
        mText3.setText(getDuration(pace)+" /mi");
        mText4.setText(mWorkouts.get(position).getWorkoutDate());
        mText5.setText(Integer.toString(mWorkouts.get(position).getCalories()));
        if(mWorkouts.get(position).getImage() != null){
            Glide.with(mContext).load(mWorkouts.get(position).getImage()).centerCrop().into(mImage);
        }
        System.out.println("bitmaps: " +position+" " + mWorkouts.get(position).getImage());


        CardView mCard = holder.mView.findViewById(R.id.card_view);
        mCard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                System.out.println("lift long click");



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

    private String getDuration(long elapsedTime){
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
    }




}



