package nl.erikduisters.letsbake.ui.activity.recipe_step_detail;

import android.support.v4.app.Fragment;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.android.support.FragmentKey;
import dagger.multibindings.IntoMap;
import nl.erikduisters.letsbake.ui.fragment.recipe_step_detail.RecipeStepDetailFragment;
import nl.erikduisters.letsbake.ui.fragment.recipe_step_detail.RecipeStepDetailFragmentSubComponent;

/**
 * Created by Erik Duisters on 31-03-2018.
 */
@Module(subcomponents = {RecipeStepDetailFragmentSubComponent.class})
abstract class RecipeStepDetailActivityBindingModule {
    @Binds
    @IntoMap
    @FragmentKey(RecipeStepDetailFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindRecipeStepDetailFragmentInjectorFactory(RecipeStepDetailFragmentSubComponent.Builder builder);
}
