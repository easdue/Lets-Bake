package nl.erikduisters.letsbake.ui.activity.recipe_step_detail;

import nl.erikduisters.letsbake.ui.BaseActivity;

/**
 * Created by Erik Duisters on 29-03-2018.
 */
public class RecipeStepDetailActivity extends BaseActivity<RecipeStepDetailActivityViewModel> {
    public static final String KEY_RECIPE_ID = "RecipeId";
    public static final String KEY_STEP_ID = "StepId";

    @Override
    protected int getLayoutResId() {
        return 0;
    }

    @Override
    protected Class<RecipeStepDetailActivityViewModel> getViewModelClass() {
        return null;
    }


}
