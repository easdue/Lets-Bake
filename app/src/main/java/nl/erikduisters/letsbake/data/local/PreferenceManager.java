package nl.erikduisters.letsbake.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import nl.erikduisters.letsbake.R;
import nl.erikduisters.letsbake.di.ApplicationContext;

/**
 * Created by Erik Duisters on 24-04-2018.
 */

@Singleton
public class PreferenceManager {
    private final String KEY_SELECTED_RECIPE_ID;

    private final SharedPreferences sharedPreferences;

    @Inject
    PreferenceManager(@ApplicationContext Context ctx) {
        android.preference.PreferenceManager.setDefaultValues(ctx, R.xml.preferences, true);
        sharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(ctx);

        KEY_SELECTED_RECIPE_ID = ctx.getString(R.string.prefs_selected_recipe_id);
    }

    public int getSelectedRecipeID() {
        return sharedPreferences.getInt(KEY_SELECTED_RECIPE_ID, -1);
    }

    public void setSelectedRecipeID(int recipeID) {
        sharedPreferences
                .edit()
                .putInt(KEY_SELECTED_RECIPE_ID, recipeID)
                .apply();
    }
}
