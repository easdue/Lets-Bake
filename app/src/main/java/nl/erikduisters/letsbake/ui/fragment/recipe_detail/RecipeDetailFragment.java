package nl.erikduisters.letsbake.ui.fragment.recipe_detail;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nl.erikduisters.letsbake.R;
import nl.erikduisters.letsbake.data.model.Step;
import nl.erikduisters.letsbake.ui.BaseFragment;

/**
 * Created by Erik Duisters on 24-03-2018.
 */

public class RecipeDetailFragment extends BaseFragment<RecipeDetailFragmentViewModel> implements RecipeAdapter.OnItemClickListener {

    private final RecipeAdapter recipeAdapter;
    private Context context;

    public static RecipeDetailFragment newInstance(int movieId) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();

        Bundle args = new Bundle();
        //TODO: put recipe_id into bundle

        fragment.setArguments(args);

        return fragment;
    }

    public RecipeDetailFragment() {
        recipeAdapter = new RecipeAdapter();
        //recipeAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        return v;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_movie_detail;
    }

    @Override
    protected Class<RecipeDetailFragmentViewModel> getViewModelClass() {
        return RecipeDetailFragmentViewModel.class;
    }

    private void render(@Nullable RecipeDetailFragmentViewState viewState) {
        if (viewState == null) {
            return;
        }
    }

    @Override
    public void onItemClick(Step recipe) {

    }
}
