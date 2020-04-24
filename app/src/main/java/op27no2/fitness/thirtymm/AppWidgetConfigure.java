package op27no2.fitness.thirtymm;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

import androidx.appcompat.app.AppCompatActivity;

import com.rilixtech.materialfancybutton.MaterialFancyButton;

public class AppWidgetConfigure extends AppCompatActivity{
private int appWidgetId;
private SharedPreferences prefs;
private SharedPreferences.Editor edt;
private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configure_widget);
        prefs = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        edt = getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
        mContext = this;

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            appWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }


        MaterialFancyButton cals = findViewById(R.id.calories);
        cals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt.putString(Integer.toString(appWidgetId), "Calories");
                edt.commit();
                updateWidgets();
                finishConfigure();
            }
        });
        MaterialFancyButton prot = findViewById(R.id.protein);
        prot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt.putString(Integer.toString(appWidgetId), "Protein");
                edt.commit();
                updateWidgets();
                finishConfigure();
            }
        });


    }

    private void updateWidgets(){
        Intent intent = new Intent(mContext, MyAppWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        // Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
        // since it seems the onUpdate() is only fired on that:
        AppWidgetManager manager= AppWidgetManager.getInstance(mContext);
        int[] ids = manager.getAppWidgetIds(new ComponentName(mContext, MyAppWidgetProvider.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        mContext.sendBroadcast(intent);
    }


    private void finishConfigure(){

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }


}
