package nl.erikduisters.letsbake.ui.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.android.AndroidInjection;
import timber.log.Timber;

/**
 * Created by Erik Duisters on 05-04-2018.
 */

@Singleton
public class IngredientsWidgetService extends RemoteViewsService {
    @Inject IngredientsWidgetViewFactory ingredientsWidgetViewFactory;

    public IngredientsWidgetService() {
        Timber.e("new IngredientsWidgetService created");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        AndroidInjection.inject(this);
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return ingredientsWidgetViewFactory;
    }
}

