package nl.erikduisters.letsbake.ui.activity.main_activity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

/**
 * Created by Erik Duisters on 24-03-2018.
 */
@Singleton
public final class MainActivityViewModel extends ViewModel {
    private final MutableLiveData<MainActivityViewState> viewStateLiveData;

    @Inject
    MainActivityViewModel() {
        Timber.d("New MainActivityViewModel created");

        MainActivityViewState viewState = new MainActivityViewState();

        viewStateLiveData = new MutableLiveData<>();
    }

    LiveData<MainActivityViewState> getViewState() {
        return viewStateLiveData;
    }
}
