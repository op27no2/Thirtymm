package op27no2.fitness.Centurion2.fragments.run;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "runtypes")
public class RunType {

    public RunType(String name, Boolean active) {
        mName = name;
        mActive = active;

    }

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "name")
    private String mName;

    @ColumnInfo(name = "active")
    private Boolean mActive = true;

    @ColumnInfo(name = "burnvalue")
    private Double calBurnValue;

    @ColumnInfo(name = "burnunit")
    private Integer calBurnUnit;

    @ColumnInfo(name = "paceunits")
    private Integer paceUnits;
    //pace value is calculated

    @ColumnInfo(name = "distanceunits")
    private Integer distanceUnits;

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
        calBurnValue = bv;
    }
    public Double getCalBurnValue(){
        return calBurnValue;
    }

    public void setCalBurnUnit(Integer bu){
        calBurnUnit = bu;
    }
    public Integer getCalBurnUnit(){
        return calBurnUnit;
    }

    public void setPaceUnits(Integer pu){
        paceUnits = pu;
    }
    public Integer getPaceUnits(){
        return paceUnits;
    }

    public void setDistanceUnits(Integer du){
        distanceUnits = du;
    }
    public Integer getDistanceUnits(){
        return distanceUnits;
    }

    @Override
    public String toString() {
        return mName;
    }

}
