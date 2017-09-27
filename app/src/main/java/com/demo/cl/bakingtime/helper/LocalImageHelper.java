package com.demo.cl.bakingtime.helper;

import com.demo.cl.bakingtime.R;

import java.util.HashMap;

/**
 * Created by CL on 9/14/17.
 */

public  class LocalImageHelper {
    public static HashMap imagePathMap;
    static {
        imagePathMap=new HashMap();
//        imagePathMap.put("Nutella Pie", R.drawable.nutella_pie);
        imagePathMap.put("Brownies", R.drawable.brownies);
        imagePathMap.put("Cheesecake", R.drawable.cheesecake);
//        imagePathMap.put("Yellow Cake", R.drawable.yellow_cake);
    }
}
