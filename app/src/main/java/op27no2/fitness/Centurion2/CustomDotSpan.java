package op27no2.fitness.Centurion2;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.LineBackgroundSpan;

public class CustomDotSpan implements LineBackgroundSpan {


        private final float radius;
        private int[] color = new int[0];
        private int DEFAULT_RADIUS = 5;

        public CustomDotSpan() {
            this.radius = DEFAULT_RADIUS;
            this.color[0] = 0;
        }


        public CustomDotSpan(int color) {
            this.radius = DEFAULT_RADIUS;
            this.color[0] = 0;
        }


        public CustomDotSpan(float radius) {
            this.radius = radius;
            this.color[0] = 0;
        }


        public CustomDotSpan(float radius, int[] color) {
            this.radius = radius;
            this.color = color;
        }

        @Override
        public void drawBackground(
                Canvas canvas, Paint paint,
                int left, int right, int top, int baseline, int bottom,
                CharSequence charSequence,
                int start, int end, int lineNum) {

                int total = color.length > 5 ? 5 : color.length;
                int leftMost = (total - 1) * -10;

                for (int i = 0; i < total; i++) {
                    int oldColor = paint.getColor();
                    if (color[i] != 0) {
                        paint.setColor(color[i]);
                    }
                    canvas.drawCircle((left + right) / 2 - leftMost, bottom + radius, radius, paint);
                    paint.setColor(oldColor);
                    leftMost = leftMost + 20;
                }
        }
}
