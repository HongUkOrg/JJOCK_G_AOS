package com.david.user.sealseeksee.TabLayoutFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.david.user.sealseeksee.JGController;


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
            case 5:
                fragment = new Fragment6();
                args = new Bundle();
                // Our object is just an integer :-P
                args.putInt(Fragment1.ARG_OBJECT, i + 1);
                fragment.setArguments(args);
                break;
            case 6:
                fragment = new Fragment7();
                args = new Bundle();
                // Our object is just an integer :-P
                args.putInt(Fragment1.ARG_OBJECT, i + 1);
                fragment.setArguments(args);
                break;
            case 7:
                fragment = new Fragment8();
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
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        JGController.getInstance().finderChangeListener.changeFinder(position+1);
    }


    @Override
    public int getCount() {
        return 8;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "OBJECT " + (position + 1);
    }
}