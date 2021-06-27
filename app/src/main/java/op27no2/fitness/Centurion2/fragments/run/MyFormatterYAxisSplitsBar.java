package op27no2.fitness.Centurion2.fragments.run;

import com.github.mikephil.charting.formatter.ValueFormatter;

public class MyFormatterYAxisSplitsBar extends ValueFormatter {


    public MyFormatterYAxisSplitsBar() {
    }

    @Override
    //value is the y position on the chart
    public String getFormattedValue(float value) {
        int secs = (int) value;
        return String.format("%02d:%02d:%02d", secs / 3600, (secs % 3600) / 60, secs % 60);
    }

}


