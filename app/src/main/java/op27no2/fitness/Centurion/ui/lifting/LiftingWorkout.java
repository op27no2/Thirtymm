package op27no2.fitness.Centurion.ui.lifting;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "lifting_workouts")

public class LiftingWorkout {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "date")
    private String workoutDate;

    @ColumnInfo(name = "lifts")
    private ArrayList<Lift> myLifts = new ArrayList<Lift>();

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

    public ArrayList<Lift> getMyLifts() {
        return myLifts;
    }

    public void setMyLifts(ArrayList<Lift> lifts) {
        this.myLifts = lifts;
    }

    public void addLift(Lift lift) {
        System.out.println("adding lift "+lift.getName());
        this.myLifts.add(lift);
    }

    public void removeLift(int index) {
        this.myLifts.remove(index);
    }

}