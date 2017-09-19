package com.demo.cl.bakingtime.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.cl.bakingtime.Interface.OnStepNavigation;
import com.demo.cl.bakingtime.R;
import com.demo.cl.bakingtime.adapter.StepDetailPagerAdapter;
import com.demo.cl.bakingtime.data.RecipesBean;
import com.demo.cl.bakingtime.helper.EventHelper;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by CL on 9/18/17.
 */

public class StepDetailFragment extends Fragment{
    @BindView(R.id.tb_step_detail)
    Toolbar tbStepDetail;
    @BindView(R.id.vp_step_detail)
    ViewPager vpStepDetail;
    private EventHelper.StepsBeanMessage stepsBeanMessage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stepsBeanMessage=EventBus.getDefault().getStickyEvent(EventHelper.StepsBeanMessage.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.step_detail, container, false);
        ButterKnife.bind(this, contentView);
        StepDetailPagerAdapter stepDetailPagerAdapter=new StepDetailPagerAdapter(getChildFragmentManager());
        stepDetailPagerAdapter.setRecipesBean(stepsBeanMessage.getRecipesBean());
        stepDetailPagerAdapter.setOnStepNavigation(new OnStepNavigation() {
            @Override
            public void onPrevious() {
                if (vpStepDetail.getCurrentItem()-1>=0) {
                    vpStepDetail.setCurrentItem(vpStepDetail.getCurrentItem()-1);
                }
            }

            @Override
            public void onNext() {
                if (vpStepDetail.getCurrentItem()+1<=stepDetailPagerAdapter.getCount()-1) {
                    vpStepDetail.setCurrentItem(vpStepDetail.getCurrentItem()+1);
                }
            }
        });
        vpStepDetail.setAdapter(stepDetailPagerAdapter);
        vpStepDetail.setCurrentItem(stepsBeanMessage.getCurrent_position(),false);
        tbStepDetail.setTitle(stepsBeanMessage.getRecipesBean().getName());
        tbStepDetail.setNavigationOnClickListener(view -> getActivity().finish());
        return contentView;
    }
}
