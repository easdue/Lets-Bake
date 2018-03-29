package nl.erikduisters.letsbake.ui.fragment.recipe_list;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import java.util.ArrayList;
import java.util.List;

import nl.erikduisters.letsbake.R;
import nl.erikduisters.letsbake.data.model.Recipe;
import nl.erikduisters.letsbake.data.model.Status;
import nl.erikduisters.letsbake.ui.BaseActivity;
import nl.erikduisters.letsbake.ui.activity.recipe_detail.RecipeDetailActivity;

/**
 * Created by Erik Duisters on 24-03-2018.
 */

public interface RecipeListFragmentViewState {
    final class RecipeViewState implements RecipeListFragmentViewState {
        final @Status int status;
        final @StringRes int loadingMessage;
        final @NonNull List<Recipe> recipeList;
        final @StringRes int emptyRecipeListMessage;
        final @StringRes int errorLabel;
        final @NonNull String errorArgument;

        private RecipeViewState(@Status int status, @StringRes int loadingMessage, @NonNull List<Recipe> recipeList, @StringRes int emptyRecipeListMessage,
                                @StringRes int errorLabel, @NonNull String errorArgument) {
            this.status = status;
            this.loadingMessage = loadingMessage;
            this.recipeList = recipeList;
            this.emptyRecipeListMessage = emptyRecipeListMessage;
            this.errorLabel = errorLabel;
            this.errorArgument = errorArgument;
        }

        static RecipeViewState getLoadingViewState() {
            return new RecipeViewState(Status.LOADING, R.string.loading, new ArrayList<>(), 0, 0, "");
        }

        static RecipeViewState getErrorViewState(@StringRes int messageLabel, @NonNull String messageArgument) {
            return new RecipeViewState(Status.ERROR, 0, new ArrayList<>(), 0, messageLabel, messageArgument);
        }

        static RecipeViewState getSuccessViewState(@NonNull List<Recipe> recipeList, @StringRes int emptyRecipeListMessage) {
            return new RecipeViewState(Status.SUCCESS, 0, recipeList, emptyRecipeListMessage, 0, "");
        }
    }

    final class StartActivityViewState implements RecipeListFragmentViewState {
        private final int recipeId;
        private final Class<? extends BaseActivity> activityClass;

        StartActivityViewState(int recipeId, Class<? extends BaseActivity> activityClass) {
            this.recipeId = recipeId;
            this.activityClass = activityClass;
        }

        Intent getIntent(Context ctx) {
            Intent intent = new Intent(ctx, activityClass);
            intent.putExtra(RecipeDetailActivity.KEY_RECIPE_ID, recipeId);

            return intent;
        }
    }
}
