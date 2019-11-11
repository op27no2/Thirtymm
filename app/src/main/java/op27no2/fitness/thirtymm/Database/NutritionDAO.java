package op27no2.fitness.thirtymm.Database;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import op27no2.fitness.thirtymm.ui.lifting.LiftingWorkout;
import op27no2.fitness.thirtymm.ui.nutrition.NutritionDay;

@Dao
public interface NutritionDAO {

    @Query("SELECT * FROM nutrition_days" )
    List<NutritionDay> getAll();

    @Query("SELECT * FROM nutrition_days where date LIKE  :date")
    NutritionDay findByDate(String date);

    @Insert
    void insertAll(NutritionDay... lws);

    @Update
    void updateDays(NutritionDay... lws);

    @Delete
    void delete(NutritionDay lw);

}
