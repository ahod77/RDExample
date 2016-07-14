package com.example.alonassaf.rubberducky;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Noam on 7/13/2016.
 */
public class ContainerCollectionPagerAdapter extends FragmentStatePagerAdapter {

    long[] pinnedPanes;
    String[] pinnedPanesNames;

    public ContainerCollectionPagerAdapter(FragmentManager fm) {
        super(fm);

        // Loads pinned tabs
        pinnedPanes = RubberDuckyDB2.Settings.get("pinnedPanes").getLongs();

        // Cache tab names
        pinnedPanesNames = new String[pinnedPanes.length];
        for (int i = 0; i < pinnedPanes.length; i++) {
            pinnedPanesNames[i] = RubberDuckyDB2.Entities.get(pinnedPanes[i]).getName();
        }
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new RDFragment();
        Bundle args = new Bundle();
        args.putLong(RDFragment.ARG_ID, pinnedPanes[i]);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return pinnedPanes.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pinnedPanesNames[position];
    }

    public long getContainerIdForItem(int i) {
        return pinnedPanes[i];
    }
}
