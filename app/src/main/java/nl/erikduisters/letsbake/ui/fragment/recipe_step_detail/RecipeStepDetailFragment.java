package nl.erikduisters.letsbake.ui.fragment.recipe_step_detail;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import nl.erikduisters.letsbake.R;
import nl.erikduisters.letsbake.data.model.Status;
import nl.erikduisters.letsbake.data.model.Step;
import nl.erikduisters.letsbake.ui.BaseFragment;
import nl.erikduisters.letsbake.ui.fragment.recipe_step_detail.RecipeStepDetailFragmentViewState.RecipeStepDetailViewState;
import nl.erikduisters.letsbake.util.CircularPageIndicatorDecorator;
import timber.log.Timber;

/**
 * Created by Erik Duisters on 24-03-2018.
 */

public class RecipeStepDetailFragment extends BaseFragment<RecipeStepDetailFragmentViewModel> {
    public static final String KEY_RECIPE_ID = "RecipeId";
    public static final String KEY_RECIPE_STEP_ID = "RecipeStepId";
    public static final String KEY_LAYOUT_MANAGER_STATE = "LayoutManagerState";
    public static final String KEY_PLAYBACK_POSISTION = "PlayBackPosition";

    private StepAdapter stepAdapter;
    private LinearLayoutManager layoutManager;
    private Parcelable layoutManagerState;
    private int currentStepId;
    private long currentPlaybackPosition;
    private Context context;
    private SimpleExoPlayer simpleExoPlayer;
    private DataSource.Factory dataSourceFactory;
    private final OnScrollListener onScrollListener;

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.textView) TextView textView;

    public RecipeStepDetailFragment() {
        onScrollListener = new OnScrollListener();
    }

    public static RecipeStepDetailFragment newInstance(int recipeId, int stepId) {
        RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();

        Bundle args = new Bundle();
        args.putInt(KEY_RECIPE_ID, recipeId);
        args.putInt(KEY_RECIPE_STEP_ID, stepId);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stepAdapter = new StepAdapter();
        initializeExoPlayer();

        viewModel.getRecipeStepDetailViewState().observe(this, this::render);

        Bundle args = getArguments();

        if (args == null) {
            throw new RuntimeException("You must instantiate RecipeStepDetailFragment by calling newInstance(int, int)");
        }

        if (savedInstanceState != null) {
            layoutManagerState = savedInstanceState.getParcelable(KEY_LAYOUT_MANAGER_STATE);
            currentStepId = savedInstanceState.getInt(KEY_RECIPE_STEP_ID);
            currentPlaybackPosition = savedInstanceState.getLong(KEY_PLAYBACK_POSISTION);
        } else {
            currentStepId = args.getInt(KEY_RECIPE_STEP_ID);
            currentPlaybackPosition = -1;
        }

        viewModel.setRecipeId(args.getInt(KEY_RECIPE_ID));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        ButterKnife.bind(this, v);

        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(stepAdapter);
        recyclerView.addOnScrollListener(onScrollListener);
        recyclerView.addItemDecoration(new CircularPageIndicatorDecorator(context));

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        simpleExoPlayer.release();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_recipe_step_detail;
    }

    @Override
    protected Class<RecipeStepDetailFragmentViewModel> getViewModelClass() {
        return RecipeStepDetailFragmentViewModel.class;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(KEY_LAYOUT_MANAGER_STATE, layoutManager.onSaveInstanceState());
        outState.putInt(KEY_RECIPE_STEP_ID, currentStepId);
        outState.putLong(KEY_PLAYBACK_POSISTION, simpleExoPlayer.getCurrentPosition());

    }

    void render(@Nullable RecipeStepDetailViewState viewState) {
        if (viewState == null) {
            return;
        }

        switch (viewState.status) {
            case Status.LOADING:
                progressBar.setVisibility(View.VISIBLE);
                textView.setText(viewState.loadingMessage);
                textView.setVisibility(View.VISIBLE);
                break;
            case Status.SUCCESS:
                progressBar.setVisibility(View.INVISIBLE);
                textView.setVisibility(View.INVISIBLE);
                stepAdapter.setSteps(viewState.recipe.getSteps());

                recyclerView.scrollToPosition(currentStepId);
                if (layoutManagerState != null) {
                    layoutManager.onRestoreInstanceState(layoutManagerState);
                    layoutManagerState = null;
                }

                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        onScrollListener.onScrolled(recyclerView, 1, 0);
                    }
                });
                break;
            case Status.ERROR:
                progressBar.setVisibility(View.GONE);
                textView.setText(getString(viewState.errorLabel, viewState.errorArgument));
                textView.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void initializeExoPlayer() {
        TrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory(new DefaultBandwidthMeter());
        TrackSelector trackSelector = new DefaultTrackSelector(trackSelectionFactory);

        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

        dataSourceFactory = new DefaultDataSourceFactory(context, Util.getUserAgent(getContext(), context.getString(R.string.app_name)), null);
        //dataSourceFactory = new DefaultHttpDataSourceFactory(Util.getUserAgent(context, context.getString(R.string.app_name)));
    }

    private class OnScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (dx == 0 && dy == 0) {
                return;
            }

            int pos = layoutManager.findFirstCompletelyVisibleItemPosition();
            if (pos != RecyclerView.NO_POSITION) {
                Timber.e("Showing pos: %d", pos);
                Step step = stepAdapter.getSteps().get(pos);
                currentStepId = step.getId();

                setMediaSource(step);
            }
        }

        private void setMediaSource(Step step) {
            boolean videoAvailable = !step.getVideoURL().isEmpty() && step.getVideoURL().endsWith(".mp4");

            simpleExoPlayer.stop();

            if (videoAvailable) {
                MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(Uri.parse(step.getVideoURL()));

                simpleExoPlayer.prepare(mediaSource);
                if (currentPlaybackPosition != -1) {
                    simpleExoPlayer.seekTo(currentPlaybackPosition);
                    currentPlaybackPosition = -1;
                }

                stepAdapter.setSimpleExoPlayer(simpleExoPlayer, currentStepId);

                simpleExoPlayer.setPlayWhenReady(true);
            }
        }
    }

}
