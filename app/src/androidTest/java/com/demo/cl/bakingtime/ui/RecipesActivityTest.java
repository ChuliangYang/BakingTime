package com.demo.cl.bakingtime.ui;


import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.v7.widget.RecyclerView;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

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

import static android.support.test.runner.lifecycle.Stage.RESUMED;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RecipesActivityTest {

    RecyclerView rv;

    List<RecipesBean> recipesBeanList;

    @Rule
    public ActivityTestRule<RecipesActivity> mActivityTestRule = new ActivityTestRule<>(RecipesActivity.class);

    @Test
    public void recipesActivityTest() {
        rv=mActivityTestRule.getActivity().findViewById(R.id.rv_recipe);
        onView(withText("Baking Time")).check(matches(isDisplayed()));
        for (int i = 0; i < rv.getAdapter().getItemCount(); i++) {
            onView(withId(R.id.rv_recipe)).perform(RecyclerViewActions.scrollToPosition(i)).check(matches(withSameRecipeAt(i)));
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

            onView(withContentDescription(R.string.navigation_content_describe)).perform(click());
        }


    }

    public  Matcher<View> withSameRecipeAt(int position) {
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                recipesBeanList =((RecipeListAdapter) (((RecyclerView) (item)).getAdapter())).getRecipesBeans();
                if(!((TextView)(rv.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.tv_recipe))).getText().toString().equals(recipesBeanList.get(position).getName())){
                    return false;
                }
                return true;
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }


    public  Matcher<View> withAdapterPosition(int position) {
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View item) {
                if (item.getParent() instanceof RecyclerView) {
                    if (item == ((RecyclerView) (item.getParent())).findViewHolderForAdapterPosition(position).itemView) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
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






