package op27no2.fitness.thirtymm.ui.lifting;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Entity(tableName = "liftmaps")
public class LiftMap {

    public LiftMap(String name){
        mName = name;
    }

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "name")
    private String mName;

    @ColumnInfo(name = "muscles")
    private ArrayList<String> muscles = new ArrayList<String>();

    @ColumnInfo(name = "weights")
    private ArrayList<Integer> ratios = new ArrayList<Integer>();


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

    public void setMuscles(ArrayList<String> mMuscles){
        muscles = mMuscles;
    }

    public ArrayList<String> getMuscles(){
        return muscles;
    }

    public void setRatios(ArrayList<Integer> mRatios){
        ratios = mRatios;
    }

    public ArrayList<Integer> getRatios(){
        return ratios;
    }


}
