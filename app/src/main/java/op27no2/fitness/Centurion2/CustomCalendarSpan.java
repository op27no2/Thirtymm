package op27no2.fitness.Centurion2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.style.LineBackgroundSpan;

import androidx.core.content.ContextCompat;

public class CustomCalendarSpan implements LineBackgroundSpan {

    String text1;
    String text2;
    Context mContext;
    public CustomCalendarSpan(String textmiles, String textsets, Context context){
        this.text1 = textmiles;
        this.text2 = textsets;
        this.mContext = context;
    }

    @Override
    public void drawBackground(Canvas canvas, Paint mPaint, int left, int right, int top, int baseline, int bottom,
                               CharSequence mText, int start, int end, int lnum) {
        if(text1 != null && text2 != null){
            mPaint.setTextAlign(Paint.Align.CENTER);
            mPaint.setTextSize(30);

            int xPos = (canvas.getWidth() / 2);
            //TODO figure out how to center span text vertically this is not correct but works to test
            int yPos = (int) ((canvas.getHeight() / 4) - ((mPaint.descent() + mPaint.ascent()) / 2)) - 10;
            //((textPaint.descent() + textPaint.ascent()) / 2) is the distance from the baseline to the center.

            mPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            mPaint.setColor(ContextCompat.getColor(mContext, R.color.darkgrey));
            canvas.drawText("1", xPos, yPos - 30, mPaint);

            mPaint.setTextSize(25);
            mPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            float offset = mPaint.measureText(" | ")/2;
            float offset2 = mPaint.measureText(text2)/2;
            float offset1 = mPaint.measureText(text1)/2;

            canvas.drawText(" | ", xPos, yPos, mPaint);

            mPaint.setColor(ContextCompat.getColor(mContext, R.color.black));
            canvas.drawText(text2, xPos-offset-offset2, yPos, mPaint);
            mPaint.setColor(ContextCompat.getColor(mContext, R.color.colorAccent));
            canvas.drawText(text1, xPos+offset+offset1, yPos, mPaint);

        }else if(text1 != null) {
            mPaint.setTextAlign(Paint.Align.CENTER);
            mPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

            int xPos = (canvas.getWidth() / 2);
            //TODO figure out how to center span text vertically this is not correct but works to test
            int yPos = (int) ((canvas.getHeight() / 4) - ((mPaint.descent() + mPaint.ascent()) / 2)) - 10;
            //((textPaint.descent() + textPaint.ascent()) / 2) is the distance from the baseline to the center.

            mPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            mPaint.setColor(ContextCompat.getColor(mContext, R.color.darkgrey));
            canvas.drawText("1", xPos, yPos - 40, mPaint);
            mPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            mPaint.setColor(ContextCompat.getColor(mContext, R.color.colorAccent));
            canvas.drawText(text1, xPos, yPos, mPaint);
        }else if(text2 != null) {
            mPaint.setTextAlign(Paint.Align.CENTER);
            int xPos = (canvas.getWidth() / 2);
            //TODO figure out how to center span text vertically this is not correct but works to test
            int yPos = (int) ((canvas.getHeight() / 4) - ((mPaint.descent() + mPaint.ascent()) / 2)) - 10;
            //((textPaint.descent() + textPaint.ascent()) / 2) is the distance from the baseline to the center.

            mPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            mPaint.setColor(ContextCompat.getColor(mContext, R.color.darkgrey));
            canvas.drawText("1", xPos, yPos - 40, mPaint);
            mPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            mPaint.setColor(ContextCompat.getColor(mContext, R.color.black));
            canvas.drawText(text2, xPos, yPos, mPaint);

        }







     //   c.drawText(String.valueOf(text), left+(right/2)-10, top+(bottom/2)-10, mPaint );
    }
}

