package nl.erikduisters.letsbake.ui.fragment.recipe_detail;

import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;
import javax.inject.Singleton;

import nl.erikduisters.letsbake.data.local.RecipeRepository;
import timber.log.Timber;

/**
 * Created by Erik Duisters on 24-03-2018.
 */

@Singleton
public class RecipeDetailFragmentViewModel extends ViewModel {
    private final RecipeRepository recipeRepository;

    @Inject
    RecipeDetailFragmentViewModel(RecipeRepository recipeRepository) {
        Timber.d("New MovieDetailFragmentViewModel created");

        this.recipeRepository = recipeRepository;
    }
}
