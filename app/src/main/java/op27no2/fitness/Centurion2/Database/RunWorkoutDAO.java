package op27no2.fitness.Centurion2.Database;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import op27no2.fitness.Centurion2.fragments.run.RunWorkout;

@Dao
public interface RunWorkoutDAO {

    @Query("SELECT * FROM run_workouts" )
    List<RunWorkout> getAll();


/*    @Query("SELECT * FROM run_workouts ORDER BY uid DESC LIMIT 6;" )
    List<RunWorkout> getTen();  */

/*
    @Query("DELETE FROM run_workouts where date LIKE  :date")
    void getTen(String date);
*/

    @Query("SELECT * FROM run_workouts where date LIKE  :date")
    List<RunWorkout> findAllByDate(String date);


    @Query("SELECT * FROM run_workouts where date LIKE  :date")
    RunWorkout findByDate(String date);


    @Query("SELECT * FROM run_workouts where uid LIKE  :id")
    RunWorkout findById(int id);

    @Insert
    void insertAll(RunWorkout... lws);

    @Update
    void updateWorkouts(RunWorkout... lws);

    @Delete
    void delete(RunWorkout lw);

}
