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
import com.demo.cl.bakingtime.adapter.RecipeDetailPagerAdapter;
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
    private RecipeDetailPagerAdapter recipeDetailPagerAdapter;
    private RecipesBean recipesBean;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            recipesBean= (RecipesBean) savedInstanceState.get("recipesBean");
        } else if (EventBus.getDefault().getStickyEvent(EventHelper.RecipesBeanMessage.class)!=null) {
                recipesBean = EventBus.getDefault().getStickyEvent(EventHelper.RecipesBeanMessage.class).getRecipesBean();
        }

        if (recipesBean != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("recipesBean", recipesBean);
            setArguments(bundle);
        } else if (getArguments()!=null&&getArguments().get("recipesBean") != null) {
            recipesBean = (RecipesBean) getArguments().get("recipesBean");
        } else {
            recipesBean=new RecipesBean();
        }


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.recipe_detail, container, false);
        ButterKnife.bind(this, contentView);
        tbRecipeDetail.setTitle(recipesBean.getName());
        tbRecipeDetail.setNavigationOnClickListener(view -> getActivity().finish());
        recipeDetailPagerAdapter =new RecipeDetailPagerAdapter(getChildFragmentManager());
        vpRecipe.setAdapter(recipeDetailPagerAdapter);
        tlRecipe.setupWithViewPager(vpRecipe);
        return contentView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("recipesBean",recipesBean);
        super.onSaveInstanceState(outState);
    }
}
