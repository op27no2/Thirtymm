package op27no2.fitness.Centurion2.fragments.lifting;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.sweetzpot.stravazpot.activity.api.ActivityAPI;
import com.sweetzpot.stravazpot.activity.model.Activity;
import com.sweetzpot.stravazpot.activity.model.ActivityType;
import com.sweetzpot.stravazpot.authenticaton.api.AuthenticationAPI;
import com.sweetzpot.stravazpot.authenticaton.model.AppCredentials;
import com.sweetzpot.stravazpot.authenticaton.model.LoginResult;
import com.sweetzpot.stravazpot.common.api.AuthenticationConfig;
import com.sweetzpot.stravazpot.common.api.StravaConfig;
import com.sweetzpot.stravazpot.common.model.Time;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import op27no2.fitness.Centurion2.Database.AppDatabase;
import op27no2.fitness.Centurion2.Database.Repository;
import op27no2.fitness.Centurion2.R;
import op27no2.fitness.Centurion2.DialogCalendar;
import op27no2.fitness.Centurion2.fragments.nutrition.CalendarDialogInterface;

public class LiftFragment extends Fragment implements CalendarDialogInterface, NamedWorkoutInterface{
    private SharedPreferences prefs;
    private SharedPreferences.Editor edt;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView2;
    private FlexboxLayoutManager mLayoutManager2;
    private LiftCardviewWorkoutAdapter mLiftAdapter;
    private NamedWorkoutAdapter mNamedWorkoutAdapter;
    private LiftingWorkout mLiftingWorkout;
    private Repository mRepository;
    private CardView addCard;
    private String formattedDate;
    private Vibrator rabbit;
    private TextView dateText;
    private Calendar cal;
    private CalendarDialogInterface mInterface;
    private NamedWorkoutInterface mDialogInterface;
    private ArrayList<NamedWorkout> mNamedWorkouts = new ArrayList<NamedWorkout>();
    private ArrayList<String> allMuscles = new ArrayList<String>();
    private ArrayList<Integer> allRatios = new ArrayList<Integer>();

    private static final int RQ_LOGIN = 1001;
    private static final String REDIRECT_URI = "http://op27no2.fitness/callback/";



    @SuppressLint("StaticFieldLeak")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lift, container, false);
        mRepository = new Repository(getActivity());
        rabbit = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        prefs = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        edt = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
        mInterface = (CalendarDialogInterface) this;
        mDialogInterface = this;
        allMuscles = new ArrayList(Arrays.asList((getActivity().getResources().getStringArray(R.array.full_muscle_list))));
        for(int i=0; i<allMuscles.size(); i++){
            allRatios.add(0);
        }
        //add liftmap for bench on firstrun
        if(prefs.getBoolean("first_open", false) == false) {
            LiftMap mLiftMap = new LiftMap("Bench Press");
            mLiftMap.setMuscles(allMuscles);
            mLiftMap.setRatios(allRatios);
            mRepository.insertLiftMap(mLiftMap);
            edt.putBoolean("first_open",true);
            edt.apply();
        }


        cal = Calendar.getInstance();
        Date c = cal.getTime();
        Long time = Calendar.getInstance().getTimeInMillis();
        System.out.println("Current time => " + time);
        SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d, ''yy");
        formattedDate = df.format(c);
        System.out.println("fomratted time => " + df.format(time));


        dateText = view.findViewById(R.id.toolbar_date);
        ImageView arrowLeft = (ImageView) view.findViewById(R.id.arrow_left);
        ImageView arrowRight = (ImageView) view.findViewById(R.id.arrow_right);
        ImageView saveButton = (ImageView) view.findViewById(R.id.save);
        ImageView uploadButton = (ImageView) view.findViewById(R.id.upload);

        arrowLeft.setAlpha(0.8f);
        arrowRight.setAlpha(0.8f);
        arrowLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rabbit.vibrate(25);
                cal.add(Calendar.DATE, -1);
                formattedDate = df.format(cal.getTime());
                getDayData();
            }
        });
        arrowRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rabbit.vibrate(25);
                cal.add(Calendar.DATE, +1);
                formattedDate = df.format(cal.getTime());
                getDayData();
            }
        });
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rabbit.vibrate(25);
                Dialog dialog = new DialogCalendar(view.getContext(), mInterface, cal);
                dialog.show();
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rabbit.vibrate(25);
                saveWorkoutDialog();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rabbit.vibrate(25);
                uploadToStrava();
            }
        });


            //MOVED BACK TO CARDVIEW
