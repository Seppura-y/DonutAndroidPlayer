package com.example.donutplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.donutplayer.databinding.ActivityPlayerBinding;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;

import java.util.ArrayList;
import java.util.Objects;

public class PlayerActivity extends AppCompatActivity {

    public static int position = -1;
    private static ActivityPlayerBinding binding = null;
    private static SimpleExoPlayer player = null;
    private static ArrayList<VideoData> playerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeLayout();

    }

    private void initializeLayout(){
        switch (Objects.requireNonNull(getIntent().getStringExtra("class"))){
            case "AllVideos":{
                playerList = new ArrayList<>();
                playerList.addAll(MainActivity.videoList);
                break;
            }
            case "FolderActivity":{
                playerList = new ArrayList<>();
                playerList.addAll(FolderActivity.currentFolderVideos);
                break;
            }
            default:
        }

        createPlayer();
    }

    private void createPlayer(){
        player = new SimpleExoPlayer.Builder(this).build();
        binding.playerView.setPlayer(player);

        MediaItem mediaItem = MediaItem.fromUri(playerList.get(position).getArtUri());
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
    }
}