package op27no2.fitness.Centurion;

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

    public MyDecoratorNumbers(Context context, Double distance, Collection<CalendarDay> dates) {
        mContext = context;
        mDistance = distance;

        this.dates = new HashSet<>(dates);

    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        CustomCalendarSpan test = new CustomCalendarSpan(mDistance.toString(), mContext);
        view.addSpan(test);
        view.addSpan(new ForegroundColorSpan(Color.TRANSPARENT));
    }
}