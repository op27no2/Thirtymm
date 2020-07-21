package op27no2.fitness.Centurion2.fragments.run;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "run_workouts")

public class RunWorkout {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "date")
    private String workoutDate;

    @ColumnInfo(name = "type")
    private String workoutType;

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

    @ColumnInfo(name = "title")
    private String title;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String tit) {
        this.title = tit;
    }

    public String getWorkoutType() {
        return workoutType;
    }

    public void setWorkoutType(String typ) {
        this.workoutType = typ;
    }

    //we just store a uid reference to the coordinates stored in sharedprefs as json object
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