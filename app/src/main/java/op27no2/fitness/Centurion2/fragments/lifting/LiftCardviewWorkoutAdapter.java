package op27no2.fitness.Centurion2.fragments.lifting;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.ArrayList;

import op27no2.fitness.Centurion2.Database.AppDatabase;
import op27no2.fitness.Centurion2.Database.Repository;
import op27no2.fitness.Centurion2.R;

/**
 * Created by CristMac on 11/3/17.
 */

//TOP LEVEL ADAPTER FOR LIFT TAB THE HOLDS THE LIFT CARDS

public class LiftCardviewWorkoutAdapter extends RecyclerView.Adapter<LiftCardviewWorkoutAdapter.ViewHolder> implements PickerDialogInterface, MyDialogInterface, NamedWorkoutInterface {
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
    private static final int FOOTER_VIEW = 1;

    private NamedWorkoutAdapter mNamedWorkoutAdapter;
    private RecyclerView mRecyclerView2;
    private ArrayList<NamedWorkout> mNamedWorkouts = new ArrayList<NamedWorkout>();
    private NamedWorkoutInterface mDialogInterface;
    private FlexboxLayoutManager mLayoutManager2;

    private Context mContext;


    //from lift selection interface
    @Override
    public void onDialogDismiss() {
        dialog.dismiss();
        notifyDataSetChanged();
    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    // So you're done with adding a footer and its action on onClick.
    // Now set the default ViewHolder for NormalViewHolder

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Define elements of a row here
        public ViewHolder(View itemView) {
            super(itemView);
            // Find view by ID and initialize here
        }

        public void bindView(int position) {
            // bindView() method to implement actions
        }
    }

      public LiftCardviewWorkoutAdapter(LiftingWorkout workout, ArrayList<NamedWorkout> namedWorkouts , Repository repository, Context context) {
        mLiftingWorkout = workout;
        mRepository = repository;
        mNamedWorkouts = namedWorkouts;
        mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public LiftCardviewWorkoutAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
     /*   View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lift_cards_view, parent, false);
*/
        //interface for lift selection
        mInterface = this;
        //interface to change weight
        passThisInterface = this;
        //interface for named workouts
        mDialogInterface = this;

        prefs = parent.getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        edt = parent.getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();




        View v;
        if (viewType == FOOTER_VIEW) {
            v = (View) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.lift_cards_view_footer, parent, false);
            FooterViewHolder vh = new FooterViewHolder(v);
            // set the view's size, margins, paddings and layout parameters
            rabbit = (Vibrator) v.getContext().getSystemService(Context.VIBRATOR_SERVICE);


            return vh;

        }else{
            v = (View) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.lift_cards_view, parent, false);
            NormalViewHolder vh = new NormalViewHolder(v);
            // set the view's size, margins, paddings and layout parameters

