package nl.erikduisters.letsbake.ui.fragment.recipe_list;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import nl.erikduisters.letsbake.di.FragmentScope;

/**
 * Created by Erik Duisters on 24-03-2018.
 */

@FragmentScope
@Subcomponent
public interface RecipeListFragmentSubComponent extends AndroidInjector<RecipeListFragment> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<RecipeListFragment> {}
}
