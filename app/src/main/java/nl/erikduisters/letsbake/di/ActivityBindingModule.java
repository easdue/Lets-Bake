package nl.erikduisters.letsbake.di;

import android.app.Activity;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;
import nl.erikduisters.letsbake.ui.activity.main_activity.MainActivity;
import nl.erikduisters.letsbake.ui.activity.main_activity.MainActivitySubcomponent;
import nl.erikduisters.letsbake.ui.activity.recipe_detail.RecipeDetailActivity;
import nl.erikduisters.letsbake.ui.activity.recipe_detail.RecipeDetailActivitySubcomponent;
import nl.erikduisters.letsbake.ui.activity.recipe_step_detail.RecipeStepDetailActivity;
import nl.erikduisters.letsbake.ui.activity.recipe_step_detail.RecipeStepDetailActivitySubcomponent;

/**
 * Created by Erik Duisters on 24-03-2018.
 */

@Module
abstract class ActivityBindingModule {
    @Binds
    @IntoMap
    @ActivityKey(MainActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindMainActivityInjectorFactory(MainActivitySubcomponent.Builder builder);

    @Binds
    @IntoMap
    @ActivityKey(RecipeDetailActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindRecipeDetailActivityInjectorFactory(RecipeDetailActivitySubcomponent.Builder builder);

    @Binds
    @IntoMap
    @ActivityKey(RecipeStepDetailActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindRecipeStepDetailActivityInjectorFactory(RecipeStepDetailActivitySubcomponent.Builder builder);
}
