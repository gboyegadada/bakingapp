package com.example.android.bakingapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
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
    private SimpleExoPlayerView mExoPlayerView;

    private boolean mShouldAutoPlay = true;

    private Uri mVideoUri;

    private String mUserAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:40.0) Gecko/20100101 Firefox/40.0";

    /**
     * The fragment argument representing the STEP that this fragment
     * represents.
     */
    public static final String ARG_STEP_ITEM = "step_item";
    private StepItem mItem;
    private View mRootView;

    private static String TAG = StepDetailFragment.class.getSimpleName();

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

            ActionBar appBar = ((AppCompatActivity)getActivity()).getSupportActionBar();

            if (appBar != null) {
                appBar.setTitle(mItem.getShortDescription());
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.step_detail, container, false);

        if (mItem != null) {
            ((TextView) mRootView.findViewById(R.id.step_detail)).setText(mItem.getDescription());

            String url = mItem.getVideoURL();
            if (!TextUtils.isEmpty(url)) {
                mVideoUri = Uri.parse(url);
            }
        }

        return mRootView;
    }


    private void initializePlayer() {
        if (null != mExoPlayerView || null == mVideoUri) {
            // Already initialized
            return;
        }

        // Otherwise initialize player
        mExoPlayerView = (SimpleExoPlayerView) getActivity().findViewById(R.id.sv_player);

        LoadControl loadControl = new DefaultLoadControl();
        TrackSelector trackSelector = new DefaultTrackSelector();

        // Create the player
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
        mExoPlayerView = (SimpleExoPlayerView) mRootView.findViewById(R.id.sv_player);

        //Set media controller
        if (null == mExoPlayerView) Log.d(TAG, " ----------- - mExoPlayerView is NULL");
        if (null == mExoPlayer) Log.d(TAG, " ----------- - mExoPlayer is NULL");
        mExoPlayerView.setUseController(true);
        mExoPlayerView.requestFocus();

        // Bind the player to the view.
        mExoPlayerView.setPlayer(mExoPlayer);
        mExoPlayer.setPlayWhenReady(mShouldAutoPlay);

        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource(
                mVideoUri,
                new DefaultDataSourceFactory(getContext(), mUserAgent),
                new DefaultExtractorsFactory(), null, null);
        // Prepare the player with the source.
        mExoPlayer.prepare(videoSource);
    }

    @Override
    public void onStart() {
        super.onStart();
        initializePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        initializePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    /**
     * Release the player when the fragment activity is destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }


}
