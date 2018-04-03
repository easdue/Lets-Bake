package nl.erikduisters.letsbake.ui.activity.recipe_detail;

import android.support.v4.app.Fragment;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.android.support.FragmentKey;
import dagger.multibindings.IntoMap;
import nl.erikduisters.letsbake.ui.fragment.recipe_detail.RecipeDetailFragment;
import nl.erikduisters.letsbake.ui.fragment.recipe_detail.RecipeDetailFragmentSubComponent;

/**
 * Created by Erik Duisters on 24-03-2018.
 */
@Module(subcomponents = {RecipeDetailFragmentSubComponent.class})
abstract class RecipeDetailActivityBindingModule {
    @Binds
    @IntoMap
    @FragmentKey(RecipeDetailFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindMovieDetailActivityFragmentInjectorFactory(RecipeDetailFragmentSubComponent.Builder builder);
}
