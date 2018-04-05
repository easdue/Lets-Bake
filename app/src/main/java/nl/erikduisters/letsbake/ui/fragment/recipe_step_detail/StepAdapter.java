package nl.erikduisters.letsbake.ui.fragment.recipe_step_detail;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nl.erikduisters.letsbake.R;
import nl.erikduisters.letsbake.data.model.Step;
import nl.erikduisters.letsbake.glide.GlideApp;
import timber.log.Timber;

/**
 * Created by Erik Duisters on 24-03-2018.
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {
    private @NonNull List<Step> steps;
    private RecyclerView recyclerView;
    private PlayerView prevPlayerView;
    private final boolean showDescription;

    StepAdapter(boolean showDescription) {
        this.showDescription = showDescription;

        steps = new ArrayList<>();
    }

    void setSteps(@NonNull List<Step> steps) {
        this.steps = steps;

        notifyDataSetChanged();
    }

    @NonNull List<Step> getSteps() {
        return steps;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        this.recyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);

        this.recyclerView = null;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_step_detail_list_item, parent, false);

        return new ViewHolder(v, showDescription);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(steps.get(position));
    }

    @Override
    public int getItemCount() {
        return steps == null ? 0 : steps.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.playerView) PlayerView playerView;
        @BindView(R.id.imageView) ImageView imageView;
        TextView textView;

        private Step step;
        private final boolean showDescription;

        ViewHolder(View itemView, boolean showDescription) {
            super(itemView);

            this.showDescription = showDescription;

            ButterKnife.bind(this, itemView);

            if (showDescription ) {
                textView = itemView.findViewById(R.id.stepDescription);
            }
        }

        void bind(Step step) {
            Timber.e("bind stepId=%d", step.getId());

            boolean videoAvailable = !step.getVideoURL().isEmpty() && step.getVideoURL().endsWith(".mp4");
            boolean thumbnailAvailable = !step.getThumbnailURL().isEmpty() && isImageUrl(step.getThumbnailURL());

            this.step = step;

            if (videoAvailable) {
                playerView.setVisibility(View.VISIBLE);
            } else {
                playerView.setVisibility(View.INVISIBLE);
            }

            if (thumbnailAvailable) {
                GlideApp.with(imageView.getContext())
                        .load(step.getThumbnailURL())
                        .fallback(R.drawable.vector_drawable_ic_no_image)
                        .error(R.drawable.vector_drawable_ic_no_image)
                        .into(imageView);

                imageView.setVisibility(View.VISIBLE);

                playerView.setVisibility(View.INVISIBLE);
            } else {
                imageView.setVisibility(View.INVISIBLE);
            }

            if (!videoAvailable && !thumbnailAvailable) {
                imageView.setImageResource(R.drawable.vector_drawable_ic_no_image);
                imageView.setVisibility(View.VISIBLE);
            }

            if (showDescription) {
                textView.setText(step.getDescription());
            }
        }

        private boolean isImageUrl(String url) {
            String lowerCaseUrl = url.toLowerCase();

            return lowerCaseUrl.endsWith(".jpg") || lowerCaseUrl.endsWith(".gif") || lowerCaseUrl.endsWith(".png");
        }
    }

    void setSimpleExoPlayer(ExoPlayer exoPlayer, int adapterPos) {
        if (recyclerView == null) {
            return;
        }

        ViewHolder vh = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(adapterPos);

        if (prevPlayerView == null || vh.playerView.getPlayer() != prevPlayerView) {
            vh.playerView.setPlayer(exoPlayer);
        } else {
            PlayerView.switchTargetView(exoPlayer, prevPlayerView, vh.playerView);
        }

        prevPlayerView = vh.playerView;
    }
}
