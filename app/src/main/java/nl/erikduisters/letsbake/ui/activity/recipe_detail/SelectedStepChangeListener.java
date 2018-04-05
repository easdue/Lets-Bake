package nl.erikduisters.letsbake.ui.activity.recipe_detail;

import nl.erikduisters.letsbake.data.model.Step;

/**
 * Created by Erik Duisters on 04-04-2018.
 */
public interface SelectedStepChangeListener {
    void onSelectedStepChanged(Step step);
}
