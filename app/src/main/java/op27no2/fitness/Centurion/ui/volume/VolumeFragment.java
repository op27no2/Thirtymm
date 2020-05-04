package op27no2.fitness.Centurion.ui.volume;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import op27no2.fitness.Centurion.Database.AppDatabase;
import op27no2.fitness.Centurion.Database.Repository;
import op27no2.fitness.Centurion.MainActivity;
import op27no2.fitness.Centurion.R;
import op27no2.fitness.Centurion.ui.DialogCalendar;
import op27no2.fitness.Centurion.ui.lifting.Lift;
import op27no2.fitness.Centurion.ui.lifting.LiftMap;
import op27no2.fitness.Centurion.ui.lifting.LiftingWorkout;
import op27no2.fitness.Centurion.ui.nutrition.CalendarDialogInterface;

import static android.graphics.Color.argb;

public class VolumeFragment extends Fragment implements DialogVolumeMapnterface, CalendarDialogInterface {
    private SharedPreferences prefs;
    private SharedPreferences.Editor edt;
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
    private ArrayList<ImageView> imageViews = new ArrayList<ImageView>();
    private ArrayList<ImageView> imageViews2 = new ArrayList<ImageView>();

    private Drawable[] darray;
    private Drawable[] darray2;
    private FrameLayout frame1;
    private FrameLayout frame2;
    private ImageView overLay1;
    private ImageView overLay2;
    private View view;
    private DialogVolumeMap dialog;
    private Repository mRepository;
    private ImageView settingsButton;
    DialogVolumeMapnterface mInterface;

    private Calendar cal;
    private String formattedDate;
    private TextView dateText2;
    private TextView dateText1;
    private CalendarDialogInterface mCalendarInterface;
    private String gender = "Male";


