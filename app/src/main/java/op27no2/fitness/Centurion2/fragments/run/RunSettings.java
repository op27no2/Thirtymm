package op27no2.fitness.Centurion2.fragments.run;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import op27no2.fitness.Centurion2.R;
import op27no2.fitness.Centurion2.RecyclerItemClickListener;


public class RunSettings extends Fragment {
    public Context mContext;
    private RecyclerView mRecyclerView;
    public LinearLayoutManager mLayoutManager;
    private RunTypeAdapter mAdapter;
    private RecyclerView mRecyclerView2;
    public LinearLayoutManager mLayoutManager2;
    private RunTypeAdapter mAdapter2;
    private ArrayList<String> activeTypes = new ArrayList<String>();
    private ArrayList<String> proposedTypes = new ArrayList<String>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_runsettings, container, false);
        mContext = getActivity();

        activeTypes.add("Run");
        activeTypes.add("Walking");
        activeTypes.add("Rowing");

        proposedTypes.add("Cycling");
        proposedTypes.add("Kayaking");
        proposedTypes.add("Skiing");
        proposedTypes.add("Hiking");
        proposedTypes.add("Tabata");

        mLayoutManager = new LinearLayoutManager(mContext);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mAdapter = new RunTypeAdapter(activeTypes);
        mRecyclerView = view.findViewById(R.id.my_recycler_view_active);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(mContext, mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        System.out.println("segment item clicked: "+position);
                        mAdapter.setSelected(position);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        System.out.println("segment item long clicked: "+position);

                    }
                })
        );

        mLayoutManager2 = new LinearLayoutManager(mContext);
        mLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        mAdapter2 = new RunTypeAdapter(activeTypes);
        mRecyclerView2 = view.findViewById(R.id.my_recycler_view_options);
        mRecyclerView2.setHasFixedSize(true);
        mRecyclerView2.setLayoutManager(mLayoutManager2);
        mRecyclerView2.setNestedScrollingEnabled(false);
        mRecyclerView2.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView2.setAdapter(mAdapter2);
        mRecyclerView2.addOnItemTouchListener(
                new RecyclerItemClickListener(mContext, mRecyclerView2, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        System.out.println("segment item clicked: "+position);
                        mAdapter2.setSelected(position);
                        mAdapter2.notifyDataSetChanged();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        System.out.println("segment item long clicked: "+position);

                    }
                })
        );







        return view;
    }







}

