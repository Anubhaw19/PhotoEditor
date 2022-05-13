package com.anubhaw19.photoeditor.viewModel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

//import androidx.lifecycle.ViewModelProvider;
//for creating custom ViewModel Objects with parameterised constructor
public class MainViewModelFactory implements ViewModelProvider.Factory {

    Context context;
    public MainViewModelFactory(Context context)
    {
        this.context= context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
//        return ViewModelProvider.Factory.super.create(modelClass);
        return (T) new MainViewModel(context);
    }

}
