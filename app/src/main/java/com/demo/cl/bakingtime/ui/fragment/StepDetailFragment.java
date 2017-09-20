package com.demo.cl.bakingtime.ui.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.demo.cl.bakingtime.Interface.OnScroll;
import com.demo.cl.bakingtime.Interface.OnStepNavigation;
import com.demo.cl.bakingtime.R;
import com.demo.cl.bakingtime.adapter.StepDetailPagerAdapter;
import com.demo.cl.bakingtime.helper.EventHelper;
import com.demo.cl.bakingtime.widget.WrapContentViewPager;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by CL on 9/18/17.
 */

public class StepDetailFragment extends Fragment{
    Toolbar tbStepDetail;
    ViewPager vpStepDetail;
    ImageView ivPrevious;
    ImageView ivNext;
    RelativeLayout rlNavigate;
    WrapContentViewPager wrapContentViewPager;
    ScrollView sv_step_detail;
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

        Configuration cf= this.getResources().getConfiguration(); //获取设置的配置信息
        int ori = cf.orientation ; //获取屏幕方向
        if(ori == cf.ORIENTATION_LANDSCAPE){
            //横屏
            wrapContentViewPager=contentView.findViewById(R.id.wcvp_step_detail);
            sv_step_detail=contentView.findViewById(R.id.sv_step_detail);
            StepDetailPagerAdapter stepDetailPagerAdapter=new StepDetailPagerAdapter(getChildFragmentManager(),getContext());
            stepDetailPagerAdapter.setRecipesBean(stepsBeanMessage.getRecipesBean());
            wrapContentViewPager.setAdapter(stepDetailPagerAdapter);
            wrapContentViewPager.setCurrentItem(stepsBeanMessage.getCurrent_position(),false);
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
            OnScroll onScroll=new OnScroll() {
                @Override
                public void ScrollTo(int x, int y) {
//                    sv_step_detail.scrollTo(x,y);
                    sv_step_detail.setScrollY(y);
                }

                @Override
                public void ScrollBy(int x, int y) {
                    sv_step_detail.scrollBy(x,y);
                }
            };
            EventBus.getDefault().removeStickyEvent(EventHelper.ScrollMessage.class);
            EventBus.getDefault().postSticky(EventHelper.create().buildScrollMessage(onScroll));

        }else if(ori == cf.ORIENTATION_PORTRAIT){
            //竖屏
            tbStepDetail=contentView.findViewById(R.id.tb_step_detail);
            vpStepDetail=contentView.findViewById(R.id.vp_step_detail);
            ivPrevious =contentView.findViewById(R.id.iv_previous);
            ivNext=contentView.findViewById(R.id.iv_next);
            rlNavigate=contentView.findViewById(R.id.rl_navigate);

            StepDetailPagerAdapter stepDetailPagerAdapter=new StepDetailPagerAdapter(getChildFragmentManager(),getContext());
            stepDetailPagerAdapter.setRecipesBean(stepsBeanMessage.getRecipesBean());
            vpStepDetail.setAdapter(stepDetailPagerAdapter);
            vpStepDetail.setCurrentItem(stepsBeanMessage.getCurrent_position(),false);
            tbStepDetail.setTitle(stepsBeanMessage.getRecipesBean().getName());
            tbStepDetail.setNavigationOnClickListener(view -> getActivity().finish());

            ivPrevious.setOnClickListener(view -> {
                if (vpStepDetail.getCurrentItem()-1>=0) {
                    vpStepDetail.setCurrentItem(vpStepDetail.getCurrentItem()-1);
                }
            });

            ivNext.setOnClickListener(view -> {
                if (vpStepDetail.getCurrentItem()+1<=stepDetailPagerAdapter.getCount()-1) {
                    vpStepDetail.setCurrentItem(vpStepDetail.getCurrentItem()+1);
                }
            });
        }






        return contentView;
    }
}
