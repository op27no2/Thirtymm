package op27no2.fitness.Centurion.ui.lifting;


import java.util.ArrayList;

public class WorkoutLift {
private ArrayList<Lift> lifts = new  ArrayList<Lift>();



    public WorkoutLift() {

    }

    public void addLift(String name){
        Lift mlift = new Lift();
        mlift.setName(name);
        lifts.add(mlift);
    }




    public ArrayList<Lift> getLifts(){
        return lifts;
    }





    static class Lift {
        String name;

        public void setName(String setname){
                name = setname;
         }
        public String getName(){
            return name;
        }


    }


}
