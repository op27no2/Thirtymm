
package op27no2.fitness.Centurion2.Database;

import android.content.Context;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import op27no2.fitness.Centurion2.fragments.activities.GoalsDetail;
import op27no2.fitness.Centurion2.fragments.lifting.LiftMap;
import op27no2.fitness.Centurion2.fragments.lifting.LiftingWorkout;
import op27no2.fitness.Centurion2.fragments.lifting.NamedWorkout;
import op27no2.fitness.Centurion2.fragments.nutrition.NutritionDay;
import op27no2.fitness.Centurion2.fragments.run.RunType;
import op27no2.fitness.Centurion2.fragments.run.RunWorkout;
import op27no2.fitness.Centurion2.fragments.run.TrackedPoints;

public class Repository {
    private LiftWorkoutDAO mLiftDao;
    private RunWorkoutDAO mRunDao;
    private NutritionDAO mNutritionDao;
    private NamedWorkoutDAO mNamedWorkoutDAO;
    private LiftMapDAO mLiftMapDao;
    private GoalListDAO mGoalListDao;
    private RunTypeDAO mRunTypeDao;
    private TrackedPointsDAO mTrackedPointsDAO;
    private LiftingWorkout liftWorkout;

    public Repository(Context context) {
        AppDatabase dataRoombase = AppDatabase.getAppDatabase(context);
        this.mLiftDao = dataRoombase.lwDAO();
        this.mNutritionDao = dataRoombase.ntDAO();
        this.mNamedWorkoutDAO = dataRoombase.nwDAO();
        this.mLiftMapDao = dataRoombase.lmDAO();
        this.mGoalListDao = dataRoombase.glDAO();
        this.mRunDao = dataRoombase.rwDAO();
        this.mRunTypeDao = dataRoombase.rtDAO();
        this.mTrackedPointsDAO = dataRoombase.trackedPointsDAO();
    }

    LiftingWorkout getTodaysWorkout(String date) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            liftWorkout = mLiftDao.findByDate(date);
        });

        return liftWorkout;
    }

// You must call this on a non-UI thread or your app will crash

    public void insertWorkout(LiftingWorkout liftWorkout) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            mLiftDao.insertAll(liftWorkout);
        });
    }

    public void insertRunWorkout(RunWorkout mWorkout) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            mRunDao.insertAll(mWorkout);
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
        System.out.println("nutrition day inserting uid:"+mDay.getUid());
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
            mLiftDao.updateWorkouts(liftWorkout);
        });
    }

    public void updateRunWorkout(RunWorkout runWorkout) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            mRunDao.updateWorkouts(runWorkout);
        });
    }
    public void deleteRunWorkout(RunWorkout runWorkout) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            mRunDao.delete(runWorkout);
        });
    }


    public void updateNutrition(NutritionDay mDay) {
        System.out.println("nutrition day updating: uid:"+mDay.getUid());
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            mNutritionDao.updateDays(mDay);
        });
    }


    public void insertNamedWorkout(NamedWorkout mWorkout) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            mNamedWorkoutDAO.insertAll(mWorkout);
        });
    }

    public void updateNamedWorkout(NamedWorkout mWorkout) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            mNamedWorkoutDAO.updateWorkouts(mWorkout);
        });
    }

    public void deleteNamedWorkout(NamedWorkout mWorkout) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            mNamedWorkoutDAO.delete(mWorkout);
        });
    }

    public void updateGoalSettingList(ArrayList<GoalsDetail> mGoalList) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            mGoalListDao.updateGoalSettingList(mGoalList);
        });
    }

    public void updateGoalSetting(GoalsDetail mGoalSetting) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            mGoalListDao.updateGoalSetting(mGoalSetting);
        });
    }

    public void updateRunType(RunType mRunType) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            mRunTypeDao.updateRunType(mRunType);
        });
    }

    public void insertRunType(RunType mRunType) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            mRunTypeDao.insertAll(mRunType);
        });
    }
    public void deleteRunType(RunType mRunType) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            mRunTypeDao.delete(mRunType);
        });
    }

    public long insertTrackedPoints(TrackedPoints mPoints) {
     /*   Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            mTrackedPointsDAO.insert(mPoints);
        });*/
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Callable<Long> insertCallable = () -> mTrackedPointsDAO.insert(mPoints);
        long rowId = 0;
        Future<Long> future = executorService.submit(insertCallable);
        try {
            rowId = future.get();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return rowId;
    }

    public TrackedPoints getTrackedPoints(String id) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Callable<TrackedPoints> insertCallable = () -> mTrackedPointsDAO.findById(Integer.parseInt(id));
        TrackedPoints mPoints = null;
        Future<TrackedPoints> future = executorService.submit(insertCallable);
        try {
            mPoints = future.get();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return mPoints;
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
