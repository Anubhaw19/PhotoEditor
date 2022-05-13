package com.anubhaw19.photoeditor.model;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;
import java.net.URI;

public class DataModel {

    private final File file;
    private Bitmap thumbnail;
    private  final String title,path;

    public DataModel(File file, String title, String path) {
        this.file = file;
        this.title = title;
        this.path = path;

    }

    public File getFile() {
        return file;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public String getPath() {
        return path;
    }
}
