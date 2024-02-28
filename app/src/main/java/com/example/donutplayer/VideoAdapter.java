package com.example.donutplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {
    private Context context;
    private ArrayList<String> videoList;

    public VideoAdapter(Context context, ArrayList<String> videoList) {
        this.context = context;
        this.videoList = videoList;
    }

    public static class VideoHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public VideoHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.videoName);
        }
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.video_view, parent, false);
        return new VideoHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoHolder holder, int position) {
        holder.title.setText(videoList.get(position));
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }
}

