package op27no2.fitness.Centurion.Database;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import op27no2.fitness.Centurion.ui.lifting.LiftingWorkout;

@Dao
public interface LiftWorkoutDAO {

    @Query("SELECT * FROM lifting_workouts" )
    List<LiftingWorkout> getAll();

    @Query("SELECT * FROM lifting_workouts where date LIKE  :date")
    LiftingWorkout findByDate(String date);

    @Query("SELECT * FROM lifting_workouts where uid LIKE  :id")
    LiftingWorkout findById(int id);

    @Insert
    void insertAll(LiftingWorkout... lws);

    @Insert
    long insert(LiftingWorkout lw);

    @Update
    void updateWorkouts(LiftingWorkout... lws);

    @Delete
    void delete(LiftingWorkout lw);

}
