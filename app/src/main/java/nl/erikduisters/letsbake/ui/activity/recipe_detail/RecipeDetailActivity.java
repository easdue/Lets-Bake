package nl.erikduisters.letsbake.ui.activity.recipe_detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import nl.erikduisters.letsbake.R;
import nl.erikduisters.letsbake.ui.BaseActivity;

/**
 * Created by Erik Duisters on 24-03-2018.
 */

public class RecipeDetailActivity extends BaseActivity<RecipeDetailActivityViewModel> implements ViewPager.OnPageChangeListener {
    public static final String KEY_RECIPE_ID = "RecipeID";

    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
