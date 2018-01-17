package com.framgia.soundclound.screen.moretrack;

import android.app.AlertDialog;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.framgia.soundclound.R;
import com.framgia.soundclound.data.model.Track;
import com.framgia.soundclound.data.source.remote.TrackDownloadManager;
import com.framgia.soundclound.data.source.repository.AlbumRepository;
import com.framgia.soundclound.util.Constant;

/**
 * Created by Sony on 1/10/2018.
 */

public class MoreTrackViewModel extends BaseObservable {
    private Track mTrack;
    private Context mContext;
    private boolean mFavorite;

    public MoreTrackViewModel(Context context, Track track) {
        mContext = context;
        mTrack = track;
        mFavorite = AlbumRepository.getInstance(mContext)
                .checkTrackExistAlbum(Constant.TRACKS_FAVORITE, mTrack);
    }

    public String getTitle() {
        return mTrack != null ? mTrack.getTitle() : "";
    }

    public String getArtist() {
        if (mTrack != null) {
            return mTrack.getPublisherMetadata() != null ? mTrack.getPublisherMetadata()
                    .getArtist() : "";
        }
        return "";
    }

    @BindingAdapter("setImage")
    public static void setImage(ImageView view, boolean favorite) {
        if (favorite) {
            view.setImageResource(R.drawable.ic_favorite_red_24dp);
            view.setClickable(false);
        } else {
            view.setImageResource(R.drawable.ic_favorite_white_24dp);
        }
    }


    @BindingAdapter("setText")
    public static void setText(TextView view, boolean favorite) {
        if (favorite) {
            view.setText(R.string.title_exist_favorite);
            view.setClickable(false);
        } else {
            view.setText(R.string.title_add_favorite);
        }
    }

    @Bindable
    public boolean isFavorite() {
        return mFavorite;
    }

    public void setFavorite(boolean favorite) {
        mFavorite = favorite;
        notifyPropertyChanged(0);
    }

    public String getUrl() {
        return mTrack != null ? mTrack.getArtworkUrl() : Constant.LINK_DEFAULT;
    }

    public String getCanDownLoad() {
        if (mTrack != null) {
            return mTrack.isDownloadable() ? mContext.getString(R.string.title_candownload)
                    : mContext.getString(R.string.title_cantdownload);
        }
        return mContext.getString(R.string.title_cantdownload);
    }

    public void onClickLike(View view) {
        boolean resultAddFavorite = AlbumRepository.getInstance(mContext)
                .addTrack(Constant.TRACKS_FAVORITE, mTrack);
        if (resultAddFavorite) {
            showNotification(R.string.msg_notifi_favorite_track_suggest);
        } else {
            showNotification(R.string.msg_notifi_favorite_track_fail);
        }

    }

    private void showNotification(int notifi) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(
                mContext, R.style.Theme_AppCompat_Light_Dialog_Alert);
        builder.setTitle(R.string.msg_notifi_favorite_track);
        builder.setMessage(notifi);
        builder.setNeutralButton(R.string.action_ok, null);
        builder.show();
    }

    public void onClickDownload(View view) {
        if (mTrack.isDownloadable()) {
            TrackDownloadManager trackDownloadManager = new TrackDownloadManager(mContext, mTrack);
            trackDownloadManager.download();
        }
    }

}
