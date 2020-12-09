package op27no2.fitness.Centurion2.fragments.activities;

public class GoalsDetail {
    private String goalName;
    // type 0 = below low limit, type 1 = between low and high, type 2 = above high limit
    private Integer goalType;
    private Integer goalLimitLow;
    private Integer goalLimitHigh;

    //used when adding after summing each week for ProgressAdapter
    private Integer weekTotal;

    public GoalsDetail(String name, Integer type, Integer lowlimit, Integer highlimit) {
        this.goalName = name;
        this.goalType = type;
        this.goalLimitLow = lowlimit;
        this.goalLimitHigh = highlimit;
    }

    public String getName(){
        return goalName;
    }
    public void setName(String name){
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

    public Integer getWeekTotal(){
        return weekTotal;
    }
    public void setWeekTotal(Integer total){
        weekTotal = total;
    }

}
