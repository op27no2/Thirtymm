package op27no2.fitness.Centurion2.fragments.nutrition;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

import op27no2.fitness.Centurion2.fragments.activities.GoalsDetail;

@Entity(tableName = "nutrition_days")
public class NutritionDay {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "datemillis")
    private Long dateMillis;

    //don't use this, switched to array
    @ColumnInfo(name = "cals")
    private Integer cals;

    @ColumnInfo(name = "protein")
    private Integer protein = 0;

    @ColumnInfo(name = "carbs")
    private Integer carbs;

    @ColumnInfo(name = "fat")
    private Integer fat;

    @ColumnInfo(name = "categories")
    private ArrayList<String> mNames = new ArrayList<String>();

    @ColumnInfo(name = "values")
    private ArrayList<Integer> mValues = new ArrayList<Integer>(3);

    @ColumnInfo(name = "flags")
    private ArrayList<Integer> mFlags = new ArrayList<Integer>();

    @ColumnInfo(name = "goals")
    private ArrayList<GoalsDetail> mGoalList = new ArrayList<GoalsDetail>();


    //goal Tpye 0 = deficit, 1 = recomp, 2 = bulk - move away from this, now include in goalsdetail per day.
    @ColumnInfo(name = "goalType")
    private Integer mGoalType;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getDateMillis() {
        return dateMillis;
    }

    public void setDateMillis(Long datemillis) {
        this.dateMillis = datemillis;
    }

    public void setNames(ArrayList<String> categories){
        mNames = categories;
    }
    public ArrayList<String> getNames(){
        return mNames;
    }

    public void setValues(ArrayList<Integer> values){
        mValues = values;
    }
    public ArrayList<Integer> getValues(){
        return mValues;
    }

    public void setFlags(ArrayList<Integer> flags){
        mFlags = flags;
    }
    public ArrayList<Integer> getFlags(){
        return mFlags;
    }

    public void setGoalList(ArrayList<GoalsDetail> goals){
        mGoalList = goals;
    }
    public ArrayList<GoalsDetail> getGoalList(){
        return mGoalList;
    }



    public Integer getGoalType(){
        return mGoalType;
    }
    public void setGoalType(Integer goalType){
        this.mGoalType = goalType;
    }

    public Integer getCals(){
        return cals;
    }

    public void setCals(Integer cals){

        this.cals = cals;
    }

    public Integer getProtein(){
        return protein;
    }

    public void setProtein(Integer prot){
        this.protein = prot;
    }

    public Integer getCarbs(){
        return carbs;
    }

    public void setCarbs(Integer carbs){
        this.carbs = carbs;
    }

    public Integer getFat(){
        return fat;
    }

    public void setFat(Integer fats){
        this.fat = fats;
    }



}
