package op27no2.fitness.Centurion2.fragments.lifting;

import java.util.ArrayList;

public class Lift {
    String mName;
    int mWeight;
    int defaultReps =0;
    private Boolean direction = true;
    private ArrayList<Integer> mReps = new ArrayList<Integer>();
    private ArrayList<Integer> mWeights = new ArrayList<Integer>();

    public Lift(String name, int weight) {
        mName = name;
        mWeight = weight;

    }

    public void setName(String setname){
        mName = setname;
    }
    public String getName(){
        return mName;
    }
    public int getWeight(){
        return mWeight;
    }
    public void setWeight(int weight){
        mWeight = weight;
    }
    public void setReps(ArrayList<Integer> mRepArray){
        mReps = mRepArray;
    }
    public ArrayList<Integer> getReps(){
        return mReps;
    }

    public void setWeights(ArrayList<Integer> mWeightArray){
        mWeights = mWeightArray;
    }
    public ArrayList<Integer> getRepWeights(){
        return mWeights;
    }

    public void setRepWeight(int index, int weight){
        mWeights.set(index, weight);
    }
    public void setRepNumber(int index, int number){
        mReps.set(index, number);
    }
    public int getRepNumber(int index){
        return mReps.get(index);
    }


    public void setDefaultReps(int reps){
        defaultReps = reps;
    }
    public int getDefaultReps(){
        return defaultReps;
    }

    public void setDirection(Boolean dir){
        direction = dir;
    }
    public Boolean getDirection(){
        return direction;
    }



    public void plusRep(int index){
        int hold = mReps.get(index);
        hold = hold+1;
        defaultReps = hold;
        mReps.set(index, hold);
    }
    public void minueRep(int index){
        int hold = mReps.get(index);
        hold = hold-1;
        defaultReps = hold;
        mReps.set(index, hold);
    }

    public void addSet(){
        mReps.add(0);
        mWeights.add(mWeight);
    }
    public void removeSet(int position){
        mReps.remove(position);
        mWeights.remove(position);
    }


}
