package com.demo.cl.bakingtime.Interface;

import com.demo.cl.bakingtime.data.RecipesBean;

import java.util.List;

/**
 * Created by CL on 9/14/17.
 */

public interface OnRecipeListResponse {
    void OnRecipeListResponseSuccess(List<RecipesBean> recipesBeans);

    void OnRecipeListResponseFailed();
}
