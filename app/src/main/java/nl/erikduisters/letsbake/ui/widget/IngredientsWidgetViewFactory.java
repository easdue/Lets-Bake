package nl.erikduisters.letsbake.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import javax.inject.Inject;
import javax.inject.Singleton;

import nl.erikduisters.letsbake.R;
import nl.erikduisters.letsbake.data.model.Ingredient;
import nl.erikduisters.letsbake.data.model.Recipe;
import nl.erikduisters.letsbake.di.ApplicationContext;

/**
 * Created by Erik Duisters on 06-04-2018.
 */
@Singleton
class IngredientsWidgetViewFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    @Nullable private Recipe recipe;

    @Inject
    IngredientsWidgetViewFactory(@ApplicationContext Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        recipe = IngredientService.currentRecipe;
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return recipe == null ? 0 : recipe.getIngredients().size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (recipe == null) {
            return null;
        }

        Ingredient ingredient = recipe.getIngredients().get(position);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget_ingredient_row);

        views.setTextViewText(R.id.quantity, ingredient.getQuantityAsString());
        views.setTextViewText(R.id.measure, ingredient.getMeasure());
        views.setTextViewText(R.id.ingredient, ingredient.getIngredient());

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
