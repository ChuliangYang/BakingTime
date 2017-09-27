package com.demo.cl.bakingtime.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.demo.cl.bakingtime.ui.fragment.IngredientFragment;
import com.demo.cl.bakingtime.ui.fragment.StepDetailPageFragment;
import com.demo.cl.bakingtime.ui.fragment.StepsFragment;
import com.demo.cl.bakingtime.widget.WrapContentViewPager;

import java.security.spec.PSSParameterSpec;

import timber.log.Timber;

/**
 * Created by CL on 9/16/17.
 */

public class RecipeDetailPagerAdapter extends FragmentStatePagerAdapter {
    private int FlagPosition = -1;


    public RecipeDetailPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Timber.w("new IngredientFragment()");
                return new IngredientFragment();
            case 1:
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
