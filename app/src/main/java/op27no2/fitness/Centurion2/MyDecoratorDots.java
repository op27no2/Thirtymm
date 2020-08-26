package op27no2.fitness.Centurion2;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;


public class MyDecoratorDots implements DayViewDecorator {

    private final HashSet<CalendarDay> dates;
    private Context mContext;
    private Boolean flagOne;
    private Boolean flagTwo;
    private Boolean flagThree;

    public MyDecoratorDots(Context context, boolean flag1, boolean flag2, boolean flag3, Collection<CalendarDay> dates) {
        mContext = context;
        this.flagOne = flag1;
        this.flagTwo = flag2;
        this.flagThree = flag3;
        this.dates = new HashSet<>(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        if(flagOne && flagTwo && flagThree) {
            int[] colors = new int[3];
            colors[0] = ContextCompat.getColor(mContext, R.color.colorAccentLight);
            colors[1] = ContextCompat.getColor(mContext, R.color.lightgreen);
            colors[2] = ContextCompat.getColor(mContext, R.color.blue);
            view.addSpan((new CustomDotSpan(5, colors)));
        }else if(flagOne && flagTwo){
            int[] colors = new int[2];
            colors[0] = ContextCompat.getColor(mContext, R.color.colorAccentLight);
            colors[1] = ContextCompat.getColor(mContext, R.color.lightgreen);
            view.addSpan((new CustomDotSpan(5, colors)));
        }else if(flagTwo && flagThree){
            int[] colors = new int[2];
            colors[0] = ContextCompat.getColor(mContext, R.color.lightgreen);
            colors[1] = ContextCompat.getColor(mContext, R.color.blue);
            view.addSpan((new CustomDotSpan(5, colors)));
        }else if(flagOne && flagThree){
            int[] colors = new int[2];
            colors[0] = ContextCompat.getColor(mContext, R.color.colorAccentLight);
            colors[1] = ContextCompat.getColor(mContext, R.color.blue);
            view.addSpan((new CustomDotSpan(5, colors)));
        }else if(flagOne){
            int[] colors = new int[1];
            colors[0] = ContextCompat.getColor(mContext, R.color.colorAccentLight);
            view.addSpan((new CustomDotSpan(5, colors)));
        }else if(flagTwo){
            int[] colors = new int[1];
            colors[0] = ContextCompat.getColor(mContext, R.color.lightgreen);
            view.addSpan((new CustomDotSpan(5, colors)));
        }else if(flagThree){
            int[] colors = new int[1];
            colors[0] = ContextCompat.getColor(mContext, R.color.blue);
            view.addSpan((new CustomDotSpan(5, colors)));
        }

    }
}