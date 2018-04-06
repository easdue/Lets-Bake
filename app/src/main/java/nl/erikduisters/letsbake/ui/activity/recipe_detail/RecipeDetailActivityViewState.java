package nl.erikduisters.letsbake.ui.activity.recipe_detail;

import android.support.annotation.NonNull;

import java.util.List;

import nl.erikduisters.letsbake.util.MyMenuItem;

/**
 * Created by Erik Duisters on 24-03-2018.
 */

interface RecipeDetailActivityViewState {
    class RecipeDetailViewState implements RecipeDetailActivityViewState {
        @NonNull final String recipeName;
        @NonNull final List<MyMenuItem> optionsMenu;

        public RecipeDetailViewState(@NonNull String recipeName, @NonNull List<MyMenuItem> optionsMenu) {
            this.recipeName = recipeName;
            this.optionsMenu = optionsMenu;
        }
    }

    class FinishViewState implements RecipeDetailActivityViewState {
    }
}
