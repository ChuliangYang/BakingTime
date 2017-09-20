package com.demo.cl.bakingtime.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.cl.bakingtime.R;
import com.demo.cl.bakingtime.adapter.StepsAdapter;
import com.demo.cl.bakingtime.data.RecipesBean;
import com.demo.cl.bakingtime.helper.EventHelper;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by CL on 9/18/17.
 */

public class StepsFragment extends Fragment {
    @BindView(R.id.rv_steps)
    RecyclerView rvSteps;
    private RecipesBean recipesBean;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipesBean = EventBus.getDefault().getStickyEvent(EventHelper.RecipesBeanMessage.class).getRecipesBean();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.steps_page, container, false);
        ButterKnife.bind(this, contentView);
        rvSteps.setHasFixedSize(false);
        rvSteps.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        StepsAdapter stepsAdapter=new StepsAdapter(recipesBean,getContext());
        rvSteps.setAdapter(stepsAdapter);
        return contentView;
    }

}