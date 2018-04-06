package nl.erikduisters.letsbake.di;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import nl.erikduisters.letsbake.MyApplication;

/**
 * Created by Erik Duisters on 24-03-2018.
 */

@Component(modules = {AndroidSupportInjectionModule.class, AppModule.class, ServiceModule.class, ActivityBindingModule.class, ViewModelBindingModule.class})
@Singleton
public interface AppComponent extends AndroidInjector<MyApplication> {
    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<MyApplication> {
    }
}
