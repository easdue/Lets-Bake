package nl.erikduisters.letsbake.ui.activity.recipe_detail;

import android.support.annotation.NonNull;

/**
 * Created by Erik Duisters on 24-03-2018.
 */

interface RecipeDetailActivityViewState {
    class RecipeDetailViewState implements RecipeDetailActivityViewState {
        @NonNull final String recipeName;

        public RecipeDetailViewState(@NonNull String recipeName) {
            this.recipeName = recipeName;
        }
    }

    class FinishViewState implements RecipeDetailActivityViewState {
    }
}
