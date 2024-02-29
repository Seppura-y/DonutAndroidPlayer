package com.example.donutplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.donutplayer.databinding.ActivityFolderBinding;

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
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return true;
    }

}