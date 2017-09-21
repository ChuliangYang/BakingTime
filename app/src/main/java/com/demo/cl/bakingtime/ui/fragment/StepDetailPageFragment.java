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
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
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
    private RecipesBean.StepsBean stepsBean;
    private EventHelper.StepsBeanMessage stepsBeanMessage;
    private OnStepNavigation onStepNavigation;
    private OnScroll onScroll;
    private String TAG =getClass().getName();
    private Boolean NoVideo=false;
    private static int toolbarSize;

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
                tb_step_detail_land.setVisibility(View.VISIBLE);
                NoVideo=true;
                contentView.measure(0,0);
                if (contentView.getMeasuredHeight()<getResources().getDisplayMetrics().heightPixels) {
                    ViewPager.LayoutParams vp_layoutParams=new ViewPager.LayoutParams();
                    vp_layoutParams.height=getResources().getDisplayMetrics().heightPixels-DisplayHelper.getStatusBarHeight(getContext());
                    vp_layoutParams.width=ViewPager.LayoutParams.MATCH_PARENT;
                    content_step_detail.setLayoutParams(vp_layoutParams);
                }

            } else {
                initializePlayer(Uri.parse(stepsBean.getVideoURL()),player_view_land);
                RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) player_view_land.getLayoutParams();
                layoutParams.height=getResources().getDisplayMetrics().heightPixels- DisplayHelper.getStatusBarHeight(getContext());
                layoutParams.width=RelativeLayout.LayoutParams.MATCH_PARENT;
                player_view_land.setLayoutParams(layoutParams);
                tb_step_detail_land.setVisibility(View.GONE);
            }
            tv_describe_land.setText(stepsBean.getDescription());

            iv_previous_land.setOnClickListener(view -> onStepNavigation.onPrevious());

            iv_next_land.setOnClickListener(view -> onStepNavigation.onNext());




        }else if(ori == cf.ORIENTATION_PORTRAIT){
            //竖屏
            mPlayerView=contentView.findViewById(R.id.player_view);
            tvDescribe=contentView.findViewById(R.id.tv_describe);
            svStepDescribe=contentView.findViewById(R.id.sv_step_describe);
            tvDescribe.setText(stepsBean.getDescription());
            if (TextUtils.isEmpty(stepsBean.getVideoURL())) {
                mPlayerView.setVisibility(View.GONE);
            } else {
                initializePlayer(Uri.parse(stepsBean.getVideoURL()),mPlayerView);

            }
        }

        return contentView;
    }



    private void initializePlayer(Uri mediaUri,SimpleExoPlayerView simpleExoPlayerView) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            simpleExoPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);
            // Prepare the MediaSource.使用uri定位媒体资源
            String userAgent = Util.getUserAgent(getContext(), getResources().getString(R.string.app_name));
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);//这个适用常规格式，自适应流不      合适
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(false);//设置就绪即自动播放
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
        if (mExoPlayer != null) {
            mExoPlayer.stop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mExoPlayer!=null) {
            mExoPlayer.release();
        }
    }

    public OnStepNavigation getOnStepNavigation() {
        return onStepNavigation;
    }

    public void setOnStepNavigation(OnStepNavigation onStepNavigation) {
        this.onStepNavigation = onStepNavigation;
    }

    public void configFragmentState(ViewPager viewPager) {
        viewPager.post(() -> {
            onScroll.setScrollY(toolbarSize);
            tb_step_detail_land.setVisibility(View.VISIBLE);
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("stepsBeanMessage",stepsBeanMessage);
        super.onSaveInstanceState(outState);
    }
}
