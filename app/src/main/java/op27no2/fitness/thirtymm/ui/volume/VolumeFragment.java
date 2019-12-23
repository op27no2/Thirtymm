package op27no2.fitness.thirtymm.ui.volume;

import android.annotation.SuppressLint;
import android.content.res.TypedArray;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import op27no2.fitness.thirtymm.Database.AppDatabase;
import op27no2.fitness.thirtymm.Database.Repository;
import op27no2.fitness.thirtymm.R;
import op27no2.fitness.thirtymm.ui.lifting.Lift;
import op27no2.fitness.thirtymm.ui.lifting.LiftMap;
import op27no2.fitness.thirtymm.ui.lifting.LiftingWorkout;

import static android.graphics.Color.argb;

public class VolumeFragment extends Fragment implements DialogVolumeMapnterface {
    private ArrayList<LiftingWorkout> weeksWorkouts = new ArrayList<LiftingWorkout>();
    private ArrayList<LiftMap> mLiftMaps = new ArrayList<LiftMap>();
    private SimpleDateFormat df;
    private HashMap<String, Integer> mVolume = new HashMap<String, Integer>();
    private ArrayList<Map.Entry<String, Integer>> listOfEntry;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private VolumeAdapter mAdapter;

    private ArrayList<TypedArray> topImageList = new ArrayList<>();
    private ArrayList<ArrayList<String>> topMuscleList = new ArrayList<ArrayList<String>>();
    private ArrayList<String> allMuscles = new ArrayList<String>();
    private HashMap<String, Double> muscleVolumes = new HashMap<String, Double>();

    private Drawable[] darray;
    private Drawable[] darray2;
    private ImageView overLay1;
    private ImageView overLay2;
    private View view;
    private DialogVolumeMap dialog;
    private Repository mRepository;
    DialogVolumeMapnterface mInterface;


