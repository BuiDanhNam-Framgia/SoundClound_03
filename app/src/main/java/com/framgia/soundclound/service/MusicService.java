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
import android.support.v4.app.TaskStackBuilder;

import com.framgia.soundclound.BaseMediaPlayer;
import com.framgia.soundclound.data.model.Track;
import com.framgia.soundclound.screen.main.MainActivity;
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

    private static final String LOG_TAG = "fuck";
    private final IBinder musicBind = new MusicBinder();
    private MediaPlayer player;
    private List<Track> mTracks;
    private int curentTrack;
    private String songTitle = "";
    private boolean mShuffle = false;
    private boolean mRepeat = false;
    private Random mRandom;
    private String mTitleTrack;
    private String mArtist;
    private String mImageTrack;

    @Override
    public void onCreate() {
        super.onCreate();
        super.onCreate();
        curentTrack = 0;
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

    public void setList(List<Track> tracks) {
        mTracks = tracks;
    }

    public void setCurentTrack(int curentTrack) {
        this.curentTrack = curentTrack;
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
            play();
        } else {
            next();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        next();
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
        if (mTracks != null && curentTrack < mTracks.size()) {
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
        curentTrack++;
        if (curentTrack > (mTracks.size() - 1)) {
            curentTrack = 0;
            play();
        } else {
            play();
        }
    }

    @Override
    public void prev() {
        curentTrack--;
        if (curentTrack < 0) {
            curentTrack = mTracks.size() - 1;
            play();
        } else {
            play();
        }
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        super.onDestroy();
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
        notIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notIntent);
        PendingIntent pendInt = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);
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
        Intent intentDel = new Intent(getApplicationContext(), MusicService.class);
        intentDel.setAction("DEL");
        PendingIntent pendingDelIntent = PendingIntent.getService(getApplicationContext(),
                1, intentDel, PendingIntent.FLAG_UPDATE_CURRENT);
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
                .setTicker(mTitleTrack)
                .setContentTitle(mTitleTrack)
                .setContentText(mArtist)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setDeleteIntent(pendingDelIntent)
                .addAction(android.R.drawable.ic_media_previous, null,
                        ppreviousIntent)
                .addAction(iconPlayPause, null,
                        pplayIntent)
                .addAction(android.R.drawable.ic_media_next, null,
                        pnextIntent);
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
