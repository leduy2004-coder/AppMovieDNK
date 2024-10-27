package com.example.appmoviednk.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<String> maBook = new MutableLiveData<>();

    public void mabook(String ma) {
        maBook.setValue(ma);
    }

    public LiveData<String> getMaBook() {
        return maBook;
    }
}
