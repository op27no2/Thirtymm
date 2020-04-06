package op27no2.fitness.thirtymm.ui.lifting;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

import op27no2.fitness.thirtymm.Database.AppDatabase;
import op27no2.fitness.thirtymm.Database.Repository;
import op27no2.fitness.thirtymm.R;
import op27no2.fitness.thirtymm.RecyclerItemClickListener;

public class DialogLifts extends Dialog  {

        public Context c;
        public Dialog d;
        public Button yes, no;
        private RecyclerView mRecyclerView;
        private LinearLayoutManager mLayoutManager;
        public MyDialogAdapter mAdapter;
        private RecyclerView mRecyclerView2;
        public LinearLayoutManager mLayoutManager2;
        private SortAdapter mAdapter2;
        private Repository mRepository;
        private LiftingWorkout mLiftingWorkout;
        private int parentposition;
        private MyDialogInterface mInterface;
        private Context mContext;
        private ArrayList<LiftMap> mList = new ArrayList<LiftMap>();
        private ArrayList<LiftMap> mBaseList = new ArrayList<LiftMap>();
        private ArrayList<String> allMuscles = new ArrayList<String>();

        private ArrayList<ArrayList<String>> sortArray = new ArrayList<ArrayList<String>>();
        private ArrayList<String> chestMuscles = new ArrayList<String>();
        private ArrayList<String> coreMuscles = new ArrayList<String>();
        private ArrayList<String> armMuscles = new ArrayList<String>();
        private ArrayList<String> backMuscles = new ArrayList<String>();
        private ArrayList<String> legMuscles = new ArrayList<String>();

        private ArrayList<Integer> allRatios = new ArrayList<Integer>();

        private ArrayList<String> cats = new ArrayList<String>();
        private ArrayList<Integer> keepThese = new ArrayList<Integer>();
        private int selected;


    public DialogLifts(@NonNull Context context, Repository repositoy, LiftingWorkout lw, int p, MyDialogInterface dialogInterface) {
        super(context);
        c = context;
        mRepository = repositoy;
        mLiftingWorkout = lw;
        parentposition = p;
        mInterface = dialogInterface;
        mContext = context;
    }


