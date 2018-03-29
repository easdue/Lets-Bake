package nl.erikduisters.letsbake.data.local;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import nl.erikduisters.letsbake.R;
import nl.erikduisters.letsbake.async.BackgroundJobHandler;
import nl.erikduisters.letsbake.data.model.Recipe;
import nl.erikduisters.letsbake.data.remote.RecipeService;
import nl.erikduisters.letsbake.data.usecase.LoadRecipesFromCache;
import nl.erikduisters.letsbake.data.usecase.SaveRecipesToCache;
import nl.erikduisters.letsbake.data.usecase.UseCase;
import nl.erikduisters.letsbake.di.ApplicationContext;
import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

import static nl.erikduisters.letsbake.data.usecase.SaveRecipesToCache.RequestInfo;

/**
 * Created by Erik Duisters on 24-03-2018.
 */

@Singleton
public class RecipeRepository {
    private static final String RECIPES_CACHE_FILE="recipes.json";
    public static final int IMVALID_RECIPE_ID = -1;

    private final RecipeService recipeService;
    private final PreferenceManager preferenceManager;
    private final NetworkMonitor networkMonitor;
    private final BackgroundJobHandler backgroundJobHandler;
    private @Nullable Call<List<Recipe>> recipeListCall;
    private boolean recipesAreFromCache;
    private final File cacheFile;
    private final Gson gson;

    private @Nullable List<Recipe> recipeList;

    @Inject
    RecipeRepository(RecipeService recipeService, PreferenceManager preferenceManager,
                     NetworkMonitor networkMonitor, BackgroundJobHandler backgroundJobHandler,
                     @ApplicationContext Context context, Gson gson) {

        this.recipeService = recipeService;
        this.preferenceManager = preferenceManager;
        this.networkMonitor = networkMonitor;
        this.backgroundJobHandler = backgroundJobHandler;
        this.cacheFile = new File(context.getCacheDir(), RECIPES_CACHE_FILE);
        this.gson = gson;
    }

    public void getRecipes(Callback<List<Recipe>> callback) {
        if (recipeList != null) {
            callback.onResponse(recipeList);
        }

        if (recipeList != null && !recipesAreFromCache) {
            return;
        }

        cancelPendingRecipeListCall();

        if (networkMonitor.isNetworkConnected()) {
            requestLiveRecipes(callback);
        } else {
            if (recipeList == null) {
                loadCachedRecipes(callback);
            }

            networkMonitor.addListener(() -> requestLiveRecipes(callback));
        }
    }

    private void requestLiveRecipes(Callback<List<Recipe>> callback) {
        recipeListCall = recipeService.getRecipes();
        //noinspection ConstantConditions
        recipeListCall.enqueue(new RecipeCallback(callback));
    }

    private void loadCachedRecipes(Callback<List<Recipe>> callback) {
        LoadRecipesFromCache.RequestInfo requestInfo = new LoadRecipesFromCache.RequestInfo(cacheFile, gson);

        UseCase useCase = new LoadRecipesFromCache(requestInfo, new UseCase.Callback<List<Recipe>>() {
            @Override
            public void onResult(@Nullable List<Recipe> result) {
                if (recipeList == null ) {
                    recipesAreFromCache = true;
                    recipeList = result;

                    callback.onResponse(result == null ? new ArrayList<>() : result);
                } else {
                    callback.onResponse(recipeList);
                }
            }

            @Override
            public void onError(@NonNull Throwable error) {
                callback.onError(R.string.recipe_api_call_failure, error.getMessage());
            }
        });

        backgroundJobHandler.runJob(useCase.getUseCaseJob());
    }

    private void cancelPendingRecipeListCall() {
        if (recipeListCall != null) {
            recipeListCall.cancel();
        }
    }

    public void getRecipe(int recipeId, Callback<Recipe> callback) {
        if (recipeList == null) {
            getRecipes(new CallbackWrapper(callback, recipeId));

            return;
        }

        Recipe recipe = getRecipeById(recipeId);

        if (recipe == null) {
            callback.onError(R.string.recipe_id_unknown, "");
        } else {
            callback.onResponse(recipe);
        }
    }

    private @Nullable Recipe getRecipeById(int recipeId) {
        if (recipeList == null) {
            return null;
        }

        for (Recipe recipe : recipeList) {
            if (recipe.getId() == recipeId) {
                return recipe;
            }
        }

        return null;
    }

    private class RecipeCallback implements retrofit2.Callback<List<Recipe>> {
        private final Callback<List<Recipe>> callback;

        RecipeCallback(Callback<List<Recipe>> callback) {
            this.callback = callback;
        }


        @Override
        public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
            Timber.d("onResponse");

            recipeListCall = null;

            if (response.isSuccessful()) {
                List<Recipe> recipeList = response.body();

                if (recipeList != null) {
                    RecipeRepository.this.recipeList = recipeList;
                    RecipeRepository.this.recipesAreFromCache = false;

                    cacheRecipeList();

                    callback.onResponse(recipeList);
                } else {
                    callback.onError(R.string.recipe_api_call_failure, "Could not parse received response");
                }
            } else {
                callback.onError(R.string.recipe_api_call_failure, response.message());
            }
        }

        @Override
        public void onFailure(Call<List<Recipe>> call, Throwable t) {
            Timber.d("onFailure");

            recipeListCall = null;

            callback.onError(R.string.recipe_api_call_failure, t.getMessage());
        }
    }

    private void cacheRecipeList() {
        RequestInfo requestInfo = new RequestInfo(cacheFile, recipeList, gson);

        UseCase useCase = new SaveRecipesToCache(requestInfo, new UseCase.Callback<Void>() {
            @Override
            public void onResult(@Nullable Void result) {
                //Don't care
            }

            @Override
            public void onError(@NonNull Throwable error) {
                //TODO: Maybe call callback.onError();
            }
        });

        backgroundJobHandler.runJob(useCase.getUseCaseJob());
    }

    public interface Callback<T> {
        void onResponse(@NonNull T response);
        void onError(@StringRes int error, @NonNull String errorArgument);
    }

    private class CallbackWrapper implements Callback<List<Recipe>> {
        private final Callback<Recipe> callback;
        private final int movieId;

        CallbackWrapper(Callback<Recipe> callback, int movieId) {
            this.callback = callback;
            this.movieId = movieId;
        }

        @Override
        public void onResponse(@NonNull List<Recipe> response) {
            getRecipe(movieId, callback);
        }

        @Override
        public void onError(@StringRes int error, @NonNull String errorArgument) {
            callback.onError(error, errorArgument);
        }
    }
}
