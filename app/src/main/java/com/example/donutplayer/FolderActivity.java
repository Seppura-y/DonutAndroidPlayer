package com.example.donutplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;

import com.example.donutplayer.databinding.ActivityFolderBinding;

import java.io.File;
import java.util.ArrayList;

public class FolderActivity extends AppCompatActivity {

    public static ArrayList<VideoData> currentFolderVideos = new ArrayList<>();

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

        currentFolderVideos = getAllVideos(MainActivity.folderList.get(position).getId());

        binding.videoRecyclerViewFragmentActivity.setHasFixedSize(true);
        binding.videoRecyclerViewFragmentActivity.setItemViewCacheSize(10);
        binding.videoRecyclerViewFragmentActivity.setLayoutManager(new LinearLayoutManager(this));
        binding.videoRecyclerViewFragmentActivity.setAdapter(new VideoAdapter(this, currentFolderVideos));
        binding.totalVideos.setText("Total Videos: " + currentFolderVideos.size());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return true;
    }

    @SuppressLint({"Range"})
    private ArrayList<VideoData> getAllVideos(String folderId){
        ArrayList<VideoData> tempList = new ArrayList<>();

        String selection = MediaStore.Video.Media.BUCKET_ID + " like?";

        String[] projection = {
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.BUCKET_ID
        };

        Cursor cursor = this.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                new String[]{folderId},
                MediaStore.Video.Media.DATE_ADDED + " DESC"
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String titleC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                String idC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID));
                String folderC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME));
                String sizeC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                String pathC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                long durationC = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));

                try {
                    File file = new File(pathC);
                    Uri artUriC = Uri.fromFile(file);
                    VideoData video = new VideoData(titleC, idC, durationC, folderC, sizeC, pathC, artUriC);
                    if (file.exists()) tempList.add(video);

                } catch (Exception e) {
                    // Handle exception
                }
            }
            cursor.close();
        }
        return tempList;

    }

}