package com.dawandeapp.pillreminder.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.dawandeapp.pillreminder.data.model.Box;
import com.dawandeapp.pillreminder.data.model.Bracelet;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;

public class BoxesViewModel extends ViewModel {
    Bracelet bracelet;

    public BoxesViewModel() {
        bracelet = Bracelet.getInstance();
    }

    public LiveData<ArrayList<Box>> getBoxes() {
        return bracelet.getInternalBoxes();
    }
}
