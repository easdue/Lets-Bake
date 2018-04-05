package nl.erikduisters.letsbake.ui.activity.recipe_detail;

import android.content.Intent;
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
import nl.erikduisters.letsbake.data.model.Step;
import nl.erikduisters.letsbake.ui.BaseActivity;
import nl.erikduisters.letsbake.ui.activity.recipe_detail.RecipeDetailActivityViewState.RecipeDetailViewState;
import nl.erikduisters.letsbake.ui.activity.recipe_step_detail.RecipeStepDetailActivity;
import nl.erikduisters.letsbake.ui.fragment.recipe_detail.RecipeDetailFragment;
import nl.erikduisters.letsbake.ui.fragment.recipe_step_detail.RecipeStepDetailFragment;

import static nl.erikduisters.letsbake.ui.activity.recipe_detail.RecipeDetailActivityViewState.FinishViewState;

/**
 * Created by Erik Duisters on 24-03-2018.
 */

public class RecipeDetailActivity extends BaseActivity<RecipeDetailActivityViewModel>
        implements SelectedStepChangeListener {
    public static final String KEY_RECIPE_ID = "RecipeID";
    public static final String TAG_RECIPE_DETAIL_FRAGMENT = "RecipeDetailFragment";

    @BindView(R.id.toolbar) Toolbar toolbar;

    private boolean isTablet;
    private boolean isLandscape;
    private RecipeDetailFragment detailFragment;
    private RecipeStepDetailFragment stepDetailFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isTablet = getResources().getBoolean(R.bool.isTablet);
        isLandscape = getResources().getBoolean(R.bool.isLandscape);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        int recipeId = getIntent().getIntExtra(KEY_RECIPE_ID, RecipeRepository.INVALID_RECIPE_ID);

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (isTablet && isLandscape) {
            detailFragment = (RecipeDetailFragment) fragmentManager.findFragmentById(R.id.recipeListFragment);
            stepDetailFragment = (RecipeStepDetailFragment) fragmentManager.findFragmentById(R.id.recipeStepDetailFragment);

            detailFragment.setRecipeId(recipeId);

            stepDetailFragment.setRecipeId(recipeId);
        } else {
            Fragment fragment = fragmentManager.findFragmentByTag(TAG_RECIPE_DETAIL_FRAGMENT);

            if (fragment == null) {
                fragmentManager
                        .beginTransaction()
                        .add(R.id.fragmentPlaceholder, RecipeDetailFragment.newInstance(recipeId), TAG_RECIPE_DETAIL_FRAGMENT)
                        .commit();
            }
        }

        viewModel.getViewState().observe(this, this::render);
        viewModel.setRecipeId(recipeId);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_recipe_detail;
    }

    @Override
    protected Class<RecipeDetailActivityViewModel> getViewModelClass() {
        return RecipeDetailActivityViewModel.class;
    }

    private void render(@Nullable RecipeDetailActivityViewState viewState) {
        if (viewState == null) {
            return;
        }

        if (viewState instanceof FinishViewState) {
            render((FinishViewState) viewState);
        }

        if (viewState instanceof RecipeDetailViewState) {
            render((RecipeDetailViewState) viewState);
        }
    }

    private void render(FinishViewState viewState) {
        NavUtils.navigateUpFromSameTask(this);
        viewModel.onFinished();
    }

    private void render(RecipeDetailViewState viewState) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(viewState.recipeName);
        }
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

    @Override
    public void startActivityFromFragment(Fragment fragment, Intent intent, int requestCode, @Nullable Bundle options) {
        if (isTablet && isLandscape) {
            stepDetailFragment.setStepId(intent.getIntExtra(RecipeStepDetailActivity.KEY_STEP_ID, RecipeRepository.INVALID_RECIPE_STEP_ID));
        } else {
            super.startActivityFromFragment(fragment, intent, requestCode, options);
        }
    }

    @Override
    public void onSelectedStepChanged(Step step) {
        if (isTablet && isLandscape) {
            detailFragment.onStepClicked(step);
        }
    }
}
