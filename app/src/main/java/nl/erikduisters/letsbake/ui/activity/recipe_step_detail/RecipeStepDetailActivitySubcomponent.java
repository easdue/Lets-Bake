package nl.erikduisters.letsbake.ui.activity.recipe_step_detail;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import nl.erikduisters.letsbake.di.ActivityScope;

/**
 * Created by Erik Duisters on 31-03-2018.
 */

@Subcomponent(modules = {RecipeStepDetailActivityBindingModule.class})
@ActivityScope
public interface RecipeStepDetailActivitySubcomponent extends AndroidInjector<RecipeStepDetailActivity> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<RecipeStepDetailActivity> {}
}
