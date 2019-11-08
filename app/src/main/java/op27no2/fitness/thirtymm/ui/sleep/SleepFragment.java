package op27no2.fitness.thirtymm.ui.sleep;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import op27no2.fitness.thirtymm.Database.AppDatabase;
import op27no2.fitness.thirtymm.R;
import op27no2.fitness.thirtymm.ui.history.NotificationsViewModel;
import op27no2.fitness.thirtymm.ui.lifting.Lift;
import op27no2.fitness.thirtymm.ui.lifting.LiftingWorkout;

public class SleepFragment extends Fragment {



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sleep, container, false);







        return view;
    }


}

