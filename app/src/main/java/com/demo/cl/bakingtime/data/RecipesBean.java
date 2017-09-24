package com.demo.cl.bakingtime.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CL on 9/14/17.
 */

public class RecipesBean implements Parcelable {

    private String id;
    private String name;
    private String servings;
    private String image;
    private List<IngredientsBean> ingredients;
    private List<StepsBean> steps;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<IngredientsBean> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientsBean> ingredients) {
        this.ingredients = ingredients;
    }

    public List<StepsBean> getSteps() {
        return steps;
    }

    public void setSteps(List<StepsBean> steps) {
        this.steps = steps;
    }

    public static class IngredientsBean implements Parcelable {

        private String quantity;
        private String measure;
        private String ingredient;
        private Boolean checked=false;


        public Boolean getChecked() {
            return checked;
        }

        public void setChecked(Boolean checked) {
            this.checked = checked;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getMeasure() {
            return measure;
        }

        public void setMeasure(String measure) {
            this.measure = measure;
        }

        public String getIngredient() {
            return ingredient;
        }

        public void setIngredient(String ingredient) {
            this.ingredient = ingredient;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.quantity);
            dest.writeString(this.measure);
            dest.writeString(this.ingredient);
        }

        public IngredientsBean() {
        }

        protected IngredientsBean(Parcel in) {
            this.quantity = in.readString();
            this.measure = in.readString();
            this.ingredient = in.readString();
        }

        public static final Creator<IngredientsBean> CREATOR = new Creator<IngredientsBean>() {
            @Override
            public IngredientsBean createFromParcel(Parcel source) {
                return new IngredientsBean(source);
            }

            @Override
            public IngredientsBean[] newArray(int size) {
                return new IngredientsBean[size];
            }
        };
    }

    public static class StepsBean implements Parcelable {

        private String id;
        private String shortDescription;
        private String description;
        private String videoURL;
        private String thumbnailURL;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getShortDescription() {
            return shortDescription;
        }

        public void setShortDescription(String shortDescription) {
            this.shortDescription = shortDescription;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getVideoURL() {
            return videoURL;
        }

        public void setVideoURL(String videoURL) {
            this.videoURL = videoURL;
        }

        public String getThumbnailURL() {
            return thumbnailURL;
        }

        public void setThumbnailURL(String thumbnailURL) {
            this.thumbnailURL = thumbnailURL;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.shortDescription);
            dest.writeString(this.description);
            dest.writeString(this.videoURL);
            dest.writeString(this.thumbnailURL);
        }

        public StepsBean() {
        }

        protected StepsBean(Parcel in) {
            this.id = in.readString();
            this.shortDescription = in.readString();
            this.description = in.readString();
            this.videoURL = in.readString();
            this.thumbnailURL = in.readString();
        }

        public static final Creator<StepsBean> CREATOR = new Creator<StepsBean>() {
            @Override
            public StepsBean createFromParcel(Parcel source) {
                return new StepsBean(source);
            }

            @Override
            public StepsBean[] newArray(int size) {
                return new StepsBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.servings);
        dest.writeString(this.image);
        dest.writeList(this.ingredients);
        dest.writeList(this.steps);
    }

    public RecipesBean() {
    }

    protected RecipesBean(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.servings = in.readString();
        this.image = in.readString();
        this.ingredients = new ArrayList<IngredientsBean>();
        in.readList(this.ingredients, IngredientsBean.class.getClassLoader());
        this.steps = new ArrayList<StepsBean>();
        in.readList(this.steps, StepsBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<RecipesBean> CREATOR = new Parcelable.Creator<RecipesBean>() {
        @Override
        public RecipesBean createFromParcel(Parcel source) {
            return new RecipesBean(source);
        }

        @Override
        public RecipesBean[] newArray(int size) {
            return new RecipesBean[size];
        }
    };
}
