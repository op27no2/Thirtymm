package op27no2.fitness.thirtymm;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.mapboxsdk.maps.Style;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.List;

import op27no2.fitness.thirtymm.ui.run.RunFragment;

import static op27no2.fitness.thirtymm.R.id.nav_host_fragment;

public class MainActivity extends AppCompatActivity implements PermissionsListener {
    private SharedPreferences prefs;
    private SharedPreferences.Editor edt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        prefs = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        edt = getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
       /* AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();*/
        NavController navController = Navigation.findNavController(this, nav_host_fragment);
       // NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);




    }

    public void goToSettings(){

        NavController navController = Navigation.findNavController(this, nav_host_fragment);
        navController.navigate(R.id.navigation_settings);

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
}