/*        addCard = view.findViewById(R.id.card_view);
        addCard.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                Lift mLift = new Lift("Bench Press", prefs.getInt("last_weightBench Press", 225));
                mLift.addSet();
                mLift.setRepNumber(0, prefs.getInt("default_reps" + "Bench Press", 0));
                System.out.println("lift  click");



                mLiftingWorkout.addLift(mLift);
                mRepository.updateWorkout(mLiftingWorkout);
                mLiftAdapter.notifyItemInserted(mLiftAdapter.getItemCount());
                mRecyclerView.smoothScrollToPosition(mLiftAdapter.getItemCount() - 1);
        }
        });*/


        //recyclerview and layoutmanager
        mRecyclerView = view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        getDayData();


        //recyclerview and layoutmanager

        //moved back to CARDVIEW
   /*     mRecyclerView2 = view.findViewById(R.id.named_workouts);
        mRecyclerView2.setHasFixedSize(true);
        mLayoutManager2 = new FlexboxLayoutManager(view.getContext());
        mLayoutManager2.setFlexDirection(FlexDirection.ROW);
        mLayoutManager2.setJustifyContent(JustifyContent.FLEX_START);
        mLayoutManager2.setAlignItems(AlignItems.FLEX_START);
        mRecyclerView2.setLayoutManager(mLayoutManager2);

*/


