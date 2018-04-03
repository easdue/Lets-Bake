package nl.erikduisters.letsbake.ui.activity.recipe_step_detail;

import android.support.annotation.NonNull;

/**
 * Created by Erik Duisters on 31-03-2018.
 */
interface RecipeStepDetailActivityViewState {
    class RecipeStepDetailViewState implements RecipeStepDetailActivityViewState{
        @NonNull  final String recipeName;

        RecipeStepDetailViewState(@NonNull String recipeName) {
            this.recipeName = recipeName;
        }
    }

    class FinishViewState implements RecipeStepDetailActivityViewState {
    }
}
