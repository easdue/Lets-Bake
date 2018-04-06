package nl.erikduisters.letsbake.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import nl.erikduisters.letsbake.R;
import nl.erikduisters.letsbake.data.model.Recipe;
import nl.erikduisters.letsbake.ui.activity.main_activity.MainActivity;

/**
 * Created by Erik Duisters on 05-04-2018.
 */
public class IngredientsWidgetProvider extends AppWidgetProvider {
    static void updateWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId);
        }
    }

    private static void updateWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        Recipe recipe = IngredientService.currentRecipe;

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);

        if (recipe == null) {
            views.setViewVisibility(R.id.recipeNameLabel, View.INVISIBLE);
            views.setViewVisibility(R.id.recipeName, View.INVISIBLE);
        } else {
            views.setViewVisibility(R.id.recipeNameLabel, View.VISIBLE);
            views.setViewVisibility(R.id.recipeName, View.VISIBLE);
            views.setTextViewText(R.id.recipeName, recipe.getName());
        }

        views.setEmptyView(R.id.ingredientList, R.id.notSetMessage);
        Intent intent = new Intent(context, IngredientsWidgetService.class);
        views.setRemoteAdapter(R.id.ingredientList, intent);

        intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.clickFrame, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        IngredientService.startActionUpdateIngredients(context);
    }
}
