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
    private ArrayList<GoalsDetail> mGoalDetails = new ArrayList<GoalsDetail>();
    private Resources resources;
    private Boolean titleRow;

    public MyGridEachRowAdapter(Context context, ArrayList<GoalsDetail> mGoalDetail, Resources res, Boolean isTitleRow) {
        this.mContext = context;
        this.mGoalDetails = mGoalDetail;
        this.resources = res;
        this.titleRow = isTitleRow;
    }

        @Override
        public int getCount() {
            return 6;
        }
    /*    public int getCount() {
            return mGoalDetails.size()*addTitleRow;
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
            if(mGoalDetails.get(position).getGoalType() == 0) {
                if(titleRow) {
                    textView.setText(mGoalDetails.get(position).getGoalName() + " < " + mGoalDetails.get(position).getGoalLimitHigh());
                    gridCell.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.darkgrey, null));
                }else {
                    if(mGoalDetails.get(position).getWeekTotal() !=null && mGoalDetails.get(position).getGoalLimitHigh() != null) {
                        textView.setText((df.format((mGoalDetails.get(position).getWeekTotal())) + "/" + df.format((mGoalDetails.get(position).getGoalLimitHigh()))));

                        //Check Success
                        if (checkSuccess(mGoalDetails.get(position)) >= 1) {
                            gridCell.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.green, null));
                        } else {
                            gridCell.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, null));
                        }
                    }else{
                        System.out.println("null week total or goal "+position);
                        System.out.println("null week total or w "+ mGoalDetails.get(position).getWeekTotal());
                        System.out.println("null week total or g "+ mGoalDetails.get(position).getGoalLimitHigh());
                    }
                }
            }

            //GOAL IS TO BE BETWEEN TWO VALUES
            if(mGoalDetails.get(position).getGoalType() == 1) {
                if(titleRow) {
                    textView.setText(mGoalDetails.get(position).getGoalLimitLow()+" < "+ mGoalDetails.get(position).getGoalName() + " < " + mGoalDetails.get(position).getGoalLimitHigh());
                    gridCell.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.darkgrey, null));
                }else {
                    if (mGoalDetails.get(position).getWeekTotal() != null && mGoalDetails.get(position).getGoalLimitLow() != null && mGoalDetails.get(position).getGoalLimitHigh() != null){

                            textView.setText(df.format(mGoalDetails.get(position).getGoalLimitLow()) + "/" + df.format(mGoalDetails.get(position).getWeekTotal()) + "/" + Double.toString(mGoalDetails.get(position).getGoalLimitHigh()));
                        //Check Success
                        if (checkSuccess(mGoalDetails.get(position)) >= 1) {
                            gridCell.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.green, null));
                        } else {
                            gridCell.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, null));
                        }
                     }else{
                        System.out.println("null week total or goal "+position);
                    }
                }
            }

            //GOAL IS TO BE ABOVE VALUE
            if(mGoalDetails.get(position).getGoalType() == 2) {
                if(titleRow) {
                    textView.setText(mGoalDetails.get(position).getGoalName() + " > " + mGoalDetails.get(position).getGoalLimitLow());
                    gridCell.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.darkgrey, null));
                }else {
                    if(mGoalDetails.get(position).getWeekTotal() !=null && mGoalDetails.get(position).getGoalLimitHigh()!= null) {
                        textView.setText(df.format(mGoalDetails.get(position).getWeekTotal()) + "/" + df.format(mGoalDetails.get(position).getGoalLimitLow()));
                        //Check Success
                        if (checkSuccess(mGoalDetails.get(position)) >= 1) {
                            gridCell.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.green, null));
                        } else {
                            gridCell.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, null));
                        }
                    }else{
                        System.out.println("null week total or goal "+position);
                        System.out.println("null week total or w "+ mGoalDetails.get(position).getWeekTotal());
                        System.out.println("null week total or g "+ mGoalDetails.get(position).getGoalLimitLow());
                    }

                }
            }









            return convertView;

        }


    private float checkSuccess(GoalsDetail detail){
        float result = 0f;
        if(detail.getGoalType() == 0) {
            if (detail.getWeekTotal() < detail.getGoalLimitHigh()) {
                result = 1+((detail.getGoalLimitHigh() - detail.getWeekTotal()) / Math.abs(detail.getGoalLimitHigh()));
            } else if (detail.getWeekTotal() == (float) detail.getGoalLimitHigh()) {
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
            result = detail.getWeekTotal()/detail.getGoalLimitLow();    //want to go over, so under is<1, i.e. 50/100 if you don't get enough
        }
        return result;
    }





}