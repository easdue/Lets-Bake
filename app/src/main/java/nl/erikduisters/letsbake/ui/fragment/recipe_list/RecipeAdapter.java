package nl.erikduisters.letsbake.ui.fragment.recipe_list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nl.erikduisters.letsbake.R;
import nl.erikduisters.letsbake.data.model.Recipe;
import nl.erikduisters.letsbake.glide.GlideApp;
import timber.log.Timber;

/**
 * Created by Erik Duisters on 24-03-2018.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> implements View.OnClickListener {
    public interface OnItemClickListener {
        void onItemClick(Recipe recipe);
    }

    private List<Recipe> recipes;
    private RecyclerView recyclerView;
    private OnItemClickListener onItemClickListener;

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;

        //TODO: Use ListUtils to generate individual changed/removed calls
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
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

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_row, parent, false);

        v.setOnClickListener(this);

        return new RecipeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder viewHolder, int position) {
        viewHolder.bind(recipes.get(position));
    }

    @Override
    public int getItemCount() {
        return recipes == null ? 0 : recipes.size();
    }

    @Override
    public void onClick(View v) {
        int position = recyclerView.getChildViewHolder(v).getAdapterPosition();

        Timber.d("onClick(), position: %d", position);

        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(recipes.get(position));
        }
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recipeImage) ImageView recipeImage;
        @BindView(R.id.recipeName) TextView recipeName;
        @BindView(R.id.serves) TextView serves;

        public RecipeViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public void bind(Recipe recipe) {
            GlideApp.with(itemView.getContext())
                    .load(recipe.getImage())
                    .fallback(R.drawable.vector_drawable_ic_no_image)
                    .error(R.drawable.vector_drawable_ic_no_image)
                    .into(recipeImage);

            recipeName.setText(recipe.getName());
            serves.setText(String.valueOf(recipe.getServings()));
        }
    }
}
