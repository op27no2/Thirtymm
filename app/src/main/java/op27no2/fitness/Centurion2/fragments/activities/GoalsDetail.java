package op27no2.fitness.Centurion2.fragments.activities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "goallist")
public class GoalsDetail {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "goalname")
    private String goalName;
    // type 0 = below low limit, type 1 = between low and high, type 2 = above high limit
    @ColumnInfo(name = "goaltype")
    private Integer goalType;

    @ColumnInfo(name = "goallimitlow")
    private Integer goalLimitLow;

    @ColumnInfo(name = "goallimithigh")
    private Integer goalLimitHigh;

    //used when adding after summing each week for ProgressAdapter
    @ColumnInfo(name = "weektotal")
    private Float weekTotal;

    //used when referring to settings
  /*  @ColumnInfo(name = "active")
    private boolean activeGoal;*/

    public GoalsDetail(String goalName, Integer goalType, Integer goalLimitLow, Integer goalLimitHigh) {
        this.goalName = goalName;
        this.goalType = goalType;
        this.goalLimitLow = goalLimitLow;
        this.goalLimitHigh = goalLimitHigh;
    }

    public String getGoalName(){
        return goalName;
    }
    public void setGoalName(String name){
        goalName = name;
    }

    public Integer getGoalType(){
        return goalType;
    }
    public void setGoalType(Integer type){
        goalType = type;
    }

    public Integer getGoalLimitLow(){
        return goalLimitLow;
    }
    public void setGoalLimitLow(Integer low){
        goalLimitLow = low;
    }

    public Integer getGoalLimitHigh(){
        return goalLimitHigh;
    }
    public void setGoalLimitHigh(Integer high){
        goalLimitHigh = high;
    }

    public Float getWeekTotal(){
        return weekTotal;
    }
    public void setWeekTotal(Float weekTotal){
        weekTotal = weekTotal;
    }

/*
    public boolean getActiveGoal(){
        return activeGoal;
    }
    public void setActiveGoal(boolean activeGoal){
        activeGoal = activeGoal;
    }
*/


    public int getUid() {
        return uid;
    }
    public void setUid(int uid) {
        this.uid = uid;
    }
}
