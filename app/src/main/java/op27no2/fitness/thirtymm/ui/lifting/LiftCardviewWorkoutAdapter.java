package op27no2.fitness.thirtymm.ui.lifting;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.rilixtech.materialfancybutton.MaterialFancyButton;

import op27no2.fitness.thirtymm.Database.Repository;
import op27no2.fitness.thirtymm.R;
import op27no2.fitness.thirtymm.ui.volume.DialogVolumeMap;
import op27no2.fitness.thirtymm.ui.volume.DialogVolumeMapnterface;

/**
 * Created by CristMac on 11/3/17.
 */

//TOP LEVEL ADAPTER FOR LIFT TAB THE HOLDS THE LIFT CARDS

public class LiftCardviewWorkoutAdapter extends RecyclerView.Adapter<LiftCardviewWorkoutAdapter.ViewHolder>  implements PickerDialogInterface, MyDialogInterface {
    private LiftingWorkout mLiftingWorkout;
    private SharedPreferences prefs;
    private SharedPreferences.Editor edt;
    private Repository mRepository;
    private int selected;
    private DialogLifts dialog;
    MyDialogInterface mInterface;
    private Boolean direction = true;
    private Vibrator rabbit;
    private PickerDialogInterface passThisInterface;


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

      public LiftCardviewWorkoutAdapter(LiftingWorkout workout, Repository repository) {
        mLiftingWorkout = workout;
        mRepository = repository;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public LiftCardviewWorkoutAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lift_cards_view, parent, false);
        // set the view's size, margins, paddings and layout parameters

        //interface for lift selection
        mInterface = this;

        //interface to change weight
        passThisInterface = this;

        prefs = parent.getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        edt = parent.getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
        rabbit = (Vibrator) v.getContext().getSystemService(Context.VIBRATOR_SERVICE);


        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

       // TextView mText = holder.mView.findViewById(R.id.text_lift);
        MaterialFancyButton mText = holder.mView.findViewById(R.id.text_lift);
        mText.setText(mLiftingWorkout.getMyLifts().get(position).getName());

        //weight text below circle
        final TextView mWeightText = holder.mView.findViewById(R.id.weight_text);
        final int[] weight = {Integer.parseInt(mWeightText.getText().toString())};
        mWeightText.setText(Integer.toString(mLiftingWorkout.getMyLifts().get(position).getWeight()));
        mWeightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new PickerDialog(view.getContext(), position,  mRepository, passThisInterface);
                dialog.show();
            }
        });


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
        LiftCardviewWorkoutAdapter madapter = this;
        final MyRepAdapter mRepAdapter = new MyRepAdapter(madapter, mLiftingWorkout, position, mRepository);
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

        mText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("lift  click");
                dialog = new DialogLifts(view.getContext(), mRepository, mLiftingWorkout, position, mInterface);
                dialog.show();

            }
        });


        ImageView addCircle = holder.mView.findViewById(R.id.circle_add);
        addCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rabbit.vibrate(10);
                System.out.println("lift  click");
                mLiftingWorkout.getMyLifts().get(position).addSet();
                int size = mLiftingWorkout.getMyLifts().get(position).getRepWeights().size();
                System.out.println("lift  click size "+size);

                mLiftingWorkout.getMyLifts().get(position).setRepNumber(size-1,  prefs.getInt("default_reps"+mLiftingWorkout.getMyLifts().get(position).getName(),0));
                mRepository.updateWorkout(mLiftingWorkout);
                mRepAdapter.notifyDataSetChanged();
            }
        });

        ImageView weightUp = holder.mView.findViewById(R.id.up);
        weightUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rabbit.vibrate(5);

                int weight = mLiftingWorkout.getMyLifts().get(position).getWeight();
                weight = weight+5;
                edt.putInt("last_weight"+mLiftingWorkout.getMyLifts().get(position).getName() , weight);
                edt.commit();
                mLiftingWorkout.getMyLifts().get(position).setWeight(weight);
                mRepository.updateWorkout(mLiftingWorkout);
                mWeightText.setText(Integer.toString(weight));
                notifyDataSetChanged();
            }
        });
        ImageView weightDown = holder.mView.findViewById(R.id.down);
        weightDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rabbit.vibrate(5);
                int weight = mLiftingWorkout.getMyLifts().get(position).getWeight();
                weight = weight-5;
                edt.putInt("last_weight"+mLiftingWorkout.getMyLifts().get(position).getName() , weight);
                edt.commit();
                mLiftingWorkout.getMyLifts().get(position).setWeight(weight);
                mRepository.updateWorkout(mLiftingWorkout);
                mWeightText.setText(Integer.toString(weight));
                notifyDataSetChanged();
            }
        });


        CardView mCard = holder.mView.findViewById(R.id.card_view);
        mCard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                System.out.println("lift long click");
                mLiftingWorkout.removeLift(position);
                mRepository.updateWorkout(mLiftingWorkout);
                notifyDataSetChanged();
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
        return mLiftingWorkout.getMyLifts().size();
    }


    @Override
    public void onPickerDialogDismiss(int weight, int position) {

        edt.putInt("last_weight"+mLiftingWorkout.getMyLifts().get(position).getName() , weight);
        edt.commit();
        mLiftingWorkout.getMyLifts().get(position).setWeight(weight);
        mRepository.updateWorkout(mLiftingWorkout);
        notifyDataSetChanged();
    }




}


