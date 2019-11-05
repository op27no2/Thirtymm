package op27no2.fitness.thirtymm.ui.lifting;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;

import op27no2.fitness.thirtymm.MainActivity;
import op27no2.fitness.thirtymm.R;

/**
 * Created by CristMac on 11/3/17.
 */

public class MyLiftAdapter extends RecyclerView.Adapter<MyLiftAdapter.ViewHolder> {
    private ArrayList<WorkoutLift.Lift> mDataset;
    private int selected;



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
    public MyLiftAdapter(ArrayList<WorkoutLift.Lift> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyLiftAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_view, parent, false);
        // set the view's size, margins, paddings and layout parameters



        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        TextView mText = holder.mView.findViewById(R.id.text_lift);
        mText.setPaintFlags(mText.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        mText.setText(mDataset.get(position).getName());
        final TextView mWeightText = holder.mView.findViewById(R.id.weight_text);
        final int[] weight = {Integer.parseInt(mWeightText.getText().toString())};

        //test workout
        final WorkoutLift mWorkout = new WorkoutLift();
        mWorkout.addLift("Bench Press");
        mWorkout.addLift("Pull Ups");
        mWorkout.addLift("Dead Lift");


        final ArrayList<WorkoutLift.Lift>[] mLiftDataset = new ArrayList[]{mWorkout.getLifts()};

        //recyclerview and layoutmanager
        RecyclerView mRecyclerView = holder.mView.findViewById(R.id.row_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(holder.mView.getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        layoutManager.setAlignItems(AlignItems.FLEX_START);

        /*mLayoutManager = new LinearLayoutManager(holder.mView.getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);*/
        mRecyclerView.setLayoutManager(layoutManager);

        //set lift data to recyclerview
        final MyRepAdapter mRepAdapter = new MyRepAdapter(mLiftDataset[0], position);
        mRecyclerView.setAdapter(mRepAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //recyclerview on touch events
      /*  mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(holder.mView.getContext(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        System.out.println("item clicked: "+position);
                        TextView mText = view.findViewById(R.id.circle_number);
                        int num = Integer.parseInt(mText.getText().toString());
                        num++;
                        mText.setText(Integer.toString(num));
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        System.out.println("item long clicked: "+position);


                    }
                })
        );*/


        ImageView addCircle = holder.mView.findViewById(R.id.circle_add);
        addCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("lift  click");
                mWorkout.addLift("Dead Lift");
                mLiftDataset[0] = mWorkout.getLifts();
                mRepAdapter.notifyDataSetChanged();
            }
        });


        final SharedPreferences prefs = holder.mView.getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        final SharedPreferences.Editor edt = holder.mView.getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();

        ImageView weightUp = holder.mView.findViewById(R.id.up);
        weightUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weight[0] = weight[0] +5;
                mWeightText.setText(Integer.toString(weight[0]));
                edt.putInt("weightRow"+position,weight[0]);
                edt.commit();
            }
        });
        ImageView weightDown = holder.mView.findViewById(R.id.down);
        weightDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weight[0] = weight[0] -5;
                mWeightText.setText(Integer.toString(weight[0]));
                edt.putInt("weightRow"+position,weight[0]);
                edt.commit();
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
        return mDataset.size();
    }




}



