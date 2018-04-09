package nl.erikduisters.letsbake;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.support.annotation.NonNull;

import dagger.android.AndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasServiceInjector;
import timber.log.Timber;

/**
 * Created by Erik Duisters on 09-04-2018.
 */
public class TestApplication extends Application
        implements HasActivityInjector, HasServiceInjector {

    public static AndroidInjector<Activity> activityAndroidInjector;
    public static AndroidInjector<Service> serviceAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new TestApplication.ReleaseTree());
        }
    }

    private static class ReleaseTree extends Timber.Tree {
        @Override
        protected void log(int priority, String tag, @NonNull String message, Throwable t) {
        }
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return activityAndroidInjector;
    }

    @Override
    public AndroidInjector<Service> serviceInjector() {
        return serviceAndroidInjector;
    }
}
