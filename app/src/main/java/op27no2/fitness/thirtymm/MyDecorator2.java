package op27no2.fitness.thirtymm;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;


public class MyDecorator2 implements DayViewDecorator {

    private final Drawable draw;
    private final HashSet<CalendarDay> dates;

    public MyDecorator2(Context context, int type, Collection<CalendarDay> dates) {
        Drawable drawable1 = null;
        if(type==1) {
            drawable1 = context.getResources().getDrawable(R.drawable.run_circle);
        }else if(type==2){
            drawable1 = context.getResources().getDrawable(R.drawable.lift_circle);
        }
        draw = drawable1;
        this.dates = new HashSet<>(dates);

    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(draw);
    }
}