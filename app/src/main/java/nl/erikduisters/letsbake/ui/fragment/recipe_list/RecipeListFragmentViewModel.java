package nl.erikduisters.letsbake.ui.fragment.recipe_list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import nl.erikduisters.letsbake.R;
import nl.erikduisters.letsbake.data.local.PreferenceManager;
import nl.erikduisters.letsbake.data.local.RecipeRepository;
import nl.erikduisters.letsbake.data.model.Recipe;
import nl.erikduisters.letsbake.ui.activity.recipe_detail.RecipeDetailActivity;
import timber.log.Timber;

/**
 * Created by Erik Duisters on 24-03-2018.
 */

@Singleton
public class RecipeListFragmentViewModel extends ViewModel {
    private final RecipeRepository recipeRepository;
    private final PreferenceManager preferenceManager;
    private final MutableLiveData<RecipeListFragmentViewState.RecipeViewState> recipeViewState;
    private final MutableLiveData<RecipeListFragmentViewState.StartActivityViewState> startActivityViewState;

    @Inject
    public RecipeListFragmentViewModel(RecipeRepository recipeRepository, PreferenceManager preferenceManager) {
        Timber.d("New RecipeListFragmentViewModel created");

        this.recipeRepository = recipeRepository;
        this.preferenceManager = preferenceManager;

        recipeViewState = new MutableLiveData<>();
        recipeViewState.setValue(RecipeListFragmentViewState.RecipeViewState.getLoadingViewState());

        startActivityViewState = new MutableLiveData<>();

        recipeRepository.getRecipes(new Callback());
    }

    LiveData<RecipeListFragmentViewState.RecipeViewState> getRecipeViewState() { return recipeViewState; }
    LiveData<RecipeListFragmentViewState.StartActivityViewState> getStartActivityViewState() { return startActivityViewState; }

    void onRecipeClicked(Recipe recipe) {
        startActivityViewState.setValue(new RecipeListFragmentViewState.StartActivityViewState(recipe.getId(), RecipeDetailActivity.class));
    }

    void onActivityStarted() {
        startActivityViewState.setValue(null);
    }

    private class Callback implements RecipeRepository.Callback<List<Recipe>> {

        @Override
        public void onResponse(@NonNull List<Recipe> response) {
            recipeViewState.setValue(RecipeListFragmentViewState.RecipeViewState.getSuccessViewState(response, R.string.no_recipes_available));
        }

        @Override
        public void onError(int error, @NonNull String errorArgument) {

        }
    }
}
