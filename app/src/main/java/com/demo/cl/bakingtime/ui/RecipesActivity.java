package com.demo.cl.bakingtime.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.demo.cl.bakingtime.Interface.OnRecipeListResponse;
import com.demo.cl.bakingtime.Interface.RecipeListModel;
import com.demo.cl.bakingtime.R;
import com.demo.cl.bakingtime.adapter.RecipesAdapter;
import com.demo.cl.bakingtime.data.RecipesBean;
import com.demo.cl.bakingtime.databinding.ActivityRecipeListBinding;
import com.demo.cl.bakingtime.helper.CastHelper;
import com.demo.cl.bakingtime.request.RecipeListRequest;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RecipesActivity extends AppCompatActivity implements OnRecipeListResponse{
     ActivityRecipeListBinding viewBinding;
     RecyclerView rvRecipe;
     RecipesAdapter recipesAdapter;
     RecipeListModel recipeListModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding=DataBindingUtil.setContentView(this,R.layout.activity_recipe_list);
        rvRecipe=viewBinding.rvRecipe;

        new RecipeListRequest(this).start();
        // TODO: 9/16/17 没有图片的情况下itemview需要保持一样的尺寸
        rvRecipe.setLayoutManager(new GridLayoutManager(this,getResources().getInteger(R.integer.GridNumber)));
        rvRecipe.setHasFixedSize(true);
    }



    @Override
    public void OnRecipeListResponseSuccess(List<RecipesBean> recipesBeans) {
        Single.create((SingleOnSubscribe<RecipesAdapter>) e -> {
            recipeListModel=CastHelper.RecipesBeanToRecipeListModel(recipesBeans);
            recipesAdapter=new RecipesAdapter(recipeListModel,this);
            e.onSuccess(recipesAdapter);
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(recipesAdapter1 -> rvRecipe.setAdapter(recipesAdapter));
    }

    @Override
    public void OnRecipeListResponseFailed() {

    }
}