/*        new AsyncTask<Void, Void, Void>() {
            //Get today's workout => finishUI
            //get today's workout, if it doesn't exist create it

            protected void onPreExecute() {
                // Pre Code
            }
            protected Void doInBackground(Void... unused) {
                mLiftingWorkout = AppDatabase.getAppDatabase(getActivity()).lwDAO().findByDate(formattedDate);
                if(mLiftingWorkout == null){
                    mLiftingWorkout = new LiftingWorkout();
                    mLiftingWorkout.setWorkoutDate(formattedDate);
                    mRepository.insertWorkout(mLiftingWorkout);
                }                return null;
            }
            protected void onPostExecute(Void unused) {
                // Post Code
                finishUI();
            }
        }.execute();*/


        //recyclerview on touch events

 /*    mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        System.out.println("item clicked: "+position);

                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        System.out.println("item long clicked: "+position);


                    }
                })
        );*/


        return view;
    }


    @SuppressLint("StaticFieldLeak")
    private void getDayData() {
        new AsyncTask<Void, Void, Void>() {
            protected void onPreExecute() {
                // Pre Code
            }
            protected Void doInBackground(Void... unused) {
                mLiftingWorkout = AppDatabase.getAppDatabase(getActivity()).lwDAO().findByDate(formattedDate);
                mNamedWorkouts = (ArrayList<NamedWorkout>) AppDatabase.getAppDatabase(getActivity()).nwDAO().getAll();
                if (mLiftingWorkout == null) {
                    mLiftingWorkout = new LiftingWorkout();
                    mLiftingWorkout.setWorkoutDate(formattedDate);
                    mRepository.insertWorkout(mLiftingWorkout);

                    long id = AppDatabase.getAppDatabase(getActivity()).lwDAO().insert(mLiftingWorkout);
                    mLiftingWorkout = AppDatabase.getAppDatabase(getActivity()).lwDAO().findById((int) id);


                }
                return null;
            }
            protected void onPostExecute(Void unused) {
                // Post Code
                finishUI();
            }
        }.execute();
    }


    private void finishUI() {
        dateText.setText(formattedDate);

        //set lift data to recyclerview
        mLiftAdapter = new LiftCardviewWorkoutAdapter(mLiftingWorkout, mNamedWorkouts, mRepository, LiftFragment.this.getActivity());
        mRecyclerView.setAdapter(mLiftAdapter);

        //set named lift data to recyclerview
        //MOVED BACK TO CARDVIEW
      /*  mNamedWorkoutAdapter = new NamedWorkoutAdapter(mDialogInterface, mLiftingWorkout, mNamedWorkouts, mRepository);
        mRecyclerView2.setAdapter(mNamedWorkoutAdapter);
        mRecyclerView2.setItemAnimator(new DefaultItemAnimator());
*/

        //recyclerview on touch events
   /*     mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        System.out.println("item clicked: "+position);

                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        System.out.println("item long clicked: "+position);


                    }
                })
        );*/

    }


    public void saveWorkoutDialog() {

        final Dialog dialog = new Dialog(getActivity());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_save_workout);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;

        dialog.getWindow().setLayout((8 * width) / 9, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView mText = dialog.findViewById(R.id.confirm_title);
        EditText mEdit = dialog.findViewById(R.id.edit_name);

        dialog.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNamedWorkout(mEdit.getText().toString());
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.dismiss();
            }
        });


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    @SuppressLint("StaticFieldLeak")
    private void saveNamedWorkout(String name) {
        new AsyncTask<Void, Void, Void>() {
            protected void onPreExecute() {
                // Pre Code
            }
            protected Void doInBackground(Void... unused) {
                mLiftingWorkout = AppDatabase.getAppDatabase(getActivity()).lwDAO().findByDate(formattedDate);
                ArrayList<String> mLiftNames = new ArrayList<String>();
                ArrayList<Lift> mLifts = mLiftingWorkout.getMyLifts();
                for(int i=0; i<mLifts.size(); i++){
                    mLiftNames.add(mLifts.get(i).getName());
                }
                addNamedWorkout(name, mLiftNames);
                return null;
            }
            protected void onPostExecute(Void unused) {
                // Post Code
                mLiftAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private void updateNamedWorkoutList() {
        new AsyncTask<Void, Void, Void>() {
            protected void onPreExecute() {
                // Pre Code
            }
            protected Void doInBackground(Void... unused) {
                ArrayList<Lift> mLifts = mLiftingWorkout.getMyLifts();
                return null;
            }
            protected void onPostExecute(Void unused) {
                // Post Code
            finishUI();
            }
        }.execute();
    }


    private void addNamedWorkout(String name, ArrayList<String> mlifts) {
        NamedWorkout mWorkout = new NamedWorkout(name, mlifts);
        mNamedWorkouts.add(mWorkout);
        mRepository.insertNamedWorkout(mWorkout);

    }

    //calendar interface
    @Override
    public void onDialogDismiss(CalendarDay m) {


        Calendar c = Calendar.getInstance();
        c.set(m.getYear(), m.getMonth()-1, m.getDay());
        Date d = c.getTime();
        SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d, ''yy");
        formattedDate = df.format(d);
        System.out.println("formdate:"+formattedDate);

        getDayData();

    }

    //standard simple interface MyDialogInterface, not a dialog, for the namedworkout adapter.
    //lift for add, namedworkout for deleting named workout on long click


    @Override
    public void onDialogDismiss(LiftingWorkout mWorkout) {

        mLiftingWorkout = mWorkout;
        mRepository.updateWorkout(mLiftingWorkout);
        mLiftAdapter.notifyDataSetChanged();
        mRecyclerView.smoothScrollToPosition(mLiftAdapter.getItemCount() - 1);


    }

    public void uploadToStrava(){

            final Dialog dialog = new Dialog(getActivity());

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_upload_lift);

            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int width = metrics.widthPixels;

            EditText mEditDescription = dialog.findViewById(R.id.workout_description);
            mEditDescription.setText(getDescription());

            dialog.getWindow().setLayout((8 * width) / 9, LinearLayout.LayoutParams.WRAP_CONTENT);
            dialog.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!prefs.getBoolean("strava2", false)) {
                        Uri intentUri = Uri.parse("https://www.strava.com/oauth/mobile/authorize")
                                .buildUpon()
                                .appendQueryParameter("client_id", "43815")
                                .appendQueryParameter("redirect_uri", REDIRECT_URI)
                                .appendQueryParameter("response_type", "code")
                                .appendQueryParameter("approval_prompt", "auto")
                                .appendQueryParameter("scope", "activity:write,read")
                                .build();
                        edt.putString("upload_fragment", "Run");
                        edt.commit();
                        Intent intent = new Intent(Intent.ACTION_VIEW, intentUri);
                        intent.putExtra("key", 999);
                        startActivityForResult(intent, RQ_LOGIN);
                    }else{
                        finishStrava();
                    }

                }
            });


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    public void finishStrava() {
        new AsyncTask<Void, Void, Void>() {

            protected void onPreExecute() {
            }

            protected Void doInBackground(Void... unused) {


                AuthenticationConfig config = AuthenticationConfig.create()
                        .debug()
                        .build();
                AuthenticationAPI api = new AuthenticationAPI(config);
                String mToken = null;
                String mToken2 = null;

                if(prefs.getBoolean("first_refresh", true) == true) {
                    LoginResult result = api.getTokenForApp(AppCredentials.with(43815, "87571a766af016d9949d28929316f894bbc57938"))
                            .withCode(prefs.getString("code", "")) //original response token placed here as well.
                            .execute();
                    mToken = result.getAccessToken();
                    mToken2 = result.getRefreshToken();
                    edt.putBoolean("first_refresh",false );
                    edt.putString("refresh_token",mToken2 );
                    edt.apply();
                }else {
                    AuthenticationConfig config2 = AuthenticationConfig.create()
                            .debug()
                            .build();
                    AuthenticationAPI api2 = new AuthenticationAPI(config);
                    LoginResult loginResult = api2.refreshTokenForApp(AppCredentials.with(43815, "87571a766af016d9949d28929316f894bbc57938"))
                            .withRefreshToken(prefs.getString("refresh_token", ""))
                            .refreshToken();
                    mToken = loginResult.getAccessToken();
                    mToken2 = loginResult.getRefreshToken();
                    edt.putString("refresh_token", mToken2);
                    edt.apply();
                }




                StravaConfig sconfig = StravaConfig.withToken(mToken)
                        .debug()
                        .build();


                String title = "Morning Lift";
                Calendar cal = Calendar.getInstance();
                int time = cal.get(Calendar.HOUR_OF_DAY);
                if(time>21 || time<3){
                    title = "Night Lift";
                }else if(time>17){
                    title = "Evening Lift";
                }else if(time>12){
                    title = "Afternoon Lift";
                }


                ActivityAPI activityAPI = new ActivityAPI(sconfig);
                Activity activity = activityAPI.createActivity(title)
                        .ofType(ActivityType.WEIGHT_TRAINING)
                        .startingOn(Calendar.getInstance().getTime())
                        .withElapsedTime(Time.seconds(3600))
                        .withDescription(getDescription())
                        .isPrivate(true)
                        .withTrainer(true)
                        .withCommute(false)
                        .execute();

                return null;
            }

            protected void onPostExecute(Void unused) {
                // Post Code
            }
        }.execute();



    }



    private String getDescription(){

        String description = getTotalWeight() +" Pounds Lifted\n";
        ArrayList<Lift> mLifts = mLiftingWorkout.getMyLifts();
        for(int i=0; i<mLifts.size(); i++){
            ArrayList<Integer> sortedReps = mLifts.get(i).getReps();
            Collections.sort(sortedReps);
            ArrayList<Integer> sortedWeights  = mLifts.get(i).getRepWeights();
            Collections.sort(sortedWeights);

            description = description + mLifts.get(i).getName()+": "+mLifts.get(i).getReps().size()+ " sets, " ;
            description = description + sortedWeights.get(0)+"-"+sortedWeights.get(sortedWeights.size()-1)+" lbs, ";
            description = description + sortedReps.get(0)+"-"+sortedReps.get(sortedReps.size()-1)+"reps\n";
        }


        return description;
    }

    private int getTotalWeight(){

        int total = 0;
        ArrayList<Lift> mLifts = mLiftingWorkout.getMyLifts();
        for(int i=0; i<mLifts.size(); i++){
            ArrayList<Integer> weights  = mLifts.get(i).getRepWeights();
            ArrayList<Integer> reps  = mLifts.get(i).getReps();
            for(int j=0; j<weights.size();j++){
                total = total + (weights.get(j)*reps.get(j));
            }
        }


        return total;
    }


}