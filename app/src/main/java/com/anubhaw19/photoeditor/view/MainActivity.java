package com.anubhaw19.photoeditor.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.anubhaw19.photoeditor.model.DataModel;
import com.anubhaw19.photoeditor.viewModel.MainViewModel;
import com.anubhaw19.photoeditor.viewModel.MainViewModelFactory;
import com.anubhaw19.photoeditor.R;
import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    MainViewModel mainViewModel;
    Button camBtn,uploadBtn;
    int IMAGE_REQUEST_CODE = 45;
    int CAMERA_REQUEST_CODE = 14;
    int RESULT_CODE = 200;

    RecyclerView recyclerView;
    ViewAdapter viewAdapter;
//    ArrayList<DataModel> imgList;
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        handlePermission();
        mainViewModel= new ViewModelProvider(this,new MainViewModelFactory(this)).get(MainViewModel.class);  //creating object of ViewModel (MainViewModel)

        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        camBtn=findViewById(R.id.btn_cam);
        uploadBtn=findViewById(R.id.btn_upload);

//        imgList=new ArrayList<>();
        mainViewModel.showImage();
        mainViewModel.getObjects().observe(this, new Observer<ArrayList<DataModel>>() {
            @Override
            public void onChanged(ArrayList<DataModel> dataModels) {
//                Log.e("mainActivity", "data SIZE: "+dataModels.size());

                viewAdapter=new ViewAdapter(MainActivity.this,dataModels);
                recyclerView.setAdapter(viewAdapter);
                viewAdapter.notifyDataSetChanged();
            }
        });

        camBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ActivityCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[] {Manifest.permission.CAMERA}, 32);
                } else {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                }
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, IMAGE_REQUEST_CODE);


            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQUEST_CODE) {
            if(data.getData() != null) {
                Uri filePath = data.getData();
                Intent dsPhotoEditorIntent = new Intent(this, DsPhotoEditorActivity.class);
                dsPhotoEditorIntent.setData(filePath);
                dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "PhotoEditor");
//                int[] toolsToHide = {DsPhotoEditorActivity.TOOL_ORIENTATION, DsPhotoEditorActivity.TOOL_CROP};
                int[] toolsToHide = {DsPhotoEditorActivity.TOOL_DRAW,DsPhotoEditorActivity.TOOL_CONTRAST,DsPhotoEditorActivity.TOOL_EXPOSURE,DsPhotoEditorActivity.TOOL_FRAME
                ,DsPhotoEditorActivity.TOOL_ROUND,DsPhotoEditorActivity.TOOL_PIXELATE,DsPhotoEditorActivity.TOOL_SATURATION,DsPhotoEditorActivity.TOOL_SHARPNESS,DsPhotoEditorActivity.TOOL_STICKER,
                        DsPhotoEditorActivity.TOOL_WARMTH,DsPhotoEditorActivity.TOOL_TEXT,DsPhotoEditorActivity.TOOL_VIGNETTE};
                dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE, toolsToHide);
                startActivityForResult(dsPhotoEditorIntent, RESULT_CODE);
            }
        }

        if(requestCode == RESULT_CODE) {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            intent.setData(data.getData());
            startActivity(intent);
            finish();
        }

        if(requestCode == CAMERA_REQUEST_CODE) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Uri uri = getImageUri(photo);
            Intent dsPhotoEditorIntent = new Intent(this, DsPhotoEditorActivity.class);
            dsPhotoEditorIntent.setData(uri);
            dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "PhotoEditor");
//            int[] toolsToHide = {DsPhotoEditorActivity.TOOL_ORIENTATION, DsPhotoEditorActivity.TOOL_CROP};
            int[] toolsToHide = {DsPhotoEditorActivity.TOOL_DRAW,DsPhotoEditorActivity.TOOL_CONTRAST,DsPhotoEditorActivity.TOOL_EXPOSURE,DsPhotoEditorActivity.TOOL_FRAME
                    ,DsPhotoEditorActivity.TOOL_ROUND,DsPhotoEditorActivity.TOOL_PIXELATE,DsPhotoEditorActivity.TOOL_SATURATION,DsPhotoEditorActivity.TOOL_SHARPNESS,DsPhotoEditorActivity.TOOL_STICKER,
                    DsPhotoEditorActivity.TOOL_WARMTH,DsPhotoEditorActivity.TOOL_TEXT,DsPhotoEditorActivity.TOOL_VIGNETTE};
            dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE, toolsToHide);
            startActivityForResult(dsPhotoEditorIntent, RESULT_CODE);
        }
    }

    public Uri getImageUri(Bitmap bitmap) {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, arrayOutputStream);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }




    private void handlePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                android.Manifest.permission.CAMERA},
                        102);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("PERMISSIONS", "onRequestPermissionsResult: " +grantResults[0]+"\n"+ grantResults[1]+"\n"+grantResults[2]+"\n");
        if (requestCode == 102) {
            if (grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED ) {
                Toast.makeText(this, "Permission  Granted", Toast.LENGTH_SHORT).show();
                Log.d("TAG", "Permission Granted");
                mainViewModel.showImage();
                mainViewModel.getObjects().observe(this, new Observer<ArrayList<DataModel>>() {
                    @Override
                    public void onChanged(ArrayList<DataModel> dataModels) {
//                Log.e("mainActivity", "data SIZE: "+dataModels.size());

                        viewAdapter=new ViewAdapter(MainActivity.this,dataModels);
                        recyclerView.setAdapter(viewAdapter);
                        viewAdapter.notifyDataSetChanged();
                    }
                });

            } else {
                Toast.makeText(this, "need to grant both Permission ", Toast.LENGTH_SHORT).show();
                Log.d("TAG", "permission DECLINED");
                handlePermission();
            }
        }
    }
}