package com.demo.cl.bakingtime.helper;

import android.os.Parcel;
import android.os.Parcelable;

import com.demo.cl.bakingtime.data.RecipesBean;
import com.demo.cl.bakingtime.Interface.OnScroll;

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

    public   ScrollMessage buildScrollMessage(OnScroll OnScroll){
        return new ScrollMessage(OnScroll);
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

    public class StepsBeanMessage implements Parcelable {
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.current_position);
            dest.writeParcelable(this.recipesBean, flags);
        }

        protected StepsBeanMessage(Parcel in) {
            this.current_position = in.readInt();
            this.recipesBean = in.readParcelable(RecipesBean.class.getClassLoader());
        }

        public  final Parcelable.Creator<StepsBeanMessage> CREATOR = new Parcelable.Creator<StepsBeanMessage>() {
            @Override
            public StepsBeanMessage createFromParcel(Parcel source) {
                return new StepsBeanMessage(source);
            }

            @Override
            public StepsBeanMessage[] newArray(int size) {
                return new StepsBeanMessage[size];
            }
        };
    }

    public class ScrollMessage{
        public ScrollMessage(OnScroll OnScroll) {
            this.OnScroll = OnScroll;
        }

        public OnScroll getOnScroll() {
            return OnScroll;
        }

        public void setOnScroll(OnScroll OnScroll) {
            this.OnScroll = OnScroll;
        }

        private OnScroll OnScroll;
        private int blank;
    }
}
