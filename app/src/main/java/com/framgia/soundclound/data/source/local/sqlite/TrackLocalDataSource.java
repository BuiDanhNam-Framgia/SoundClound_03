package com.framgia.soundclound.data.source.local.sqlite;

import android.content.Context;

import com.framgia.soundclound.data.model.Album;
import com.framgia.soundclound.data.model.Track;
import com.framgia.soundclound.data.source.TrackDataSource;
import com.framgia.soundclound.util.Constant;

import java.util.List;

/**
 * Created by Bui Danh Nam on 10/1/2018.
 */

public class TrackLocalDataSource implements TrackDataSource.LocalDataSource {
    private static TrackLocalDataSource INSTANCE;
    private Context mContext;

    public TrackLocalDataSource(Context context) {
        mContext = context;
    }

    public static TrackLocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new TrackLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getListTrack(int idAlbum, int limit, TrackDataSource.Callback<List<Track>> callback) {
        List<Track> tracks = AlbumLocalDataSource.getInstance(mContext).getAllTrack(idAlbum);
        if (tracks == null) {
            callback.onGetFailure(Constant.ERROR_NULL);
        } else {
            callback.onGetSuccess(tracks);
        }
    }

    @Override
    public void removeTrack(int idAlbum, int idTrack, TrackDataSource.Callback<Album> callback) {
//// TODO: 11/1/2018 remote track  
    }

    @Override
    public void addTrack(int idAlbum, Track track, TrackDataSource.Callback<Album> callback) {
        //// TODO: 11/1/2018 add track  
    }

    @Override
    public void addListTrack(int idAlbum, List<Track> tracks, TrackDataSource.Callback<Album> callback) {
// TODO: 11/1/2018 add list track 
    }
}
