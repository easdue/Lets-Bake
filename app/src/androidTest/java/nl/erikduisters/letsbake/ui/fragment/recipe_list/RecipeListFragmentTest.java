package nl.erikduisters.letsbake.ui.fragment.recipe_list;

import android.app.Activity;
import android.app.Instrumentation;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Provider;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.DispatchingAndroidInjector_Factory;
import nl.erikduisters.letsbake.R;
import nl.erikduisters.letsbake.RecyclerViewMatcher;
import nl.erikduisters.letsbake.TestApplication;
import nl.erikduisters.letsbake.ViewModelUtil;
import nl.erikduisters.letsbake.data.model.Recipe;
import nl.erikduisters.letsbake.ui.activity.main_activity.MainActivity;
import nl.erikduisters.letsbake.ui.activity.main_activity.MainActivityViewModel;
import nl.erikduisters.letsbake.ui.activity.main_activity.MainActivityViewState;
import nl.erikduisters.letsbake.ui.activity.recipe_detail.RecipeDetailActivity;
import nl.erikduisters.letsbake.ui.fragment.recipe_list.RecipeListFragmentViewState.StartActivityViewState;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.anyIntent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Erik Duisters on 09-04-2018.
 */

@RunWith(AndroidJUnit4.class)
public class RecipeListFragmentTest {
    @Rule
    public IntentsTestRule<MainActivity> activityRule =
            new IntentsTestRule<MainActivity>(MainActivity.class, true, false);

    private MainActivityViewModel mainActivityViewModel;
    private final MutableLiveData<MainActivityViewState> mainActivityViewState = new MutableLiveData<>();

    private RecipeListFragmentViewModel recipeListFragmentViewModel;
    private final MutableLiveData<RecipeListFragmentViewState.RecipeViewState> recipeViewState = new MutableLiveData<>();
    private final MutableLiveData<StartActivityViewState> startActivityViewState = new MutableLiveData<>();

    private DispatchingAndroidInjector<Fragment> createMainActivityDispatchingAndroidInjector() {
        AndroidInjector<Fragment> injector = new AndroidInjector<Fragment>() {
            @Override
            public void inject(Fragment instance) {
                ((RecipeListFragment) instance).viewModelFactory = ViewModelUtil.createFor(recipeListFragmentViewModel);
            }
        };

        AndroidInjector.Factory<Fragment> factory = new AndroidInjector.Factory<Fragment>() {
            @Override
            public AndroidInjector<Fragment> create(Fragment instance) {
                return injector;
            }
        };

        Map<Class<? extends Fragment>, Provider<AndroidInjector.Factory<? extends Fragment>>> map = new HashMap<>();
        map.put(RecipeListFragment.class, new Provider<AndroidInjector.Factory<? extends Fragment>>() {
            @Override
            public AndroidInjector.Factory<? extends Fragment> get() {
                return factory;
            }
        });

        return DispatchingAndroidInjector_Factory.newDispatchingAndroidInjector(map);
    }

