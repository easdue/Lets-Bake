package nl.erikduisters.letsbake.ui.activity.main_activity;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import nl.erikduisters.letsbake.di.ActivityScope;

/**
 * Created by Erik Duisters on 24-03-2018.
 */

@Subcomponent(modules = {MainActivityBindingModule.class})
@ActivityScope
public interface MainActivitySubcomponent extends AndroidInjector<MainActivity> {
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MainActivity> {}
}
