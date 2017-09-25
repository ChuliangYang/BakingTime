package com.demo.cl.bakingtime.ui.fragment;

import android.content.Intent;
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
import com.demo.cl.bakingtime.data.Constant;
import com.demo.cl.bakingtime.data.RecipesBean;
import com.demo.cl.bakingtime.helper.EventHelper;
import com.demo.cl.bakingtime.helper.SharedPreferencesHelper;
import com.demo.cl.bakingtime.service.WidgetService;

import org.greenrobot.eventbus.EventBus;

import java.sql.Time;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by CL on 9/16/17.
 */

public class IngredientFragment extends Fragment {

    @BindView(R.id.rv_ingredient)
    RecyclerView rvIngredient;
    private RecipesBean recipesBean;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.w("fragment created");
        if (savedInstanceState != null) {
            recipesBean= (RecipesBean) savedInstanceState.get("recipesBean");
        } else if (EventBus.getDefault().getStickyEvent(EventHelper.RecipesBeanMessage.class)!=null) {
            recipesBean = EventBus.getDefault().getStickyEvent(EventHelper.RecipesBeanMessage.class).getRecipesBean();
        }

        if (recipesBean != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("recipesBean", recipesBean);
            setArguments(bundle);
        } else if (getArguments()!=null&&getArguments().get("recipesBean") != null) {
            recipesBean = (RecipesBean) getArguments().get("recipesBean");
        } else {
            recipesBean=new RecipesBean();
        }


        Timber.w("saveingredientList start");
        Single.create((SingleOnSubscribe<Boolean>) e -> {
            if (recipesBean!=null) {
                try {
                    SharedPreferencesHelper.saveObject(getContext().getApplicationContext(), Constant.DataKey.SHARED_PREFERENCES_NAME,Constant.DataKey.INGREDIENT_LIST_KEY,recipesBean.getIngredients());
                    SharedPreferencesHelper.saveKeyValue(getContext().getApplicationContext(), Constant.DataKey.SHARED_PREFERENCES_NAME,Constant.DataKey.RECIPE_NAME_KEY,recipesBean.getName());
                    Timber.w("saveingredientList success");
                    e.onSuccess(true);
                } catch (Exception e1) {
                    e.onError(e1);
                }
        }
        }).subscribeOn(Schedulers.io()).subscribe(aBoolean ->
                getContext().startService(new Intent(getContext(),WidgetService.class))
        );

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
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("recipesBean",recipesBean);
        super.onSaveInstanceState(outState);
    }
}
