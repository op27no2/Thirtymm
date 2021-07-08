package op27no2.fitness.Centurion2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mapbox.android.core.permissions.PermissionsListener;

import java.util.List;

import op27no2.fitness.Centurion2.Database.AlarmReceiver;
import op27no2.fitness.Centurion2.fragments.run.RunFragment;

import static op27no2.fitness.Centurion2.R.id.nav_host_fragment;

public class MainActivity extends AppCompatActivity implements PermissionsListener {
    private SharedPreferences prefs;
    private SharedPreferences.Editor edt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("MainActivity onCreate");
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        prefs = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        edt = getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();

        Intent intent = new Intent(this, FirstRunActivity.class);
   /*     EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);*/

        //TODO bring back intro and create firstlogin logic
     //   startActivity(intent);

        Intent alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, 0);
        AlarmManager alarmManager =
                (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
            10000,
            60000, pendingIntent);


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
       /* AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();*/
        NavController navController = Navigation.findNavController(this, nav_host_fragment);
       // NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);



        NavigationUI.setupWithNavController(navView, navController);

      /*  NavOptions animationOptions = new NavOptions.Builder().setEnterAnim(R.anim.nav_default_enter_anim)
                .setExitAnim(R.anim.nav_default_exit_anim)
                .setPopEnterAnim(R.anim.nav_default_pop_enter_anim)
                .setPopExitAnim(R.anim.nav_default_pop_exit_anim).build();*/

    }

    public void goToSettings(){
        NavController navController = Navigation.findNavController(this, nav_host_fragment);
        navController.navigate(R.id.navigation_settings);
    }

    public void goToActivitySettings(){
        NavController navController = Navigation.findNavController(this, nav_host_fragment);
        navController.navigate(R.id.navigation_activitysettings);
    }

    public void goToNutritionSettings(){
        NavController navController = Navigation.findNavController(this, nav_host_fragment);
        navController.navigate(R.id.navigation_nutritionsettings);
    }




    public void goToRunSettings(){
        NavController navController = Navigation.findNavController(this, nav_host_fragment);
        navController.navigate(R.id.navigation_runsettings);
    }

    public void goToRunDetail(int id){
        /*ConfirmationAction action =  SpecifyAmountFragmentDirections.confirmationAction();
        action.setAmount(amount);
        Navigation.findNavController(view).navigate(action);
*/
        System.out.println("activity level runworkout id: "+id);

        NavController navController = Navigation.findNavController(this, nav_host_fragment);
        Bundle bundle = new Bundle();
        bundle.putInt("uid", id);
        navController.navigate(R.id.navigation_rundetail, bundle);

    }


    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        System.out.println("permission requests results "+grantResults[0]);
       /* RunFragment fragment = (RunFragment) getSupportFragmentManager().findFragmentById(prefs.getInt("fragtag",0));
        fragment.enableLocationComponentAfterResult();*/
        Fragment navHostFragment = getSupportFragmentManager().getPrimaryNavigationFragment();
        Fragment fragment = navHostFragment.getChildFragmentManager().getFragments().get(0);

        ((RunFragment) fragment).enableLocationComponentAfterResult();

    }


    @Override
    public void onPermissionResult(boolean granted) {
        System.out.println("permission result");

        if (granted) {
            System.out.println("permission granted");


        } else {
            Toast.makeText(this, "Location Permission not Granted, Location Cannot Load", Toast.LENGTH_LONG).show();
            System.out.println("permission NOT granted");
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("strava on activity result "+resultCode);
  /*      Fragment navHostFragment = getSupportFragmentManager().getPrimaryNavigationFragment();
        Fragment fragment = navHostFragment.getChildFragmentManager().getFragments().get(0);
        fragment.onActivityResult(resultCode, data);*/

    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("activity onPause");

    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("activity onStop");

    }


    @Override
    public void onResume() {
        super.onResume();
        System.out.println("MainActivity onResume");

        Uri data = getIntent().getData();
        if(data!=null && getIntent().getIntExtra("key",0) != 999){
            System.out.println("EZ DATA "+ getIntent().getData().getQueryParameters("code"));

            List<String> code = getIntent().getData().getQueryParameters("code");
            System.out.println("EZ DATA "+code.get(0));
            edt.putString("code", code.get(0));
            edt.putBoolean("strava9", true);
            edt.apply();
            getIntent().removeExtra("key");

            System.out.println("back with key yo");
 /*           if(prefs.getString("upload_fragment","").equals("Lift")) {
                NavController navController = Navigation.findNavController(MainActivity.this, nav_host_fragment);
                navController.navigate(R.id.navigation_lift);
            }else{
                NavController navController = Navigation.findNavController(MainActivity.this, nav_host_fragment);
                navController.navigate(R.id.navigation_run);
            }*/

/*

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    Fragment navHostFragment = getSupportFragmentManager().getPrimaryNavigationFragment();
                    Fragment fragment = navHostFragment.getChildFragmentManager().getFragments().get(0);
                    if(prefs.getString("upload_fragment","").equals("Lift")) {
                        ((LiftFragment) fragment).finishStrava(code.get(0));
                    }else{
                        ((RunFragment) fragment).finishStrava(code.get(0));
                    }
                }
            }, 1000);
*/



        }
    }




}
