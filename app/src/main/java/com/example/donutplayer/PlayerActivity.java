package com.example.donutplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.donutplayer.databinding.ActivityPlayerBinding;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Objects;

import android.view.Window;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;


public class PlayerActivity extends AppCompatActivity {

    public static int position = -1;
    private Boolean repeat = false;
    private Boolean isFullscreen = false;
    private Boolean isLocked = false;
    private static ActivityPlayerBinding binding = null;
    private static Runnable runnable;
    private static SimpleExoPlayer player = null;
    private static ArrayList<VideoData> playerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // 应用自定义主题
        // 需要在setContentView前调用
        setTheme(R.style.playerActivityTheme);

        // 隐藏标题栏
        // 需要在setContentView前调用
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
            // 内容延伸至刘海区域
            layoutParams.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().setAttributes(layoutParams);
        }

        binding = ActivityPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 沉浸式 immersive mode
        // 需要在setContentView后调用
        // 需要引入implementation 'androidx.core:core-ktx:1.6.0'

        // 获取当前窗口对象
        Window window = getWindow();
        // 设置窗口不适配系统窗口
        WindowCompat.setDecorFitsSystemWindows(window, false);
        WindowInsetsControllerCompat controller = new WindowInsetsControllerCompat(window, binding.getRoot());
        // 隐藏系统栏
        controller.hide(WindowInsetsCompat.Type.systemBars());
        // 设置系统栏的行为为通过滑动屏幕边缘来临时显示
        controller.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);

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
        if(repeat) binding.repeatBtn.setImageResource(R.drawable.exo_controls_repeat_all);
        else binding.repeatBtn.setImageResource(R.drawable.exo_controls_repeat_off);
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

        binding.repeatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(repeat){
                    repeat = false;
                    player.setRepeatMode(Player.REPEAT_MODE_OFF);
                    binding.repeatBtn.setImageResource(R.drawable.exo_controls_repeat_off);
                }
                else{
                    repeat = true;
                    player.setRepeatMode(Player.REPEAT_MODE_ALL);
                    binding.repeatBtn.setImageResource(R.drawable.exo_controls_repeat_all);
                }
            }
        });

        binding.fullscreenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFullscreen){
                    isFullscreen = false;
                    playInFullscreen(false);
                }
                else{
                    isFullscreen = true;
                    playInFullscreen(true);
                }
            }
        });

        binding.lockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isLocked){
                    isLocked = true;
                    binding.playerView.hideController();
                    binding.playerView.setUseController(false);
                    binding.lockBtn.setImageResource(R.drawable.lock_icon);
                }else {
                    isLocked = false;
                    binding.playerView.showController();
                    binding.playerView.setUseController(true);
                    binding.lockBtn.setImageResource(R.drawable.unlock_icon);
                }
            }
        });

        binding.moreFeaturesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseVideo();
                View customDialog = LayoutInflater.from(binding.getRoot().getContext()).inflate(R.layout.more_features, binding.getRoot(), false);
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(binding.getRoot().getContext())
                        .setView(customDialog)
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                playVideo();
                            }
                        })
                        .setBackground(new ColorDrawable(0x803700b3));

                builder.create();
                builder.show();
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

        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                Player.Listener.super.onPlaybackStateChanged(playbackState);
                if(playbackState == Player.STATE_ENDED && repeat){
                    nextPreviousVideo(true);
                }
            }
        });

        playVideo();
        playInFullscreen(isFullscreen);
        setVisibility();
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

    private void playInFullscreen(Boolean enable){
        if(enable){
            binding.playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
            player.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            binding.fullscreenBtn.setImageResource(R.drawable.fullscreen_exit_icon);
        }
        else{
            binding.playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
            player.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT);
            binding.fullscreenBtn.setImageResource(R.drawable.fullscreen_icon);
        }
    }
    private void setVisibility(){
        runnable = new Runnable() {
            @Override
            public void run() {
                if(binding.playerView.isControllerVisible()){
                    changeVisibility(View.VISIBLE);
                }
                else{
                    changeVisibility(View.INVISIBLE);
                }

                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(runnable, 30);
            }
        };

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(runnable, 0);
    }

    private void changeVisibility(int visibility){
        binding.topContorller.setVisibility(visibility);
        binding.bottomContorller.setVisibility(visibility);
        binding.playPauseBtn.setVisibility(visibility);
        if(isLocked) binding.lockBtn.setVisibility(View.VISIBLE);
        else binding.lockBtn.setVisibility(visibility);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
    }
}