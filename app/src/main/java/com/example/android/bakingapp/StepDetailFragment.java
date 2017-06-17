package com.example.android.bakingapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.parceler.Parcels;

/**
 * A fragment representing a single Step detail screen.
 * This fragment is either contained in a {@link StepListActivity}
 * in two-pane mode (on tablets) or a {@link StepDetailActivity}
 * on handsets.
 */
public class StepDetailFragment extends Fragment {

    private SimpleExoPlayer mExoPlayer;
    private SurfaceView mSurfaceView;
    private SimpleExoPlayerView mExoPlayerView;

    private boolean mShouldAutoPlay = true;
    private int mResumeWindow;
    private long mResumePosition;

    private StepItem mStep;
    private Uri mVideoUri;
    private int RENDERER_COUNT = 300000;
    private int minBufferMs =    250000;
    private final int BUFFER_SEGMENT_SIZE = 64 * 1024;
    private final int BUFFER_SEGMENT_COUNT = 256;

    private String mUserAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:40.0) Gecko/20100101 Firefox/40.0";

    /**
     * The fragment argument representing the STEP that this fragment
     * represents.
     */
    public static final String ARG_STEP_ITEM = "step_item";
    private StepItem mItem;
    private View mRootView;


    private static String TAG = StepDetailFragment.class.getSimpleName();
    private TrackSelector mTrackSelector;
    private TrackSelection mTrackSelectionHelper;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StepDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_STEP_ITEM)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = Parcels.unwrap(getArguments().getParcelable(ARG_STEP_ITEM));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getShortDescription());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.step_detail, container, false);

        if (mItem != null) {
            ((TextView) mRootView.findViewById(R.id.step_detail)).setText(mItem.getDescription());
            mVideoUri = Uri.parse(mItem.getVideoURL());

            initializePlayer();
        }

        return mRootView;
    }


    private void initializePlayer() {
        mExoPlayerView = (SimpleExoPlayerView) getActivity().findViewById(R.id.sv_player);

        // Measures bandwidth during playback. Can be null if not required.
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        // Create the player
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);
        mExoPlayerView = (SimpleExoPlayerView) mRootView.findViewById(R.id.sv_player);

        //Set media controller
        if (null == mExoPlayerView) Log.d(TAG, " ----------- - mExoPlayerView is NULL");
        if (null == mExoPlayer) Log.d(TAG, " ----------- - mExoPlayer is NULL");
        mExoPlayerView.setUseController(true);
        mExoPlayerView.requestFocus();

        // Bind the player to the view.
        mExoPlayerView.setPlayer(mExoPlayer);
        mExoPlayer.setPlayWhenReady(mShouldAutoPlay);

        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                mUserAgent);
        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource(mVideoUri,
                dataSourceFactory, extractorsFactory, null, null);
        // Prepare the player with the source.
        mExoPlayer.prepare(videoSource);
    }

    /*
    @Override
    public void onNewIntent(Intent intent) {
        releasePlayer();
        mShouldAutoPlay = true;
        clearResumePosition();
        setIntent(intent);
    }
    */

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || mExoPlayer == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mShouldAutoPlay = mExoPlayer.getPlayWhenReady();
            updateResumePosition();
            mExoPlayer.release();
            mExoPlayer = null;
            mTrackSelector = null;
            mTrackSelectionHelper = null;
            // mEventLogger = null;
        }
    }

    private void updateResumePosition() {
        mResumeWindow = mExoPlayer.getCurrentWindowIndex();
        mResumePosition = mExoPlayer.isCurrentWindowSeekable() ? Math.max(0, mExoPlayer.getCurrentPosition())
                : C.TIME_UNSET;
    }

    private void clearResumePosition() {
        mResumeWindow = C.INDEX_UNSET;
        mResumePosition = C.TIME_UNSET;
    }

}
