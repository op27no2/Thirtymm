package op27no2.fitness.Centurion;


import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import op27no2.fitness.Centurion.ui.activities.ChildActivitiesFragment;
import op27no2.fitness.Centurion.ui.activities.ChildProgressFragment;

public class MyAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public MyAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ChildProgressFragment progFragment = new ChildProgressFragment();
                return progFragment;
            case 1:
                ChildActivitiesFragment activitiesFragment= new ChildActivitiesFragment();
                return activitiesFragment;
            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}