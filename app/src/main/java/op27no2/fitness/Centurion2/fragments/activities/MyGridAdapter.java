package op27no2.fitness.Centurion2.fragments.activities;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;

import op27no2.fitness.Centurion2.R;

public class MyGridAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<GoalsDetail> mValues = new ArrayList<GoalsDetail>();
    private Resources resources;
    private Boolean drawText;

    public MyGridAdapter(Context context, ArrayList<GoalsDetail> mGoalDetail, Resources res) {
        this.mContext = context;
        this.mValues = mGoalDetail;
        this.resources = res;
    }

        @Override
        public int getCount() {
            return mValues.size();
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            if (convertView == null) {
                final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                convertView = layoutInflater.inflate(R.layout.adapter_grid_cell, null);
            }

            final LinearLayout gridCell = (LinearLayout) convertView.findViewById(R.id.grid_cell_layout);
            final TextView textView = (TextView) convertView.findViewById(R.id.grid_text);

            //TODO change to consider goal type
            if(mValues.get(position).getGoalType() == 2) {
                    textView.setText(Double.toString(mValues.get(position).getWeekTotal()) + "/" + Double.toString(mValues.get(position).getGoalLimitHigh()));
            }

            if (checkSuccess(mValues.get(position)) >= 1) {
                gridCell.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.green, null));
            } else {
                gridCell.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, null));
            }


            return convertView;

        }


    private float checkSuccess(GoalsDetail detail){
        float result = 0f;
        if(detail.getGoalType() == 0){
            result = detail.getGoalLimitLow()/detail.getWeekTotal(); //e.g  100/50 will be 2 for a deficit goal. if you go over, 100/150, you are below 1 and we can colorize, this isn't really linear currently, hard to get low on scale
        }
        if(detail.getGoalType() == 1){
            if(detail.getWeekTotal() > detail.getGoalLimitLow() && detail.getWeekTotal()<detail.getGoalLimitHigh()){
                result = 1;
            }else if(detail.getWeekTotal() < detail.getGoalLimitLow()){
                result = detail.getWeekTotal()/detail.getGoalLimitLow();                  //50/100
            }else if(detail.getWeekTotal() > detail.getGoalLimitHigh()){
                result = detail.getGoalLimitHigh()/detail.getWeekTotal();    //e.g. again 100/150 gives low numbers if you go over
            }
        }
        if(detail.getGoalType() == 2) {
            result = detail.getWeekTotal()/detail.getGoalLimitHigh();    //want to go over, so under is<1, i.e. 50/100 if you don't get enough
        }
        return result;
    }





}