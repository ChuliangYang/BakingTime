package com.demo.cl.bakingtime.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.demo.cl.bakingtime.ui.fragment.IngredientFragment;

/**
 * Created by CL on 9/16/17.
 */

public class RecipeDetailAdapter extends FragmentStatePagerAdapter {

    public RecipeDetailAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new IngredientFragment();
            case 1:
                // TODO: 9/17/17 步骤fragment
                return new Fragment();
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
