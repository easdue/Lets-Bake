package nl.erikduisters.letsbake.ui.fragment.recipe_detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import nl.erikduisters.letsbake.R;
import nl.erikduisters.letsbake.data.local.PreferenceManager;
import nl.erikduisters.letsbake.data.local.RecipeRepository;
import nl.erikduisters.letsbake.data.model.Recipe;
import nl.erikduisters.letsbake.data.model.Step;
import nl.erikduisters.letsbake.ui.activity.recipe_step_detail.RecipeStepDetailActivity;
import nl.erikduisters.letsbake.ui.fragment.recipe_detail.RecipeDetailFragmentViewState.RecipeDetailViewState;
import nl.erikduisters.letsbake.ui.fragment.recipe_detail.RecipeDetailFragmentViewState.StartActivityViewState;
import nl.erikduisters.letsbake.ui.fragment.recipe_detail.RecipeDetailFragmentViewState.UpdateWidgetViewState;
import nl.erikduisters.letsbake.util.MyMenuItem;
import timber.log.Timber;

/**
 * Created by Erik Duisters on 24-03-2018.
 */

@Singleton
public class RecipeDetailFragmentViewModel extends ViewModel {
    private final RecipeRepository recipeRepository;
    private final PreferenceManager preferenceManager;

    private final MutableLiveData<RecipeDetailViewState> recipeDetailViewState;
    private final MutableLiveData<StartActivityViewState> startActivityViewState;
    private final MutableLiveData<UpdateWidgetViewState> updateWidgetViewStateMutableLiveData;
    private final List<MyMenuItem> optionsMenu;
    private Recipe recipe;

    @Inject
    RecipeDetailFragmentViewModel(RecipeRepository recipeRepository, PreferenceManager preferenceManager) {
        Timber.d("New MovieDetailFragmentViewModel created");

        this.recipeRepository = recipeRepository;
        this.preferenceManager = preferenceManager;

        optionsMenu = new ArrayList<>();
        optionsMenu.add(new MyMenuItem(R.id.showInWidget, true, false));

        recipeDetailViewState = new MutableLiveData<>();
        recipeDetailViewState.setValue(RecipeDetailViewState.getLoadingState(optionsMenu));

        startActivityViewState = new MutableLiveData<>();
        updateWidgetViewStateMutableLiveData = new MutableLiveData<>();
    }

    LiveData<RecipeDetailViewState> getRecipeDetailViewState() { return recipeDetailViewState; }
    LiveData<StartActivityViewState> getStartActivityViewState() { return startActivityViewState; }
    LiveData<UpdateWidgetViewState> getUpdateWidgetViewState() { return updateWidgetViewStateMutableLiveData; }

    void setRecipeId(int recipeId) {
        RecipeDetailViewState viewState = recipeDetailViewState.getValue();

        if (viewState != null && viewState.recipe != null && viewState.recipe.getId() == recipeId) {
            return;
        }

        if (recipeId == RecipeRepository.INVALID_RECIPE_ID) {
            optionsMenu.get(0).visible = false;
            recipeDetailViewState.setValue(RecipeDetailViewState.getErrorState(R.string.recipe_id_invalid, "", optionsMenu));
        } else {
            recipeRepository.getRecipe(recipeId, new RecipeRepository.Callback<Recipe>() {
                @Override
                public void onResponse(@NonNull Recipe response) {
                    recipe = response;
                    optionsMenu.get(0).visible = true;
                    recipeDetailViewState.setValue(RecipeDetailViewState.getSuccessState(response, optionsMenu));
                }

                @Override
                public void onError(int error, @NonNull String errorArgument) {
                    optionsMenu.get(0).visible = false;
                    recipeDetailViewState.setValue(RecipeDetailViewState.getErrorState(error, errorArgument, optionsMenu));
                }
            });
        }
    }

    void onStepClicked(Step step) {
        RecipeDetailViewState viewState = recipeDetailViewState.getValue();

        if (viewState == null) {
            return;
        }

        Recipe recipe = viewState.recipe;

        startActivityViewState.setValue(new StartActivityViewState(recipe.getId(), step.getId(), RecipeStepDetailActivity.class));
    }

    void onActivityStarted() {
        startActivityViewState.setValue(null);
    }

    void onMenuItemClicked(@IdRes int menuId) {
        if (menuId == R.id.showInWidget) {
            preferenceManager.setSelectedWidgetRecipeID(recipe.getId());

            updateWidgetViewStateMutableLiveData.setValue(new UpdateWidgetViewState(recipe.getId()));
        }
    }

    void widgetUpdated() {
        updateWidgetViewStateMutableLiveData.setValue(null);
    }
}
