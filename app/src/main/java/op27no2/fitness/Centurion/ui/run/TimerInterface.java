package op27no2.fitness.Centurion.ui.run;

import com.mapbox.geojson.Point;

import java.util.ArrayList;

public interface TimerInterface {

    public void getData(long timestamp, long elapsedTime, ArrayList<Point> routeCoordinates, double minalt, double maxalt, ArrayList<TrackedPoint> mPoints);




}
