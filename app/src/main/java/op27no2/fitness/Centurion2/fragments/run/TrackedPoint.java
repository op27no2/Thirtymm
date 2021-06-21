package op27no2.fitness.Centurion2.fragments.run;

import com.mapbox.geojson.Point;

public class TrackedPoint {
private long timestamp;
private Point point;
private double distance;
private int heartrate;
private float pressureAltitude;

    public TrackedPoint(long time, Point pnt, double totaldistance, int heartrate, float pressureAltitude){
        timestamp = time;
        point = pnt;
        distance = totaldistance;
        this.heartrate = heartrate;
        this.pressureAltitude = pressureAltitude;
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
public void setHeartRate(int hr){
    heartrate = hr;
}
public double getHeartRate(){
    return heartrate;
}
public void setPressureAlitude(float press){
    pressureAltitude = press;
}
public float getPressureAltitude(){
        return pressureAltitude;
}



}
