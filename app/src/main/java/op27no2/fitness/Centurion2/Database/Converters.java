package op27no2.fitness.Centurion2.Database;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mapbox.geojson.Point;

import java.lang.reflect.Type;
import java.util.ArrayList;

import op27no2.fitness.Centurion2.fragments.activities.GoalsDetail;
import op27no2.fitness.Centurion2.fragments.lifting.Lift;
import op27no2.fitness.Centurion2.fragments.run.RunType;
import op27no2.fitness.Centurion2.fragments.run.TrackedPoint;

public class Converters {
    @TypeConverter
    public static ArrayList<Lift> fromString(String value) {
        Type listType = new TypeToken<ArrayList<Lift>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<Lift> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }


    @TypeConverter
    public static ArrayList<String> fromString2(String value) {
        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList2(ArrayList<String> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static ArrayList<Integer> fromString3(String value) {
        Type listType = new TypeToken<ArrayList<Integer>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList3(ArrayList<Integer> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static ArrayList<Double> fromString4(String value) {
        Type listType = new TypeToken<ArrayList<Double>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList4(ArrayList<Double> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static ArrayList<Point> fromString5(String value) {
        Type listType = new TypeToken<ArrayList<Point>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList5(ArrayList<Point> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static ArrayList<GoalsDetail> fromString6(String value) {
        Type listType = new TypeToken<ArrayList<GoalsDetail>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList6(ArrayList<GoalsDetail> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }


    @TypeConverter
    public static RunType fromString7(String value) {
        Type listType = new TypeToken<RunType>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromRunType(RunType type) {
        Gson gson = new Gson();
        String json = gson.toJson(type);
        return json;
    }

    @TypeConverter
    public static ArrayList<TrackedPoint> fromString8(String value) {
        Type listType = new TypeToken<ArrayList<TrackedPoint>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList8(ArrayList<TrackedPoint> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }





}