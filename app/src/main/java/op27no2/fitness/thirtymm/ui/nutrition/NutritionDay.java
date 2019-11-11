package op27no2.fitness.thirtymm.ui.nutrition;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "nutrition_days")
public class NutritionDay {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "cals")
    private Integer cals;

    @ColumnInfo(name = "protein")
    private Integer protein;

    @ColumnInfo(name = "carbs")
    private Integer carbs;

    @ColumnInfo(name = "fat")
    private Integer fat;


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

    public Integer getCals(){
        return cals;
    }

    public void setCals(Integer cals){
        System.out.println("nutrition set cals");

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
