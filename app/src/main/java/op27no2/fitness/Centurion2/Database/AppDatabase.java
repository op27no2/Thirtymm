package op27no2.fitness.Centurion2.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import op27no2.fitness.Centurion2.fragments.activities.GoalsDetail;
import op27no2.fitness.Centurion2.fragments.lifting.LiftMap;
import op27no2.fitness.Centurion2.fragments.lifting.LiftingWorkout;
import op27no2.fitness.Centurion2.fragments.lifting.NamedWorkout;
import op27no2.fitness.Centurion2.fragments.nutrition.NutritionDay;
import op27no2.fitness.Centurion2.fragments.run.RunType;
import op27no2.fitness.Centurion2.fragments.run.RunWorkout;

@Database(entities = {LiftingWorkout.class, NutritionDay.class, LiftMap.class, RunWorkout.class, NamedWorkout.class, GoalsDetail.class, RunType.class}, version = 7)
@TypeConverters({Converters.class})

public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract LiftWorkoutDAO lwDAO();
    public abstract RunWorkoutDAO rwDAO();
    public abstract NutritionDAO ntDAO();
    public abstract NamedWorkoutDAO nwDAO();
    public abstract LiftMapDAO lmDAO();
    public abstract RunTypeDAO rtDAO();
    public abstract GoalListDAO glDAO();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "user-database")
                   // .build();
                    .addMigrations(MIGRATION_1_2).addMigrations(MIGRATION_2_3).addMigrations(MIGRATION_3_4).addMigrations(MIGRATION_4_5).addMigrations(MIGRATION_5_6).addMigrations(MIGRATION_6_7).build();

                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.

                          //  .allowMainThreadQueries()

        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE nutrition_days ADD COLUMN flags TEXT DEFAULT ''");

            //   database.execSQL("CREATE TABLE `run_workouts` (`uid` INTEGER, " + "`name` TEXT, PRIMARY KEY(`id`))");
        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE nutrition_days ADD COLUMN goals TEXT DEFAULT ''");
            database.execSQL("ALTER TABLE nutrition_days ADD COLUMN goalType TEXT DEFAULT ''");
            database.execSQL("ALTER TABLE nutrition_days ADD COLUMN datemillis TEXT DEFAULT ''");
        }
    };

    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE run_workouts ADD COLUMN savemap BOOLEAN DEFAULT ''");
        }
    };

    static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
              database.execSQL("CREATE TABLE IF NOT EXISTS `goallist` (`uid` INTEGER NOT NULL, goallimithigh INTEGER , weektotal REAL, goallimitlow INTEGER, goalname TEXT, goaltype INTEGER, PRIMARY KEY(`uid`))");
        }
    };
    static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            //BOOLEANS ARE STORED IN ROOMDB as INTEGER !!!!
            database.execSQL("CREATE TABLE IF NOT EXISTS `runtypes` (`uid` INTEGER NOT NULL, name TEXT , active INTEGER, weightfactor INTEGER, burnvalue INTEGER, burnunit INTEGER, paceunits INTEGER, distanceunits INTEGER, PRIMARY KEY(`uid`))");
        }
    };
    static final Migration MIGRATION_6_7 = new Migration(6, 7) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
                    database.execSQL("CREATE TABLE IF NOT EXISTS `runtypes_temporary` (`uid` INTEGER NOT NULL, name TEXT , active INTEGER, weightfactor INTEGER, burnvalue REAL, burnunit INTEGER, paceunits INTEGER, distanceunits INTEGER, PRIMARY KEY(`uid`))");
                    database.execSQL("INSERT INTO runtypes_temporary(uid, name, active, weightfactor, burnvalue, burnunit, paceunits, distanceunits) SELECT uid, name, active, weightfactor, burnvalue, burnunit, paceunits, distanceunits FROM runtypes");
                    database.execSQL("DROP TABLE runtypes");
                    database.execSQL("ALTER TABLE runtypes_temporary RENAME TO runtypes");
        }
    };




 /*   static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            //   database.execSQL("CREATE TABLE `run_workouts` (`uid` INTEGER, " + "`name` TEXT, PRIMARY KEY(`id`))");
        }
    };*/



}
