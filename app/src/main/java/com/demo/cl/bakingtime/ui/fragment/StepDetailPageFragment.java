package com.demo.cl.bakingtime.ui.fragment;

import android.content.res.Configuration;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.demo.cl.bakingtime.Interface.OnScroll;
import com.demo.cl.bakingtime.Interface.OnStepNavigation;
import com.demo.cl.bakingtime.R;
import com.demo.cl.bakingtime.data.Constant;
import com.demo.cl.bakingtime.data.RecipesBean;
import com.demo.cl.bakingtime.helper.DisplayHelper;
import com.demo.cl.bakingtime.helper.EventHelper;
import com.demo.cl.bakingtime.helper.PlayerHelper;
import com.demo.cl.bakingtime.widget.WrapContentViewPager;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.android.exoplayer2.Player.REPEAT_MODE_ALL;

/**
 * Created by CL on 9/18/17.
 */

public class StepDetailPageFragment extends Fragment implements ExoPlayer.EventListener {
    SimpleExoPlayerView mPlayerView;
    TextView tvDescribe;
    ScrollView svStepDescribe;
    SimpleExoPlayer mExoPlayer;
    Toolbar tb_step_detail_land;
    SimpleExoPlayerView player_view_land;
    TextView tv_describe_land;
    ImageView iv_previous_land;
    ImageView iv_next_land;
    RelativeLayout content_step_detail;
    RelativeLayout rl_navigation;
    private RecipesBean.StepsBean stepsBean;
    private EventHelper.StepsBeanMessage stepsBeanMessage;
    private OnStepNavigation onStepNavigation;
    private OnScroll onScroll;
    private String TAG =getClass().getName();
    private static int toolbarSize;
    private ExtractorMediaSource mediaSource;
    private Boolean createdView=false;
    private Boolean isCache=true;
    private int position;
    private WrapContentViewPager viewPager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stepsBean=getArguments().getParcelable(Constant.DataKey.STEP_BEAN);
        if (savedInstanceState != null) {
            stepsBeanMessage= (EventHelper.StepsBeanMessage) savedInstanceState.get("stepsBeanMessage");
        } else {
            stepsBeanMessage= EventBus.getDefault().getStickyEvent(EventHelper.StepsBeanMessage.class);
        }
        if (EventBus.getDefault().getStickyEvent(EventHelper.ScrollMessage.class)!=null) {
            onScroll=EventBus.getDefault().getStickyEvent(EventHelper.ScrollMessage.class).getOnScroll();
        }

        if (stepsBeanMessage != null) {
            Bundle bundle=getArguments();
            bundle.putParcelable("stepsBeanMessage", stepsBeanMessage);
            setArguments(bundle);
        } else if (getArguments()!=null&&getArguments().get("stepsBeanMessage") != null) {
            stepsBeanMessage = (EventHelper.StepsBeanMessage) getArguments().get("stepsBeanMessage");
        } else {
            stepsBeanMessage=EventHelper.create().buildStepsBeanMessage(0,new RecipesBean());
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.step_detail_page, container, false);

        Configuration cf= this.getResources().getConfiguration(); //获取设置的配置信息
        int ori = cf.orientation ; //获取屏幕方向
        if(ori == cf.ORIENTATION_LANDSCAPE){
            //横屏
                tb_step_detail_land=contentView.findViewById(R.id.tb_step_detail_land);
                player_view_land=contentView.findViewById(R.id.player_view_land);
                tv_describe_land=contentView.findViewById(R.id.tv_describe_land);
                iv_previous_land=contentView.findViewById(R.id.iv_previous_land);
                iv_next_land=contentView.findViewById(R.id.iv_next_land);
                content_step_detail=contentView.findViewById(R.id.content_step_detail);
                 rl_navigation=contentView.findViewById(R.id.rl_navigation);

            if (!getResources().getBoolean(R.bool.isTablet)) {
                tb_step_detail_land.setTitle(stepsBeanMessage.getRecipesBean().getName());
                tb_step_detail_land.setNavigationOnClickListener(view -> {
                    getActivity().finish();
                });

                if (toolbarSize==0) {
                    tb_step_detail_land.measure(0,0);
                    toolbarSize=tb_step_detail_land.getMeasuredHeight();
                }


                if (TextUtils.isEmpty(stepsBean.getVideoURL())) {
                    player_view_land.setVisibility(View.GONE);
                } else {
                    if (!isCache) {
                        initializePlayer(Uri.parse(stepsBean.getVideoURL()),player_view_land);
                    }
                    RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) player_view_land.getLayoutParams();
                    layoutParams.height=getResources().getDisplayMetrics().heightPixels- DisplayHelper.getStatusBarHeight(getContext());
                    layoutParams.width=RelativeLayout.LayoutParams.MATCH_PARENT;
                    player_view_land.setLayoutParams(layoutParams);

                }

                contentView.measure(0,0);
                if (contentView.getMeasuredHeight()<getResources().getDisplayMetrics().heightPixels) {
                    ViewPager.LayoutParams vp_layoutParams=new ViewPager.LayoutParams();
                    vp_layoutParams.height=getResources().getDisplayMetrics().heightPixels-DisplayHelper.getStatusBarHeight(getContext());
                    vp_layoutParams.width=ViewPager.LayoutParams.MATCH_PARENT;
                    content_step_detail.setLayoutParams(vp_layoutParams);
                }

                iv_previous_land.setOnClickListener(view -> onStepNavigation.onPrevious());

                iv_next_land.setOnClickListener(view -> onStepNavigation.onNext());

                if (viewPager!=null) {
                    viewPager.measureCurrentView(contentView);
                    viewPager.post(() -> onScroll.setScrollY(toolbarSize));
                }
            } else {
                tb_step_detail_land.setVisibility(View.GONE);

                if (TextUtils.isEmpty(stepsBean.getVideoURL())) {
                    player_view_land.setVisibility(View.GONE);
                } else {
                    if (!isCache) {
                        initializePlayer(Uri.parse(stepsBean.getVideoURL()),player_view_land);
                    }
                    RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) player_view_land.getLayoutParams();
                    layoutParams.height=(getResources().getDisplayMetrics().heightPixels- DisplayHelper.getStatusBarHeight(getContext()))*6/10;
                    layoutParams.width=RelativeLayout.LayoutParams.MATCH_PARENT;
                    player_view_land.setLayoutParams(layoutParams);


                }

