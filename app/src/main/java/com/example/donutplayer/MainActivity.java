package com.example.donutplayer;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.donutplayer.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'donutplayer' library on application startup.
    static {
        System.loadLibrary("donutplayer");
    }

    private ActivityMainBinding binding;
    private ActionBarDrawerToggle toggle = null;

    public static ArrayList<VideoData> videoList = new ArrayList<>();
    public static ArrayList<FolderData> folderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setTheme(R.style.coolPinkNav);
        setContentView(binding.getRoot());

        // 一般来说，申请权限函数应该在setContentView之后调用，这样可以避免在用户还没有看到界面的时候就弹出权限请求对话框
        if(requestRuntimePermission() == true)
        {
            videoList = getAllVideos();
            setFragment(new VideosFragment());
        }

        toggle = new ActionBarDrawerToggle(this, binding.getRoot(), R.string.open, R.string.close);
        binding.getRoot().addDrawerListener(toggle);
        toggle.syncState();
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }



        binding.bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
              @Override
              public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                  Toast.makeText(MainActivity.this, "Item Clicked", Toast.LENGTH_SHORT).show();
                  switch(item.getItemId()){
                      case R.id.videoView:
                          setFragment(new VideosFragment());
                          break;
                      case R.id.foldersView:
                          setFragment(new FoldersFragment());
                          break;
                      default:
                          setFragment(new Fragment());
                          break;
                  }
                  return true;
              }
          }
        );

        binding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.feedBackNav:
                        Toast.makeText(MainActivity.this, "Feedback", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.themesNav:
                        Toast.makeText(MainActivity.this, "Theme", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.sortOrderNav:
                        Toast.makeText(MainActivity.this, "SortOrder", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.aboutNav:
                        Toast.makeText(MainActivity.this, "About", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.exitNav:
                        System.exit(1);
                        break;
                }
                return true;
            }
        });


    }

    private void setFragment(Fragment fragment){
        // 获取当前Activity的FragmentTransaction对象，用于执行 Fragment 的添加、替换等操作
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // 用新的 Fragment 替换布局中的Id 为 fragmentFrameLayout 的 FrameLayout
        transaction.replace(binding.fragmentFrameLayout.getId(), fragment);

        //设置不将事务添加到返回栈。这意味着当用户按下返回按钮时，不会返回到之前的 Fragment
        transaction.disallowAddToBackStack();

        //提交事务，使之生效
        transaction.commit();
    }

    // 请求写外部存储的权限
    private Boolean requestRuntimePermission(){
        if(ActivityCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            String[] p = new String[]{WRITE_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this, p, 13);
            // 返回false，表示没有权限。
            return false;
        }

        // 返回true，表示已经拥有权限
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 13) {
            //检查授权结果数组（grantResults）是否为空，以及第一个元素是否等于PackageManager.PERMISSION_GRANTED。
            //如果是，表示用户同意了权限请求，使用Toast类，向用户显示提示信息“Permission Granted”。
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                folderList = new ArrayList<>();
                videoList = getAllVideos();
                setFragment(new VideosFragment());
            } else {
                //如果不是，表示用户拒绝了权限请求，它再次创建一个字符串数组，包含要请求的权限，然后使用ActivityCompat.requestPermissions方法，向用户再次发起权限请求。
                //这是为了让用户有机会重新考虑，或者在设置中手动开启权限。
                String[] p = new String[]{WRITE_EXTERNAL_STORAGE};
                ActivityCompat.requestPermissions(this, p, 13);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @SuppressLint({"Range"})
    private ArrayList<VideoData> getAllVideos(){
        ArrayList<VideoData> tempList = new ArrayList<>();
        ArrayList<String> tempFolderList = new ArrayList<>();

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
                null,
                null,
                MediaStore.Video.Media.DATE_ADDED + " DESC"
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String titleC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                String idC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID));
                String folderC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME));
                String folderIdC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_ID));
                String sizeC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                String pathC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                long durationC = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));

                try {
                    File file = new File(pathC);
                    Uri artUriC = Uri.fromFile(file);
                    VideoData video = new VideoData(idC, titleC, durationC, folderC, sizeC, pathC, artUriC);
                    if (file.exists()) tempList.add(video);

                    if(!tempFolderList.contains(folderC)){
                        tempFolderList.add(folderC);
                        folderList.add(new FolderData(folderIdC, folderC));
                    }
                } catch (Exception e) {
                    // Handle exception
                }
            }
            cursor.close();
        }
        return tempList;

    }

    public native String stringFromJNI();
}