    @SuppressLint("StaticFieldLeak")
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_lift);
        System.out.println("on create");
        allMuscles = new ArrayList(Arrays.asList((mContext.getResources().getStringArray(R.array.full_muscle_list))));
        for(int i=0; i<allMuscles.size(); i++){
            allRatios.add(0);
        }

        chestMuscles = new ArrayList(Arrays.asList((mContext.getResources().getStringArray(R.array.Chest))));
        armMuscles = new ArrayList(Arrays.asList((mContext.getResources().getStringArray(R.array.Arms))));
        backMuscles = new ArrayList(Arrays.asList((mContext.getResources().getStringArray(R.array.Back))));
        coreMuscles = new ArrayList(Arrays.asList((mContext.getResources().getStringArray(R.array.Core))));
        legMuscles = new ArrayList(Arrays.asList((mContext.getResources().getStringArray(R.array.Legs))));
        sortArray.add(chestMuscles);
        sortArray.add(backMuscles);
        sortArray.add(coreMuscles);
        sortArray.add(armMuscles);
        sortArray.add(legMuscles);


            cats.add("Chest");
            cats.add("Back");
            cats.add("Core");
            cats.add("Arms");
            cats.add("Legs");
            mRecyclerView2 = findViewById(R.id.my_recycler_view1);
            mRecyclerView2.setHasFixedSize(true);
            mLayoutManager2 = new LinearLayoutManager(c);
            mLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
            mRecyclerView2.setLayoutManager(mLayoutManager2);
            mRecyclerView2.setNestedScrollingEnabled(false);
            mRecyclerView2.setItemAnimator(new DefaultItemAnimator());
            mAdapter2 = new SortAdapter(cats);
            mRecyclerView2.setAdapter(mAdapter2);
            mRecyclerView2.addOnItemTouchListener(
                    new RecyclerItemClickListener(c, mRecyclerView2, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override public void onItemClick(View view, int position) {
                            System.out.println("segment item clicked: "+position);
                            if(mAdapter2.getSelected() == position){
                                mAdapter2.setSelected(2000);
                                selected = 2000;
                            }else{
                                mAdapter2.setSelected(position);
                                selected= position;
                            }
                            mAdapter2.notifyDataSetChanged();


                            mList.clear();
                            keepThese.clear();
                            //list is sorted list, base list is all the liftmaps for the lifts in the list, we copy over then remove items
                            mList.addAll(mBaseList);

                            //2000 tracks double tap, i.e. will not do any filtering, clears selection, sets selected to 2000
                            if(selected != 2000) {
                                System.out.println("mlist: " + mList);
                                System.out.println("mbaselist: " + mBaseList);

                                //for every lift on the list
                                for (int i = 0; i < mBaseList.size(); i++) {
                                    ArrayList<Integer> rats = mBaseList.get(i).getRatios();
                                    //check whether each ratio is greater than 0
                                    for (int j = 0; j < rats.size(); j++) {
                                        //TODO adding allMuscles size cause residual list is longer and throws error, reloading app should fix
                                        if (rats.get(j) > 0 && j < allMuscles.size()) {
                                            //if greater than zero, check if that muscle is included in the selected position
                                            //if its not add to the remove list
                                            System.out.println("lift num: " + i + " _name: " + mBaseList.get(i).getName() + " _muscle > 0 is: " + allMuscles.get(j));
                                            if (sortArray.get(position).contains(allMuscles.get(j))) {
                                                System.out.println("keep " + i);
                                                keepThese.add(i);
                                                break;
                                            }

                                        }

                                    }
                                }
                                for (int k = mList.size() - 1; k >= 0; k--) {
                                    if (!keepThese.contains(k)) {
                                        mList.remove(k);
                                    }
                                }
                            }


                            mAdapter = new MyDialogAdapter(mContext, mList, mRepository, mLiftingWorkout, parentposition,mInterface);
                            mRecyclerView.setAdapter(mAdapter);


                        }

                        @Override
                        public void onItemLongClick(View view, int position) {
                            System.out.println("segment item long clicked: "+position);
                        }
                    })
            );




            new AsyncTask<Void, Void, Void>() {
                //Get today's workout => finishUI
                //get today's workout, if it doesn't exist create it

                protected void onPreExecute() {
                    // Pre Code
                }
                protected Void doInBackground(Void... unused) {
                    System.out.println("setting base list");

                    mList = new ArrayList<LiftMap>(AppDatabase.getAppDatabase(mContext).lmDAO().getAll());
                    mBaseList = new ArrayList<LiftMap>(AppDatabase.getAppDatabase(mContext).lmDAO().getAll());

                    if(mList == null || mList.size()==0){
                        LiftMap mLiftMap = new LiftMap("Bench Press");
                        mLiftMap.setMuscles(allMuscles);
                        mLiftMap.setRatios(allRatios);
                        LiftMap mLiftMap2 = new LiftMap("Squat");
                        mLiftMap2.setMuscles(allMuscles);
                        mLiftMap2.setRatios(allRatios);
                        LiftMap mLiftMap3 = new LiftMap("Pull Ups");
                        mLiftMap3.setMuscles(allMuscles);
                        mLiftMap3.setRatios(allRatios);
                        mList.add(mLiftMap);
                        mList.add(mLiftMap2);
                        mList.add(mLiftMap3);
                        mRepository.insertLiftMap(mLiftMap);
                        mRepository.insertLiftMap(mLiftMap2);
                        mRepository.insertLiftMap(mLiftMap3);
                        mAdapter = new MyDialogAdapter(mContext, mList, mRepository, mLiftingWorkout, parentposition,mInterface);
                        mAdapter.notifyDataSetChanged();
                    }                return null;
                }
                protected void onPostExecute(Void unused) {
                    // Post Code
                    finishUI();
                }
            }.execute();


            EditText mEdit = findViewById(R.id.edit_text);
            ImageView addLift = findViewById(R.id.circle_add);
            addLift.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LiftMap mMap = new LiftMap(mEdit.getText().toString());
                    mMap.setMuscles(allMuscles);
                    mMap.setRatios(allRatios);
                    System.out.println("ratio size on save "+allRatios.size());
                    mList.add(mMap);
                    mRepository.insertLiftMap(mMap);
                    mEdit.setText("");
                    mAdapter.notifyDataSetChanged();
                }
            });

            //recyclerview and layoutmanager
            mRecyclerView = findViewById(R.id.my_recycler_view);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(c);
            mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setNestedScrollingEnabled(false);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());



        }


        public void finishUI(){

            System.out.println("finish ui");

            //set lift data to recyclerview
            mAdapter = new MyDialogAdapter(mContext, mList, mRepository, mLiftingWorkout, parentposition,mInterface);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter2.setSelected(1000);





        }

}