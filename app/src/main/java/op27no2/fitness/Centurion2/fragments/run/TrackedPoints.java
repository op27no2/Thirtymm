package op27no2.fitness.Centurion2.fragments.run;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "tracked_points")
public class TrackedPoints {

    public TrackedPoints(ArrayList<TrackedPoint> trackedPoints) {
        this.trackedPoints = trackedPoints;

    }

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "points")
    private ArrayList<TrackedPoint> trackedPoints;

    public void setUid(int id) {
        uid = id;
    }
    public int getUid() {
        return uid;
    }

    public void setTrackedPoints(ArrayList<TrackedPoint> mPoints){
        trackedPoints = mPoints;
    }
    public ArrayList<TrackedPoint> getTrackedPoints(){
        return trackedPoints;
    }


}
