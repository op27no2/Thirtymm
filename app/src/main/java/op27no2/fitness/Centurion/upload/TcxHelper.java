package op27no2.fitness.Centurion.upload;

import android.content.Context;
import android.content.ContextWrapper;

import com.sweetzpot.tcxzpot.Position;
import com.sweetzpot.tcxzpot.TCXDate;
import com.sweetzpot.tcxzpot.Trackpoint;
import com.sweetzpot.tcxzpot.serializers.FileSerializer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import op27no2.fitness.Centurion.ui.run.TrackedPoint;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;
import static com.sweetzpot.tcxzpot.Activities.activities;
import static com.sweetzpot.tcxzpot.Intensity.ACTIVE;
import static com.sweetzpot.tcxzpot.Notes.notes;
import static com.sweetzpot.tcxzpot.Sport.RUNNING;
import static com.sweetzpot.tcxzpot.TCXDate.tcxDate;
import static com.sweetzpot.tcxzpot.Track.trackWith;
import static com.sweetzpot.tcxzpot.TriggerMethod.MANUAL;
import static com.sweetzpot.tcxzpot.builders.ActivityBuilder.activity;
import static com.sweetzpot.tcxzpot.builders.ApplicationBuilder.application;
import static com.sweetzpot.tcxzpot.builders.BuildBuilder.aBuild;
import static com.sweetzpot.tcxzpot.builders.DeviceBuilder.device;
import static com.sweetzpot.tcxzpot.builders.LapBuilder.aLap;
import static com.sweetzpot.tcxzpot.builders.TrainingCenterDatabaseBuilder.trainingCenterDatabase;
import static com.sweetzpot.tcxzpot.builders.VersionBuilder.version;

public class TcxHelper {
    private TCXDate startTime;

    public String createTCX(String filename, ArrayList<TrackedPoint> mPoints, int distance, int calories, int time){
        System.out.println("trackedpoints size "+mPoints.size());

        Calendar cal = Calendar.getInstance();
        double totaltime = (double) (mPoints.get(mPoints.size()-1).getTimestamp() - mPoints.get(0).getTimestamp())/1000;
        System.out.println("TOTAL TIME:"+totaltime);

        ArrayList<Trackpoint> tcxPoints = new ArrayList<Trackpoint>();
        for(int i=0; i<mPoints.size(); i++){
            TrackedPoint point = mPoints.get(i);
            cal.setTimeInMillis(point.getTimestamp());
            TCXDate mDate = new TCXDate(cal.getTime());
            if(i==0){
                startTime = mDate;
            }
            System.out.println("Date: "+mDate.toString());
            Position mPosition = new Position(point.getPoint().latitude(), point.getPoint().longitude());
            Trackpoint mTrackPoint = new Trackpoint(mDate,mPosition,point.getPoint().altitude(), point.getDistance(),null,null,null,null);
            tcxPoints.add(mTrackPoint);
        }



        FileSerializer serializer = null;
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("tcxDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,filename+".tcx");

        try {
            serializer = new FileSerializer(mypath);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("file save failed - initiate serializer");
        }
        trainingCenterDatabase()
                .withActivities(activities(
                        activity(RUNNING)
                                .withID(tcxDate(12, cal.getTime().getMonth(), cal.getTime().getYear(), cal.getTime().getHours(), 45, 0))
                                .withCreator(
                                        device("Test sensor")
                                                .withVersion(version().major(1).minor(0))
                                                .withUnitId(1)
                                                .withProductId(1234567)
                                )
                                .withNotes(notes("My sample notes"))
                                .withLaps(
                                        aLap(startTime)
                                                .withTotalTime(totaltime)
                                                .withDistance(distance)
                                                .withCalories(calories)
                                                .withIntensity(ACTIVE)
                                                .withTriggerMethod(MANUAL)
                                                .withTracks(trackWith(tcxPoints))
                                )
                ))
                .withAuthor(
                        application("BreathZpot")
                                .withBuild(aBuild()
                                        .withVersion(version().major(2).minor(3)))
                                .withLanguageID("en")
                                .withPartNumber("123-45678-90")
                ).build()
                .serialize(serializer);
        try {
            serializer.save();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("file save failed - close serializer");
        }

        return mypath.getAbsolutePath();
    }






}
