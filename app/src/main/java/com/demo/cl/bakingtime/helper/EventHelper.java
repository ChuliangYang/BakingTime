package com.demo.cl.bakingtime.helper;

import com.demo.cl.bakingtime.data.RecipesBean;

import java.util.List;

/**
 * Created by CL on 9/16/17.
 */

public class EventHelper {
    private EventHelper() {
    }

    public static EventHelper create(){
        return new EventHelper();
    }

    public   RecipesBeanMessage buildRecipesBeanMessage(RecipesBean recipesBean){
        return new RecipesBeanMessage(recipesBean);
    }






    public  class RecipesBeanMessage{
        public RecipesBeanMessage(RecipesBean recipesBean) {
            this.recipesBean = recipesBean;
        }

        public RecipesBean getRecipesBean() {
            return recipesBean;
        }

        public void setRecipesBean(RecipesBean recipesBean) {
            this.recipesBean = recipesBean;
        }

        RecipesBean recipesBean;
    }
}
