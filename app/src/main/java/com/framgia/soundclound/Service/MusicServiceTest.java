package com.framgia.soundclound.Service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.framgia.BaseMediaPlayer;
import com.framgia.soundclound.data.model.Track;
import com.framgia.soundclound.screen.playtrack.PlayTrackActivity;

import java.util.ArrayList;
import java.util.Random;

/*
   service
 */

public class MusicServiceTest extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener, BaseMediaPlayer {

    private MediaPlayer player;
    private ArrayList<Track> mTracks;
    private int curentTrack;
    private final IBinder musicBind = new MusicBinder();
    private String songTitle = "";
    private static final int NOTIFY_ID = 1;
    private boolean shuffle = false;
    private Random rand;

    @Override
    public void onCreate() {
        super.onCreate();
        super.onCreate();
        curentTrack = 0;
        rand = new Random();
        player = new MediaPlayer();
        initMusicPlayer();
    }

    public void initMusicPlayer() {
        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    public void setList(ArrayList<Track> tracks) {
        mTracks = tracks;
    }

    //binder
    public class MusicBinder extends Binder {
        public MusicServiceTest getService() {
            return MusicServiceTest.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        player.stop();
        player.release();
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (player.getCurrentPosition() > 0) {
            mp.reset();
            play(curentTrack);
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.v("Fuck", " Error");
        mp.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        //notification
        Intent notIntent = new Intent(this, PlayTrackActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentIntent(pendInt)
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setTicker(songTitle)
                .setOngoing(true)
                .setContentTitle("hihi")
                .setContentText(songTitle);
        Notification not = builder.build();
        startForeground(NOTIFY_ID, not);
    }

    public int getPosition() {
        return player.getCurrentPosition();
    }

    @Override
    public void play(int index) {
        curentTrack = index;
        player.reset();
        Track track = mTracks.get(curentTrack);
        songTitle = track.getTitle();
        Uri uri = Uri.parse(track.getPermalinkUrl());
        try {
            player.setDataSource(getApplicationContext(), uri);
            player.prepareAsync();
        } catch (Exception e) {
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
    }

    @Override
    public void pause() {
        if (player != null) {
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
        if (curentTrack < (mTracks.size() - 1)) {
            play(curentTrack + 1);
        } else {
            play(0);
        }
    }

    @Override
    public void prev() {
        if (curentTrack > 0) {
            play(curentTrack - 1);
        } else {
            play(mTracks.size() - 1);
        }
    }

    public int getDuration() {
        return player.getDuration();
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
    }

    public void setShuffle() {
        if (shuffle) shuffle = false;
        else shuffle = true;
    }

}
