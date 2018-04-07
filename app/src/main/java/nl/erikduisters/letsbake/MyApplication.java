package nl.erikduisters.letsbake;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasServiceInjector;
import nl.erikduisters.letsbake.di.DaggerAppComponent;
import timber.log.Timber;

/**
 * Created by Erik Duisters on 24-03-2018.
 */

public class MyApplication extends Application
        implements HasActivityInjector, HasServiceInjector {
    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidActivityInjector;
    @Inject
    DispatchingAndroidInjector<Service> dispatchingAndroidServiceInjector;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new ReleaseTree());
        }

        DaggerAppComponent.builder()
                .create(this)
                .inject(this);
    }

    private static class ReleaseTree extends Timber.Tree {
        @Override
        protected void log(int priority, String tag, @NonNull String message, Throwable t) {
        }
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidActivityInjector;
    }

    @Override
    public DispatchingAndroidInjector<Service> serviceInjector() {
        return dispatchingAndroidServiceInjector;
    }
}
