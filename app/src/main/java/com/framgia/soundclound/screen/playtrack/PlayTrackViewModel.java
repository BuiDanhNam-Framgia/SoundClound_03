package com.framgia.soundclound.screen.playtrack;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.AsyncTask;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.framgia.soundclound.R;
import com.framgia.soundclound.data.model.Track;
import com.framgia.soundclound.service.MusicService;
import com.framgia.soundclound.util.StringUtil;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Sony on 1/11/2018.
 */

public class PlayTrackViewModel extends BaseObservable implements SeekBar.OnSeekBarChangeListener {
    private String mTotalDuration;
    private String mCurentDuration;
    private int mProgressSeekBar = 0;
    private List<Track> mTracks;
    private int mIndexCurrentTrack;
    private Track mTrack;
    private static MusicService mMusicService;
    private boolean mMusicBound = false;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder musicServiceBinder = (MusicService.MusicBinder) service;
            mMusicService = musicServiceBinder.getService();
            mMusicService.setList(mTracks);
            mMusicService.setCurentTrack(mIndexCurrentTrack);
            mMusicBound = true;
            startPlayMusic();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMusicBound = false;
        }
    };

    public PlayTrackViewModel(List<Track> tracks, int indexCurentTrack) {
        mTracks = tracks;
        mIndexCurrentTrack = indexCurentTrack;
    }

    public String getTitle() {
        return mTrack != null ? mTrack.getTitle() : "";
    }

    public String getArtist() {
        if (mTrack != null) {
            return mTrack.getPublisherMetadata() != null ? mTrack.getPublisherMetadata().getArtist()
                    : "";
        }
        return "";
    }

    @Bindable
    public String getTotalDuration() {
        return mTotalDuration;
    }

    public void setTotalDuration(String totalDuration) {
        mTotalDuration = totalDuration;
        notifyPropertyChanged(0);
    }

    @Bindable
    public String getCurentDuration() {
        return mCurentDuration;
    }

    public void setCurentDuration(String curentDuration) {
        mCurentDuration = curentDuration;
        notifyPropertyChanged(0);
    }

    @Bindable
    public int getProgressSeekBar() {
        return mProgressSeekBar;
    }

    public void setProgressSeekBar(int progressSeekBar) {
        mProgressSeekBar = progressSeekBar;
        notifyPropertyChanged(0);
    }

    public ServiceConnection getServiceConnection() {
        return mServiceConnection;
    }

    public boolean isMusicBound() {
        return mMusicBound;
    }

    public void startPlayMusic() {
        mMusicService.play();
        TimeAsysnTask asyncTask = new TimeAsysnTask();
        asyncTask.execute();
    }

    public void onClickPause(View view) {
        ImageView imageView = (ImageView) view;
        if (mMusicService.isPlay()) {
            mMusicService.pause();
            imageView.setImageResource(android.R.drawable.ic_media_pause);
        } else {
            mMusicService.start();
            imageView.setImageResource(android.R.drawable.ic_media_play);
        }
    }

    public void onClickNext(View view) {
        mMusicService.next();
    }

    public void onClickShuffle(View view) {
        mMusicService.setShuffle();
        ImageView imageView = (ImageView) view;
        int colorSelect;
        if (mMusicService.isShuffle()) {
            colorSelect = R.color.color_orange;
            imageView.setImageResource(android.R.drawable.ic_media_pause);
        } else {
            colorSelect = R.color.color_gray;
            imageView.setImageResource(android.R.drawable.ic_media_play);
        }
        imageView.setColorFilter(colorSelect);
    }

    public void onClickLoop(View view) {
        mMusicService.setRepeat();
        ImageView imageView = (ImageView) view;
        int colorSelect;
        if (mMusicService.isRepeat()) {
            colorSelect = R.color.color_orange;
        } else {
            colorSelect = R.color.color_gray;
        }
        imageView.setColorFilter(colorSelect);
    }

    public void onClickPrevious(View view) {
        mMusicService.prev();
    }

    public void onClickLike(View view) {

    }

    public void onClickDownload(View view) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
//         asyncTask.cancel(true);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int currentPosition = StringUtil.progressToTimer(
                seekBar.getProgress(), mMusicService.getDuration());
        mMusicService.seekto(currentPosition);
    }

    /**
     * AsyncTask update time
     */

    public class TimeAsysnTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            setCurentDuration(StringUtil.milliSecondsToTimer(mMusicService.getPosition()));
            setTotalDuration(StringUtil.milliSecondsToTimer(mMusicService.getDuration()));
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Logger.getLogger(e.toString());
                }
                publishProgress();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            long totalDuration = mMusicService.getDuration();
            long currentDuration = mMusicService.getPosition();
            setCurentDuration(StringUtil.milliSecondsToTimer(currentDuration));
            setTotalDuration(StringUtil.milliSecondsToTimer(totalDuration));
            int progress = (int) (StringUtil.getProgressPercentage(currentDuration, totalDuration));
            setProgressSeekBar(progress);
        }
    }
}
