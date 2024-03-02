package com.example.donutplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

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
        initializeBinding();
    }

    private void initializeLayout(){
        switch (Objects.requireNonNull(getIntent().getStringExtra("class"))){
            case "AllVideos":{
                playerList = new ArrayList<>();
                playerList.addAll(MainActivity.videoList);
                createPlayer();
                break;
            }
            case "FolderActivity":{
                playerList = new ArrayList<>();
                playerList.addAll(FolderActivity.currentFolderVideos);
                createPlayer();
                break;
            }
            default:
        }
    }

    private void initializeBinding(){
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.playPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(player.isPlaying()){
                    pauseVideo();
                } else{
                  playVideo();
                }
            }
        });

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextPreviousVideo(true);
            }
        });

        binding.previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextPreviousVideo(false);
            }
        });
    }

    private void createPlayer(){
        try{player.release();} catch(Exception e){}
        player = new SimpleExoPlayer.Builder(this).build();
        binding.playerView.setPlayer(player);
        binding.videoTitle.setText(playerList.get(position).getTitle());
        binding.videoTitle.setSelected(true);

        MediaItem mediaItem = MediaItem.fromUri(playerList.get(position).getArtUri());
        player.setMediaItem(mediaItem);
        player.prepare();
//        player.play();

        playVideo();
    }

    private void playVideo(){
        binding.playPauseBtn.setImageResource(R.drawable.pause_icon);
        player.play();
    }

    private void pauseVideo(){
        binding.playPauseBtn.setImageResource(R.drawable.play_icon);
        player.pause();
    }

    private void nextPreviousVideo(Boolean isNext)
    {
        if(isNext){
            setPosition(true);
        }
        else{
            setPosition(false);
        }

        createPlayer();
    }

    private void setPosition(Boolean isIncrement){
        if(isIncrement){
            if(playerList.size() - 1 == position){
                position = 0;
            }
            else {
                position++;
            }
        }
        else{
            if(position == 0){
                position = playerList.size() - 1;
            }
            else{
                position--;
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
    }
}