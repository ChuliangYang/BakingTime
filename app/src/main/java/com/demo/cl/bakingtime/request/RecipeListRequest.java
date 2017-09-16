package com.demo.cl.bakingtime.request;

import android.util.Log;

import com.demo.cl.bakingtime.Interface.OnRecipeListResponse;
import com.demo.cl.bakingtime.data.Constant;
import com.demo.cl.bakingtime.data.RecipesBean;
import com.demo.cl.bakingtime.helper.OkHttpHelper;
import com.demo.cl.bakingtime.ui.RecipesActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by CL on 9/14/17.
 */

public class RecipeListRequest {
    OnRecipeListResponse onRecipeListResponse;

    public RecipeListRequest(OnRecipeListResponse onRecipeListResponse) {
        this.onRecipeListResponse=onRecipeListResponse;
    }

    public void start(){
        OkHttpClient client = OkHttpHelper.BuildCustomOkHttpClient();

        Request request = new Request.Builder()
                    .url(Constant.NetWork.SERVER_URL)
                    .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                onRecipeListResponse.OnRecipeListResponseFailed();
            }

            @Override public void onResponse(Call call, Response response)  {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) {
                        Log.e("RecipeListRequest","Unexpected: " + response);
                        onRecipeListResponse.OnRecipeListResponseFailed();
                        return;
                    }
                    onRecipeListResponse.OnRecipeListResponseSuccess(new Gson().fromJson(responseBody.string(),new TypeToken<List<RecipesBean>>() {}.getType()));
                } catch (IOException e) {
                    e.printStackTrace();
                    onRecipeListResponse.OnRecipeListResponseFailed();
                }
            }
        });

    }
}
