package nl.erikduisters.letsbake.ui.activity.recipe_detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import nl.erikduisters.letsbake.data.local.RecipeRepository;
import nl.erikduisters.letsbake.data.model.Recipe;
import timber.log.Timber;

/**
 * Created by Erik Duisters on 24-03-2018.
 */

@Singleton
public class RecipeDetailActivityViewModel extends ViewModel {
    private final MutableLiveData<RecipeDetailActivityViewState> viewStateLiveData;

    private final RecipeRepository recipeRepository;

    @Inject
    RecipeDetailActivityViewModel(RecipeRepository recipeRepository) {
        Timber.d("New DetailActivityViewModel created");

        this.recipeRepository = recipeRepository;

        viewStateLiveData = new MutableLiveData<>();
    }

    LiveData<RecipeDetailActivityViewState> getViewState() {
        return viewStateLiveData;
    }

    void setRecipeId(int recipeId) {
        recipeRepository.getRecipe(recipeId, new RecipeRepository.Callback<Recipe>() {
            @Override
            public void onResponse(@NonNull Recipe response) {
                viewStateLiveData.setValue(new RecipeDetailActivityViewState.RecipeDetailViewState(response.getName()));
            }

            @Override
            public void onError(int error, @NonNull String errorArgument) {
                //Don't care
            }
        });
    }

    void onHomeAsUpPressed() {
        viewStateLiveData.setValue(new RecipeDetailActivityViewState.FinishViewState());
    }

    void onFinished() {
        viewStateLiveData.setValue(null);
    }
}
