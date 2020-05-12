package op27no2.fitness.Centurion.upload;

import com.sweetzpot.tcxzpot.serializers.FileSerializer;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;


import static com.sweetzpot.tcxzpot.Activities.activities;
import static com.sweetzpot.tcxzpot.Intensity.ACTIVE;
import static com.sweetzpot.tcxzpot.Notes.notes;
import static com.sweetzpot.tcxzpot.Position.position;
import static com.sweetzpot.tcxzpot.Sport.RUNNING;
import static com.sweetzpot.tcxzpot.TCXDate.tcxDate;
import static com.sweetzpot.tcxzpot.Track.trackWith;
import static com.sweetzpot.tcxzpot.TriggerMethod.MANUAL;
import static com.sweetzpot.tcxzpot.builders.ActivityBuilder.activity;
import static com.sweetzpot.tcxzpot.builders.ApplicationBuilder.application;
import static com.sweetzpot.tcxzpot.builders.BuildBuilder.aBuild;
import static com.sweetzpot.tcxzpot.builders.DeviceBuilder.device;
import static com.sweetzpot.tcxzpot.builders.LapBuilder.aLap;
import static com.sweetzpot.tcxzpot.builders.TrackpointBuilder.aTrackpoint;
import static com.sweetzpot.tcxzpot.builders.TrainingCenterDatabaseBuilder.trainingCenterDatabase;
import static com.sweetzpot.tcxzpot.builders.VersionBuilder.version;
import static java.util.Calendar.FEBRUARY;

public class TcxHelper {

    public File createTCX(){

        Calendar cal = Calendar.getInstance();

        FileSerializer serializer = null;
        File mFile = new File("./sample.tcx");
        try {
            serializer = new FileSerializer(mFile);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("file save failed - initiate serializer");
        }
        trainingCenterDatabase()
                .withActivities(activities(
                        activity(RUNNING)
                                .withID(tcxDate(10, cal.getTime().getMonth(), 2017, 10, 42, 0))
                                .withCreator(
                                        device("BreathZpot Sensor")
                                                .withVersion(version().major(1).minor(0))
                                                .withUnitId(1)
                                                .withProductId(1234567)
                                )
                                .withNotes(notes("A sample session"))
                                .withLaps(
                                        aLap(tcxDate(10, FEBRUARY, 2017, 10, 42, 0))
                                                .withTotalTime(3000)
                                                .withDistance(1200)
                                                .withCalories(100)
                                                .withIntensity(ACTIVE)
                                                .withTriggerMethod(MANUAL)
                                                .withTracks(
                                                        trackWith(
                                                                aTrackpoint()
                                                                        .onTime(tcxDate(10, FEBRUARY, 2017, 10, 42, 15))
                                                                        .withPosition(position(-3.6714, 36.8936)),
                                                                aTrackpoint()
                                                                        .onTime(tcxDate(10, FEBRUARY, 2017, 10, 42, 43))
                                                                        .withPosition(position(-3.6727, 36.8946)),
                                                                aTrackpoint()
                                                                        .onTime(tcxDate(10, FEBRUARY, 2017, 10, 43, 20))
                                                                        .withPosition(position(-3.6733, 36.901))
                                                        )
                                                )
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

        return mFile;
    }






}
