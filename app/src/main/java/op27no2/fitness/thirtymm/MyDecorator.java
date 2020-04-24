package op27no2.fitness.thirtymm;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.ArcShape;

import androidx.core.content.ContextCompat;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import static android.graphics.Color.argb;


public class MyDecorator implements DayViewDecorator {

    private final Drawable drawable;

    public MyDecorator(Activity context) {
        drawable = context.getResources().getDrawable(R.drawable.semi_top_left);


    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return true;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(drawable);
    }
}