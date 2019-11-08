package op27no2.fitness.thirtymm.ui.run;

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

public class RunFragment extends Fragment {



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_run, container, false);






        return view;
    }


}

