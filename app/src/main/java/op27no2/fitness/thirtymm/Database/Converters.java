package op27no2.fitness.thirtymm.Database;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import op27no2.fitness.thirtymm.ui.lifting.Lift;

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
}