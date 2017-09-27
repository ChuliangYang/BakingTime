package com.demo.cl.bakingtime.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.demo.cl.bakingtime.Interface.RecipeListModel;
import com.demo.cl.bakingtime.R;
import com.demo.cl.bakingtime.data.RecipesBean;
import com.demo.cl.bakingtime.helper.EventHelper;
import com.demo.cl.bakingtime.ui.RecipeDetailActivity;
import com.demo.cl.bakingtime.widget.DynamicHeightImageView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by CL on 9/14/17.
 */

public class RecipeListAdapter extends RecyclerView.Adapter {
    private final int WITH_PICTURE = 1;
    private final int WITHOUT_PICTURE = 2;
    private RecipeListModel recipeListModel;
    private String TAG = "RecipeListAdapter";
    private Context context;
    private List<RecipesBean> recipesBeans;

    public RecipeListAdapter(RecipeListModel recipeListModel, Context context) {
        this.recipeListModel = recipeListModel;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case WITH_PICTURE:
                return new RecipeWithPictureViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe_picture, null));
            case WITHOUT_PICTURE:
                return new RecipeWithoutPictureViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe_name, null));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecipeWithPictureViewHolder) {
            RecipeWithPictureViewHolder recipeWithPictureViewHolder = (RecipeWithPictureViewHolder) holder;
            recipeWithPictureViewHolder.tvRecipe.setText(recipeListModel.getRecipeName(position));
            if (recipeListModel.getPictureType(position) == RecipeListModel.LOCAL_PICTURE) {
                //noinspection ConstantConditions
                recipeWithPictureViewHolder.ivRecipe.setImageResource((Integer) recipeListModel.getRecipePicture(position));
            } else if (recipeListModel.getPictureType(position) == RecipeListModel.NETWORK_PICTURE) {
                Glide.with(context)
                        .load(recipeListModel.getRecipePicture(position))
                        .into(recipeWithPictureViewHolder.ivRecipe);
            } else {
                Log.e(TAG, "onBindViewHolder: " + "picture path is illegal");
            }
        } else if (holder instanceof RecipeWithoutPictureViewHolder) {
            RecipeWithoutPictureViewHolder recipeWithoutPictureViewHolder = (RecipeWithoutPictureViewHolder) holder;
            recipeWithoutPictureViewHolder.tvRecipeNoPicture.setText(recipeListModel.getRecipeName(position));
            Timber.e("no picture at %s", position);
        }
    }

    @Override
    public int getItemCount() {
        return recipeListModel != null ? recipeListModel.getRecipeCounts() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (recipeListModel.getRecipePicture(position) != null) {
            return WITH_PICTURE;
        } else {
            return WITHOUT_PICTURE;
        }
    }

    public List<RecipesBean> getRecipesBeans() {
        return recipesBeans;
    }

    public void setRecipesBeans(List<RecipesBean> recipesBeans) {
        this.recipesBeans = recipesBeans;
    }

    public class RecipeWithPictureViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_recipe)
        DynamicHeightImageView ivRecipe;
        @BindView(R.id.tv_recipe)
        TextView tvRecipe;

        public RecipeWithPictureViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(context, RecipeDetailActivity.class);
                EventBus.getDefault().removeStickyEvent(EventHelper.RecipesBeanMessage.class);
                EventBus.getDefault().postSticky(EventHelper.create().buildRecipesBeanMessage((RecipesBean) recipeListModel.get(getAdapterPosition())));
                context.startActivity(intent);
            });
        }
    }

    public class RecipeWithoutPictureViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_recipe_no_picture)
        TextView tvRecipeNoPicture;

        public RecipeWithoutPictureViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(context, RecipeDetailActivity.class);
                EventBus.getDefault().removeStickyEvent(EventHelper.RecipesBeanMessage.class);
                EventBus.getDefault().postSticky(EventHelper.create().buildRecipesBeanMessage((RecipesBean) recipeListModel.get(getAdapterPosition())));
                context.startActivity(intent);
            });
        }
    }

}
