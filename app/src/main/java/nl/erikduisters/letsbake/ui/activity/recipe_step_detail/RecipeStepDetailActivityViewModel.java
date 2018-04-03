package nl.erikduisters.letsbake.ui.activity.recipe_step_detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import nl.erikduisters.letsbake.data.local.RecipeRepository;
import nl.erikduisters.letsbake.data.model.Recipe;
import nl.erikduisters.letsbake.ui.activity.recipe_step_detail.RecipeStepDetailActivityViewState.RecipeStepDetailViewState;
import timber.log.Timber;

/**
 * Created by Erik Duisters on 29-03-2018.
 */

@Singleton
public class RecipeStepDetailActivityViewModel extends ViewModel {
    private final MutableLiveData<RecipeStepDetailActivityViewState> viewStateLiveData;

    private final RecipeRepository recipeRepository;

    @Inject
    RecipeStepDetailActivityViewModel(RecipeRepository recipeRepository) {
        Timber.d("New RecipeStepDetailActivityViewModel created");

        this.recipeRepository = recipeRepository;

        viewStateLiveData = new MutableLiveData<>();
    }

    LiveData<RecipeStepDetailActivityViewState> getViewState() {
        return viewStateLiveData;
    }

    void setRecipeId(int recipeId) {
        recipeRepository.getRecipe(recipeId, new RecipeRepository.Callback<Recipe>() {
            @Override
            public void onResponse(@NonNull Recipe response) {
                viewStateLiveData.setValue(new RecipeStepDetailViewState(response.getName()));
            }

            @Override
            public void onError(int error, @NonNull String errorArgument) {
                //Don't care
            }
        });
    }

    void onHomeAsUpPressed() {
        viewStateLiveData.setValue(new RecipeStepDetailActivityViewState.FinishViewState());
    }

    void onFinished() {
        viewStateLiveData.setValue(null);
    }
}
