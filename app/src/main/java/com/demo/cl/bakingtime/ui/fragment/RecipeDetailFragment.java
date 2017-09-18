package com.demo.cl.bakingtime.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.cl.bakingtime.R;
import com.demo.cl.bakingtime.adapter.RecipeDetailAdapter;
import com.demo.cl.bakingtime.data.RecipesBean;
import com.demo.cl.bakingtime.helper.EventHelper;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CL on 9/16/17.
 */

public class RecipeDetailFragment extends android.support.v4.app.Fragment {

    @BindView(R.id.tb_recipe_detail)
    Toolbar tbRecipeDetail;
    @BindView(R.id.tl_recipe)
    TabLayout tlRecipe;
    @BindView(R.id.vp_recipe)
    ViewPager vpRecipe;
    private RecipeDetailAdapter recipeDetailAdapter;

    public RecipeDetailFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.recipe_detail, container, false);
        ButterKnife.bind(this, contentView);
        RecipesBean recipesBean = EventBus.getDefault().getStickyEvent(EventHelper.RecipesBeanMessage.class).getRecipesBean();
        tbRecipeDetail.setTitle(recipesBean.getName());
        tbRecipeDetail.setNavigationOnClickListener(view -> getActivity().finish());
        recipeDetailAdapter=new RecipeDetailAdapter(getChildFragmentManager());
        vpRecipe.setAdapter(recipeDetailAdapter);
        tlRecipe.setupWithViewPager(vpRecipe);
        return contentView;
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}
