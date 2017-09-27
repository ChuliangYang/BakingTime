package com.demo.cl.bakingtime.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.cl.bakingtime.R;
import com.demo.cl.bakingtime.data.RecipesBean;
import com.demo.cl.bakingtime.helper.DisplayHelper;
import com.demo.cl.bakingtime.helper.EventHelper;
import com.demo.cl.bakingtime.ui.StepDetailActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CL on 9/18/17.
 */

public class StepsAdapter extends RecyclerView.Adapter {
    private RecipesBean recipesBean;
    private Context context;

    public StepsAdapter(RecipesBean recipesBean, Context context) {
        this.recipesBean = recipesBean;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StepViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recipe_step, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        StepViewHolder stepViewHolder = (StepViewHolder) holder;
        if (position == 0) {
            stepViewHolder.vPlaceHolder.setVisibility(View.VISIBLE);
            stepViewHolder.setIsRecyclable(false);
        } else if (position == recipesBean.getSteps().size() - 1) {
            stepViewHolder.vPlaceHolder.setVisibility(View.GONE);
            stepViewHolder.setIsRecyclable(false);
            stepViewHolder.tvStepSum.setPadding(0, 0, 0, DisplayHelper.dip2px(context, 0));
            stepViewHolder.cl_step_item.setPadding(0, 0, 0, DisplayHelper.dip2px(context, 24));
            stepViewHolder.iv_line.setVisibility(View.GONE);
        } else {
            stepViewHolder.vPlaceHolder.setVisibility(View.GONE);
            stepViewHolder.setIsRecyclable(true);
        }
        stepViewHolder.tvStep.setText(String.valueOf(position + 1));
        stepViewHolder.tvStepTitle.setText(recipesBean.getSteps().get(position).getShortDescription());
        stepViewHolder.tvStepSum.setText(recipesBean.getSteps().get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return recipesBean != null && recipesBean.getSteps() != null ? recipesBean.getSteps().size() : 0;
    }


    public class StepViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_line)
        ImageView iv_line;
        @BindView(R.id.tv_step_title)
        TextView tvStepTitle;
        @BindView(R.id.tv_step_sum)
        TextView tvStepSum;
        @BindView(R.id.v_place_holder)
        View vPlaceHolder;
        @BindView(R.id.tv_step_num)
        TextView tvStep;
        @BindView(R.id.cl_step_item)
        ConstraintLayout cl_step_item;

        public StepViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(itemView -> {
                if (!context.getResources().getBoolean(R.bool.isTablet)) {
                    EventBus.getDefault().removeStickyEvent(EventHelper.StepsBeanMessage.class);
                    EventBus.getDefault().postSticky(EventHelper.create().buildStepsBeanMessage(getAdapterPosition(), recipesBean));
                    Intent intent = new Intent(context, StepDetailActivity.class);
                    context.startActivity(intent);
                } else {
                    EventBus.getDefault().removeStickyEvent(EventHelper.StepsBeanMessage.class);
                    EventBus.getDefault().postSticky(EventHelper.create().buildStepsBeanMessage(getAdapterPosition(), recipesBean).setRefreshFragment(true));
                }
            });
        }
    }
}
