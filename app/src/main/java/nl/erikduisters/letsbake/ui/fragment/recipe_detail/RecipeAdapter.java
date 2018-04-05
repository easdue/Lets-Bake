package nl.erikduisters.letsbake.ui.fragment.recipe_detail;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import nl.erikduisters.letsbake.R;
import nl.erikduisters.letsbake.data.model.Ingredient;
import nl.erikduisters.letsbake.data.model.Recipe;
import nl.erikduisters.letsbake.data.model.Step;
import timber.log.Timber;

/**
 * Created by Erik Duisters on 24-03-2018.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeDetailViewHolder> implements View.OnClickListener {
    public interface OnStepClickedListener {
        void onStepClicked(Step step);
    }

    private static final int VIEW_TYPE_INGREDIENT = 0;
    private static final int VIEW_TYPE_STEP = 1;

    private Recipe recipe;
    private RecyclerView recyclerView;
    private OnStepClickedListener onStepClickedListener;
    private int selectedStepId;
    private boolean isSelectionEnabled;

    RecipeAdapter() {
        selectedStepId = -1;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
        notifyDataSetChanged();
    }

    public void setOnStepClickedListener(OnStepClickedListener listener) {
        onStepClickedListener = listener;
    }

    void setSelectionEnabled(boolean isSelectionEnabled) {
        this.isSelectionEnabled = isSelectionEnabled;
    }

    void setSelectedStepId(int selectedStepId) {
        if (this.selectedStepId != -1) {
            setSelected(getAdapterPosForStepId(this.selectedStepId), false);
        }

        setSelected(getAdapterPosForStepId(selectedStepId), true);

        this.selectedStepId = selectedStepId;
    }

    private int getAdapterPosForStepId(int stepId) {
        int adapterPosition = RecyclerView.NO_POSITION;

        if (recipe != null) {
            for (int i=0, n=recipe.getSteps().size(); i < n; i++) {
                if (recipe.getSteps().get(i).getId() == stepId) {
                    adapterPosition = i + 1;
                    break;
                }
            }
        }

        return adapterPosition;
    }

    private void setSelected(int adapterPos, boolean selected) {
        RecyclerView.ViewHolder vh = recyclerView.findViewHolderForAdapterPosition(adapterPos);

        if (vh != null) {
            vh.itemView.setSelected(selected);
        }

        if (vh == null || recyclerView.isComputingLayout()) {
            recyclerView.post(() -> scrollIntoView(adapterPos));
        } else {
            scrollIntoView(adapterPos);
        }
    }

    private void scrollIntoView(int adapterPosition) {
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

            RecyclerView.ViewHolder vh = recyclerView.findViewHolderForAdapterPosition(layoutManager.findLastVisibleItemPosition());

            if (!isPositionCompletelyVisible(adapterPosition)) {
                layoutManager.scrollToPositionWithOffset(adapterPosition, recyclerView.getHeight() - vh.itemView.getHeight());
            }
        }
    }

    private boolean isPositionCompletelyVisible(int position) {
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

            int firstVisible = layoutManager.findFirstCompletelyVisibleItemPosition();
            int lastVisible = layoutManager.findLastCompletelyVisibleItemPosition();

            return firstVisible != RecyclerView.NO_POSITION && (position >= firstVisible && position <= lastVisible);
        }

        return true;
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        this.recyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(final RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);

        this.recyclerView = null;
    }

    @NonNull
    @Override
    public RecipeDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutResId = viewType == VIEW_TYPE_INGREDIENT ?
                R.layout.recipe_detail_ingredients_list_row :
                R.layout.recipe_detail_step_list_row;

        View v = LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);

        if (viewType == VIEW_TYPE_INGREDIENT) {
            return new IngredientViewHolder(v);
        } else {
            v.setOnClickListener(this);

            return new StepViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeDetailViewHolder holder, int position) {
        if (position == 0) {
            holder.bind(recipe, position);
        } else {
            Step step = recipe.getSteps().get(position - 1);
            holder.bind(recipe, position - 1);
            holder.itemView.setSelected(isSelectionEnabled && step.getId() == selectedStepId);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_INGREDIENT : VIEW_TYPE_STEP;
    }

    @Override
    public int getItemCount() {
        return recipe == null ? 0 : recipe.getSteps().size() + 1;
    }

    @Override
    public void onClick(View v) {
        StepViewHolder vh = (StepViewHolder) recyclerView.getChildViewHolder(v);

        if (vh.itemView.isSelected()) {
            return;
        }

        int position = vh.getAdapterPosition();

        Timber.d("onClick(), position: %d", position);

        Step step = recipe.getSteps().get(position - 1);

        //setSelectedStepId(step.getId());

        if (onStepClickedListener != null) {
            onStepClickedListener.onStepClicked(step);
        }
    }

    static abstract class RecipeDetailViewHolder extends RecyclerView.ViewHolder {
        public RecipeDetailViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public abstract void bind(Recipe recipe, int position);
    }

    static class IngredientViewHolder extends RecipeDetailViewHolder {
        @BindView(R.id.ingredientTable) TableLayout tableLayout;

        public IngredientViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bind(Recipe recipe, int position) {
            if (tableLayout.getChildCount() > 0) {
                return;
            }

            LayoutInflater inflater = LayoutInflater.from(itemView.getContext());

            for (Ingredient ingredient : recipe.getIngredients()) {
                TableRow row = (TableRow) inflater.inflate(R.layout.recipe_detail_ingredient_row, tableLayout, false);
                TextView quantityView = row.findViewById(R.id.quantity);
                TextView unitView = row.findViewById(R.id.unit);
                TextView ingredientView = row.findViewById(R.id.ingredient);

                quantityView.setText(ingredient.getQuantityAsString());
                unitView.setText(ingredient.getMeasure());
                ingredientView.setText(ingredient.getIngredient());

                tableLayout.addView(row, new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));
            }
        }
    }

    static class StepViewHolder extends RecipeDetailViewHolder {
        @BindView(R.id.shortDescription) TextView shortDescription;

        public StepViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bind(Recipe recipe, int position) {
            Timber.e("bind, position = %d", position);

            Step step = recipe.getSteps().get(position);

            shortDescription.setText(step.getShortDescription());
        }
    }
}
