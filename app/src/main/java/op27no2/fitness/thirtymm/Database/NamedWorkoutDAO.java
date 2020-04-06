package op27no2.fitness.thirtymm.Database;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import op27no2.fitness.thirtymm.ui.lifting.NamedWorkout;
import op27no2.fitness.thirtymm.ui.nutrition.NutritionDay;

@Dao
public interface NamedWorkoutDAO {

    @Query("SELECT * FROM named_workouts" )
    List<NamedWorkout> getAll();

    @Insert
    void insertAll(NamedWorkout... lws);

    @Update
    void updateWorkouts(NamedWorkout... lws);

    @Delete
    void delete(NamedWorkout lw);

    @Query("DELETE FROM named_workouts")
    void nukeTable();



}
