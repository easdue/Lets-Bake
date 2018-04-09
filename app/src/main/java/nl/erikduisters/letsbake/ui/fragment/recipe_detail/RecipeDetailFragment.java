package nl.erikduisters.letsbake.ui.fragment.recipe_detail;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import nl.erikduisters.letsbake.R;
import nl.erikduisters.letsbake.data.model.Status;
import nl.erikduisters.letsbake.data.model.Step;
import nl.erikduisters.letsbake.ui.BaseFragment;
import nl.erikduisters.letsbake.ui.fragment.recipe_detail.RecipeDetailFragmentViewState.RecipeDetailViewState;
import nl.erikduisters.letsbake.ui.widget.IngredientService;
import nl.erikduisters.letsbake.util.MenuUtil;
import nl.erikduisters.letsbake.util.MyMenuItem;

/**
 * Created by Erik Duisters on 24-03-2018.
 */

public class RecipeDetailFragment extends BaseFragment<RecipeDetailFragmentViewModel> implements RecipeAdapter.OnStepClickedListener {
    private final static String KEY_RECIPE_ID = "RecipeId";
    private final static String KEY_SELECTED_STEP_ID = "SelectedStepId";
    private final static String KEY_LAYOUTMANAGER_STATE = "LayoutManagerState";

    private RecipeAdapter recipeAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Parcelable layoutManagerState;
    private boolean isTablet;
    private boolean isLandscape;
    private int selectedStepId;
    @NonNull private List<MyMenuItem> optionsMenu;

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.textView) TextView textView;

    public static RecipeDetailFragment newInstance(int recipeId) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();

        Bundle args = new Bundle();
        args.putInt(KEY_RECIPE_ID, recipeId);

        fragment.setArguments(args);

        return fragment;
    }

    public RecipeDetailFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        optionsMenu = new ArrayList<>();

        isTablet = getResources().getBoolean(R.bool.isTablet);
        isLandscape = getResources().getBoolean(R.bool.isLandscape);

        recipeAdapter = new RecipeAdapter();
        recipeAdapter.setOnStepClickedListener(this);
        recipeAdapter.setSelectionEnabled(isTablet && isLandscape);

        viewModel.getRecipeDetailViewState().observe(this, this::render);
        viewModel.getStartActivityViewState().observe(this, this::render);
        viewModel.getUpdateWidgetViewState().observe(this, this::handle);

        Bundle args = getArguments();

        if (args != null && args.containsKey(KEY_RECIPE_ID)) {
            viewModel.setRecipeId(getArguments().getInt(KEY_RECIPE_ID));
        }

        selectedStepId = -1;

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_LAYOUTMANAGER_STATE)) {
            RecipeDetailFragmentViewState.RecipeDetailViewState state = viewModel.getRecipeDetailViewState().getValue();

            if (state == null || state.status == Status.LOADING) {
                layoutManagerState = savedInstanceState.getParcelable(KEY_LAYOUTMANAGER_STATE);
            }

            selectedStepId = savedInstanceState.getInt(KEY_SELECTED_STEP_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recipeAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        return v;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(KEY_LAYOUTMANAGER_STATE, layoutManager.onSaveInstanceState());
        outState.putInt(KEY_SELECTED_STEP_ID, selectedStepId);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_recipe_detail;
    }

    @Override
    protected Class<RecipeDetailFragmentViewModel> getViewModelClass() {
        return RecipeDetailFragmentViewModel.class;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        recipeAdapter.setOnStepClickedListener(null);
    }

    private void render(@Nullable RecipeDetailViewState viewState) {
        if (viewState == null) {
            return;
        }

        optionsMenu = viewState.optionsMenu;
        invalidateOptionsMenu();

        switch (viewState.status) {
            case Status.SUCCESS:
                progressBar.setVisibility(View.INVISIBLE);
                textView.setVisibility(View.INVISIBLE);

                recipeAdapter.setRecipe(viewState.recipe);

                if (selectedStepId == -1) {
                    Step step = viewState.recipe.getSteps().get(0);

                    selectedStepId = step.getId();

                    if (isTablet && isLandscape) {
                        viewModel.onStepClicked(step);
                    }
                }

                recipeAdapter.setSelectedStepId(selectedStepId);

                if (layoutManagerState != null) {
                    layoutManager.onRestoreInstanceState(layoutManagerState);
                    layoutManagerState = null;
                }
                break;
            case Status.ERROR:
                progressBar.setVisibility(View.GONE);
                textView.setText(getString(viewState.errorLabel, viewState.errorArgument));
                textView.setVisibility(View.VISIBLE);
                break;
            case Status.LOADING:
                progressBar.setVisibility(View.VISIBLE);
                textView.setText(viewState.loadingMessage);
                textView.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void render(@Nullable RecipeDetailFragmentViewState.StartActivityViewState viewState) {
        if (viewState == null) {
            return;
        }

        startActivity(viewState.getIntent(getContext()));

        viewModel.onActivityStarted();
    }

    private void handle(@Nullable RecipeDetailFragmentViewState.UpdateWidgetViewState viewState) {
        if (viewState == null) {
            return;
        }

        IngredientService.startActionUpdateIngredients(getContext());

        viewModel.widgetUpdated();
    }

    @Override
    public void onStepClicked(Step step) {
        viewModel.onStepClicked(step);

        selectedStepId = step.getId();

        if (isLandscape && isTablet) {
            recipeAdapter.setSelectedStepId(step.getId());
        }
    }

    public void setRecipeId(int recipeId) {
        viewModel.setRecipeId(recipeId);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.recipe_detail_fragment, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuUtil.updateMenu(menu, optionsMenu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.showInWidget:
                viewModel.onMenuItemClicked(item.getItemId());
                return true;
        }

        return false;
    }
}
