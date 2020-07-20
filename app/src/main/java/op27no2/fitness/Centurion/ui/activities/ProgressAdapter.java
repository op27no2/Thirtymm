package op27no2.fitness.Centurion.ui.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import op27no2.fitness.Centurion.Database.Repository;
import op27no2.fitness.Centurion.R;
import op27no2.fitness.Centurion.ui.lifting.DialogLifts;
import op27no2.fitness.Centurion.ui.lifting.MyDialogInterface;

/**
 * Created by CristMac on 11/3/17.
 */

//TOP LEVEL ADAPTER FOR LIFT TAB THE HOLDS THE LIFT CARDS

public class ProgressAdapter extends RecyclerView.Adapter<ProgressAdapter.ViewHolder>  implements MyDialogInterface {
    private SharedPreferences prefs;
    private SharedPreferences.Editor edt;
    private Repository mRepository;
    private int selected;
    private DialogLifts dialog;
    MyDialogInterface mInterface;
    private Boolean direction = true;
    private ImageView mapView;
    private Context mContext;
    ArrayList<Float> setWeekData = new ArrayList<Float>();
    ArrayList<Integer> calWeekData = new ArrayList<Integer>();
    ArrayList<Integer> proteinWeekData = new ArrayList<Integer>();
    ArrayList<String> calendarWeekData = new ArrayList<String>();

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
    public ProgressAdapter(ArrayList<Float> sets, ArrayList<Integer> cals, ArrayList<Integer> protein, ArrayList<String> weeks, Context context) {
        setWeekData = sets;
        calWeekData = cals;
        proteinWeekData = protein;
        calendarWeekData = weeks;
        mContext = context;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ProgressAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view


        View view = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_progress_line, parent, false);
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
        Resources res = holder.itemView.getContext().getResources();
        ResourcesCompat.getColor(res, R.color.colorPrimary, null);

        TextView mText = holder.mView.findViewById(R.id.week_text);
        GridView grid = holder.mView.findViewById(R.id.grid_view);
        ArrayList<Double> gridCol = new ArrayList<Double>();

        int pos = holder.getAdapterPosition();
        gridCol.add(calWeekData.get(pos) / (double) prefs.getInt("volume",0));
        gridCol.add(calWeekData.get(pos) / (double) prefs.getInt("deficit", 0));
        gridCol.add(proteinWeekData.get(pos) /    ((double) prefs.getInt("weight", 0))*((double) prefs.getFloat("protein", 0))   );
        System.out.println("data: "+gridCol.get(0) + " "+gridCol.get(1)+" "+gridCol.get(2));
        MyGridAdapter gridAdapter = new MyGridAdapter(mContext, gridCol, res);
        grid.setNumColumns(gridCol.size());
        grid.setAdapter(gridAdapter);

        mText.setText(calendarWeekData.get(holder.getAdapterPosition()));



      /*  ImageView image1 = holder.mView.findViewById(R.id.column1);
        ImageView image2 = holder.mView.findViewById(R.id.column2);
        ImageView image3 = holder.mView.findViewById(R.id.column3);
        ImageView image4 = holder.mView.findViewById(R.id.column4);*/

/*
        if(holder.getAdapterPosition()<setWeekData.size() && setWeekData.get(holder.getAdapterPosition()) > prefs.getFloat("volume",0f)){
            image1.setBackgroundColor(ResourcesCompat.getColor(res, R.color.green, null));
            }else{
            image1.setBackgroundColor(ResourcesCompat.getColor(res, R.color.orange, null));
        }
        if(holder.getAdapterPosition()<calWeekData.size() && calWeekData.get(holder.getAdapterPosition()) < prefs.getInt("deficit", 0)) {
            image2.setBackgroundColor(ResourcesCompat.getColor(res, R.color.green, null));
            }else{
            image2.setBackgroundColor(ResourcesCompat.getColor(res, R.color.yellow, null));
        }
        if(holder.getAdapterPosition()<proteinWeekData.size() && proteinWeekData.get(holder.getAdapterPosition()) > prefs.getFloat("protein", 0)) {
            image3.setBackgroundColor(ResourcesCompat.getColor(res, R.color.green, null));
            }else{
            image3.setBackgroundColor(ResourcesCompat.getColor(res, R.color.orange, null));
        }
        image4.setBackgroundColor(ResourcesCompat.getColor(res, R.color.colorPrimaryDark, null));
*/


        //holder.getAdapterPosition()





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
        return calWeekData.size();
    }



}



