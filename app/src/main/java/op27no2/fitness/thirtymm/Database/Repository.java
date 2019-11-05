/*
package op27no2.fitness.thirtymm.Database;

import android.app.Application;
import android.os.AsyncTask;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import op27no2.fitness.thirtymm.MyApplication;
import op27no2.fitness.thirtymm.ui.lifting.LiftingWorkout;

public class DataRepository {
    private LiftWorkoutDAO mDataDao;
    private LiftingWorkout liftWorkout;

    public DataRepository(Application application) {
        AppDatabase dataRoombase = AppDatabase.getAppDatabase(MyApplication.getAppContext());
        this.mDataDao = dataRoombase.lwDAO();
    }

    LiftingWorkout getTodaysWorkout(String date) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            liftWorkout = mDataDao.findByDate(date);
        });

        return liftWorkout;
    }

// You must call this on a non-UI thread or your app will crash

    public void insertItem(LiftingWorkout liftWorkout) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            mDataDao.insertAll(liftWorkout);
        });
    }

    private static class deleteAsyncTask extends AsyncTask<DataItem, Void, Void> {
        private DataDAO mAsyncTaskDao;
        deleteAsyncTask(DataDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final DataItem... params) {
            mAsyncTaskDao.deleteItem(params[0]);
            return null;
        }
    }

    public void deleteItemById(Long idItem) {
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
    }
}*/
