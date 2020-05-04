package op27no2.fitness.Centurion.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import op27no2.fitness.Centurion.Database.AppDatabase;
import op27no2.fitness.Centurion.R;
import op27no2.fitness.Centurion.ui.lifting.Lift;
import op27no2.fitness.Centurion.ui.lifting.LiftingWorkout;

public class HistoryFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history, container, false);

        final TextView textView = view.findViewById(R.id.text_notifications);
        textView.setText("History Fragment");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  populateWithTestData(AppDatabase.getAppDatabase(getActivity()));
                LiftingWorkout mWorkout = AppDatabase.getAppDatabase(getActivity()).lwDAO().findByDate("today motherfucker");
                ArrayList<Lift> mLifts = mWorkout.getMyLifts();
                System.out.println("my workout date: "+ mWorkout.getWorkoutDate());
                System.out.println("my workout lift 1 name: "+ mLifts.size());*/
            }
        });



        return view;
    }

    private static LiftingWorkout addUser(final AppDatabase db, LiftingWorkout workout) {
        db.lwDAO().insertAll(workout);
        return workout;
    }

    private static void populateWithTestData(AppDatabase db) {
        LiftingWorkout work = new LiftingWorkout();
        work.setWorkoutDate("today motherfucker");
        Lift mLift = new Lift("Bench Press", 225);
        work.addLift(mLift);
        addUser(db, work);
    }
}

