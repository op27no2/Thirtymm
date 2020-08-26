package op27no2.fitness.Centurion2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.style.LineBackgroundSpan;

import androidx.core.content.ContextCompat;

public class CustomCalendarSpan implements LineBackgroundSpan {

    String textDistance;
    String textSets;
    String date;
    Context mContext;
    public CustomCalendarSpan(String textsets , String textmiles, int dayNum, Context context){
        this.textDistance = textmiles;
        this.textSets = textsets;
        this.date = Integer.toString(dayNum);
        this.mContext = context;
    }

    @Override
    public void drawBackground(Canvas canvas, Paint mPaint, int left, int right, int top, int baseline, int bottom,
                               CharSequence mText, int start, int end, int lnum) {
        if(textDistance != null && textSets != null){
            mPaint.setTextAlign(Paint.Align.CENTER);
            mPaint.setTextSize(30);

            int xPos = (canvas.getWidth() / 2);
            //TODO figure out how to center span text vertically this is not correct but works to test
            int yPos = (int) ((canvas.getHeight() / 4) - ((mPaint.descent() + mPaint.ascent()) / 2)) - 10;
            //((textPaint.descent() + textPaint.ascent()) / 2) is the distance from the baseline to the center.

            mPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            mPaint.setColor(ContextCompat.getColor(mContext, R.color.darkgrey));
            canvas.drawText(date, xPos, yPos - 30, mPaint);

            mPaint.setTextSize(25);
            mPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            float offset = mPaint.measureText(" | ")/2;
            float offset2 = mPaint.measureText(textSets)/2;
            float offset1 = mPaint.measureText(textDistance)/2;

            canvas.drawText(" | ", xPos, yPos, mPaint);

            mPaint.setColor(ContextCompat.getColor(mContext, R.color.black));
            canvas.drawText(textSets, xPos-offset-offset2, yPos, mPaint);
            mPaint.setColor(ContextCompat.getColor(mContext, R.color.colorAccent));
            canvas.drawText(textDistance, xPos+offset+offset1, yPos, mPaint);

        }else if(textDistance != null) {
            mPaint.setTextAlign(Paint.Align.CENTER);
            mPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

            int xPos = (canvas.getWidth() / 2);
            //TODO figure out how to center span text vertically this is not correct but works to test
            int yPos = (int) ((canvas.getHeight() / 4) - ((mPaint.descent() + mPaint.ascent()) / 2)) - 10;
            //((textPaint.descent() + textPaint.ascent()) / 2) is the distance from the baseline to the center.

            mPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            mPaint.setColor(ContextCompat.getColor(mContext, R.color.darkgrey));
            canvas.drawText(date, xPos, yPos - 40, mPaint);
            mPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            mPaint.setColor(ContextCompat.getColor(mContext, R.color.colorAccent));
            canvas.drawText(textDistance, xPos, yPos, mPaint);
        }else if(textSets != null) {
            mPaint.setTextAlign(Paint.Align.CENTER);
            int xPos = (canvas.getWidth() / 2);
            //TODO figure out how to center span text vertically this is not correct but works to test
            int yPos = (int) ((canvas.getHeight() / 4) - ((mPaint.descent() + mPaint.ascent()) / 2)) - 10;
            //((textPaint.descent() + textPaint.ascent()) / 2) is the distance from the baseline to the center.

            mPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            mPaint.setColor(ContextCompat.getColor(mContext, R.color.darkgrey));
            canvas.drawText(date, xPos, yPos - 40, mPaint);
            mPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            mPaint.setColor(ContextCompat.getColor(mContext, R.color.black));
            canvas.drawText(textSets, xPos, yPos, mPaint);

        }







     //   c.drawText(String.valueOf(text), left+(right/2)-10, top+(bottom/2)-10, mPaint );
    }
}

