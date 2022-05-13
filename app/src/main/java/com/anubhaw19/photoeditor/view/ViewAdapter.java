package com.anubhaw19.photoeditor.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anubhaw19.photoeditor.R;
import com.anubhaw19.photoeditor.model.DataModel;

import java.util.ArrayList;
import java.util.List;

public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ImageViewHolder>{
    private final List<DataModel> imageList;
    Context context;

    public ViewAdapter(Context context,List<DataModel> imageList) {
        this.context = context;
        this.imageList = imageList;

    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.gallery_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
            DataModel dataModel=imageList.get(position);
            holder.imageView.setImageBitmap(dataModel.getThumbnail());
//            holder.imageView.setImageURI(dataModel.getImguri());
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {


        ImageView imageView;
        public ImageViewHolder(View view) {
            super(view);
            imageView=itemView.findViewById(R.id.imageView);
        }
    }

}
