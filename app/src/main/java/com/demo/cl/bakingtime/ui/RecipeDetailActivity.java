package com.demo.cl.bakingtime.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.demo.cl.bakingtime.R;
import com.demo.cl.bakingtime.data.RecipesBean;
import com.demo.cl.bakingtime.helper.EventHelper;
import com.demo.cl.bakingtime.ui.fragment.RecipeDetailFragment;
import com.demo.cl.bakingtime.ui.fragment.StepDetailFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by CL on 9/16/17.
 */

public class RecipeDetailActivity extends AppCompatActivity {

    private Toolbar tb_recipe;
    private FrameLayout fl_list;
    private FrameLayout fl_detail;
    private RecipesBean recipesBean;
    private boolean isTablet;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whole_fragment);
        isTablet=getResources().getBoolean(R.bool.isTablet);
        if (isTablet) {
            if (savedInstanceState != null) {
                recipesBean= (RecipesBean) savedInstanceState.get("recipesBean");
            } else if (EventBus.getDefault().getStickyEvent(EventHelper.RecipesBeanMessage.class)!=null) {
                recipesBean = EventBus.getDefault().getStickyEvent(EventHelper.RecipesBeanMessage.class).getRecipesBean();
            }


            tb_recipe=findViewById(R.id.tb_recipe);
            fl_list=findViewById(R.id.fl_list);
            fl_detail=findViewById(R.id.fl_detail);
            tb_recipe.setTitle(recipesBean.getName());
            tb_recipe.setNavigationOnClickListener(view -> finish());

            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fl_list,new RecipeDetailFragment());
//            fragmentTransaction.replace(R.id.fl_detail,new StepDetailFragment());
            fragmentTransaction.commit();


        } else {
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            RecipeDetailFragment recipeDetailFragment=new RecipeDetailFragment();
            fragmentTransaction.replace(R.id.fl_content,recipeDetailFragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putParcelable("recipesBean",recipesBean);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isTablet) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isTablet) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventHelper.StepsBeanMessage event) {
        if (event.refreshFragment()) {
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fl_detail,new StepDetailFragment());
            fragmentTransaction.commit();
        }
    };
}
