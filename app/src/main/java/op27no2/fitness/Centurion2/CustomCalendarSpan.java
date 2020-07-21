package op27no2.fitness.Centurion2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.style.LineBackgroundSpan;

import androidx.core.content.ContextCompat;

public class CustomCalendarSpan implements LineBackgroundSpan {

    String text;
    Context mContext;
    public CustomCalendarSpan(String text, Context context){
        this.text = text;
        this.mContext = context;
    }

    @Override
    public void drawBackground(Canvas canvas, Paint mPaint, int left, int right, int top, int baseline, int bottom,
                               CharSequence mText, int start, int end, int lnum) {
        mText = this.text;
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        int xPos = (canvas.getWidth() / 2);
        //TODO figure out how to center span text vertically this is not correct but works to test
        int yPos = (int) ((canvas.getHeight() / 4) - ((mPaint.descent() + mPaint.ascent()) / 2))-10 ;
        //((textPaint.descent() + textPaint.ascent()) / 2) is the distance from the baseline to the center.

        mPaint.setColor(ContextCompat.getColor(mContext, R.color.darkgrey));
        canvas.drawText("1", xPos, yPos-40, mPaint);
        mPaint.setColor(ContextCompat.getColor(mContext, R.color.colorAccent));
        canvas.drawText(text, xPos, yPos, mPaint);

     //   c.drawText(String.valueOf(text), left+(right/2)-10, top+(bottom/2)-10, mPaint );
    }
}

