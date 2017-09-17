package com.demo.cl.bakingtime.Interface;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;

/**
 * Created by CL on 9/14/17.
 */

public interface RecipeListModel {
    int NO_PICTURE=0;
    int LOCAL_PICTURE=1;
    int NETWORK_PICTURE=2;

    String getRecipeName(int position);
    int getRecipeCounts();
    @Nullable Object getRecipePicture(int position);
    int getPictureType(int position);
    Object get(int position);
}
