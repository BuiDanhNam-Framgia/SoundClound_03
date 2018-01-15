package com.framgia.soundclound.screen.playtrack;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.framgia.soundclound.R;
import com.framgia.soundclound.data.model.Track;
import com.framgia.soundclound.data.source.local.SharePreferences;
import com.framgia.soundclound.databinding.ActivityPlayTrackBinding;
import com.framgia.soundclound.service.MusicService;

import java.util.List;

/**
 * Created by Sony on 1/11/2018.
 */

public class PlayTrackActivity extends AppCompatActivity {
    private PlayTrackViewModel mPlayTrackViewModel;
    private List<Track> mTracks;
    private int mIndexCurrent;

    public static Intent getInstance(Context context) {
        return new Intent(context, PlayTrackActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPlayTrackBinding activityPlayTrackBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_play_track);
        getlistTrack();
        mPlayTrackViewModel = new PlayTrackViewModel(mTracks, mIndexCurrent);
        activityPlayTrackBinding.setViewModel(mPlayTrackViewModel);
        bindService(mPlayTrackViewModel.getServiceConnection());
        getSupportActionBar().hide();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    public void bindService(ServiceConnection serviceConnection) {
        Intent playIntent = new Intent(this, MusicService.class);
        playIntent.setAction("START_SERVICE");
        bindService(playIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        startService(playIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayTrackViewModel.isMusicBound()) {
            unbindService(mPlayTrackViewModel.getServiceConnection());
        }
    }

    public void getlistTrack() {
        mTracks = SharePreferences.getInstance().getListTrack();
        mIndexCurrent = SharePreferences.getInstance().getIndex();
    }
}
