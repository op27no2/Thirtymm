package op27no2.fitness.Centurion;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class FancyScrollView extends ScrollView {
        private boolean enableScrolling = true;

        public FancyScrollView(Context context) {
            super(context);
        }

        public FancyScrollView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public FancyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public FancyScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }
        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {

            if (scrollingEnabled()) {
                return super.onInterceptTouchEvent(ev);
            } else {
                return false;
            }
        }
        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            if (scrollingEnabled()) {
                return super.onTouchEvent(ev);
            } else {
                return false;
            }
        }
        private boolean scrollingEnabled(){
            return enableScrolling;
        }
        public void setScrolling(boolean enableScrolling) {
            this.enableScrolling = enableScrolling;
        }

}
