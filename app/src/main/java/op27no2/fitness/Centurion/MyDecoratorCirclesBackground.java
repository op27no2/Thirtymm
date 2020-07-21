package op27no2.fitness.Centurion;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;


public class MyDecoratorCirclesBackground implements DayViewDecorator {

    private final Drawable draw1;
    private final Drawable draw2;
    private final HashSet<CalendarDay> dates;
    private Context mContext;

    public MyDecoratorCirclesBackground(Context context, boolean lift, boolean run, Collection<CalendarDay> dates) {
        mContext = context;
        Drawable drawable1 = null;
        Drawable drawable2 = null;
        if(lift) {
            drawable1 = context.getResources().getDrawable(R.drawable.run_circle);
            drawable2 = context.getResources().getDrawable(R.drawable.run_selector);
        }
        if(run) {
            drawable1 = context.getResources().getDrawable(R.drawable.lift_circle);
            drawable2 = context.getResources().getDrawable(R.drawable.lift_selector);
        }
        if(lift && run){
            drawable1 = context.getResources().getDrawable(R.drawable.run_lift_circle2);
            drawable2 = context.getResources().getDrawable(R.drawable.run_lift_selector2);
        }
        draw1 = drawable1;
        draw2 = drawable2;
        this.dates = new HashSet<>(dates);

    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
     /*   CustomCalendarSpan test = new CustomCalendarSpan("23", mContext);
        view.addSpan(test);*/
       // view.addSpan(new ForegroundColorSpan(Color.TRANSPARENT));
        view.setBackgroundDrawable(draw1);
        view.setSelectionDrawable(draw2);
    }
}