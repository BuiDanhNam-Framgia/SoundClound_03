package com.framgia.soundclound.data.source;

/**
 * Created by Sony on 1/5/2018.
 */

public class TrackRepository {

    private static TrackRepository INSTANCE = null;

    private final TrackDataSource.LocalDataSource mLocalDataSource;
    private final TrackDataSource.RemoveDataSource mRemoteDataSource;

    private TrackRepository(TrackDataSource.RemoveDataSource remoteDataSource,
                            TrackDataSource.LocalDataSource localDataSource) {
        mRemoteDataSource = remoteDataSource;
        mLocalDataSource = localDataSource;
    }

    public static TrackRepository getInstance(TrackDataSource.RemoveDataSource remoteDataSource,
                                              TrackDataSource.LocalDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new TrackRepository(remoteDataSource, localDataSource);
        }
        return INSTANCE;
    }

    public TrackDataSource.LocalDataSource getLocalDataSource() {
        return mLocalDataSource;
    }

    public TrackDataSource.RemoveDataSource getRemoteDataSource() {
        return mRemoteDataSource;
    }

}