    @SuppressLint("StaticFieldLeak")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_volume, container, false);
        prefs = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        edt = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
        mCalendarInterface = this;

        df= new SimpleDateFormat("EEE, MMM d, ''yy");
        cal = Calendar.getInstance();
        Date c = cal.getTime();
        Long time = Calendar.getInstance().getTimeInMillis();
        formattedDate = df.format(c);

        Calendar cal2 = Calendar.getInstance();
        DateFormat df2= new SimpleDateFormat("EEE, M/d");
        Date c2 = cal2.getTime();
        String day2 = df2.format(c2);
        while(cal2.get(Calendar.DAY_OF_WEEK) != 2) {
            cal2.add(Calendar.DATE, -1);
        }
        Date c3 = cal2.getTime();
        String day1 = df2.format(c3);

        dateText1 = view.findViewById(R.id.toolbar_date1);
        dateText2 = view.findViewById(R.id.toolbar_date2);
        dateText1.setText(day1 +" - ");
        dateText2.setText(day2);
        dateText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new DialogCalendar(view.getContext(), mCalendarInterface, cal);
                dialog.show();
            }
        });
        dateText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new DialogCalendar(view.getContext(), mCalendarInterface, cal);
                dialog.show();
            }
        });



        mInterface = this;
        mRecyclerView = view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        overLay1 = view.findViewById(R.id.my_overlay1);
        frame1 = view.findViewById(R.id.frame_layout1);
        frame2 = view.findViewById(R.id.frame_layout2);
        overLay2 = view.findViewById(R.id.my_overlay2);
        settingsButton = (ImageView) view.findViewById(R.id.save);


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

        gender = prefs.getString("gender","Male");
        if(gender.equals("Male")) {
            diagramSetup(0);
        }else{
            diagramSetup(1);
        }

        mRepository = new Repository(getActivity());

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).goToSettings();
            }
        });




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
            //TODO this gave a null object reference once, please check timing
            LiftingWorkout mWorkout = weeksWorkouts.get(i);
            if(mWorkout != null) {
                ArrayList<Lift> mLifts = mWorkout.getMyLifts();

                for (int j = 0; j < mLifts.size(); j++) {
                    String name = mLifts.get(j).getName();
                    int sets = mLifts.get(j).getReps().size();
                    if (mVolume.containsKey(name)) {
                        int hold = mVolume.get(name);
                        hold = hold + sets;
                        mVolume.put(name, hold);
                    } else {
                        mVolume.put(name, sets);
                    }
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

        System.out.println("oncreate GetLiftMaps called");
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
        if(prefs.getString("gender","Male").equals("Male")) {
            for (int i = 0; i < darray.length; i++) {
                if (muscleVolumes.get(topMuscleList.get(0).get(i)) != null) {
                    double vol = muscleVolumes.get(topMuscleList.get(0).get(i)) * 5;
                    if (vol > 100) {
                        vol = 100;
                    }

                    if (vol >= 75) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            imageViews.get(i).setColorFilter(new BlendModeColorFilter(argb(125 + (int) Math.floor((vol - 75) * 4), 255, (255 - (int) Math.floor(2.5 * (vol - 50) * 2)), 0), BlendMode.SRC_ATOP));
                        } else {
                            imageViews.get(i).setColorFilter(argb(125 + (int) Math.floor((vol - 75) * 4), 255, (255 - (int) Math.floor(2.5 * (vol - 50) * 2)), 0), PorterDuff.Mode.SRC_ATOP);
                        }
                        //  imgg.setColorFilter(argb(125 + (int) Math.floor((progress - 75) * 4), 255, (255 - (int) Math.floor(2.5 * (progress - 50) * 2)), 0), PorterDuff.Mode.SRC_ATOP);

                    }
                    if (vol >= 50 && vol < 75) {
                        imageViews.get(i).setColorFilter(argb(125, 255, (255 - (int) Math.floor(2.5 * (vol - 50) * 2)), 0), PorterDuff.Mode.SRC_ATOP);

                    }
                    if (vol >= 25 && vol < 50) {
                        imageViews.get(i).setColorFilter(argb(125, (int) Math.floor((vol - 25) * 4 * 2.55), 255, 0), PorterDuff.Mode.SRC_ATOP);

                    }
                    if (vol < 25) {
                        imageViews.get(i).setColorFilter(argb(125, 0, 255, (255 - (int) Math.floor(2.55 * (vol) * 4))), PorterDuff.Mode.SRC_ATOP);

                    }
                }
            }
            for (int i = 0; i < darray2.length; i++) {
                if (muscleVolumes.get(topMuscleList.get(1).get(i)) != null) {
                    double vol = muscleVolumes.get(topMuscleList.get(1).get(i)) * 5;
                    if (vol > 100) {
                        vol = 100;
                    }
                    if (vol >= 75) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            imageViews2.get(i).setColorFilter(new BlendModeColorFilter(argb(125 + (int) Math.floor((vol - 75) * 4), 255, (255 - (int) Math.floor(2.5 * (vol - 50) * 2)), 0), BlendMode.SRC_ATOP));
                        } else {
                            imageViews2.get(i).setColorFilter(argb(125 + (int) Math.floor((vol - 75) * 4), 255, (255 - (int) Math.floor(2.5 * (vol - 50) * 2)), 0), PorterDuff.Mode.SRC_ATOP);
                        }
                        //  imgg.setColorFilter(argb(125 + (int) Math.floor((progress - 75) * 4), 255, (255 - (int) Math.floor(2.5 * (progress - 50) * 2)), 0), PorterDuff.Mode.SRC_ATOP);

                    }
                    if (vol >= 50 && vol < 75) {
                        imageViews2.get(i).setColorFilter(argb(125, 255, (255 - (int) Math.floor(2.5 * (vol - 50) * 2)), 0), PorterDuff.Mode.SRC_ATOP);

                    }
                    if (vol >= 25 && vol < 50) {
                        imageViews2.get(i).setColorFilter(argb(125, (int) Math.floor((vol - 25) * 4 * 2.55), 255, 0), PorterDuff.Mode.SRC_ATOP);

                    }
                    if (vol < 25) {
                        imageViews2.get(i).setColorFilter(argb(125, 0, 255, (255 - (int) Math.floor(2.55 * (vol) * 4))), PorterDuff.Mode.SRC_ATOP);

                    }
                }
            }
        }else if(prefs.getString("gender","Male").equals("Female")){
            for (int i = 0; i < darray.length; i++) {
                if (muscleVolumes.get(topMuscleList.get(2).get(i)) != null) {
                    double vol = muscleVolumes.get(topMuscleList.get(2).get(i)) * 5;
                    if (vol > 100) {
                        vol = 100;
                    }

                    if (vol >= 75) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            imageViews.get(i).setColorFilter(new BlendModeColorFilter(argb(125 + (int) Math.floor((vol - 75) * 4), 255, (255 - (int) Math.floor(2.5 * (vol - 50) * 2)), 0), BlendMode.SRC_ATOP));
                        } else {
                            imageViews.get(i).setColorFilter(argb(125 + (int) Math.floor((vol - 75) * 4), 255, (255 - (int) Math.floor(2.5 * (vol - 50) * 2)), 0), PorterDuff.Mode.SRC_ATOP);
                        }
                        //  imgg.setColorFilter(argb(125 + (int) Math.floor((progress - 75) * 4), 255, (255 - (int) Math.floor(2.5 * (progress - 50) * 2)), 0), PorterDuff.Mode.SRC_ATOP);

                    }
                    if (vol >= 50 && vol < 75) {
                        imageViews.get(i).setColorFilter(argb(125, 255, (255 - (int) Math.floor(2.5 * (vol - 50) * 2)), 0), PorterDuff.Mode.SRC_ATOP);

                    }
                    if (vol >= 25 && vol < 50) {
                        imageViews.get(i).setColorFilter(argb(125, (int) Math.floor((vol - 25) * 4 * 2.55), 255, 0), PorterDuff.Mode.SRC_ATOP);

                    }
                    if (vol < 25) {
                        imageViews.get(i).setColorFilter(argb(125, 0, 255, (255 - (int) Math.floor(2.55 * (vol) * 4))), PorterDuff.Mode.SRC_ATOP);

                    }
                }
            }
            for (int i = 0; i < darray2.length; i++) {
                if (muscleVolumes.get(topMuscleList.get(3).get(i)) != null) {
                    double vol = muscleVolumes.get(topMuscleList.get(3).get(i)) * 5;
                    if (vol > 100) {
                        vol = 100;
                    }
                    if (vol >= 75) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            imageViews2.get(i).setColorFilter(new BlendModeColorFilter(argb(125 + (int) Math.floor((vol - 75) * 4), 255, (255 - (int) Math.floor(2.5 * (vol - 50) * 2)), 0), BlendMode.SRC_ATOP));
                        } else {
                            imageViews2.get(i).setColorFilter(argb(125 + (int) Math.floor((vol - 75) * 4), 255, (255 - (int) Math.floor(2.5 * (vol - 50) * 2)), 0), PorterDuff.Mode.SRC_ATOP);
                        }
                        //  imgg.setColorFilter(argb(125 + (int) Math.floor((progress - 75) * 4), 255, (255 - (int) Math.floor(2.5 * (progress - 50) * 2)), 0), PorterDuff.Mode.SRC_ATOP);

                    }
                    if (vol >= 50 && vol < 75) {
                        imageViews2.get(i).setColorFilter(argb(125, 255, (255 - (int) Math.floor(2.5 * (vol - 50) * 2)), 0), PorterDuff.Mode.SRC_ATOP);

                    }
                    if (vol >= 25 && vol < 50) {
                        imageViews2.get(i).setColorFilter(argb(125, (int) Math.floor((vol - 25) * 4 * 2.55), 255, 0), PorterDuff.Mode.SRC_ATOP);

                    }
                    if (vol < 25) {
                        imageViews2.get(i).setColorFilter(argb(125, 0, 255, (255 - (int) Math.floor(2.55 * (vol) * 4))), PorterDuff.Mode.SRC_ATOP);

                    }
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
           // myFrame.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.male_bb_front));
            Glide.with(this).load(R.drawable.male_bb_front).into(myFrame);
          //  myFrame2.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.male_bb_back));
            Glide.with(this).load(R.drawable.male_bb_back).into(myFrame2);

        } else if (type == 1) {
            Glide.with(this).load(R.drawable.female_front).into(myFrame);
            //  myFrame2.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.male_bb_back));
            Glide.with(this).load(R.drawable.female_back).into(myFrame2);
        }

        TypedArray imgs;
        TypedArray imgs2;
        //get all images for that type
        if(type == 0) {
            imgs = topImageList.get(0);
            imgs2 = topImageList.get(1);
        }else{
            imgs = topImageList.get(2);
            imgs2 = topImageList.get(3);
        }
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

        for(int i=0; i<darray.length; i++){
            ImageView mImage = new ImageView(getActivity());
            Glide.with(this).load(darray[i]).centerCrop().into(mImage);
            imageViews.add(mImage);
            frame1.addView(mImage);
        }

        for(int i=0; i<darray2.length; i++){
            ImageView mImage = new ImageView(getActivity());
            Glide.with(this).load(darray2[i]).centerCrop().into(mImage);
            imageViews2.add(mImage);
            frame2.addView(mImage);
        }


        //add layers to overlaym
      //  Glide.with(this).load(layer).into(overLay1);
     //   Glide.with(this).load(layer2).into(overLay2);

