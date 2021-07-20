package op27no2.fitness.Centurion2.Database;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import java.util.List;

import op27no2.fitness.Centurion2.fragments.lifting.LiftingWorkout;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // For our recurring task, we'll just display a message
        System.out.println("alarm message received");
        new AsyncTask<Void, Void, Void>() {
            protected void onPreExecute() {}
            protected Void doInBackground(Void... unused) {
                List<LiftingWorkout> mLiftingWorkout = AppDatabase.getAppDatabase(context).lwDAO().getAll();

                return null;}
            protected void onPostExecute(Void unused) {}
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }
}