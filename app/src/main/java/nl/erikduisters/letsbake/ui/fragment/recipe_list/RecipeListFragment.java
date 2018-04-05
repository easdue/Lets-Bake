package nl.erikduisters.letsbake.ui.fragment.recipe_list;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import nl.erikduisters.letsbake.R;
import nl.erikduisters.letsbake.data.model.Recipe;
import nl.erikduisters.letsbake.data.model.Status;
import nl.erikduisters.letsbake.ui.BaseFragment;

/**
 * Created by Erik Duisters on 24-03-2018.
 */

//TODO: increase font size for higher res devices
public class RecipeListFragment extends BaseFragment<RecipeListFragmentViewModel> implements RecipeAdapter.OnItemClickListener {
    private final static String KEY_LAYOUTMANAGER_STATE = "LayoutManagerState";

    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.textView) TextView textView;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    private RecyclerView.LayoutManager layoutManager;
    private RecipeAdapter recipeAdapter;
    private Parcelable layoutManagerState;
    private boolean isTablet;
    private boolean isLandscape;

    public static RecipeListFragment newInstance() {
        return new RecipeListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel.getRecipeViewState().observe(this, this::render);
        viewModel.getStartActivityViewState().observe(this, this::render);

        recipeAdapter = new RecipeAdapter();
        recipeAdapter.setOnItemClickListener(this);

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_LAYOUTMANAGER_STATE)) {
            RecipeListFragmentViewState.RecipeViewState state = viewModel.getRecipeViewState().getValue();

            if (state == null || state.status == Status.LOADING) {
                layoutManagerState = savedInstanceState.getParcelable(KEY_LAYOUTMANAGER_STATE);
            }
        }

        isTablet = getResources().getBoolean(R.bool.isTablet);
        isLandscape = getResources().getBoolean(R.bool.isLandscape);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        if (isTablet && isLandscape) {
            layoutManager = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);
        } else {
            layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recipeAdapter);

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        recipeAdapter.setOnItemClickListener(null);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_recipe_list;
    }

    @Override
    protected Class<RecipeListFragmentViewModel> getViewModelClass() {
        return RecipeListFragmentViewModel.class;
    }

    private void render(@Nullable RecipeListFragmentViewState.RecipeViewState viewState) {
        if (viewState == null) {
            return;
        }

        if (viewState.status == Status.LOADING) {
            progressBar.setVisibility(View.VISIBLE);
            textView.setText(viewState.loadingMessage);
            textView.setVisibility(View.VISIBLE);
        } else if (viewState.status == Status.SUCCESS) {
            progressBar.setVisibility(View.INVISIBLE);
            if (viewState.recipeList.isEmpty()) {
                textView.setText(viewState.emptyRecipeListMessage);
                textView.setVisibility(View.VISIBLE);
            } else {
                textView.setVisibility(View.INVISIBLE);
                recipeAdapter.setRecipes(viewState.recipeList);

                if (layoutManagerState != null) {
                    layoutManager.onRestoreInstanceState(layoutManagerState);
                    layoutManagerState = null;
                }
            }
        } else {
            progressBar.setVisibility(View.GONE);
            textView.setText(getString(viewState.errorLabel, viewState.errorArgument));
            textView.setVisibility(View.VISIBLE);
        }
    }

    private void render(@Nullable RecipeListFragmentViewState.StartActivityViewState viewState) {
        if (viewState == null) {
            return;
        }

        startActivity(viewState.getIntent(getContext()));

        viewModel.onActivityStarted();
    }

    @Override
    public void onItemClick(Recipe recipe) {
        viewModel.onRecipeClicked(recipe);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(KEY_LAYOUTMANAGER_STATE, layoutManager.onSaveInstanceState());
    }
}
