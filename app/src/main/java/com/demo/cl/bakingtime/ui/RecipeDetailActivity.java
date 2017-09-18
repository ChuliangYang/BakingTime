package com.demo.cl.bakingtime.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.demo.cl.bakingtime.R;
import com.demo.cl.bakingtime.data.RecipesBean;
import com.demo.cl.bakingtime.helper.EventHelper;
import com.demo.cl.bakingtime.ui.fragment.RecipeDetailFragment;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by CL on 9/16/17.
 */

public class RecipeDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        RecipeDetailFragment recipeDetailFragment=new RecipeDetailFragment();
        fragmentTransaction.add(R.id.fl_recipe_detail,recipeDetailFragment);
        fragmentTransaction.commit();
    }
}
