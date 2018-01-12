package com.framgia.soundclound.Service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.framgia.BaseMediaPlayer;
import com.framgia.soundclound.data.model.Track;

import java.util.ArrayList;

/**
 * Created by Bui Danh Nam on 11/1/2018.
 */

public class MusicService extends Service implements
        BaseMediaPlayer, MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
    private MediaPlayer mMediaPlayer;
    private ArrayList<Track> mTracks;
    private int mCurentIndex;
    private final IBinder mIBinder = new MusicServiceBinder();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mTracks = (ArrayList<Track>) intent.getSerializableExtra("a");
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mCurentIndex = 0;
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mIBinder;
    }

    @Override
    public void play(int index) {
        mCurentIndex = index;
        if (mTracks != null) {
            Track track = mTracks.get(mCurentIndex);
            String urlMusic = track.getPermalinkUrl();
            try {
                mMediaPlayer.reset();
                mMediaPlayer.setDataSource(urlMusic);
                mMediaPlayer.prepareAsync();
            } catch (Exception e) {
                Log.e("MUSIC SERVICE", "Error setting data source", e);
            }

        }
    }

    @Override
    public void pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }  

    @Override
    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
    }

    @Override
    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
        }
    }

    @Override
    public void seekto(int position) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(position);
        }
    }

    @Override
    public void next() {
        if (mCurentIndex < (mTracks.size() - 1)) {
            play(mCurentIndex + 1);
        } else {
            play(0);
        }
    }

    @Override
    public void prev() {
        if (mCurentIndex > 0) {
            play(mCurentIndex - 1);
        } else {
            play(mTracks.size() - 1);
        }
    }

    @Override
    public int getDuration() {
        return mMediaPlayer.getDuration();
    }

    @Override
    public int getPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    public class MusicServiceBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }
}
