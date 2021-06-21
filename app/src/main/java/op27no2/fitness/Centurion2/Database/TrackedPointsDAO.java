package op27no2.fitness.Centurion2.Database;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import op27no2.fitness.Centurion2.fragments.run.TrackedPoints;

@Dao
public interface TrackedPointsDAO {

    @Query("SELECT * FROM tracked_points" )
    List<TrackedPoints> getAll();


/*    @Query("SELECT * FROM run_workouts ORDER BY uid DESC LIMIT 6;" )
    List<RunWorkout> getTen();  */

/*
    @Query("DELETE FROM run_workouts where date LIKE  :date")
    void getTen(String date);
*/


    @Query("SELECT * FROM tracked_points where uid LIKE  :id")
    TrackedPoints findById(int id);


    @Insert
    long insert(TrackedPoints lws);

    @Update
    void updateWorkouts(TrackedPoints... lws);

    @Delete
    void delete(TrackedPoints lw);

}
