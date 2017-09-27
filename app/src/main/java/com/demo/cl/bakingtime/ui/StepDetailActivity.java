package com.demo.cl.bakingtime.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.demo.cl.bakingtime.R;
import com.demo.cl.bakingtime.ui.fragment.StepDetailFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CL on 9/18/17.
 */

public class StepDetailActivity extends AppCompatActivity {
    @BindView(R.id.fl_content)
    FrameLayout flContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whole_fragment);
        ButterKnife.bind(this);
        if (savedInstanceState != null && getSupportFragmentManager().findFragmentByTag("StepDetailFragment") != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fl_content, getSupportFragmentManager().findFragmentByTag("StepDetailFragment"));
            fragmentTransaction.commit();
        } else {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fl_content, new StepDetailFragment(), "StepDetailFragment");
            fragmentTransaction.commit();
        }

    }
}
