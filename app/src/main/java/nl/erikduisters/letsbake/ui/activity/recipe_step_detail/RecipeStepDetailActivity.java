package nl.erikduisters.letsbake.ui.activity.recipe_step_detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import nl.erikduisters.letsbake.R;
import nl.erikduisters.letsbake.data.local.RecipeRepository;
import nl.erikduisters.letsbake.ui.BaseActivity;
import nl.erikduisters.letsbake.ui.activity.recipe_step_detail.RecipeStepDetailActivityViewState.FinishViewState;
import nl.erikduisters.letsbake.ui.activity.recipe_step_detail.RecipeStepDetailActivityViewState.RecipeStepDetailViewState;
import nl.erikduisters.letsbake.ui.fragment.recipe_step_detail.RecipeStepDetailFragment;

/**
 * Created by Erik Duisters on 29-03-2018.
 */
public class RecipeStepDetailActivity extends BaseActivity<RecipeStepDetailActivityViewModel> {
    public static final String KEY_RECIPE_ID = "RecipeId";
    public static final String KEY_STEP_ID = "StepId";
    public static final String TAG_RECIPE_STEP_DETAIL_FRAGMENT = "RecipeStepDetailFragment";

    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        int recipeId = getIntent().getIntExtra(KEY_RECIPE_ID, RecipeRepository.INVALID_RECIPE_ID);
        int recipeStepId = getIntent().getIntExtra(KEY_STEP_ID, RecipeRepository.INVALID_RECIPE_STEP_ID);

        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment fragment = fragmentManager.findFragmentByTag(TAG_RECIPE_STEP_DETAIL_FRAGMENT);

        if (fragment == null) {
            fragmentManager
                    .beginTransaction()
                    .add(R.id.fragmentPlaceholder, RecipeStepDetailFragment.newInstance(recipeId, recipeStepId), TAG_RECIPE_STEP_DETAIL_FRAGMENT)
                    .commit();
        }

        viewModel.getViewState().observe(this, this::render);

        viewModel.setRecipeId(recipeId);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_recipe_step_detail;
    }

    @Override
    protected Class<RecipeStepDetailActivityViewModel> getViewModelClass() {
        return RecipeStepDetailActivityViewModel.class;
    }

    private void render(@Nullable RecipeStepDetailActivityViewState viewState) {
        if (viewState == null) {
            return;
        }

        if (viewState instanceof FinishViewState) {
            render((FinishViewState)viewState);
        }

        if (viewState instanceof RecipeStepDetailViewState) {
            render((RecipeStepDetailViewState)viewState);
        }
    }

    private void render(FinishViewState viewState) {
        NavUtils.navigateUpFromSameTask(this);
        viewModel.onFinished();
    }

    private void render(RecipeStepDetailViewState viewState) {
        getSupportActionBar().setTitle(viewState.recipeName);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                viewModel.onHomeAsUpPressed();
                return true;
        }

        return false;
    }
}
