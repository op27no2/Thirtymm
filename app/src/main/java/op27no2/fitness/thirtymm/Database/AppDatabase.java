package op27no2.fitness.thirtymm.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import op27no2.fitness.thirtymm.ui.lifting.LiftMap;
import op27no2.fitness.thirtymm.ui.lifting.LiftingWorkout;
import op27no2.fitness.thirtymm.ui.lifting.NamedWorkout;
import op27no2.fitness.thirtymm.ui.nutrition.NutritionDay;
import op27no2.fitness.thirtymm.ui.run.RunWorkout;

@Database(entities = {LiftingWorkout.class, NutritionDay.class, LiftMap.class, RunWorkout.class, NamedWorkout.class}, version = 1)
@TypeConverters({Converters.class})

public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract LiftWorkoutDAO lwDAO();
    public abstract RunWorkoutDAO rwDAO();
    public abstract NutritionDAO ntDAO();
    public abstract NamedWorkoutDAO nwDAO();
    public abstract LiftMapDAO lmDAO();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "user-database")
                    .build();
                 //   .addMigrations(MIGRATION_1_2).build();

                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.

                          //  .allowMainThreadQueries()

        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

/*    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `named_workouts` (`name` TEXT," + "`uid` INTEGER NOT NULL," + "`mlifts` TEXT, PRIMARY KEY(`uid`))");
         //   database.execSQL("CREATE TABLE `run_workouts` (`uid` INTEGER, " + "`name` TEXT, PRIMARY KEY(`id`))");
        }
    };*/


 /*   static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            //   database.execSQL("CREATE TABLE `run_workouts` (`uid` INTEGER, " + "`name` TEXT, PRIMARY KEY(`id`))");
        }
    };*/



}
