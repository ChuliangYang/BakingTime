package com.demo.cl.bakingtime.ui;


import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.v7.widget.RecyclerView;
import android.test.suitebuilder.annotation.LargeTest;
import android.text.TextUtils;
import android.view.View;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;

import com.demo.cl.bakingtime.R;
import com.demo.cl.bakingtime.adapter.RecipeListAdapter;
import com.demo.cl.bakingtime.data.RecipesBean;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import static android.support.test.runner.lifecycle.Stage.RESUMED;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MyApplicationTest {

    RecyclerView rv;

    List<RecipesBean> recipesBeanList;

    @Rule
    public ActivityTestRule<RecipesActivity> mActivityTestRule = new ActivityTestRule<>(RecipesActivity.class);

    @Test
    public void recipesActivityTest() {
        rv=mActivityTestRule.getActivity().findViewById(R.id.rv_recipe);
        mActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        recipesBeanList =((RecipeListAdapter) (rv.getAdapter())).getRecipesBeans();
        onView(withText("Baking Time")).check(matches(isDisplayed()));
        for (int i = 0; i < rv.getAdapter().getItemCount(); i++) {
            onView(withId(R.id.rv_recipe)).perform(RecyclerViewActions.scrollToPosition(i));
            onView(allOf(withParent(withId(R.id.rv_recipe)),withAdapterPosition(i))).check(matches(hasDescendant(withText(recipesBeanList.get(i).getName()))));
        }
        for (int position = 0; position < recipesBeanList.size(); position++) {
            onView(withId(R.id.rv_recipe)).perform(RecyclerViewActions.actionOnItemAtPosition(position,click()));

            //RecipeDetailActivity
            RecipeDetailActivity recipeDetailActivity= (RecipeDetailActivity) getActivityInstance();
            RecipesBean recipesBean=recipesBeanList.get(position);
            onView(withText(recipesBean.getName())).check(matches(isDisplayed()));
            onView(allOf(withText("ingredient"),isDescendantOfA(withId(R.id.tl_recipe)))).check(matches(isDisplayed()));
            onView(allOf(withText("steps"),isDescendantOfA(withId(R.id.tl_recipe)))).check(matches(isDisplayed()));

            onView(withId(R.id.rv_ingredient)).check(matches(isDisplayed()));

            for (int i = 0; i <recipesBean.getIngredients().size() ; i++) {
                onView(withId(R.id.rv_ingredient)).perform(RecyclerViewActions.scrollToPosition(i));
                onView(withId(R.id.rv_ingredient)).perform(RecyclerViewActions.actionOnItemAtPosition(i,click()));
                onView(allOf(withParent(withId(R.id.rv_ingredient)),withAdapterPosition(i))).check(
                        matches(allOf(hasDescendant(withText(recipesBean.getIngredients().get(i).getIngredient())),
                                hasDescendant(allOf(withId(R.id.cb_ingredient_state),isChecked()))
                        )));

                onView(withId(R.id.rv_ingredient)).perform(RecyclerViewActions.actionOnItemAtPosition(i,click()));

                onView(allOf(withParent(withId(R.id.rv_ingredient)),withAdapterPosition(i))).check(
                        matches(hasDescendant(allOf(withId(R.id.cb_ingredient_state),not(isChecked())))));
            }


            List<RecipesBean.StepsBean> stepBeanList=recipesBeanList.get(position).getSteps();
            onView(allOf(withText("steps"),isDescendantOfA(withId(R.id.tl_recipe)))).perform(click());
            onView(withId(R.id.rv_steps)).check(matches(isDisplayed()));


            for (int i = 0; i < stepBeanList.size(); i++) {
                onView(withId(R.id.rv_steps)).perform(RecyclerViewActions.scrollToPosition(i));
                onView(allOf(withParent(withId(R.id.rv_steps)),withAdapterPosition(i))).check(
                        matches(allOf(hasDescendant(withText(stepBeanList.get(i).getDescription())),
                                hasDescendant(withText(stepBeanList.get(i).getShortDescription())),hasDescendant(withText(String.valueOf(i+1)))
                        )));
            }


            for (int k = 0; k < recipesBeanList.get(position).getSteps().size(); k++) {
                onView(withId(R.id.rv_steps)).perform(RecyclerViewActions.actionOnItemAtPosition(k,click()));
                RecipesBean.StepsBean stepsBean=recipesBeanList.get(position).getSteps().get(k);


                if (!TextUtils.isEmpty(stepsBean.getVideoURL())) {
                    onView(allOf(withId(R.id.player_view),withParent(isDisplayed()))).check(matches(isDisplayed()));
                } else {
                    onView(allOf(withId(R.id.player_view),withParent(isDisplayed()))).check(matches(not(isDisplayed())));
                }

                onView(allOf(withId(R.id.tv_describe),withParent(isDisplayed()))).check(matches(withText(stepsBean.getDescription())));
                onView(withId(R.id.rl_navigate)).check(matches(isDisplayed()));

                int swipeLeftLimit;
                int swipeRightLimit;
                int currentPosition=k;
                int random= new Random().nextInt(2);
                if (random == 0) {
                    //first swipe right
                    swipeRightLimit=currentPosition;
                    swipeLeftLimit=recipesBeanList.get(position).getSteps().size()-1;

                    for (int i = 0; i <swipeRightLimit ; i++) {
                        if (new Random().nextInt(2) == 0) {
                            onView(withId(R.id.vp_step_detail)).perform(swipeRight());
                        } else {
                            onView(withId(R.id.iv_previous)).perform(click());
                        }

                        currentPosition-=1;

                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (!TextUtils.isEmpty(recipesBeanList.get(position).getSteps().get(currentPosition).getVideoURL())) {
                            onView(allOf(withId(R.id.player_view),withParent(isDisplayed()))).check(matches(isDisplayed()));
                        } else {
                            onView(allOf(withId(R.id.player_view),withParent(isDisplayed()))).check(matches(not(isDisplayed())));
                        }

                        onView(allOf(withId(R.id.tv_describe),withParent(isDisplayed()))).check(matches(withText(recipesBeanList.get(position).getSteps().get(currentPosition).getDescription())));
                        onView(withId(R.id.rl_navigate)).check(matches(isDisplayed()));


                    }

                    for (int i = 0; i <swipeLeftLimit ; i++) {
                        if (new Random().nextInt(2) == 0) {
                            onView(withId(R.id.vp_step_detail)).perform(swipeLeft());
                        } else {
                            onView(withId(R.id.iv_next)).perform(click());
                        }
                        currentPosition+=1;

                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (!TextUtils.isEmpty(recipesBeanList.get(position).getSteps().get(currentPosition).getVideoURL())) {
                            onView(allOf(withId(R.id.player_view),withParent(isDisplayed()))).check(matches(isDisplayed()));
                        } else {
                            onView(allOf(withId(R.id.player_view),withParent(isDisplayed()))).check(matches(not(isDisplayed())));
                        }

                        onView(allOf(withId(R.id.tv_describe),withParent(isDisplayed()))).check(matches(withText(recipesBeanList.get(position).getSteps().get(currentPosition).getDescription())));
                        onView(withId(R.id.rl_navigate)).check(matches(isDisplayed()));

                    }

                } else {
                    //first swipe left
                    swipeLeftLimit=recipesBeanList.get(position).getSteps().size()-1-currentPosition;
                    swipeRightLimit=recipesBeanList.get(position).getSteps().size()-1;
                    for (int i = 0; i <swipeLeftLimit ; i++) {
                        if (new Random().nextInt(2) == 0) {
                            onView(withId(R.id.vp_step_detail)).perform(swipeLeft());
                        } else {
                            onView(withId(R.id.iv_next)).perform(click());
                        }
                        currentPosition+=1;

                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (!TextUtils.isEmpty(recipesBeanList.get(position).getSteps().get(currentPosition).getVideoURL())) {
                            onView(allOf(withId(R.id.player_view),withParent(isDisplayed()))).check(matches(isDisplayed()));
                        } else {
                            onView(allOf(withId(R.id.player_view),withParent(isDisplayed()))).check(matches(not(isDisplayed())));
                        }

                        onView(allOf(withId(R.id.tv_describe),withParent(isDisplayed()))).check(matches(withText(recipesBeanList.get(position).getSteps().get(currentPosition).getDescription())));
                        onView(withId(R.id.rl_navigate)).check(matches(isDisplayed()));
                    }

                    for (int i = 0; i <swipeRightLimit ; i++) {
                        if (new Random().nextInt(2) == 0) {
                            onView(withId(R.id.vp_step_detail)).perform(swipeRight());
                        } else {
                            onView(withId(R.id.iv_previous)).perform(click());
                        }

                        currentPosition-=1;

                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (!TextUtils.isEmpty(recipesBeanList.get(position).getSteps().get(currentPosition).getVideoURL())) {
                            onView(allOf(withId(R.id.player_view),withParent(isDisplayed()))).check(matches(isDisplayed()));
                        } else {
                            onView(allOf(withId(R.id.player_view),withParent(isDisplayed()))).check(matches(not(isDisplayed())));
                        }

                        onView(allOf(withId(R.id.tv_describe),withParent(isDisplayed()))).check(matches(withText(recipesBeanList.get(position).getSteps().get(currentPosition).getDescription())));
                        onView(withId(R.id.rl_navigate)).check(matches(isDisplayed()));

                    }
                }

                onView(withContentDescription(R.string.navigation_content_describe)).perform(click());

            }

            onView(withContentDescription(R.string.navigation_content_describe)).perform(click());
        }
    }

    public  Matcher<View> withAdapterPosition(int position) {
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                if (item.getParent() instanceof RecyclerView) {
                    if (item == ((RecyclerView) (item.getParent())).findViewHolderForAdapterPosition(position).itemView) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }


    public Activity getActivityInstance() {
        final Activity[] activity = new Activity[1];
        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable( ) {
            public void run() {
                Activity currentActivity = null;
                Collection resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(RESUMED);
                if (resumedActivities.iterator().hasNext()){
                    currentActivity = (Activity) resumedActivities.iterator().next();
                    activity[0] = currentActivity;
                }
            }
        });

        return activity[0];
    }

}






