package nl.erikduisters.letsbake.ui.fragment.recipe_detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import nl.erikduisters.letsbake.R;
import nl.erikduisters.letsbake.data.local.RecipeRepository;
import nl.erikduisters.letsbake.data.model.Recipe;
import nl.erikduisters.letsbake.data.model.Step;
import nl.erikduisters.letsbake.ui.fragment.recipe_detail.RecipeDetailFragmentViewState.RecipeDetailViewState;
import timber.log.Timber;

/**
 * Created by Erik Duisters on 24-03-2018.
 */

@Singleton
public class RecipeDetailFragmentViewModel extends ViewModel {
    private final RecipeRepository recipeRepository;

    private final MutableLiveData<RecipeDetailViewState> recipeDetailViewState;

    @Inject
    RecipeDetailFragmentViewModel(RecipeRepository recipeRepository) {
        Timber.d("New MovieDetailFragmentViewModel created");

        this.recipeRepository = recipeRepository;

        recipeDetailViewState = new MutableLiveData<>();
        recipeDetailViewState.setValue(RecipeDetailViewState.getLoadingState());
    }

    LiveData<RecipeDetailViewState> getRecipeDetaiLViewState() { return recipeDetailViewState; }

    void setRecipeId(int recipeId) {
        RecipeDetailViewState viewState = recipeDetailViewState.getValue();

        if (viewState != null && viewState.recipe != null && viewState.recipe.getId() == recipeId) {
            return;
        }

        if (recipeId == RecipeRepository.IMVALID_RECIPE_ID) {
            recipeDetailViewState.setValue(RecipeDetailViewState.getErrorState(R.string.recipe_id_invalid, ""));
        } else {
            recipeRepository.getRecipe(recipeId, new RecipeRepository.Callback<Recipe>() {
                @Override
                public void onResponse(@NonNull Recipe response) {
                    recipeDetailViewState.setValue(RecipeDetailViewState.getSuccessState(response));
                }

                @Override
                public void onError(int error, @NonNull String errorArgument) {
                    recipeDetailViewState.setValue(RecipeDetailViewState.getErrorState(error, errorArgument));
                }
            });
        }
    }

    void onStepClicked(Step step) {
        Recipe recipe = recipeDetailViewState.getValue().recipe;

    }
}