/*        overLay1.setImageDrawable(layer);
        overLay2.setImageDrawable(layer2);*/
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
                if(mMap != null) {
                    ArrayList<String> names = mMap.getMuscles();
                    ArrayList<Integer> ratios = mMap.getRatios();
                    if (mMap.getRatios() != null) {
                        for (int i = 0; i < mMap.getRatios().size(); i++) {
                            if (muscleVolumes.containsKey(names.get(i))) {
                                Double hold = muscleVolumes.get(names.get(i));
                                //dividing by 10 because stored as integers
                                hold = hold +  ((double) ratios.get(i)/10) * numSets;
                                muscleVolumes.put(names.get(i), hold);
                            } else {
                                //dividing by 10 because stored as integers
                                muscleVolumes.put(names.get(i), ((double) ratios.get(i)/10) * numSets);
                            }
                        }
                    }
                }else{
                    System.out.println("LIFT MAP NULL: "+liftName);
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
        System.out.println("Volume Dialog ondismiss getliftmaps called");
        muscleVolumes.clear();
        for(int j=0; j<listOfEntry.size(); j++){
            if(j==listOfEntry.size()-1){
                //flags the last entry, this list is the lifts and their volumes, have to get ratios (lift map) for each lift/volume key pair
                getLiftMap(true, listOfEntry.get(j).getKey(),listOfEntry.get(j).getValue());
            }else{
                getLiftMap(false, listOfEntry.get(j).getKey(), listOfEntry.get(j).getValue());
            }
        }

    }

    @Override
    public void onPause() {
        System.out.println("volume onPause");
        super.onPause();
    }
    @Override
    public void onResume() {
        System.out.println("volume onReusme");
        super.onResume();
    }


    @Override
    public void onDialogDismiss(CalendarDay m) {

    }
}

