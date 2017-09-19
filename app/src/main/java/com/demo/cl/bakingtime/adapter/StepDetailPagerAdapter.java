package com.demo.cl.bakingtime.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.demo.cl.bakingtime.Interface.OnStepNavigation;
import com.demo.cl.bakingtime.data.Constant;
import com.demo.cl.bakingtime.data.RecipesBean;
import com.demo.cl.bakingtime.ui.fragment.StepDetailPageFragment;

/**
 * Created by CL on 9/18/17.
 */

public class StepDetailPagerAdapter extends FragmentStatePagerAdapter {

    RecipesBean recipesBean;

    private OnStepNavigation onStepNavigation;

    public StepDetailPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        StepDetailPageFragment stepDetailPageFragment=new StepDetailPageFragment();
        Bundle bundle=new Bundle();
        bundle.putParcelable(Constant.DataKey.STEP_BEAN,recipesBean.getSteps().get(position));
        stepDetailPageFragment.setArguments(bundle);
        stepDetailPageFragment.setOnStepNavigation(onStepNavigation);
        return stepDetailPageFragment;
    }

    @Override
    public int getCount() {
        return recipesBean!=null?recipesBean.getSteps().size():0;
    }

    public RecipesBean getRecipesBean() {
        return recipesBean;
    }

    public void setRecipesBean(RecipesBean recipesBean) {
        this.recipesBean = recipesBean;
    }

    public OnStepNavigation getOnStepNavigation() {
        return onStepNavigation;
    }

    public void setOnStepNavigation(OnStepNavigation onStepNavigation) {
        this.onStepNavigation = onStepNavigation;
    }
}
