package com.framgia.soundclound.screen.detailgenre;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.framgia.soundclound.R;
import com.framgia.soundclound.databinding.ActivityGenreDetailBinding;
import com.framgia.soundclound.util.Constant;

/**
 * Created by Sony on 1/5/2018.
 */

public class GenreDetailActivity extends AppCompatActivity {


    public static Intent getInstance(Context context, String genre) {
        Intent intent = new Intent(context, GenreDetailActivity.class);
        intent.putExtra(Constant.EXTRA_GENRE, genre);
        return intent;
    }

    public static Intent getInstance(Context context, int idAlbum) {
        Intent intent = new Intent(context, GenreDetailActivity.class);
        intent.putExtra(Constant.EXTRA_ID_ALBUM, idAlbum);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityGenreDetailBinding activityGenreDetailBinding = DataBindingUtil
                .setContentView(this, R.layout.activity_genre_detail);
        GenreDetailViewModel genreDetailViewModel = null;
        Intent intent = getIntent();
        if (intent != null) {
            String keyGenre = intent.getExtras().getString(Constant.EXTRA_GENRE, null);
            int idAlbum = intent.getExtras().getInt(
                    Constant.EXTRA_ID_ALBUM,
                    Constant.VALUE_ID_ALBUM_NULL);
            if (keyGenre != null) {
                genreDetailViewModel = new GenreDetailViewModel(this, keyGenre);
            }
            if (idAlbum != Constant.VALUE_ID_ALBUM_NULL) {
                genreDetailViewModel = new GenreDetailViewModel(this, idAlbum);
            }
        }
        activityGenreDetailBinding.setViewModel(genreDetailViewModel);
    }

}
