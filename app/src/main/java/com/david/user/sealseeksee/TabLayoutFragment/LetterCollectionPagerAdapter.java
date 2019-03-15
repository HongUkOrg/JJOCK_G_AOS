package com.david.user.sealseeksee.TabLayoutFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class LetterCollectionPagerAdapter extends FragmentStatePagerAdapter {
    public LetterCollectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new Fragment1();
        Bundle args;
        switch (i) {
            case 0:
                fragment = new Fragment1();
                args = new Bundle();
                // Our object is just an integer :-P
                args.putInt(Fragment1.ARG_OBJECT, i + 1);
                fragment.setArguments(args);

                break;
            case 1:
                fragment = new Fragment2();
                args = new Bundle();
                // Our object is just an integer :-P
                args.putInt(Fragment1.ARG_OBJECT, i + 1);
                fragment.setArguments(args);
                break;
            case 2:
                fragment = new Fragment3();
                args = new Bundle();
                // Our object is just an integer :-P
                args.putInt(Fragment1.ARG_OBJECT, i + 1);
                fragment.setArguments(args);
                break;
            case 3:
                fragment = new Fragment4();
                args = new Bundle();
                // Our object is just an integer :-P
                args.putInt(Fragment1.ARG_OBJECT, i + 1);
                fragment.setArguments(args);
                break;
            case 4:
                fragment = new Fragment5();
                args = new Bundle();
                // Our object is just an integer :-P
                args.putInt(Fragment1.ARG_OBJECT, i + 1);
                fragment.setArguments(args);
                break;
            default:
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "OBJECT " + (position + 1);
    }
}