package op27no2.fitness.Centurion2.Database;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

import op27no2.fitness.Centurion2.fragments.activities.GoalsDetail;

@Dao
public interface GoalListDAO {

    @Query("SELECT * FROM goallist" )
    List<GoalsDetail> getAll();

    @Query("SELECT * FROM goallist where goalname LIKE  :mName")
    GoalsDetail findByName(String mName);

    @Insert
    void insertAll(ArrayList<GoalsDetail> goalsDetail);


    @Update
    void updateGoalList(GoalsDetail... goalsDetail);

    @Delete
    void delete(GoalsDetail goalsDetail);

}
