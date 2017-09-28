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
import com.demo.cl.bakingtime.data.RecipesBean;
import com.demo.cl.bakingtime.helper.EventHelper;
import com.demo.cl.bakingtime.helper.PlayerHelper;
import com.demo.cl.bakingtime.widget.WrapContentViewPager;

import org.greenrobot.eventbus.EventBus;

import timber.log.Timber;

import static android.support.v4.view.ViewPager.SCROLL_STATE_DRAGGING;
import static android.view.View.FOCUS_LEFT;
import static android.view.View.FOCUS_RIGHT;


/**
 * Created by CL on 9/18/17.
 */

public class StepDetailFragment extends Fragment implements OnScroll {
    Toolbar tbStepDetail;
    ViewPager vpStepDetail;
    ImageView ivPrevious;
    ImageView ivNext;
    RelativeLayout rlNavigate;
    WrapContentViewPager wrapContentViewPager;
    ScrollView sv_step_detail;
    boolean FlagOnce = true;
    private EventHelper.StepsBeanMessage stepsBeanMessage;
    private int currentPosition;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            stepsBeanMessage = (EventHelper.StepsBeanMessage) savedInstanceState.get("stepsBeanMessage");
            currentPosition = (int) savedInstanceState.get("currentPosition");
        } else {
            stepsBeanMessage = EventBus.getDefault().getStickyEvent(EventHelper.StepsBeanMessage.class);
            currentPosition = stepsBeanMessage.getCurrent_position();
        }

        if (stepsBeanMessage != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("stepsBeanMessage", stepsBeanMessage);
            setArguments(bundle);
        } else if (getArguments() != null && getArguments().get("stepsBeanMessage") != null) {
            stepsBeanMessage = (EventHelper.StepsBeanMessage) getArguments().get("stepsBeanMessage");
        } else {
            stepsBeanMessage = EventHelper.create().buildStepsBeanMessage(0, new RecipesBean());
        }

        PlayerHelper.getInstance().initPlayer(getContext());


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.step_detail, container, false);
        Configuration cf = this.getResources().getConfiguration(); //获取设置的配置信息
        int ori = cf.orientation; //获取屏幕方向
        if (ori == cf.ORIENTATION_LANDSCAPE) {
            EventBus.getDefault().removeStickyEvent(EventHelper.ScrollMessage.class);
            EventBus.getDefault().postSticky(EventHelper.create().buildScrollMessage(this));

            //横屏
            wrapContentViewPager = contentView.findViewById(R.id.wcvp_step_detail);
            sv_step_detail = contentView.findViewById(R.id.sv_step_detail);
            StepDetailPagerAdapter stepDetailPagerAdapter = new StepDetailPagerAdapter(getChildFragmentManager(), getContext());
            stepDetailPagerAdapter.setRecipesBean(stepsBeanMessage.getRecipesBean());
            wrapContentViewPager.setAdapter(stepDetailPagerAdapter);
            wrapContentViewPager.setCurrentItem(currentPosition, false);
            stepDetailPagerAdapter.setOnStepNavigation(new OnStepNavigation() {
                @Override
                public void onPrevious() {
                    if (wrapContentViewPager.getCurrentItem() > 0) {
                        PlayerHelper.getInstance().putProgress(currentPosition, PlayerHelper.getInstance().getCurrentVideoProgress());
                        PlayerHelper.getInstance().stopPlayer();
                        wrapContentViewPager.arrowScroll(FOCUS_LEFT);
                    }
                }

                @Override
                public void onNext() {
                    if (wrapContentViewPager.getCurrentItem() < wrapContentViewPager.getAdapter().getCount() - 1) {
                        PlayerHelper.getInstance().putProgress(currentPosition, PlayerHelper.getInstance().getCurrentVideoProgress());
                        PlayerHelper.getInstance().stopPlayer();
                        wrapContentViewPager.arrowScroll(FOCUS_RIGHT);
                    }

                }
            });

            wrapContentViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    currentPosition = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if (state == SCROLL_STATE_DRAGGING) {
                        if (FlagOnce) {
                            PlayerHelper.getInstance().putProgress(currentPosition, PlayerHelper.getInstance().getCurrentVideoProgress());
                            PlayerHelper.getInstance().stopPlayer();
                            FlagOnce = false;
                        }
                    } else {
                        FlagOnce = true;
                    }
                }
            });

        } else if (ori == cf.ORIENTATION_PORTRAIT) {
            //竖屏
            tbStepDetail = contentView.findViewById(R.id.tb_recipe);
            vpStepDetail = contentView.findViewById(R.id.vp_step_detail);
            ivPrevious = contentView.findViewById(R.id.iv_previous);
            ivNext = contentView.findViewById(R.id.iv_next);
            rlNavigate = contentView.findViewById(R.id.rl_navigate);

            if (getResources().getBoolean(R.bool.isTablet)) {
                tbStepDetail.setVisibility(View.GONE);
                rlNavigate.setVisibility(View.GONE);
            }

            StepDetailPagerAdapter stepDetailPagerAdapter = new StepDetailPagerAdapter(getChildFragmentManager(), getContext());
            stepDetailPagerAdapter.setRecipesBean(stepsBeanMessage.getRecipesBean());
            vpStepDetail.setAdapter(stepDetailPagerAdapter);
            vpStepDetail.setCurrentItem(currentPosition, false);
            tbStepDetail.setTitle(stepsBeanMessage.getRecipesBean().getName());
            tbStepDetail.setNavigationOnClickListener(view -> getActivity().finish());
            vpStepDetail.setOffscreenPageLimit(1);

            ivPrevious.setOnClickListener(view -> {
                if (vpStepDetail.getCurrentItem() > 0) {
                    PlayerHelper.getInstance().putProgress(currentPosition, PlayerHelper.getInstance().getCurrentVideoProgress());
                    PlayerHelper.getInstance().stopPlayer();
                    vpStepDetail.arrowScroll(FOCUS_LEFT);
                }


            });

            ivNext.setOnClickListener(view -> {

                if (vpStepDetail.getCurrentItem() < vpStepDetail.getAdapter().getCount() - 1) {
                    PlayerHelper.getInstance().putProgress(currentPosition, PlayerHelper.getInstance().getCurrentVideoProgress());
                    PlayerHelper.getInstance().stopPlayer();
                    vpStepDetail.arrowScroll(FOCUS_RIGHT);
                }
            });


            vpStepDetail.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    currentPosition = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if (state == SCROLL_STATE_DRAGGING) {
                        if (FlagOnce) {
                            PlayerHelper.getInstance().putProgress(currentPosition, PlayerHelper.getInstance().getCurrentVideoProgress());
                            PlayerHelper.getInstance().stopPlayer();
                            FlagOnce = false;
                        }
                    } else {
                        FlagOnce = true;
                    }
                }
            });
        }

        return contentView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("stepsBeanMessage", stepsBeanMessage);
        outState.putInt("currentPosition", currentPosition);
        PlayerHelper.getInstance().putProgress(currentPosition, PlayerHelper.getInstance().getCurrentVideoProgress());
        PlayerHelper.getInstance().setKeepStateFlag(true);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        PlayerHelper.getInstance().setKeepStateFlag(false);
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onStop() {
        super.onStop();
        PlayerHelper.getInstance().stopPlayer();
    }

    @Override
    public void onDestroy() {
        if (getResources().getBoolean(R.bool.isTablet)) {
            PlayerHelper.getInstance().stopPlayer();
        } else {
            PlayerHelper.getInstance().releasePlayer();
        }
        super.onDestroy();

    }

    @Override
    public void setScrollX(int x) {
        if (sv_step_detail != null) {
            sv_step_detail.setScrollX(x);
        }
    }

    @Override
    public void setScrollY(int y) {
        if (sv_step_detail != null) {
            sv_step_detail.post(() -> {
                sv_step_detail.setScrollY(y);
            });
        }
    }
}