                rl_navigation.setVisibility(View.GONE);

                contentView.measure(0,0);
                if (contentView.getMeasuredHeight()<getResources().getDisplayMetrics().heightPixels) {
                    ViewPager.LayoutParams vp_layoutParams=new ViewPager.LayoutParams();
                    vp_layoutParams.height=getResources().getDisplayMetrics().heightPixels-DisplayHelper.getStatusBarHeight(getContext());
                    vp_layoutParams.width=ViewPager.LayoutParams.MATCH_PARENT;
                    content_step_detail.setLayoutParams(vp_layoutParams);
                }

                if (viewPager!=null) {
                    viewPager.measureCurrentView(contentView);
                }

            }

                tv_describe_land.setText(stepsBean.getDescription());





        }else if(ori == cf.ORIENTATION_PORTRAIT){
            //竖屏
            mPlayerView=contentView.findViewById(R.id.player_view);
            tvDescribe=contentView.findViewById(R.id.tv_describe);
            svStepDescribe=contentView.findViewById(R.id.sv_step_describe);

            if (getResources().getBoolean(R.bool.isTablet)) {
                mPlayerView.post(() -> {
                    RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) mPlayerView.getLayoutParams();
                    layoutParams.height=mPlayerView.getWidth()/3*2;
                    layoutParams.width=RelativeLayout.LayoutParams.MATCH_PARENT;
                    mPlayerView.setLayoutParams(layoutParams);
                });
            }
            tvDescribe.setText(stepsBean.getDescription());
            if (TextUtils.isEmpty(stepsBean.getVideoURL())) {
                mPlayerView.setVisibility(View.GONE);
            } else {
                if (!isCache) {
                    initializePlayer(Uri.parse(stepsBean.getVideoURL()), mPlayerView);
                }
            }
        }
        createdView=true;
        return contentView;
    }



    private void initializePlayer(Uri mediaUri,SimpleExoPlayerView simpleExoPlayerView) {
            if (PlayerHelper.getInstance().getPlayer(getContext()) instanceof SimpleExoPlayer) {
                mExoPlayer = (SimpleExoPlayer) PlayerHelper.getInstance().getPlayer(getContext());
             }
//        if (mExoPlayer.isLoading()) {
            mExoPlayer.stop();
//        }
            simpleExoPlayerView.setPlayer(mExoPlayer);
            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);
            // Prepare the MediaSource.使用uri定位媒体资源
            String userAgent = Util.getUserAgent(getContext(), getResources().getString(R.string.app_name));
             mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                     getContext(), userAgent), new DefaultExtractorsFactory(), null, null);//这个适用常规格式，自适应流不      合适
            mExoPlayer.prepare(mediaSource);

        if (PlayerHelper.getInstance().getProgress(position) != null) {
            mExoPlayer.seekTo(PlayerHelper.getInstance().getProgress(position));
        }
            mExoPlayer.setRepeatMode(REPEAT_MODE_ALL);
            mExoPlayer.setPlayWhenReady(true);
//        }
        //设置就绪即自动播放
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        if (player_view_land!=null) {
            player_view_land.setVisibility(View.GONE);
        }
        if (mPlayerView!=null) {
            mPlayerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onPause() {
        super.onPause();

    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isCache=false;
        createdView=false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public OnStepNavigation getOnStepNavigation() {
        return onStepNavigation;
    }

    public void setOnStepNavigation(OnStepNavigation onStepNavigation) {
        this.onStepNavigation = onStepNavigation;
    }

    public void configLandFragmentState(ViewPager viewPager,int position) {
        this.position=position;
        if (createdView) {
            if (player_view_land.getVisibility()==View.VISIBLE) {
                if (!getResources().getBoolean(R.bool.isTablet)) {
                    viewPager.post(() -> onScroll.setScrollY(toolbarSize));
                }
                initializePlayer(Uri.parse(stepsBean.getVideoURL()), player_view_land);
            }
        }else {
            isCache=false;
            this.viewPager= (WrapContentViewPager) viewPager;}
    }

    public void configPortFragmentState(int position) {
        this.position=position;

        if (createdView ) {
            if (mPlayerView.getVisibility() == View.VISIBLE) {
                initializePlayer(Uri.parse(stepsBean.getVideoURL()), mPlayerView);
            }
        } else {
            isCache=false;
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("stepsBeanMessage",stepsBeanMessage);
        super.onSaveInstanceState(outState);
    }



}



