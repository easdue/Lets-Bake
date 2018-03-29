package nl.erikduisters.letsbake.di;

import android.arch.lifecycle.ViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import nl.erikduisters.letsbake.ui.activity.main_activity.MainActivityViewModel;
import nl.erikduisters.letsbake.ui.activity.recipe_detail.RecipeDetailActivityViewModel;
import nl.erikduisters.letsbake.ui.fragment.recipe_detail.RecipeDetailFragmentViewModel;
import nl.erikduisters.letsbake.ui.fragment.recipe_list.RecipeListFragmentViewModel;
import nl.erikduisters.letsbake.ui.fragment.recipe_step_detail.RecipeStepDetailFragmentViewModel;

/**
 * Created by Erik Duisters on 24-03-2018.
 */

@Module
abstract class ViewModelBindingModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel.class)
    abstract ViewModel bindMainActivityViewModel(MainActivityViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RecipeListFragmentViewModel.class)
    abstract ViewModel bindMovieListFragmentViewModel(RecipeListFragmentViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RecipeDetailActivityViewModel.class)
    abstract ViewModel bindDetailActivityViewModel(RecipeDetailActivityViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RecipeDetailFragmentViewModel.class)
    abstract ViewModel bindMovieDetailFragmentViewModel(RecipeDetailFragmentViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RecipeStepDetailFragmentViewModel.class)
    abstract ViewModel bindMovieReviewsFragmentViewModel(RecipeStepDetailFragmentViewModel viewModel);
}
