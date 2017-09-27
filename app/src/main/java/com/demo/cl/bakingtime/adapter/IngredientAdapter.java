package com.demo.cl.bakingtime.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.Animatable2.AnimationCallback;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.cl.bakingtime.R;
import com.demo.cl.bakingtime.data.RecipesBean;
import com.demo.cl.bakingtime.helper.DisplayHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by CL on 9/16/17.
 */

public class IngredientAdapter extends RecyclerView.Adapter {

    private RecipesBean recipesBean;
    private Context context;

    public IngredientAdapter(RecipesBean recipesBean, Context context) {
        this.recipesBean = recipesBean;
        this.context = context;
    }

    @Override

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new IngredientViewHolder(LayoutInflater.from(context).inflate(R.layout.item_ingredient, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position==0) {
            holder.itemView.setPadding(0, DisplayHelper.dip2px(context,8),0,0);
        }else{
            holder.itemView.setPadding(0, 0,0,0);
        }
        String s=String.format("<html><font color='#000000'><small>%s</small></font>&nbsp;&nbsp;&nbsp;%s</html>",recipesBean.getIngredients().get(position).getQuantity()+recipesBean.getIngredients().get(position).getMeasure(),recipesBean.getIngredients().get(position).getIngredient());
        IngredientViewHolder ingredientViewHolder= (IngredientViewHolder) holder;
        ingredientViewHolder.tvIngredient.setText(Html.fromHtml(s));
//        ingredientViewHolder.tvIngredient.setText(Html.fromHtml(s)+" "+recipesBean.getIngredients().get(position).getIngredient());
        ingredientViewHolder.cb_ingredient_state.setChecked(recipesBean.getIngredients().get(position).getChecked());



    }

    @Override
    public int getItemCount() {
        return recipesBean != null ? recipesBean.getIngredients().size() : 0;
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_ingredient)
        ImageView ivIngredient;
        @BindView(R.id.tv_ingredient)
        TextView tvIngredient;
        @BindView(R.id.cb_ingredient_state)
        CheckBox cb_ingredient_state;
        @SuppressLint("NewApi")
        public IngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            cb_ingredient_state.setOnCheckedChangeListener((compoundButton, b) -> {
                if (b) {
                    cb_ingredient_state.setBackgroundResource(R.drawable.cross_to_tick_anim);
                    AnimatedVectorDrawable animatedVectorDrawable= (AnimatedVectorDrawable)(cb_ingredient_state.getBackground());
                    animatedVectorDrawable.start();
                } else {
                    cb_ingredient_state.setBackgroundResource(R.drawable.tick_to_cross_anim);
                    ((AnimatedVectorDrawable)(cb_ingredient_state.getBackground())).start();
                }
                recipesBean.getIngredients().get(getAdapterPosition()).setChecked(b);
            });

            itemView.setOnClickListener(view ->cb_ingredient_state.performClick() );
        }
    }
}
