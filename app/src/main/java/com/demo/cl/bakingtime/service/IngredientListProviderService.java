package com.demo.cl.bakingtime.service;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.demo.cl.bakingtime.R;
import com.demo.cl.bakingtime.data.Constant;

import java.util.List;

import timber.log.Timber;

public class IngredientListProviderService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Timber.e("RemoteViewsFactory");
        return new IngredientListFactory(getApplicationContext(), intent);
    }

    class IngredientListFactory implements RemoteViewsService.RemoteViewsFactory {
        private Context context;
        private Intent intent;
        private List<String> ingredients;

        public IngredientListFactory(Context context, Intent intent) {
            this.context = context;
            this.intent = intent;
            ingredients = intent.getStringArrayListExtra(Constant.DataKey.INGREDIENT_LIST_KEY);
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            ingredients = intent.getStringArrayListExtra(Constant.DataKey.INGREDIENT_LIST_KEY);
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return ingredients != null ? ingredients.size() : 0;
        }

        @Override
        public RemoteViews getViewAt(int i) {
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.item_widget_ingredient);
            rv.setTextViewText(R.id.tv_ingredient_name, ingredients.get(i));
            return rv;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}