            rabbit = (Vibrator) v.getContext().getSystemService(Context.VIBRATOR_SERVICE);
            return vh;
        }

    }

    // Define a ViewHolder for Footer view
    public class FooterViewHolder extends ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);


        }
    }

    // Now define the ViewHolder for Normal list item
    public class NormalViewHolder extends ViewHolder {
        public NormalViewHolder(View itemView) {
            super(itemView);



        }
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        String type = "normal";
        try {
            if (holder instanceof NormalViewHolder) {
                NormalViewHolder vh = (NormalViewHolder) holder;
                vh.bindView(position);
                holder = vh;
                type="normal";
            } else if (holder instanceof FooterViewHolder) {
                FooterViewHolder vh = (FooterViewHolder) holder;
                holder = vh;
                type="footer";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



        if(type.equals("normal")) {
            // TextView mText = holder.mView.findViewById(R.id.text_lift);
            MaterialFancyButton mText = holder.itemView.findViewById(R.id.text_lift);
            mText.setText(mLiftingWorkout.getMyLifts().get(position).getName());

            //weight text below circle
            TextView mWeightText = holder.itemView.findViewById(R.id.weight_text);
            final int[] weight = {Integer.parseInt(mWeightText.getText().toString())};
            mWeightText.setText(Integer.toString(mLiftingWorkout.getMyLifts().get(position).getWeight()));
            mWeightText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog dialog = new PickerDialog(view.getContext(), position, mRepository, passThisInterface);
                    dialog.show();
                }
            });

            //recyclerview and layoutmanager
            RecyclerView mRecyclerView = holder.itemView.findViewById(R.id.row_recycler_view);
            mRecyclerView.setHasFixedSize(true);
            FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(holder.itemView.getContext());
            layoutManager.setFlexDirection(FlexDirection.ROW);
            layoutManager.setJustifyContent(JustifyContent.FLEX_START);
            layoutManager.setAlignItems(AlignItems.FLEX_START);

        /*mLayoutManager = new LinearLayoutManager(holder.mView.getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);*/
            mRecyclerView.setLayoutManager(layoutManager);

            //set lift data to recyclerview
            LiftCardviewWorkoutAdapter madapter = this;
            MyRepAdapter mRepAdapter = new MyRepAdapter(madapter, mLiftingWorkout, position, mRepository);
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


            ImageView addCircle = holder.itemView.findViewById(R.id.circle_add);
            addCircle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    rabbit.vibrate(10);
                    System.out.println("lift  click");
                    mLiftingWorkout.getMyLifts().get(position).addSet();
                    int size = mLiftingWorkout.getMyLifts().get(position).getRepWeights().size();
                    System.out.println("lift  click size " + size);

                    mLiftingWorkout.getMyLifts().get(position).setRepNumber(size - 1, prefs.getInt("default_reps" + mLiftingWorkout.getMyLifts().get(position).getName(), 0));
                    mRepository.updateWorkout(mLiftingWorkout);
                    mRepAdapter.notifyDataSetChanged();

                }
            });

            ImageView weightUp = holder.itemView.findViewById(R.id.up);
            weightUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rabbit.vibrate(5);
                    mWeightText.setText(Integer.toString(10));

                    int weight = mLiftingWorkout.getMyLifts().get(position).getWeight();

                    System.out.println("weight: " + weight);
                    weight = weight + 5;
                    System.out.println("weight: " + weight);
                    mWeightText.setText(Integer.toString(weight));


                    edt.putInt("last_weight" + mLiftingWorkout.getMyLifts().get(position).getName(), weight);
                    edt.apply();

                    //TODO move repository updates to onPause so this isn't so slow??
                    mLiftingWorkout.getMyLifts().get(position).setWeight(weight);
                    mRepository.updateWorkout(mLiftingWorkout);
                    mWeightText.setText(Integer.toString(weight));
                    notifyDataSetChanged();
                }
            });
            ImageView weightDown = holder.itemView.findViewById(R.id.down);
            weightDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rabbit.vibrate(5);
                    int weight = mLiftingWorkout.getMyLifts().get(position).getWeight();
                    weight = weight - 5;
                    edt.putInt("last_weight" + mLiftingWorkout.getMyLifts().get(position).getName(), weight);
                    edt.apply();
                    mLiftingWorkout.getMyLifts().get(position).setWeight(weight);
                    mRepository.updateWorkout(mLiftingWorkout);
                    mWeightText.setText(Integer.toString(weight));
                    notifyDataSetChanged();
                }
            });


            CardView mCard = holder.itemView.findViewById(R.id.card_view);
            mCard.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    final Dialog dialog = new Dialog(mContext);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_delete);
                    DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
                    int width = metrics.widthPixels;
                    dialog.getWindow().setLayout((8 * width) / 9, LinearLayout.LayoutParams.WRAP_CONTENT);
                    TextView mText = dialog.findViewById(R.id.confirm_title);
                    mText.setText("Delete?");

                    dialog.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            System.out.println("lift long click");
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, getItemCount());
                            mLiftingWorkout.removeLift(position);
                            mRepository.updateWorkout(mLiftingWorkout);

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
        else if(type.equals("footer")){

            System.out.println("footer");
            CardView addButton = holder.itemView.findViewById(R.id.card_view);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Lift mLift = new Lift("Bench Press", prefs.getInt("last_weightBench Press", 225));
                    mLift.addSet();
                    mLift.setRepNumber(0, prefs.getInt("default_reps" + "Bench Press", 0));

                    mLiftingWorkout.addLift(mLift);
                    mRepository.updateWorkout(mLiftingWorkout);
                    notifyItemInserted(getItemCount());

                }
            });

            ViewHolder finalHolder = holder;


            //set named lift data to recyclerview
            //recyclerview and layoutmanager
            mRecyclerView2 = holder.itemView.findViewById(R.id.named_workouts);
            mRecyclerView2.setHasFixedSize(true);
            mLayoutManager2 = new FlexboxLayoutManager(holder.itemView.getContext());
            mLayoutManager2.setFlexDirection(FlexDirection.ROW);
            mLayoutManager2.setJustifyContent(JustifyContent.FLEX_START);
            mLayoutManager2.setAlignItems(AlignItems.FLEX_START);
            mRecyclerView2.setLayoutManager(mLayoutManager2);


            mNamedWorkoutAdapter = new NamedWorkoutAdapter(mDialogInterface, mLiftingWorkout, mNamedWorkouts, mRepository);
            mRecyclerView2.setAdapter(mNamedWorkoutAdapter);
            mRecyclerView2.setItemAnimator(new DefaultItemAnimator());




            //end footer
        }







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
        if(mLiftingWorkout.getMyLifts() == null){
            return 0;
        }


        return mLiftingWorkout.getMyLifts().size()+1;
    }

    // Now define getItemViewType of your own.

    @Override
    public int getItemViewType(int position) {
        if (position == mLiftingWorkout.getMyLifts().size()) {
            // This is where we'll add footer.
            return FOOTER_VIEW;
        }

        return super.getItemViewType(position);
    }


    //from dialog to change weight
    @Override
    public void onPickerDialogDismiss(int weight, int position) {

        edt.putInt("last_weight"+mLiftingWorkout.getMyLifts().get(position).getName() , weight);
        edt.apply();
        mLiftingWorkout.getMyLifts().get(position).setWeight(weight);
        mRepository.updateWorkout(mLiftingWorkout);
        notifyDataSetChanged();
    }


    //from adding named workout
    @Override
    public void onDialogDismiss(LiftingWorkout mWorkout) {
    System.out.println("named workout dialog callback");
        mLiftingWorkout = mWorkout;
        mRepository.updateWorkout(mLiftingWorkout);
        updateNamedWorkouts();
        notifyDataSetChanged();

        //optional smooth scroll to position;
    }


    private void updateNamedWorkouts() {
        new AsyncTask<Void, Void, Void>() {
            protected void onPreExecute() {
                // Pre Code
            }
            protected Void doInBackground(Void... unused) {
                mNamedWorkouts = (ArrayList<NamedWorkout>) AppDatabase.getAppDatabase(mContext).nwDAO().getAll();

                return null;
            }
            protected void onPostExecute(Void unused) {
                // Post Code
                mNamedWorkoutAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

}



