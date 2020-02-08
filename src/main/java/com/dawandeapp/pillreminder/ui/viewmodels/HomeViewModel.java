package com.dawandeapp.pillreminder.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.dawandeapp.pillreminder.data.model.Box;
import com.dawandeapp.pillreminder.data.model.Bracelet;
import com.dawandeapp.pillreminder.utilities.M;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {
    Bracelet bracelet;

    public HomeViewModel() {
        bracelet = Bracelet.getInstance();
    }

    public LiveData<ArrayList<Box>> getBoxes() {
        return bracelet.getInternalBoxes();
    }

    public void putBox(Box box) {
        if (box.isValidBox(false)) {
            // TODO Добавть диалог
            bracelet.putBox(box);
        } else M.d("Box is Not Valid!");
    }

    public void deleteBox(int id) {
        bracelet.deleteBox(id, false);
    }
}
