package com.demo.cl.bakingtime.service;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.text.format.DateUtils;
import android.widget.DatePicker;
import android.widget.RemoteViews;

import com.demo.cl.bakingtime.NewAppWidget;
import com.demo.cl.bakingtime.R;
import com.demo.cl.bakingtime.data.Constant;
import com.demo.cl.bakingtime.data.RecipesBean;
import com.demo.cl.bakingtime.helper.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import timber.log.Timber;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 *
 */
public class WidgetService extends IntentService {
    List<RecipesBean.IngredientsBean> ingredientList;
    String recipeName;

    public WidgetService() {
        super("WidgetService");
    }

    @Override
    protected void onHandleIntent(Intent mintent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, NewAppWidget.class));
            List<?> ingredients= SharedPreferencesHelper.getObject(getApplicationContext(), Constant.DataKey.SHARED_PREFERENCES_NAME,Constant.DataKey.INGREDIENT_LIST_KEY);
            try {
                ingredientList = (List<RecipesBean.IngredientsBean>)ingredients;
                Timber.w("read ingredientList success");
            }catch (Exception e){
                e.printStackTrace();
            }

            recipeName= (String) SharedPreferencesHelper.getValueByKey(getApplicationContext(),Constant.DataKey.SHARED_PREFERENCES_NAME,Constant.DataKey.RECIPE_NAME_KEY,"");
            for (int appWidgetId:appWidgetIds) {
                RemoteViews views = new RemoteViews(getApplicationContext().getPackageName(), R.layout.widget_ingredient_list);

                Intent intent = new Intent(getApplicationContext(), IngredientListProviderService.class);
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
//                intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
                intent.setData(Uri.fromParts("content", String.valueOf(appWidgetId+ new Date().getTime()), null));
                ArrayList<String> ingredientsName =new ArrayList();
                for (RecipesBean.IngredientsBean ingredientsBean:
                        ingredientList) {
                    ingredientsName.add(ingredientsBean.getIngredient());
                }
                intent.putStringArrayListExtra(Constant.DataKey.INGREDIENT_LIST_KEY,ingredientsName);
                views.setRemoteAdapter(R.id.lv_ingredient_list, intent);
                views.setTextViewText(R.id.tv_recipe_name,recipeName);

                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,R.id.lv_ingredient_list);
                appWidgetManager.updateAppWidget(appWidgetId,views);
                Timber.e(" appWidgetManager.updateAppWidget(appWidgetId,views);");
            }
    }

}
