package op27no2.fitness.Centurion2.Database;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import op27no2.fitness.Centurion2.fragments.run.RunType;

@Dao
public interface RunTypeDAO {

    @Query("SELECT * FROM runtypes" )
    List<RunType> getAll();

    @Query("SELECT * FROM runtypes where active")
    List<RunType> getAllActive();


    @Query("SELECT * FROM runtypes where uid LIKE  :id")
    RunType findById(int id);

    @Insert
    void insertAll(RunType... lws);

    @Update
    void updateRunType(RunType... lws);

    @Delete
    void delete(RunType lw);

}