    @SuppressLint("StaticFieldLeak")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_volume, container, false);
        df= new SimpleDateFormat("EEE, MMM d, ''yy");
        mInterface = this;
        mRecyclerView = view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        overLay1 = view.findViewById(R.id.my_overlay1);
        overLay2 = view.findViewById(R.id.my_overlay2);

        // Creates ArrayList of ArrayLists for Images and Names
        topImageList.add(getResources().obtainTypedArray(R.array.images_male_bb_front));
        topImageList.add(getResources().obtainTypedArray(R.array.images_male_bb_back));
        topImageList.add(getResources().obtainTypedArray(R.array.images_female_front));
        topImageList.add(getResources().obtainTypedArray(R.array.images_female_back));

        //creates List of string names for each muscles in each image
        //Dialog uses muscle list to store muscles, drawing code searches muscle list for name, and matches position to the image list.
        //positions in image list must match muscle lists.
        //names stored must match muscle lists for each image drawn.
        topMuscleList.add(new ArrayList<String>(Arrays.asList((getResources().getStringArray(R.array.male_bb_front)))));
        topMuscleList.add(new ArrayList<String>(Arrays.asList((getResources().getStringArray(R.array.male_bb_back)))));
        topMuscleList.add(new ArrayList<String>(Arrays.asList((getResources().getStringArray(R.array.female_front)))));
        topMuscleList.add(new ArrayList<String>(Arrays.asList((getResources().getStringArray(R.array.female_back)))));

        //not currently used?
        allMuscles = new ArrayList(Arrays.asList((getResources().getStringArray(R.array.full_muscle_list))));

        diagramSetup(0);

        mRepository = new Repository(getActivity());




        new AsyncTask<Void, Void, Void>() {
            protected void onPreExecute() {
                // Pre Code
            }
            protected Void doInBackground(Void... unused) {

                Calendar cal = Calendar.getInstance();
                Date date = cal.getTime();
                String formattedDate = df.format(date);
                LiftingWorkout mLiftingWorkout = AppDatabase.getAppDatabase(getActivity()).lwDAO().findByDate(formattedDate);
                mLiftMaps = (ArrayList<LiftMap>) AppDatabase.getAppDatabase(getActivity()).lmDAO().getAll();

                weeksWorkouts.add(mLiftingWorkout);

                while(cal.get(Calendar.DAY_OF_WEEK) != 2){
                    cal.add(Calendar.DATE, -1);
                    Date mDate = cal.getTime();
                    String mformattedDate = df.format(mDate);
                    System.out.println("task dates " + mformattedDate);
                    LiftingWorkout mWorkout = AppDatabase.getAppDatabase(getActivity()).lwDAO().findByDate(mformattedDate);
                    if(mWorkout != null) {
                        weeksWorkouts.add(mWorkout);
                    }

                }

                return null;
            }
            protected void onPostExecute(Void unused) {
                // Post Code
                otherStuff();
            }
        }.execute();





        return view;
    }

    public void otherStuff(){
        for(int i=0; i<weeksWorkouts.size(); i++){
            ArrayList<Lift> mLifts = weeksWorkouts.get(i).getMyLifts();
            for(int j=0; j<mLifts.size();j++){
                String name = mLifts.get(j).getName();
                int sets = mLifts.get(j).getReps().size();
                if(mVolume.containsKey(name)){
                    int hold = mVolume.get(name);
                    hold = hold + sets;
                    mVolume.put(name, hold);
                }else{
                    mVolume.put(name, sets);
                }
            }
        }

        for (Map.Entry<String, Integer> entry : mVolume.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }

        Set<Map.Entry<String, Integer>> entrySet = mVolume.entrySet();
        listOfEntry = new ArrayList<Map.Entry<String, Integer>>(entrySet);

        //set lift data to recyclerview
        mAdapter = new VolumeAdapter(listOfEntry, mRepository, mInterface);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //get all info and set colors
        for(int j=0; j<listOfEntry.size(); j++){
            if(j==listOfEntry.size()-1){
                getLiftMap(true, listOfEntry.get(j).getKey(),listOfEntry.get(j).getValue());
            }else{
                getLiftMap(false, listOfEntry.get(j).getKey(), listOfEntry.get(j).getValue());
            }
        }

    }



    public void setColors() {
        System.out.println("set colors");
        for(int i=0; i<darray.length; i++) {
            if(muscleVolumes.get(topMuscleList.get(0).get(i))!= null) {
                double vol = muscleVolumes.get(topMuscleList.get(0).get(i)) * 5;
                if (vol > 100) {
                    vol = 100;
                }

                if (vol >= 75) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        darray[i].setColorFilter(new BlendModeColorFilter(argb(125 + (int) Math.floor((vol - 75) * 4), 255, (255 - (int) Math.floor(2.5 * (vol - 50) * 2)), 0), BlendMode.SRC_ATOP));
                    } else {
                        darray[i].setColorFilter(argb(125 + (int) Math.floor((vol - 75) * 4), 255, (255 - (int) Math.floor(2.5 * (vol - 50) * 2)), 0), PorterDuff.Mode.SRC_ATOP);
                    }
                    //  imgg.setColorFilter(argb(125 + (int) Math.floor((progress - 75) * 4), 255, (255 - (int) Math.floor(2.5 * (progress - 50) * 2)), 0), PorterDuff.Mode.SRC_ATOP);

                }
                if (vol >= 50 && vol < 75) {
                    darray[i].setColorFilter(argb(125, 255, (255 - (int) Math.floor(2.5 * (vol - 50) * 2)), 0), PorterDuff.Mode.SRC_ATOP);

                }
                if (vol >= 25 && vol < 50) {
                    darray[i].setColorFilter(argb(125, (int) Math.floor((vol - 25) * 4 * 2.55), 255, 0), PorterDuff.Mode.SRC_ATOP);

                }
                if (vol < 25) {
                    darray[i].setColorFilter(argb(125, 0, 255, (255 - (int) Math.floor(2.55 * (vol) * 4))), PorterDuff.Mode.SRC_ATOP);

                }
            }
        }
        for(int i=0; i<darray2.length; i++) {
            if(muscleVolumes.get(topMuscleList.get(1).get(i))!= null) {
                double vol = muscleVolumes.get(topMuscleList.get(1).get(i)) * 5;
                if (vol > 100) {
                    vol = 100;
                }
                if (vol >= 75) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        darray2[i].setColorFilter(new BlendModeColorFilter(argb(125 + (int) Math.floor((vol - 75) * 4), 255, (255 - (int) Math.floor(2.5 * (vol - 50) * 2)), 0), BlendMode.SRC_ATOP));
                    } else {
                        darray2[i].setColorFilter(argb(125 + (int) Math.floor((vol - 75) * 4), 255, (255 - (int) Math.floor(2.5 * (vol - 50) * 2)), 0), PorterDuff.Mode.SRC_ATOP);
                    }
                    //  imgg.setColorFilter(argb(125 + (int) Math.floor((progress - 75) * 4), 255, (255 - (int) Math.floor(2.5 * (progress - 50) * 2)), 0), PorterDuff.Mode.SRC_ATOP);

                }
                if (vol >= 50 && vol < 75) {
                    darray2[i].setColorFilter(argb(125, 255, (255 - (int) Math.floor(2.5 * (vol - 50) * 2)), 0), PorterDuff.Mode.SRC_ATOP);

                }
                if (vol >= 25 && vol < 50) {
                    darray2[i].setColorFilter(argb(125, (int) Math.floor((vol - 25) * 4 * 2.55), 255, 0), PorterDuff.Mode.SRC_ATOP);

                }
                if (vol < 25) {
                    darray2[i].setColorFilter(argb(125, 0, 255, (255 - (int) Math.floor(2.55 * (vol) * 4))), PorterDuff.Mode.SRC_ATOP);

                }
            }
        }
    }

    public void diagramSetup(int type) {
        System.out.println("TYPE:" + type);

        //set my frame with correct backgroundimage
        ImageView myFrame = (ImageView) view.findViewById(R.id.my_frame1);
        ImageView myFrame2 = (ImageView) view.findViewById(R.id.my_frame2);

        if (type == 0) {
            myFrame.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.male_bb_front));
            myFrame2.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.male_bb_back));
        } else if (type == 1) {
            myFrame.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.female_front));
            myFrame2.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.female_back));
        }

        //get all images for that type
        TypedArray imgs = topImageList.get(type);
        TypedArray imgs2 = topImageList.get(type+1);
        //add each image to a drawable array
        darray = new Drawable[imgs.length()];
        for(int i=0; i<imgs.length();i++) {
            darray[i] = imgs.getDrawable(i);
        }

        darray2 = new Drawable[imgs2.length()];
        for(int i=0; i<imgs2.length();i++) {
            darray2[i] = imgs2.getDrawable(i);
        }

        // create layer drawable from drawable array
        LayerDrawable layer = new LayerDrawable(darray);
        LayerDrawable layer2 = new LayerDrawable(darray2);

        //add layers to overlay
        overLay1.setImageDrawable(layer);
        overLay2.setImageDrawable(layer2);
    }

    @SuppressLint("StaticFieldLeak")
    private void getLiftMap(Boolean last, String liftName, Integer numSets){

        new AsyncTask<Void, Void, Void>() {
            //Get today's workout => finishUI
            //get today's workout, if it doesn't exist create it

            protected void onPreExecute() {
                // Pre Code
            }
            protected Void doInBackground(Void... unused) {
                LiftMap mMap = AppDatabase.getAppDatabase(getActivity()).lmDAO().findByName(liftName);
                ArrayList<String> names = mMap.getMuscles();
                ArrayList<Double> ratios = mMap.getRatios();
                if(mMap.getRatios() !=null) {
                    for(int i=0; i<mMap.getRatios().size(); i++) {
                        if (muscleVolumes.containsKey(names.get(i))) {
                            Double hold = muscleVolumes.get(names.get(i));
                            hold = hold + ratios.get(i) * numSets;
                            muscleVolumes.put(names.get(i), hold);
                        } else {
                            muscleVolumes.put(names.get(i), ratios.get(i) * numSets);
                        }
                    }
                }


                return null;
            }
            protected void onPostExecute(Void unused) {
                // Post Code
                if(last) {
                    setColors();
                }
            }
        }.execute();





    }


    @Override
    public void onDialogDismiss() {
        System.out.println("Volume Dialog ondismiss called");
        muscleVolumes.clear();
        for(int j=0; j<listOfEntry.size(); j++){
            if(j==listOfEntry.size()-1){
                getLiftMap(true, listOfEntry.get(j).getKey(),listOfEntry.get(j).getValue());
            }else{
                getLiftMap(false, listOfEntry.get(j).getKey(), listOfEntry.get(j).getValue());
            }
        }

    }



}

