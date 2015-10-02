package dear.dearles.customclasses;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import dear.dearles.activities.AmistadTab;
import dear.dearles.activities.RelacionEstableTab;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    CharSequence Titles[];

    // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    int NumbOfTabs;

    ArrayList<String> UsersUsingHashtag = null;
    String Hashtag = null;

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
    }

    public ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb, String Hashtag, ArrayList<String> UsersUsingHashtag) {
        this(fm, mTitles, mNumbOfTabsumb);
        this.Hashtag = Hashtag;
        this.UsersUsingHashtag = UsersUsingHashtag;

        for (String UserName : UsersUsingHashtag) {
            System.out.println("ESTOY EN VIEWPAGERADAPTER.java Y LA LISTA DE USUARIOS CONTIENE: " + UserName);
        }
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        Bundle bundle = new Bundle();
        bundle.putString("Hashtag", Hashtag);
        bundle.putStringArrayList("UsersUsingHashtag", UsersUsingHashtag);



        if(position == 0)
        // if the position is 0 we are returning the First tab
        {
            RelacionEstableTab tab1 = new RelacionEstableTab();
            tab1.setArguments(bundle);
            return tab1;
        }
        else
        // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
        {
            AmistadTab tab2 = new AmistadTab();
            tab2.setArguments(bundle);
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
