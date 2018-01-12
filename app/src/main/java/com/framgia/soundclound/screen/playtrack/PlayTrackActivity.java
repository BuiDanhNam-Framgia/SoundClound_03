package com.framgia.soundclound.screen.playtrack;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.framgia.soundclound.R;
import com.framgia.soundclound.Service.MusicServiceTest;
import com.framgia.soundclound.data.model.Track;

import java.util.ArrayList;

/**
 * Created by Sony on 1/11/2018.
 */
public class PlayTrackActivity extends AppCompatActivity {
    private Intent playIntent;
    private MusicServiceTest mMusicService;
    private boolean mMusicBound = false;
    private ArrayList<Track> mTracks;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicServiceTest.MusicBinder musicServiceBinder = (MusicServiceTest.MusicBinder) service;
            mMusicService = musicServiceBinder.getService();
            mMusicService.setList(mTracks);
            mMusicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMusicBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_track);
        ImageView imageView = findViewById(R.id.image_pause);
        ImageView imageViewnext = findViewById(R.id.image_next);
        getlistTrack();
        // // TODO: 12/1/2018
//        setController();

        final SeekBar seekBar = findViewById(R.id.seek_bar_play_track);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMusicService.play(0);
//                seekBar.setMax(40000);

            }
        });
        imageViewnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMusicService.play(1);
//                seekBar.setMax(mMusicService.getDuration());
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
//                    mMusicService.seekto(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(this, MusicServiceTest.class);
            bindService(playIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    public void getlistTrack() {
        mTracks = new ArrayList<>();
        Track track = new Track();
        Track track2 = new Track();
        final String uri =
                "https://zmp3-mp3-s1.zadn.vn/ffe961cab58e5cd0059f/68695268730484457?authen=exp=1515821631~acl=/ffe961cab58e5cd0059f/*~hmac=56b6d2c5139189c8fe71483ea2c8229d";
        final String uri2 = "https://zmp3-mp3-s1.zadn.vn/7147afa27be692b8cbf7/3858972813232405650?authen=exp=1515822718~acl=/7147afa27be692b8cbf7/*~hmac=4e057f384a403d22dbea1fea782c45b6";
        track.setPermalinkUrl(uri);
        track2.setPermalinkUrl(uri2);
        mTracks.add(track);
        mTracks.add(track2);
        //// TODO: 12/1/2018
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMusicBound) {
            unbindService(mServiceConnection);
        }
        super.onDestroy();
    }
}
