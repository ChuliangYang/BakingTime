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

    public   StepsBeanMessage buildStepsBeanMessage(int current_position, RecipesBean recipesBean){
        return new StepsBeanMessage(current_position,recipesBean);
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

    public class StepsBeanMessage{
        private int current_position;
        private RecipesBean recipesBean;

        public StepsBeanMessage(int current_position, RecipesBean recipesBean) {
            this.current_position = current_position;
            this.recipesBean = recipesBean;
        }

        public int getCurrent_position() {
            return current_position;
        }

        public void setCurrent_position(int current_position) {
            this.current_position = current_position;
        }

        public RecipesBean getRecipesBean() {
            return recipesBean;
        }

        public void setRecipesBean(RecipesBean recipesBean) {
            this.recipesBean = recipesBean;
        }
    }
}
