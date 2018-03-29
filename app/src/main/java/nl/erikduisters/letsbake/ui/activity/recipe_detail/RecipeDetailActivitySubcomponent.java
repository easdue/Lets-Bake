package nl.erikduisters.letsbake.ui.activity.recipe_detail;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import nl.erikduisters.letsbake.di.ActivityScope;

/**
 * Created by Erik Duisters on 24-03-2018.
 */

@Subcomponent(modules = {RecipeDetailActivityBindingModule.class})
@ActivityScope
public interface RecipeDetailActivitySubcomponent extends AndroidInjector<RecipeDetailActivity> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<RecipeDetailActivity> {}
}