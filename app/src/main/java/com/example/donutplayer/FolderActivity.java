package com.example.donutplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.donutplayer.databinding.ActivityFolderBinding;

import java.util.ArrayList;

public class FolderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityFolderBinding binding = ActivityFolderBinding.inflate(getLayoutInflater());
        setTheme(R.style.coolPinkNav);
        setContentView(binding.getRoot());

        int position = getIntent().getIntExtra("position", 0);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(MainActivity.folderList.get(position).getFolderName());
        }

        ArrayList<VideoData> tempList = new ArrayList<>();
        tempList.add(MainActivity.videoList.get(0));
        tempList.add(MainActivity.videoList.get(1));
        tempList.add(MainActivity.videoList.get(2));

        binding.videoRecyclerViewFragmentActivity.setHasFixedSize(true);
        binding.videoRecyclerViewFragmentActivity.setItemViewCacheSize(10);
        binding.videoRecyclerViewFragmentActivity.setLayoutManager(new LinearLayoutManager(this));
        binding.videoRecyclerViewFragmentActivity.setAdapter(new VideoAdapter(this, tempList));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return true;
    }

}