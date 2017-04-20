package com.example.avinash.nittcart.display_item_files;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.MotionEvent;

/**
 * Created by AVINASH on 4/13/2017.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position ==0) {
            return new contact_us();
        } else {
            return new contact_us();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

}