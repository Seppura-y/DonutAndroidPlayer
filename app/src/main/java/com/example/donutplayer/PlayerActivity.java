package com.example.donutplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.donutplayer.databinding.ActivityPlayerBinding;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;

import java.util.ArrayList;
import java.util.Objects;

import android.view.Window;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;


public class PlayerActivity extends AppCompatActivity {

    public static int position = -1;
    private static ActivityPlayerBinding binding = null;
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