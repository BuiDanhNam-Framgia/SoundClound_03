package com.framgia.soundclound.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;

import com.framgia.soundclound.BaseMediaPlayer;
import com.framgia.soundclound.data.model.Track;
import com.framgia.soundclound.data.source.local.SharePreferences;
import com.framgia.soundclound.screen.playtrack.PlayTrackActivity;
import com.framgia.soundclound.util.Constant;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

/*
   service
 */

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener, BaseMediaPlayer {

    private final IBinder musicBind = new MusicBinder();
    private MediaPlayer player;
    private List<Track> mTracks;
    private int curentTrack;
    private boolean mShuffle = false;
    private boolean mRepeat = false;
    private Random mRandom;
    private String mTitleTrack;
    private String mArtist;
    private String mImageTrack;

    @Override
    public void onCreate() {
        super.onCreate();
        curentTrack = SharePreferences.getInstance().getIndex();
        mTracks = SharePreferences.getInstance().getListTrack();
        mRandom = new Random();
        player = new MediaPlayer();
        initMusicPlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            switch (action) {
                case Constant.ACTION_PLAY:
                    if (isPlay()) {
                        pause();
                        showNotifi();
                    } else {
                        start();
                        showNotifi();
                    }
                    break;
                case Constant.ACTION_NEXT:
                    next();
                    showNotifi();
                    break;
                case Constant.ACTION_PREV:
                    prev();
                    showNotifi();
                    break;
                case Constant.ACTION_DISMIS:
                    onDestroy();
                    break;
                default:
                    break;
            }
        }
        return START_NOT_STICKY;
    }

    public void initMusicPlayer() {
        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mRepeat) {
            play();
        } else if (mShuffle) {
            curentTrack = mRandom.nextInt((mTracks.size() - 1));
            SharePreferences.getInstance().putIndex(curentTrack);
            play();
        } else {
            next();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.seekTo(0);
        mp.start();
        showNotifi();

    }

    public int getPosition() {
        return player.getCurrentPosition();
    }

    public boolean isShuffle() {
        return mShuffle;
    }

    public boolean isRepeat() {
        return mRepeat;
    }

    @Override
    public boolean isPlay() {
        return player.isPlaying();
    }

    @Override
    public void play() {
        player.reset();
        curentTrack = SharePreferences.getInstance().getIndex();
        mTracks = SharePreferences.getInstance().getListTrack();
        Track track = mTracks.get(curentTrack);
        mArtist = track.getPublisherMetadata().getArtist();
        mTitleTrack = track.getTitle();
        mImageTrack = track.getArtworkUrl();
        String uri = track.getUri();
        try {
            player.setDataSource(uri);
            player.prepareAsync();
        } catch (Exception e) {
            Logger.getLogger(e.toString());
        }
    }


    public boolean checkTrackPlay(Track track) {
        Track trackCurent = mTracks.get(curentTrack);
        return track.getUri().equals(trackCurent.getUri());
    }

    @Override
    public void start() {
        player.start();
    }

    @Override
    public void pause() {
        if (player.isPlaying()) {
            player.pause();
        }
    }

    @Override
    public void stop() {
        if (player != null) {
            player.stop();
        }
    }

    @Override
    public void release() {
        if (player != null) {
            player.release();
        }
    }

    @Override
    public void seekto(int position) {
        if (player != null) {
            player.seekTo(position);
        }
    }

    @Override
    public void next() {
        curentTrack = SharePreferences.getInstance().getIndex();
        curentTrack++;
        if (curentTrack > (mTracks.size() - 1)) {
            curentTrack = 0;
            SharePreferences.getInstance().putIndex(curentTrack);
            play();
        } else {
            SharePreferences.getInstance().putIndex(curentTrack);
            play();
        }

    }

    @Override
    public void prev() {
        curentTrack = SharePreferences.getInstance().getIndex();
        curentTrack--;
        if (curentTrack < 0) {
            curentTrack = mTracks.size() - 1;
            SharePreferences.getInstance().putIndex(curentTrack);
            play();
        } else {
            SharePreferences.getInstance().putIndex(curentTrack);
            play();
        }
        SharePreferences.getInstance().putIndex(curentTrack);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        stopSelf();

    }

    public int getDuration() {
        return player.getDuration();
    }

    public void setShuffle() {
        if (mShuffle) {
            mShuffle = false;
        } else {
            mShuffle = true;
        }
    }

    public void setRepeat() {
        if (mRepeat) {
            mRepeat = false;
        } else {
            mRepeat = true;
        }
    }

    // // TODO: 15/1/2018
    public void showNotifi() {
        //notification
        Intent notIntent = new Intent(this, PlayTrackActivity.class);
        notIntent.setAction(Constant.ACTION_MAIN);
        notIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(getApplicationContext(),
                0,
                notIntent, 0);

        Intent previousIntent = new Intent(getApplicationContext(), MusicService.class);
        previousIntent.setAction(Constant.ACTION_PREV);
        PendingIntent ppreviousIntent = PendingIntent.getService(getApplicationContext(),
                0,
                previousIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent playIntent = new Intent(getApplicationContext(), MusicService.class);
        playIntent.setAction(Constant.ACTION_PLAY);
        PendingIntent pplayIntent = PendingIntent.getService(getApplicationContext(),
                0,
                playIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent nextIntent = new Intent(getApplicationContext(), MusicService.class);
        nextIntent.setAction(Constant.ACTION_NEXT);
        PendingIntent pnextIntent = PendingIntent.getService(getApplicationContext(),
                0,
                nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // dismis
        Intent dismisIntent = new Intent(getApplicationContext(), MusicService.class);
        previousIntent.setAction(Constant.ACTION_DISMIS);
        PendingIntent pdismisIntent = PendingIntent.getService(getApplicationContext(),
                0,
                previousIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        int iconPlayPause;
        if (isPlay()) {
            iconPlayPause = android.R.drawable.ic_media_play;
        } else {
            iconPlayPause = android.R.drawable.ic_media_pause;
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext(), null);
        builder.setContentIntent(pendInt)
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setContentTitle(mTitleTrack)
                .setContentText(mArtist)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//                .addAction(android.R.drawable.ic_media_previous, null,
//                        ppreviousIntent)
                .addAction(iconPlayPause, null,
                        pplayIntent)
                .addAction(android.R.drawable.ic_media_next, null,
                        pnextIntent)
                .addAction(android.R.drawable.ic_delete, null,
                        pdismisIntent);
        Notification not = builder.build();
        startForeground(Constant.ID_FOREGROUND_SERVICE, not);
    }

    /**
     * Binder
     */

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }
}
