package nl.erikduisters.letsbake.ui.activity.main_activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import nl.erikduisters.letsbake.R;
import nl.erikduisters.letsbake.ui.BaseActivity;
import nl.erikduisters.letsbake.ui.fragment.recipe_list.RecipeListFragment;

public class MainActivity extends BaseActivity<MainActivityViewModel> {
    private static final String TAG_RECIPE_LIST_FRAGMENT = "RecipeListFragment";

    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);

        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment fragment = fragmentManager.findFragmentByTag(TAG_RECIPE_LIST_FRAGMENT);

        if (fragment == null) {
            fragmentManager
                    .beginTransaction()
                    .add(R.id.fragmentPlaceholder, RecipeListFragment.newInstance(), TAG_RECIPE_LIST_FRAGMENT)
                    .commit();
        }

        viewModel.getViewState().observe(this, this::render);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected Class<MainActivityViewModel> getViewModelClass() {
        return MainActivityViewModel.class;
    }

    private void render(@Nullable MainActivityViewState viewState) {
        if (viewState == null) {
            return;
        }
    }
}
