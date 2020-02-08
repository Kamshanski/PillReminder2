package com.dawandeapp.pillreminder.data.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dawandeapp.pillreminder.data.database.Database;
import com.dawandeapp.pillreminder.data.database.ResultFlag;

import java.util.ArrayList;
import java.util.List;

public class Bracelet {
    private static Bracelet instance = new Bracelet();
    MutableLiveData<ArrayList<Box>> internalBoxes = new MutableLiveData<>();
    MutableLiveData<ArrayList<Box>> externalBoxes = new MutableLiveData<>();

    public Bracelet() {
        Database db = Database.getInstance();
        internalBoxes.setValue(db.getAll(false));
    }

    public static Bracelet getInstance() {
        return instance;
    }

    public LiveData<ArrayList<Box>> getInternalBoxes() {
        return internalBoxes;
    }

    public int putBox(Box box) {
        Box newBox = Database.getInstance().put(box);
        if (newBox.isInternal()) {
            updateInternal(newBox);
        } else {
            // External
        }
        return ResultFlag.SUCESS;
    }

    public int deleteBox(int id, boolean external) {
        return deleteBox(internalBoxes.getValue().get(Box.idToComplex(id, external)));
    }

    public int deleteBox(Box delBox) {
        Database db = Database.getInstance();
        Box emptyBox = Box.getEmpty(delBox.getComplexId());
        db.delete(delBox);
        emptyBox = db.put(emptyBox);
        updateInternal(emptyBox);
        return ResultFlag.SUCESS;
    }

    public void updateInternal(Box box) {
        final ArrayList<Box> list = internalBoxes.getValue();
        list.set(box.getId(), box);
        internalBoxes.setValue(list);
    }
}
