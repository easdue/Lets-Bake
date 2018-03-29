package nl.erikduisters.letsbake.ui.activity.recipe_detail;

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
import nl.erikduisters.letsbake.ui.fragment.recipe_detail.RecipeDetailFragment;

/**
 * Created by Erik Duisters on 24-03-2018.
 */

public class RecipeDetailActivity extends BaseActivity<RecipeDetailActivityViewModel> {
    public static final String KEY_RECIPE_ID = "RecipeID";
    public static final String TAG_RECIPE_DETAIL_FRAGMENT = "RecipeDetailFragment";

    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        int recipeId = getIntent().getIntExtra(KEY_RECIPE_ID, RecipeRepository.IMVALID_RECIPE_ID);

        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment fragment = fragmentManager.findFragmentByTag(TAG_RECIPE_DETAIL_FRAGMENT);

        if (fragment == null) {
            fragmentManager
                    .beginTransaction()
                    .add(R.id.fragmentPlaceholder, RecipeDetailFragment.newInstance(recipeId), TAG_RECIPE_DETAIL_FRAGMENT)
                    .commit();
        }

        viewModel.getViewState().observe(this, this::render);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_detail;
    }

    @Override
    protected Class<RecipeDetailActivityViewModel> getViewModelClass() {
        return RecipeDetailActivityViewModel.class;
    }

    private void render(@Nullable RecipeDetailActivityViewState viewState) {
        if (viewState == null) {
            return;
        }

        if (viewState instanceof RecipeDetailActivityViewState.FinishViewState) {
            NavUtils.navigateUpFromSameTask(this);
            viewModel.onFinished();
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
}
