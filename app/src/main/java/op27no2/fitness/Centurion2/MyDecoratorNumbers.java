package op27no2.fitness.Centurion2;

import android.content.Context;
import android.graphics.Color;
import android.text.style.ForegroundColorSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;


public class MyDecoratorNumbers implements DayViewDecorator {

    private final HashSet<CalendarDay> dates;
    private Context mContext;
    private Double mDistance;
    private Double mSets;

    public MyDecoratorNumbers(Context context, Double distance, Double sets, Collection<CalendarDay> dates) {
        mContext = context;
        mDistance = distance;
        mSets = sets;
        this.dates = new HashSet<>(dates);

    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        if(mDistance != null && mSets != null) {
            CustomCalendarSpan test = new CustomCalendarSpan(mDistance.toString(), mSets.toString(), mContext);
            view.addSpan(test);
            view.addSpan(new ForegroundColorSpan(Color.TRANSPARENT));
        }else if(mDistance != null) {
            CustomCalendarSpan test = new CustomCalendarSpan(mDistance.toString(), null, mContext);
            view.addSpan(test);
            view.addSpan(new ForegroundColorSpan(Color.TRANSPARENT));
        }else if(mSets != null) {
            CustomCalendarSpan test = new CustomCalendarSpan(null, mSets.toString(), mContext);
            view.addSpan(test);
            view.addSpan(new ForegroundColorSpan(Color.TRANSPARENT));
        }






    }
}