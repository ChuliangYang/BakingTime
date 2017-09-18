package com.demo.cl.bakingtime.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.cl.bakingtime.R;
import com.demo.cl.bakingtime.adapter.IngredientAdapter;
import com.demo.cl.bakingtime.data.RecipesBean;
import com.demo.cl.bakingtime.helper.EventHelper;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CL on 9/16/17.
 */

public class IngredientFragment extends Fragment {

    @BindView(R.id.rv_ingredient)
    RecyclerView rvIngredient;
    private RecipesBean recipesBean;

    public IngredientFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipesBean=EventBus.getDefault().getStickyEvent(EventHelper.RecipesBeanMessage.class).getRecipesBean();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.ingredient_page, container, false);
        ButterKnife.bind(this, contentView);
        rvIngredient.setHasFixedSize(true);
        rvIngredient.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

        IngredientAdapter ingredientAdapter=new IngredientAdapter(recipesBean,getContext());
        rvIngredient.setAdapter(ingredientAdapter);
        return contentView;
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
