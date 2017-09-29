package com.demo.cl.bakingtime.helper;

import android.content.Context;
import android.util.Pair;

import com.demo.cl.bakingtime.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;

import java.util.HashMap;

/**
 * Created by CL on 9/21/17.
 */

public class PlayerHelper {
    private static final PlayerHelper ourInstance = new PlayerHelper();

    SimpleExoPlayer simpleExoPlayer;

    private HashMap<Integer, Long> currentProgress = new HashMap();


    private boolean StateFlag = false;

    private Context context;


    private PlayerHelper() {

    }

    public static PlayerHelper getInstance() {
        return ourInstance;
    }

    public Object getPlayer(Context context) {
        if (simpleExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);
        }
        return simpleExoPlayer;
    }

    public Object initPlayer(Context context) {
        if (simpleExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);
            this.context = context;
        } else {
            simpleExoPlayer.setPlayWhenReady(false);
        }
        return simpleExoPlayer;

    }

    public void releasePlayer() {
        if (!StateFlag) {
            if (simpleExoPlayer != null) {
                simpleExoPlayer.release();
                simpleExoPlayer = null;
            }
            currentProgress.clear();
        }
    }

    public void startPlayer() {
        if (simpleExoPlayer != null&&!simpleExoPlayer.getPlayWhenReady()) {
            simpleExoPlayer.setPlayWhenReady(true);
        }
    }

    public void stopPlayer() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.stop();
        }
    }

    public void pausePlayer() {
        if (simpleExoPlayer != null&&simpleExoPlayer.getPlayWhenReady()) {
            simpleExoPlayer.setPlayWhenReady(false);
        }
    }

    public void putProgress(int position, long ms) {
        currentProgress.put(position, ms);
    }

    public Long getProgress(int position) {
        return currentProgress.get(position);
    }

    public long getCurrentVideoProgress() {
        if (simpleExoPlayer != null) {
            return simpleExoPlayer.getCurrentPosition();
        } else {
            return 0;
        }
    }

    public void setStateFlag(boolean stateFlag) {
        StateFlag = stateFlag;
    }
}
