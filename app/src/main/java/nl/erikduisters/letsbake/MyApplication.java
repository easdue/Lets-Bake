package nl.erikduisters.letsbake;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.NonNull;

import com.facebook.stetho.Stetho;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import nl.erikduisters.letsbake.di.DaggerAppComponent;
import timber.log.Timber;

/**
 * Created by Erik Duisters on 24-03-2018.
 */

public class MyApplication extends Application implements HasActivityInjector {
    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

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

        Stetho.initializeWithDefaults(this);
    }

    private static class ReleaseTree extends Timber.Tree {
        @Override
        protected void log(int priority, String tag, @NonNull String message, Throwable t) {
        }
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
