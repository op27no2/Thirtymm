package op27no2.fitness.Centurion2;

import android.os.Bundle;
import android.view.View;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;


public class MyPreferenceFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        PreferenceManager prefMgr = getPreferenceManager();
        prefMgr.setSharedPreferencesName("PREFS");
        setPreferencesFromResource(R.xml.ic_settings, rootKey);



    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View lv = getView().findViewById(android.R.id.list);
        if (lv != null) lv.setPadding(0, 0, 0, 0);
    }
}

