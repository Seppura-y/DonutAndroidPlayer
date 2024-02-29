package com.example.donutplayer;

import android.net.Uri;

public class VideoData {
    private String id;
    private String title;
    private long duration;
    private String folderName;
    private String size;
    private String path;
    private Uri artUri;

    public VideoData(String id, String title, long duration, String folderName, String size, String path, Uri artUri) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.folderName = folderName;
        this.size = size;
        this.path = path;
        this.artUri = artUri;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Uri getArtUri() {
        return artUri;
    }

    public void setArtUri(Uri artUri) {
        this.artUri = artUri;
    }
}


class FolderData{
    private String id;
    private String folderName;

    public FolderData(String id, String folderName){
        this.id = id;
        this.folderName = folderName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getId() {
        return id;
    }

    public String getFolderName() {
        return folderName;
    }
}