package com.example.ingozu2.ui.Herramientas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HerramientasViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HerramientasViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}