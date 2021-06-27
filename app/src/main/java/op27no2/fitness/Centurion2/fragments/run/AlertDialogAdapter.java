package op27no2.fitness.Centurion2.fragments.run;

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

public class AlertDialogAdapter extends RecyclerView.Adapter<AlertDialogAdapter.ViewHolder>  {
    private SharedPreferences prefs;
    private SharedPreferences.Editor edt;
    private int selected;
    private Repository mRepository;
    private ArrayList<AlertProfile> mAlertProfiles;
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
    public AlertDialogAdapter(Context context, Repository repository,ArrayList<AlertProfile> profiles) {
        mRepository = repository;
        mContext = context;
        mAlertProfiles = profiles;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public AlertDialogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
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
        mText.setText(mAlertProfiles.get(holder.getAdapterPosition()).getmName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

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
        return mAlertProfiles.size();
    }
}





