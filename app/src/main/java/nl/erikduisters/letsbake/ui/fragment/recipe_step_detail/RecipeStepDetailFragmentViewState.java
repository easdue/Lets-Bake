package nl.erikduisters.letsbake.ui.fragment.recipe_step_detail;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import nl.erikduisters.letsbake.R;
import nl.erikduisters.letsbake.data.model.Recipe;
import nl.erikduisters.letsbake.data.model.Status;

/**
 * Created by Erik Duisters on 24-03-2018.
 */

interface RecipeStepDetailFragmentViewState {
    class RecipeStepDetailViewState implements RecipeStepDetailFragmentViewState {
        final @Status int status;
        final @StringRes int loadingMessage;
        @Nullable final Recipe recipe;
        final @StringRes int errorLabel;
        final @NonNull String errorArgument;

        private RecipeStepDetailViewState(@Status int status, @StringRes int loadingMessage, @Nullable Recipe recipe, @StringRes int errorLabel, @NonNull String errorArgument) {
            this.status = status;
            this.loadingMessage = loadingMessage;
            this.recipe = recipe;
            this.errorLabel = errorLabel;
            this.errorArgument = errorArgument;
        }

        static RecipeStepDetailViewState getLoadingState() {
            return new RecipeStepDetailViewState(Status.LOADING, R.string.loading, null, 0, "");
        }

        static RecipeStepDetailViewState getSuccessState(@NonNull Recipe recipe) {
            return new RecipeStepDetailViewState(Status.SUCCESS, 0, recipe, 0, "");
        }

        static RecipeStepDetailViewState getErrorState(@StringRes int errorLabel, @NonNull String errorArgument) {
            return new RecipeStepDetailViewState(Status.ERROR, 0, null, errorLabel, errorArgument);
        }
    }

    //TODO: FinishState
}
