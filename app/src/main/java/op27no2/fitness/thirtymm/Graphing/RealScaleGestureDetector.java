package op27no2.fitness.thirtymm.Graphing;
 
 
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ScaleGestureDetector;

@SuppressLint("NewApi")
public class RealScaleGestureDetector extends ScaleGestureDetector {
	public RealScaleGestureDetector(Context context, final op27no2.fitness.thirtymm.Graphing.ScaleGestureDetector fakeScaleGestureDetector, final op27no2.fitness.thirtymm.Graphing.ScaleGestureDetector.SimpleOnScaleGestureListener fakeListener) {
		super(context, new SimpleOnScaleGestureListener() {
			@Override
			public boolean onScale(ScaleGestureDetector detector) {
				return fakeListener.onScale(fakeScaleGestureDetector);
			}
		});
	}
}