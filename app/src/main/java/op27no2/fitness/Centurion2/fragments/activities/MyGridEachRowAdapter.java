package op27no2.fitness.Centurion2.fragments.activities;

import android.content.Context;
import android.content.res.Resources;
import android.icu.text.DecimalFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;

import op27no2.fitness.Centurion2.R;

public class MyGridEachRowAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<GoalsDetail> mValues = new ArrayList<GoalsDetail>();
    private Resources resources;
    private Boolean titleRow;

    public MyGridEachRowAdapter(Context context, ArrayList<GoalsDetail> mGoalDetail, Resources res, Boolean isTitleRow) {
        this.mContext = context;
        this.mValues = mGoalDetail;
        this.resources = res;
        this.titleRow = isTitleRow;
    }

        @Override
        public int getCount() {
            return 6;
        }
    /*    public int getCount() {
            return mValues.size()*addTitleRow;
        }*/

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
            DecimalFormat df = new DecimalFormat("#.#");


            //GOAL IS TO BE BELOW VALUE
            if(mValues.get(position).getGoalType() == 0) {
                if(titleRow) {
                    textView.setText(mValues.get(position).getName() + " < " + mValues.get(position).getGoalLimitLow());
                    gridCell.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.darkgrey, null));
                }else {
                    textView.setText((df.format(mValues.get(position).getWeekTotal())) + "/" + df.format((mValues.get(position).getGoalLimitLow())));
                }
            }

            //GOAL IS TO BE BETWEEN TWO VALUES
            if(mValues.get(position).getGoalType() == 1) {
                if(titleRow) {
                    textView.setText(mValues.get(position).getGoalLimitLow()+" < "+mValues.get(position).getName() + " < " + mValues.get(position).getGoalLimitHigh());
                    gridCell.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.darkgrey, null));
                }else {
                    textView.setText(df.format(mValues.get(position).getGoalLimitHigh()) + "/" + df.format(mValues.get(position).getWeekTotal()) + "/" + Double.toString(mValues.get(position).getGoalLimitHigh()));
                }
            }

            //GOAL IS TO BE ABOVE VALUE
            if(mValues.get(position).getGoalType() == 2) {
                if(titleRow) {
                    textView.setText(mValues.get(position).getName() + " < " + mValues.get(position).getGoalLimitHigh());
                    gridCell.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.darkgrey, null));
                }else {
                    textView.setText(df.format(mValues.get(position).getWeekTotal()) + "/" + df.format(mValues.get(position).getGoalLimitHigh()));
                }
            }

            //Check Success
            if (checkSuccess(mValues.get(position)) >= 1) {
                gridCell.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.green, null));
            } else {
                gridCell.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, null));
            }








            return convertView;

        }


    private float checkSuccess(GoalsDetail detail){
        float result = 0f;
        if(detail.getGoalType() == 0) {
            if (detail.getWeekTotal() < detail.getGoalLimitLow()) {
                result = 1+((detail.getGoalLimitLow() - detail.getWeekTotal()) / Math.abs(detail.getGoalLimitLow()));
            } else if (detail.getWeekTotal() == detail.getGoalLimitLow()) {
                result = 1;
            }
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