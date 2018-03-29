package nl.erikduisters.letsbake.ui.activity.main_activity;

import android.support.v4.app.Fragment;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.android.support.FragmentKey;
import dagger.multibindings.IntoMap;
import nl.erikduisters.letsbake.ui.fragment.recipe_list.RecipeListFragment;
import nl.erikduisters.letsbake.ui.fragment.recipe_list.RecipeListFragmentSubComponent;

/**
 * Created by Erik Duisters on 24-03-2018.
 */

@Module(subcomponents = {RecipeListFragmentSubComponent.class})
abstract class MainActivityBindingModule {
    @Binds
    @IntoMap
    @FragmentKey(RecipeListFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindMainActivityFragmentInjectorFactory(RecipeListFragmentSubComponent.Builder builder);
}
