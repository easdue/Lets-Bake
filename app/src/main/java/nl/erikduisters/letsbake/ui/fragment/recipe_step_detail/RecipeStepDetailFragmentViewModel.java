package nl.erikduisters.letsbake.ui.fragment.recipe_step_detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import javax.inject.Inject;
import javax.inject.Singleton;

import nl.erikduisters.letsbake.data.local.RecipeRepository;
import nl.erikduisters.letsbake.data.model.Recipe;
import nl.erikduisters.letsbake.data.model.Status;
import nl.erikduisters.letsbake.ui.fragment.recipe_step_detail.RecipeStepDetailFragmentViewState.RecipeStepDetailViewState;

/**
 * Created by Erik Duisters on 24-03-2018.
 */

@Singleton
public class RecipeStepDetailFragmentViewModel extends ViewModel {
    private final RecipeRepository recipeRepository;

    private final MutableLiveData<RecipeStepDetailViewState> recipeStepDetailViewState;

    @Inject
    public RecipeStepDetailFragmentViewModel(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;

        recipeStepDetailViewState = new MutableLiveData<>();
        recipeStepDetailViewState.setValue(RecipeStepDetailViewState.getLoadingState());
    }

    LiveData<RecipeStepDetailViewState> getRecipeStepDetailViewState() { return recipeStepDetailViewState; }

    void setRecipeId(int recipeId) {
        RecipeStepDetailViewState viewState = recipeStepDetailViewState.getValue();

        if (viewState != null && viewState.status == Status.SUCCESS && viewState.recipe.getId() == recipeId) {
            return;
        }

        recipeRepository.getRecipe(recipeId, new RecipeRepository.Callback<Recipe>() {
            @Override
            public void onResponse(@NonNull Recipe response) {
                recipeStepDetailViewState.setValue(RecipeStepDetailViewState.getSuccessState(response));
            }

            @Override
            public void onError(@StringRes int error, @NonNull String errorArgument) {
                recipeStepDetailViewState.setValue(RecipeStepDetailViewState.getErrorState(error, errorArgument));
            }
        });
    }
}
