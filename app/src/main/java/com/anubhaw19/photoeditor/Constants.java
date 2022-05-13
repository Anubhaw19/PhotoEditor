package com.anubhaw19.photoeditor;

import android.os.Environment;

import java.io.File;

public class Constants {

    public static final File  IMAGE_DIRECTORY= new
            File(Environment.getExternalStorageDirectory()+File.separator+"Pictures/PhotoEditor");

    public  static  final String APP_DIRECTORY=Environment.getExternalStorageDirectory() +File.separator+"PhotoEditor";
    public static final int THUMBSIZE=128;

}
