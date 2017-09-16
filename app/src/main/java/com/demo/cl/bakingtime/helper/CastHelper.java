package com.demo.cl.bakingtime.helper;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.demo.cl.bakingtime.Interface.RecipeListModel;
import com.demo.cl.bakingtime.data.RecipesBean;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by CL on 9/14/17.
 */

public class CastHelper {

    public static RecipeListModel RecipesBeanToRecipeListModel(final List<RecipesBean> recipesBeans){
            return new RecipeListModel() {
                @Override
                public String getRecipeName(int position) {
                    return recipesBeans.get(position).getName();
                }

                @Override
                public int getRecipeCounts() {
                    return recipesBeans.size();
                }

                @Override
                public @Nullable Object getRecipePicture(int position) {
                    if (!TextUtils.isEmpty(recipesBeans.get(position).getImage())) {
                            return recipesBeans.get(position).getImage();
                    } else if (LocalImageHelper.imagePathMap.containsKey(recipesBeans.get(position).getName())) {
                            return LocalImageHelper.imagePathMap.get(recipesBeans.get(position).getName());
                    } else {
                        return null;
                    }
                }

                @Override
                public int getPictureType(int position) {
                    if (!TextUtils.isEmpty(recipesBeans.get(position).getImage())) {
                        return NETWORK_PICTURE;
                    } else if (LocalImageHelper.imagePathMap.containsKey(recipesBeans.get(position).getName())) {
                        return LOCAL_PICTURE;
                    } else {
                        return NO_PICTURE;
                    }
                }
            };
    }
}
