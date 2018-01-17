package com.framgia.soundclound.data.source;

import com.framgia.soundclound.data.model.Album;
import com.framgia.soundclound.data.model.Track;

import java.util.List;

/**
 * Created by Bui Danh Nam on 8/1/2018.
 */

public interface AlbumDataSource {
    List<Album> getAllAlbum();

    boolean addAlbum(Album album);

    boolean deleteAlbum(Album album);

    Album getAlbumById(int idAlbum);

    boolean updateAlbum(Album album);

    Album getAlbumByName(String nameAlbum);

    boolean addTrack(int idAlbum, Track track);

    boolean addTrack(String nameAlbum, Track track);

    List<Track> getAllTrack(int idAlbum);

    List<Track> getAllTrack(String nameAlbum);

    boolean removeTrack(int idAlbum, Track track);

    boolean removeTrack(String nameAlbum, Track track);

    boolean renameAlbum(Album album);

    boolean checkTrackExistAlbum(String nameAlbum, Track track);
}
