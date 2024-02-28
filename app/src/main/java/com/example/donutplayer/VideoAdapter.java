package com.example.donutplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.donutplayer.databinding.VideoViewBinding;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {
    private Context context;
    private ArrayList<VideoData> videoList;

    public VideoAdapter(Context context, ArrayList<VideoData> videoList) {
        this.context = context;
        this.videoList = videoList;
    }


    public static class VideoHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView folder;
        public TextView duration;

        public ShapeableImageView image;

        public VideoHolder(VideoViewBinding binding) {
            super(binding.getRoot());
//            title = (TextView) itemView.findViewById(R.id.videoName);
            title = binding.videoName;
            folder = binding.folderName;
            duration = binding.duration;
            image = binding.videoImage;
        }
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        View view = layoutInflater.inflate(R.layout.video_view, parent, false);

        VideoViewBinding binding = VideoViewBinding.inflate(layoutInflater,parent,false);
        return new VideoHolder(binding);
    }

    @Override
    public void onBindViewHolder(VideoHolder holder, int position) {
        holder.title.setText(videoList.get(position).getTitle());
        holder.folder.setText(videoList.get(position).getFolderName());
        holder.duration.setText(DateUtils.formatElapsedTime(videoList.get(position).getDuration() / 1000));
        Glide.with(context)
                .asBitmap()
                .load(videoList.get(position).getArtUri())
                .apply(new RequestOptions().placeholder(R.mipmap.ic_video_player).centerCrop())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }
}

