package nl.erikduisters.letsbake.ui.fragment.recipe_step_detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nl.erikduisters.letsbake.ui.BaseFragment;

/**
 * Created by Erik Duisters on 24-03-2018.
 */

public class RecipeStepDetailFragment extends BaseFragment<RecipeStepDetailFragmentViewModel> {

    public RecipeStepDetailFragment() {}

    public static RecipeStepDetailFragment newInstance() {
        RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();

        return fragment;
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
    protected int getLayoutResId() {
        return 0;
    }

    @Override
    protected Class<RecipeStepDetailFragmentViewModel> getViewModelClass() {
        return RecipeStepDetailFragmentViewModel.class;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
