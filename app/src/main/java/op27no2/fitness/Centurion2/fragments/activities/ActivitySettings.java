package op27no2.fitness.Centurion2.fragments.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import op27no2.fitness.Centurion2.R;
import op27no2.fitness.Centurion2.fragments.run.RunTypeAdapter;


public class ActivitySettings extends Fragment {
    public Context mContext;
    private RecyclerView mRecyclerView;
    public LinearLayoutManager mLayoutManager;
    private RunTypeAdapter mAdapter;
    private RecyclerView mRecyclerView2;
    public LinearLayoutManager mLayoutManager2;
    private RunTypeAdapter mAdapter2;
    private ArrayList<String> activeTypes = new ArrayList<String>();
    private ArrayList<String> proposedTypes = new ArrayList<String>();
    private SharedPreferences prefs;
    private SharedPreferences.Editor edt;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_activitysettings, container, false);
        mContext = getActivity();
        prefs = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        edt = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();





        return view;
    }






}

