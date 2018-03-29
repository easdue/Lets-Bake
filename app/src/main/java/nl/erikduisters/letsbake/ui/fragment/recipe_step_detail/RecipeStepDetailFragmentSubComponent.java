package nl.erikduisters.letsbake.ui.fragment.recipe_step_detail;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import nl.erikduisters.letsbake.di.FragmentScope;

/**
 * Created by Erik Duisters on 24-03-2018.
 */

@FragmentScope
@Subcomponent
public interface RecipeStepDetailFragmentSubComponent extends AndroidInjector<RecipeStepDetailFragment> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<RecipeStepDetailFragment> {}
}
