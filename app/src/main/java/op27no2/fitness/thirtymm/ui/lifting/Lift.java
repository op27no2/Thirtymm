package op27no2.fitness.thirtymm.ui.lifting;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Lift {
    String mName;
    int mWeight;
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

    public void setReps(ArrayList<Integer> mRepArray){
        mReps = mRepArray;
    }
    public ArrayList<Integer> getReps(){
        return mReps;
    }

    public void setmWeights(ArrayList<Integer> mWeightArray){
        mWeights = mWeightArray;
    }
    public ArrayList<Integer> getWeights(){
        return mWeights;
    }

    public void plusRep(int index){
        int hold = mReps.get(index);
        hold = hold+1;
        mReps.set(index, hold);
    }
    public void minueRep(int index){
        int hold = mReps.get(index);
        hold = hold-1;
        mReps.set(index, hold);
    }

    public void addRep(){
        mReps.add(0);
        mWeights.add(mWeight);
    }
    public void removeRep(){
        mReps.remove(mReps.size()-1);
        mWeights.remove(mWeights.size()-1);
    }


}
