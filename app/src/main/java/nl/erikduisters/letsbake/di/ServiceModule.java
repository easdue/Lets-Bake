package nl.erikduisters.letsbake.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import nl.erikduisters.letsbake.ui.widget.IngredientService;
import nl.erikduisters.letsbake.ui.widget.IngredientsWidgetService;

/**
 * Created by Erik Duisters on 05-04-2018.
 */

@Module
public abstract class ServiceModule {
    @ContributesAndroidInjector
    abstract IngredientsWidgetService bindIngredientsWidgetsService();

    @ContributesAndroidInjector
    abstract IngredientService bindIngredientServide();
}
