package op27no2.fitness.Centurion2.fragments.run;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "runtypes")
public class RunType {

    public RunType(String name, Boolean active, Double calBurnValue, Integer calBurnUnit, Integer paceUnits, Integer distanceUnits) {
        mName = name;
        mActive = active;
        mCalBurnValue = calBurnValue;
        mCalBurnUnit = calBurnUnit;
        mPaceUnits = paceUnits;
        mDistanceUnits = distanceUnits;
    }

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "name")
    private String mName;

    @ColumnInfo(name = "active")
    private Boolean mActive = true;

    @ColumnInfo(name = "burnvalue")
    private Double mCalBurnValue;

    @ColumnInfo(name = "burnunit")
    private Integer mCalBurnUnit;

    @ColumnInfo(name = "paceunits")
    private Integer mPaceUnits;
    //pace value is calculated

    @ColumnInfo(name = "distanceunits")
    private Integer mDistanceUnits;

    public void setUid(int id) {
        uid = id;
    }
    public int getUid() {
        return uid;
    }

    public void setName(String setname){
        mName = setname;
    }
    public String getName(){
        return mName;
    }

    public void setActive(Boolean active){
        mActive = active;
    }
    public Boolean getActive(){
        return mActive;
    }

    public void setCalBurnValue(Double bv){
        mCalBurnValue = bv;
    }
    public Double getCalBurnValue(){
        return mCalBurnValue;
    }

    public void setCalBurnUnit(Integer bu){
        mCalBurnUnit = bu;
    }
    public Integer getCalBurnUnit(){
        return mCalBurnUnit;
    }

    public void setPaceUnits(Integer pu){
        mPaceUnits = pu;
    }
    public Integer getPaceUnits(){
        return mPaceUnits;
    }

    public void setDistanceUnits(Integer du){
        mDistanceUnits = du;
    }
    public Integer getDistanceUnits(){
        return mDistanceUnits;
    }

    @Override
    public String toString() {
        return mName;
    }

}
