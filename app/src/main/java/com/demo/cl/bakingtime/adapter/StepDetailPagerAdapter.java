package com.demo.cl.bakingtime.adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.demo.cl.bakingtime.Interface.OnScroll;
import com.demo.cl.bakingtime.Interface.OnStepNavigation;
import com.demo.cl.bakingtime.data.Constant;
import com.demo.cl.bakingtime.data.RecipesBean;
import com.demo.cl.bakingtime.ui.fragment.StepDetailPageFragment;
import com.demo.cl.bakingtime.widget.WrapContentViewPager;

/**
 * Created by CL on 9/18/17.
 */

public class StepDetailPagerAdapter extends FragmentStatePagerAdapter {

    RecipesBean recipesBean;
    OnStepNavigation onStepNavigation;
    Context context;
    private OnScroll onScroll;

    private int FlagPosition = -1;

    public StepDetailPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        if (FlagPosition != position) {
            Configuration cf = context.getResources().getConfiguration(); //获取设置的配置信息
            int ori = cf.orientation; //获取屏幕方向
            if (ori == cf.ORIENTATION_LANDSCAPE) {
                    StepDetailPageFragment fragment = null;
                    WrapContentViewPager pager = null;
                    if (object instanceof StepDetailPageFragment) {
                        fragment = (StepDetailPageFragment) object;
                    }

                    if (container instanceof WrapContentViewPager) {
                        pager = (WrapContentViewPager) container;
                    }

                    if (fragment!=null&&pager!=null) {
                        if (fragment.getView() != null) {
                            pager.measureCurrentView(fragment.getView());
                        }
                        fragment.configLandFragmentState((ViewPager) container, position);
                    }
            } else {
                if (object instanceof StepDetailPageFragment) {
                    StepDetailPageFragment fragment = (StepDetailPageFragment) object;
                    fragment.configPortFragmentState(position);
                }
            }
            FlagPosition = position;
        }

    }

    @Override
    public Fragment getItem(int position) {
        StepDetailPageFragment stepDetailPageFragment = new StepDetailPageFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.DataKey.STEP_BEAN, recipesBean.getSteps().get(position));
        stepDetailPageFragment.setArguments(bundle);
        if (onStepNavigation != null) {
            stepDetailPageFragment.setOnStepNavigation(onStepNavigation);
        }
        return stepDetailPageFragment;
    }

    @Override
    public int getCount() {
        return recipesBean != null ? recipesBean.getSteps().size() : 0;
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
