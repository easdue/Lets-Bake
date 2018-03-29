package nl.erikduisters.letsbake.ui.fragment.recipe_detail;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import nl.erikduisters.letsbake.data.model.Step;
import nl.erikduisters.letsbake.util.ViewHolderClickListener;

/**
 * Created by Erik Duisters on 24-03-2018.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ViewHolderClickListener<RecyclerView.ViewHolder> {
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void onClick(RecyclerView.ViewHolder viewHolder) {

    }

    public interface OnItemClickListener {
        void onItemClick(Step step);
    }

}
