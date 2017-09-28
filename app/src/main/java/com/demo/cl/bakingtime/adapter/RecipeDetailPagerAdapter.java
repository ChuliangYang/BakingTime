package com.demo.cl.bakingtime.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.demo.cl.bakingtime.ui.fragment.IngredientFragment;
import com.demo.cl.bakingtime.ui.fragment.StepsFragment;

import timber.log.Timber;

/**
 * Created by CL on 9/16/17.
 */

public class RecipeDetailPagerAdapter extends FragmentStatePagerAdapter {

    public RecipeDetailPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Timber.e("new IngredientFragment()");
                return new IngredientFragment();
            case 1:
                Timber.e("new StepsFragment()");
                return new StepsFragment();
        }
        return new Fragment();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "ingredient";
            case 1:
                return "steps";
        }
        return "";
    }
}
