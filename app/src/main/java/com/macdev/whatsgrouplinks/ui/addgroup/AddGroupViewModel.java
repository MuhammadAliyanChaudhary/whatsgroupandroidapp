package com.macdev.whatsgrouplinks.ui.addgroup;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddGroupViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public AddGroupViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is addgroup fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}