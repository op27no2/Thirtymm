package op27no2.fitness.thirtymm.ui.run;

import android.graphics.Bitmap;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.mapbox.geojson.Point;

import java.util.ArrayList;

import op27no2.fitness.thirtymm.ui.lifting.Lift;

@Entity(tableName = "run_workouts")

public class RunWorkout {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "date")
    private String workoutDate;

    @ColumnInfo(name = "duration")
    private Integer duration;

    @ColumnInfo(name = "distance")
    private Integer distance;

    @ColumnInfo(name = "calories")
    private Integer calories;

    @ColumnInfo(name = "image")
    private String image;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "runs")
    //private ArrayList<Point> coordinates = new ArrayList<Point>();
    private String coordinates;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getWorkoutDate() {
        return workoutDate;
    }

    public void setWorkoutDate(String date) {
        this.workoutDate = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String descrip) {
        this.description = descrip;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coords) {
        this.coordinates = coords;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer dur) {
        this.duration = dur;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer dist) {
        this.distance = dist;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer cals) {
        this.calories = cals;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String bmap) {
        this.image = bmap;
    }


}