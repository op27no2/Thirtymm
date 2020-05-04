package op27no2.fitness.Centurion.ui.lifting;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "named_workouts")
public class NamedWorkout {

    public NamedWorkout(String name, ArrayList<String> lifts){
        mName = name;
        mLifts = lifts;
    }

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "name")
    private String mName;

    @ColumnInfo(name = "mlifts")
    private ArrayList<String> mLifts = new ArrayList<String>();


    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setName(String setname){
        mName = setname;
    }

    public String getName(){
        return mName;
    }

/*    public void setLifts(ArrayList<Lift> lifts){
        mLifts = lifts;
    }

    public ArrayList<Lift> getLifts(){
        return mLifts;
    }*/

    public void setLifts(ArrayList<String> lifts){
        mLifts = lifts;
    }

    public ArrayList<String> getLifts(){
        return mLifts;
    }


}
