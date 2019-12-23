
package op27no2.fitness.thirtymm.Database;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import op27no2.fitness.thirtymm.MyApplication;
import op27no2.fitness.thirtymm.ui.lifting.LiftMap;
import op27no2.fitness.thirtymm.ui.lifting.LiftingWorkout;
import op27no2.fitness.thirtymm.ui.nutrition.NutritionDay;

public class Repository {
    private LiftWorkoutDAO mDataDao;
    private NutritionDAO mNutritionDao;
    private LiftMapDAO mLiftMapDao;
    private LiftingWorkout liftWorkout;

    public Repository(Context context) {
        AppDatabase dataRoombase = AppDatabase.getAppDatabase(context);
        this.mDataDao = dataRoombase.lwDAO();
        this.mNutritionDao = dataRoombase.ntDAO();
        this.mLiftMapDao = dataRoombase.lmDAO();
    }

    LiftingWorkout getTodaysWorkout(String date) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            liftWorkout = mDataDao.findByDate(date);
        });

        return liftWorkout;
    }

// You must call this on a non-UI thread or your app will crash

    public void insertWorkout(LiftingWorkout liftWorkout) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            mDataDao.insertAll(liftWorkout);
        });
    }

    public void insertLiftMap(LiftMap liftMap) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            mLiftMapDao.insertAll(liftMap);
        });
    }

    public void deleteLiftMap(LiftMap liftMap) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            mLiftMapDao.delete(liftMap);
        });
    }


    public void insertNutrition(NutritionDay mDay) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            mNutritionDao.insertAll(mDay);
        });
    }

    public void deleteAllNutrition() {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            mNutritionDao.nukeTable();
        });
    }



    public void updateLiftMap(LiftMap liftMap) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            mLiftMapDao.updateMaps(liftMap);
        });
    }
    public void updateWorkout(LiftingWorkout liftWorkout) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            mDataDao.updateWorkouts(liftWorkout);
        });
    }
    public void updateNutrition(NutritionDay mDay) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            mNutritionDao.updateDays(mDay);
        });
    }




/*    public void deleteItemById(Long idItem) {
        new deleteByIdAsyncTask(mDataDao).execute(idItem);
    }

    private static class deleteByIdAsyncTask extends AsyncTask<Long, Void, Void> {
        private DataDAO mAsyncTaskDao;
        deleteByIdAsyncTask(DataDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Long... params) {
            mAsyncTaskDao.deleteByItemId(params[0]);
            return null;
        }
    }*/
}
