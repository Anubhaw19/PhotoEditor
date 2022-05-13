package com.anubhaw19.photoeditor.viewModel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.anubhaw19.photoeditor.Constants;
import com.anubhaw19.photoeditor.model.DataModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class MainViewModel extends ViewModel {
    Context context;
    Handler handler = new Handler();
    ArrayList<DataModel> imgList;
    private MutableLiveData<ArrayList<DataModel>> liveObjects = new MutableLiveData<>();


    public MainViewModel(Context context)  // we have  used modelViewFactory to use this constructor
    {

        this.context=context;
    }

     public LiveData<ArrayList<DataModel>> getObjects() {
        return liveObjects;
    }




    public void showImage() {

        imgList=new ArrayList<>();
        if (Constants.IMAGE_DIRECTORY.exists()) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    //listing all images files in the [APP_DIRECTORY] folder.
                    File[] Files = Constants.IMAGE_DIRECTORY.listFiles();
//                    Log.e("TAG", " FILE SIZE: "+ Files.length);
                    if (Files != null && Files.length > 0) {

                        Arrays.sort(Files);

                        for (final File file : Files) {
                            DataModel dataModel = new DataModel(file, file.getName(), file.getAbsolutePath());

                            dataModel.setThumbnail(getThumbNail(dataModel));
                            imgList.add(dataModel);
                            liveObjects.postValue(imgList);
//                            Log.e("TAG", " FILE SIZE: "+dataModel.getThumbnail());

                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
//                                Log.e("Modelview run", " FILE SIZE1: "+ imgList.size());

                            }
                        });
                    }

//                    Log.e("Modelview", " FILE SIZE1: "+ imgList.size());
                }

            }).start();
        }
//        Log.e("Modelview", " FILE SIZE2: "+ imgList.size());
//        return imgList;
    }
    private Bitmap getThumbNail(DataModel dataModel) {

            return ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(dataModel.getFile().getAbsolutePath()),
                    Constants.THUMBSIZE, Constants.THUMBSIZE);

    }







}
