package com.indiainclusionsummit.indiainclusionsummit;

/**
 * Created by I055845 on 10/9/2015.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class Tabsadapter  extends FragmentStatePagerAdapter{

    private int TOTAL_TABS = 2;

    public Tabsadapter(FragmentManager fm) {
        super(fm);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Fragment getItem(int index) {
        // TODO Auto-generated method stub
        switch (index) {
   //         case 0:
    //            return new HomeWebFragment();

            case 0:

                return new PatientStatistics();

            case 1:
                return new NavigationFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return TOTAL_TABS;
    }

}
