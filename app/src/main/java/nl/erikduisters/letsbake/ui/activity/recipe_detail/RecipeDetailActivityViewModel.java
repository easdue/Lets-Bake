package nl.erikduisters.letsbake.ui.activity.recipe_detail;

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
public class RecipeDetailActivityViewModel extends ViewModel {
    private final MutableLiveData<RecipeDetailActivityViewState> viewStateLiveData;

    @Inject
    RecipeDetailActivityViewModel() {
        Timber.d("New DetailActivityViewModel created");

        viewStateLiveData = new MutableLiveData<>();
    }

    LiveData<RecipeDetailActivityViewState> getViewState() {
        return viewStateLiveData;
    }

    void onHomeAsUpPressed() {
        viewStateLiveData.setValue(new RecipeDetailActivityViewState.FinishViewState());
    }

    void onFinished() {
        viewStateLiveData.setValue(null);
    }
}
