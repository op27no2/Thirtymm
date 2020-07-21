package op27no2.fitness.Centurion2.fragments.run;

import com.mapbox.geojson.Point;

public class TrackedPoint {
private long timestamp;
private Point point;
private double distance;

    public TrackedPoint(long time, Point pnt, double totaldistance){
        timestamp = time;
        point = pnt;
        distance = totaldistance;
        }

public void setTimestamp(long time){
        timestamp = time;
}
public long getTimestamp(){
        return timestamp;
}
    public void setPoint(Point mPoint){
        point = mPoint;
    }
    public Point getPoint(){
        return point;
    }
public void setDistance(Double dist){
        distance = dist;
    }
public double getDistance(){
        return distance;
    }


}
