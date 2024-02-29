package com.example.donutplayer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.donutplayer.databinding.FragmentVideosBinding;

import java.util.ArrayList;


public class VideosFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_videos, container, false);
        FragmentVideosBinding binding = FragmentVideosBinding.bind(view);

        binding.videoRecyclerView.setHasFixedSize(true);
        binding.videoRecyclerView.setItemViewCacheSize(10);
        binding.videoRecyclerView.setLayoutManager(new LinearLayoutManager(this.requireContext()));
        binding.videoRecyclerView.setAdapter(new VideoAdapter(this.requireContext(), MainActivity.videoList));
        binding.totalVideos.setText("Total Videos: " + MainActivity.videoList.size());
        return view;
    }
}