package op27no2.fitness.Centurion2.fragments.run;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "alert_profiles")
public class AlertProfile {

    public AlertProfile(String mName, ArrayList<AlertItem> mAlerts){
        this.mName = mName;
        this.mAlerts = mAlerts;
    }

        @PrimaryKey(autoGenerate = true)
        private int uid;

        @ColumnInfo(name = "name")
        private String mName;

        @ColumnInfo(name = "lifts")
        private ArrayList<AlertItem> mAlerts = new ArrayList<AlertItem>();

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getmName() {
            return mName;
        }

        public void setmName(String mName) {
            this.mName = mName;
        }

        public ArrayList<AlertItem> getmAlerts() {
            return mAlerts;
        }

        public void setmAlerts(ArrayList<AlertItem> mAlerts) {
            this.mAlerts = mAlerts;
        }



}