    @Before
    public void setUp() throws Exception {
        mainActivityViewModel = mock(MainActivityViewModel.class);
        recipeListFragmentViewModel = mock(RecipeListFragmentViewModel.class);

        when(mainActivityViewModel.getViewState()).thenReturn(mainActivityViewState);
        when(recipeListFragmentViewModel.getRecipeViewState()).thenReturn(recipeViewState);
        when(recipeListFragmentViewModel.getStartActivityViewState()).thenReturn(startActivityViewState);

        TestApplication.activityAndroidInjector = new AndroidInjector<Activity>() {
            @Override
            public void inject(Activity instance) {
                MainActivity activity = (MainActivity) instance;

                activity.viewModelFactory = ViewModelUtil.createFor(mainActivityViewModel);
                activity.dispatchingAndroidInjector = createMainActivityDispatchingAndroidInjector();
            }
        };

        activityRule.launchActivity(new Intent());

        intending(anyIntent()).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @After
    public void tearDown() throws Exception {
        TestApplication.activityAndroidInjector = null;
    }

    @Test
    public void loadingViewStateIsCorrectlyRendered() {
        RecipeListFragmentViewState.RecipeViewState viewState = RecipeListFragmentViewState.RecipeViewState.getLoadingViewState();

        recipeViewState.postValue(viewState);

        onView(withId(R.id.progressBar)).check(matches(isDisplayed()));
        onView(withId(R.id.textView)).check(matches(isDisplayed()));
        onView(withId(R.id.textView)).check(matches(withText(viewState.loadingMessage)));
    }

    @Test
    public void errorViewStateIsCorrectlyRendered() {
        RecipeListFragmentViewState.RecipeViewState viewState = RecipeListFragmentViewState.RecipeViewState.getErrorViewState(R.string.recipe_api_call_failure, "ERROR");

        recipeViewState.postValue(viewState);

        onView(withId(R.id.progressBar)).check(matches(not(isDisplayed())));
        onView(withId(R.id.textView)).check(matches(isDisplayed()));
        onView(withId(R.id.textView)).check(matches(withText(activityRule.getActivity().getString(viewState.errorLabel, viewState.errorArgument))));
    }

    @Test
    public void successViewStateWithoutRecipesIsCorrectlyRendered() {
        RecipeListFragmentViewState.RecipeViewState viewState = RecipeListFragmentViewState.RecipeViewState.getSuccessViewState(new ArrayList<>(), R.string.no_recipes_available);

        recipeViewState.postValue(viewState);

        onView(withId(R.id.progressBar)).check(matches(not(isDisplayed())));
        onView(withId(R.id.textView)).check(matches(isDisplayed()));
        onView(withId(R.id.textView)).check(matches(withText(viewState.emptyRecipeListMessage)));
        onView(recyclerViewMatcher().atPosition(0)).check(doesNotExist());
    }

    @Test
    public void successViewStateWithRecipesIsCorrectlyRendered() {
        RecipeListFragmentViewState.RecipeViewState viewState = RecipeListFragmentViewState.RecipeViewState.getSuccessViewState(getDummyRecipes(), 0);

        recipeViewState.postValue(viewState);

        onView(withId(R.id.progressBar)).check(matches(not(isDisplayed())));
        onView(withId(R.id.textView)).check(matches(not(isDisplayed())));

        for (int i=0; i < viewState.recipeList.size(); i++) {
            Recipe recipe = viewState.recipeList.get(i);

            onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.scrollToPosition(i));
            onView(recyclerViewMatcher().atPositionOnView(i, R.id.recipeName)).check(matches(withText(recipe.getName())));
            onView(recyclerViewMatcher().atPositionOnView(i, R.id.serves)).check(matches(withText(String.valueOf(recipe.getServings()))));
        }
    }

    @Test
    public void clickOnRecyclerViewItemIsHandledCorrectly() {
        RecipeListFragmentViewState.RecipeViewState viewState = RecipeListFragmentViewState.RecipeViewState.getSuccessViewState(getDummyRecipes(), 0);

        recipeViewState.postValue(viewState);

        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(5, click()));

        verify(recipeListFragmentViewModel).onRecipeClicked(viewState.recipeList.get(5));
    }

    @Test
    public void startActivityViewStateIsHandledCorrectly() {
        StartActivityViewState viewState = new StartActivityViewState(5, RecipeDetailActivity.class);
        startActivityViewState.postValue(viewState);

        intended(allOf(
                hasComponent(RecipeDetailActivity.class.getName()),
                hasExtra(RecipeDetailActivity.KEY_RECIPE_ID, 5)));
    }

    private List<Recipe> getDummyRecipes() {
        List<Recipe> recipes = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Recipe recipe = new Recipe();

            recipe.setId(i);
            recipe.setName(String.format("Recipe#%d", i+1));
            recipe.setServings(i+1);
            recipe.setImage("");

            recipes.add(recipe);
        }

        return recipes;
    }

    @NonNull
    private RecyclerViewMatcher recyclerViewMatcher() {
        return new RecyclerViewMatcher(R.id.recyclerView);
    }
}