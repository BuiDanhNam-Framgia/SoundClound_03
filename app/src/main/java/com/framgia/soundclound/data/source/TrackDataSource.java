package com.framgia.soundclound.data.source;

import com.framgia.soundclound.data.model.Album;
import com.framgia.soundclound.data.model.Track;

import java.util.List;

/**
 * Created by Sony on 1/5/2018.
 */
public interface TrackDataSource {
    /**
     * @return <T> specified type of object
     */
    interface Callback<T> {
        void onStartLoading();

        void onGetSuccess(T data);

        void onGetFailure(String message);

        void onComplete();
    }

    /**
     * Track remove data source
     */
    interface RemoveDataSource {
        void getListTrack(String url, String genre, int limit, int offSet,
                          Callback<List<Track>> callback);
    }

    /**
     * Track local data source
     */
    interface LocalDataSource {
        void getListTrack(int idAlbum, int limit, Callback<List<Track>> callback);

        void removeTrack(int idAlbum, int idTrack, Callback<Album> callback);

        void addTrack(int idAlbum, Track track, Callback<Album> callback);

        void addListTrack(int idAlbum, List<Track> tracks, Callback<Album> callback);

    }
}
