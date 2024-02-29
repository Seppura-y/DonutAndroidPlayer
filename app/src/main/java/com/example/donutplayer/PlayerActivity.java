package com.example.donutplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.donutplayer.databinding.ActivityPlayerBinding;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;

public class PlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityPlayerBinding binding = ActivityPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SimpleExoPlayer player = new SimpleExoPlayer.Builder(this).build();
        binding.playerView.setPlayer(player);

        MediaItem mediaItem = MediaItem.fromUri(MainActivity.videoList.get(0).getArtUri());
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();

    }
}