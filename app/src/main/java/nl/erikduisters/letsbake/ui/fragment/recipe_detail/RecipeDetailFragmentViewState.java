package nl.erikduisters.letsbake.ui.fragment.recipe_detail;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import java.util.List;

import nl.erikduisters.letsbake.R;
import nl.erikduisters.letsbake.data.model.Recipe;
import nl.erikduisters.letsbake.data.model.Status;
import nl.erikduisters.letsbake.ui.BaseActivity;
import nl.erikduisters.letsbake.ui.activity.recipe_step_detail.RecipeStepDetailActivity;
import nl.erikduisters.letsbake.util.MyMenuItem;

/**
 * Created by Erik Duisters on 24-03-2018.
 */

interface RecipeDetailFragmentViewState {
    class RecipeDetailViewState implements RecipeDetailFragmentViewState {
        final @Status int status;
        final @StringRes int loadingMessage;
        final @Nullable Recipe recipe;
        final @StringRes int errorLabel;
        final @NonNull String errorArgument;
        final @NonNull List<MyMenuItem> optionsMenu;

        private RecipeDetailViewState(@Status int status, @StringRes int loadingMessage,
                                      @Nullable Recipe recipe, @StringRes int errorLabel,
                                      @NonNull String errorArgument, @NonNull List<MyMenuItem> optionsMenu) {
            this.status = status;
            this.loadingMessage = loadingMessage;
            this.recipe = recipe;
            this.errorLabel = errorLabel;
            this.errorArgument = errorArgument;
            this.optionsMenu = optionsMenu;
        }

        static RecipeDetailViewState getLoadingState(List<MyMenuItem> optionsMenu) {
            return new RecipeDetailViewState(Status.LOADING, R.string.loading, null, 0, "", optionsMenu);
        }

        static RecipeDetailViewState getErrorState(@StringRes int errorLabel, @NonNull String errorArgument, @NonNull List<MyMenuItem> optionsMenu) {
            return new RecipeDetailViewState(Status.ERROR, 0, null, errorLabel, errorArgument, optionsMenu);
        }

        static RecipeDetailViewState getSuccessState(@NonNull Recipe recipe, @NonNull List<MyMenuItem> optionsMenu) {
            return new RecipeDetailViewState(Status.SUCCESS, 0, recipe, 0, "", optionsMenu);
        }
    }

    class StartActivityViewState implements RecipeDetailFragmentViewState {
        private final int recipeId;
        private final int stepId;
        private final Class<? extends BaseActivity> activityClass;

        StartActivityViewState(int recipeId, int stepId, Class<? extends BaseActivity> activityClass) {
            this.recipeId = recipeId;
            this.stepId = stepId;
            this.activityClass = activityClass;
        }

        Intent getIntent(Context ctx) {
            Intent intent = new Intent(ctx, activityClass);
            intent.putExtra(RecipeStepDetailActivity.KEY_RECIPE_ID, recipeId);
            intent.putExtra(RecipeStepDetailActivity.KEY_STEP_ID, stepId);

            return intent;
        }

        int getRecipeId() { return recipeId; }
        int getStepId() { return getStepId(); }
    }

    class UpdateWidgetViewState implements RecipeDetailFragmentViewState {
        private final int recipeId;

        UpdateWidgetViewState(int recipeId) {
            this.recipeId = recipeId;
        }
    }
}
