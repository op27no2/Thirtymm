package op27no2.fitness.Centurion.ui.activities;

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

import op27no2.fitness.Centurion.R;

public class MyGridHeaderAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Double> mValues = new ArrayList<Double>();
    private Resources resources;
    private Boolean drawText;

    public MyGridHeaderAdapter(Context context, ArrayList<Double> values, Resources res) {
        this.mContext = context;
        this.mValues = values;
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

            final Double value = mValues.get(position);

            if (convertView == null) {
                final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                convertView = layoutInflater.inflate(R.layout.adapter_grid_cell, null);
            }

            final LinearLayout gridCell = (LinearLayout) convertView.findViewById(R.id.grid_cell_layout);
            final TextView textView = (TextView)convertView.findViewById(R.id.grid_text);


             //   textView.setText(Double.toString(mValues.get(position)));


                if (value > 1) {
                    gridCell.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.green, null));
                } else {
                    gridCell.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, null));
                }

            return convertView;

        }

    }