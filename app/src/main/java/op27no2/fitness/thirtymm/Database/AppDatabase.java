package op27no2.fitness.thirtymm.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import op27no2.fitness.thirtymm.ui.lifting.LiftMap;
import op27no2.fitness.thirtymm.ui.lifting.LiftingWorkout;
import op27no2.fitness.thirtymm.ui.nutrition.NutritionDay;

@Database(entities = {LiftingWorkout.class, NutritionDay.class, LiftMap.class}, version = 1)
@TypeConverters({Converters.class})

public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract LiftWorkoutDAO lwDAO();
    public abstract NutritionDAO ntDAO();
    public abstract LiftMapDAO lmDAO();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "user-database")
                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                          //  .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
