package com.framgia.soundclound.screen.favourite;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.framgia.soundclound.R;
import com.framgia.soundclound.data.model.Track;
import com.framgia.soundclound.databinding.ItemTrackFavoriteBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sony on 1/15/2018.
 */

public class FavoriteTrackAdapter extends
        RecyclerView.Adapter<FavoriteTrackAdapter.FavoriteViewHolder> {

    private List<Track> mTracks;
    private FavoriteClickListener mFavoriteClickListener;
    private ItemClickListener mItemClickListener;

    public FavoriteTrackAdapter() {
        mTracks = new ArrayList<>();
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemTrackFavoriteBinding itemTrackFavoriteBinding = DataBindingUtil.inflate(layoutInflater,
                R.layout.item_track_favorite, parent, false);
        return new FavoriteViewHolder(itemTrackFavoriteBinding, mFavoriteClickListener,
                mItemClickListener);
    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, int position) {
        holder.bindData(mTracks.get(position));
    }

    @Override
    public int getItemCount() {
        return mTracks != null ? mTracks.size() : 0;
    }

    public void setFavoriteClickListener(FavoriteClickListener favoriteClickListener) {
        mFavoriteClickListener = favoriteClickListener;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public List<Track> getData() {
        return mTracks;
    }

    public void addData(List<Track> tracks) {
        if (tracks == null) {
            return;
        }
        mTracks.addAll(tracks);
        notifyDataSetChanged();
    }

    public void updateData(int posChange) {
        mTracks.remove(posChange);
        notifyDataSetChanged();
    }

    /**
     * Created by Sony on 1/5/2018.
     */
    public class FavoriteViewHolder extends RecyclerView.ViewHolder {
        private ItemTrackFavoriteBinding mBinding;
        private ItemClickListener mItemClickListener;
        private FavoriteClickListener mFavoriteClickListener;

        public FavoriteViewHolder(ItemTrackFavoriteBinding itemTrackFavoriteBinding,
                                  FavoriteClickListener favoriteClickListener,
                                  ItemClickListener itemClickListener) {
            super(itemTrackFavoriteBinding.getRoot());
            mBinding = itemTrackFavoriteBinding;
            mFavoriteClickListener = favoriteClickListener;
            mItemClickListener = itemClickListener;
        }

        public void bindData(Track track) {
            if (track == null) {
                return;
            }
            mBinding.executePendingBindings();
            mBinding.setViewModel(new ItemFavoriteViewModel(track, mFavoriteClickListener,
                    mItemClickListener, getAdapterPosition()));
        }
    }
}
