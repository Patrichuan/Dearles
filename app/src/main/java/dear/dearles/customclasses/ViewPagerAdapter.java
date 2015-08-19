package dear.dearles.customclasses;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import dear.dearles.activities.AmistadTab;
import dear.dearles.activities.RelacionEstableTab;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    CharSequence Titles[];

    // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    int NumbOfTabs;

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        if(position == 0)
        // if the position is 0 we are returning the First tab
        {
            RelacionEstableTab tab1 = new RelacionEstableTab();
            return tab1;
        }
        else
        // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
        {
            AmistadTab tab2 = new AmistadTab();
            return tab2;
        }
    }


    // This method return the titles for the Tabs in the Tab Strip
    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip
    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}
