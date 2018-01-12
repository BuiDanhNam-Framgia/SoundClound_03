package com.framgia;

/**
 * Created by Bui Danh Nam on 11/1/2018.
 */

public interface BaseMediaPlayer {

    void play(int index);

    void pause();

    void stop();

    void release();

    void seekto(int position);

    void next();

    void prev();

    int getDuration();

    int getPosition();
}
