package nl.erikduisters.letsbake.ui.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import nl.erikduisters.letsbake.R;
import nl.erikduisters.letsbake.data.local.PreferenceManager;
import nl.erikduisters.letsbake.data.local.RecipeRepository;
import nl.erikduisters.letsbake.data.model.Recipe;
import timber.log.Timber;

/**
 * Created by Erik Duisters on 05-04-2018.
 */
public class IngredientService extends IntentService {
    private static final String ACTION_UPDATE_INGREDIENTS = "com.erikduisters.letsbake.action.update_ingredients";
    public static Recipe currentRecipe;

    @Inject RecipeRepository recipeRepository;
    @Inject PreferenceManager preferenceManager;

    public IngredientService() {
        super(IngredientService.class.getSimpleName());

        Timber.e("new IngredientService created");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        AndroidInjection.inject(this);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_INGREDIENTS.equals(action)) {
                getCurrentRecipe();
            }
        }
    }

    private void getCurrentRecipe() {
        int recipeId = preferenceManager.getSelectedWidgetRecipeID();

        if (recipeId != RecipeRepository.INVALID_RECIPE_ID) {
            recipeRepository.getRecipe(recipeId, new RecipeRepository.Callback<Recipe>() {
                @Override
                public void onResponse(@NonNull Recipe response) {
                    currentRecipe = response;

                    updateIngredientWidget();
                }

                @Override
                public void onError(int error, @NonNull String errorArgument) {
                    currentRecipe = null;
                }
            });
        }
    }

    private void updateIngredientWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getBaseContext());

        int[] widgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(getBaseContext(), IngredientsWidgetProvider.class));

        appWidgetManager.notifyAppWidgetViewDataChanged(widgetIds, R.id.ingredientList);

        IngredientsWidgetProvider.updateWidgets(getBaseContext(), appWidgetManager, widgetIds);
    }

    public static void startActionUpdateIngredients(Context context) {
        Intent intent = new Intent(context, IngredientService.class);
        intent.setAction(ACTION_UPDATE_INGREDIENTS);

        context.startService(intent);
    }
}

