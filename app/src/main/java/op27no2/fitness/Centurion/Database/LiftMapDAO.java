package op27no2.fitness.Centurion.Database;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import op27no2.fitness.Centurion.ui.lifting.LiftMap;

@Dao
public interface LiftMapDAO {

    @Query("SELECT * FROM liftmaps" )
    List<LiftMap> getAll();

    @Query("SELECT * FROM liftmaps where name LIKE  :mName")
    LiftMap findByName(String mName);

    @Insert
    void insertAll(LiftMap... liftmaps);


    @Update
    void updateMaps(LiftMap... liftmaps);

    @Delete
    void delete(LiftMap liftmap);

}
