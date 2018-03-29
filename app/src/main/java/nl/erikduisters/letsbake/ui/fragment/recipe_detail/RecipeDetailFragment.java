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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import nl.erikduisters.letsbake.R;
import nl.erikduisters.letsbake.data.model.Status;
import nl.erikduisters.letsbake.data.model.Step;
import nl.erikduisters.letsbake.ui.BaseFragment;
import nl.erikduisters.letsbake.ui.fragment.recipe_detail.RecipeDetailFragmentViewState.RecipeDetailViewState;

/**
 * Created by Erik Duisters on 24-03-2018.
 */

public class RecipeDetailFragment extends BaseFragment<RecipeDetailFragmentViewModel> implements RecipeAdapter.OnStepClickedListener {
    private final static String KEY_RECIPE_ID = "RecipeId";
    private final static String KEY_LAYOUTMANAGER_STATE = "LayoutManagerState";

    private RecipeAdapter recipeAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Context context;
    private Parcelable layoutManagerState;

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

        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recipeAdapter = new RecipeAdapter();
        recipeAdapter.setOnStepClickedListener(this);

        viewModel.getRecipeDetailViewState().observe(this, this::render);
        viewModel.setRecipeId(getArguments().getInt(KEY_RECIPE_ID));

        viewModel.getStartActivityViewState().observe(this, this::render);

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_LAYOUTMANAGER_STATE)) {
            RecipeDetailFragmentViewState.RecipeDetailViewState state = viewModel.getRecipeDetailViewState().getValue();

            if (state == null || state.status == Status.LOADING) {
                layoutManagerState = savedInstanceState.getParcelable(KEY_LAYOUTMANAGER_STATE);
            }
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

        switch (viewState.status) {
            case Status.SUCCESS:
                progressBar.setVisibility(View.INVISIBLE);
                textView.setVisibility(View.INVISIBLE);

                recipeAdapter.setRecipe(viewState.recipe);

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

    @Override
    public void onStepClicked(Step step) {
        viewModel.onStepClicked(step);
    }
}
