package com.dawandeapp.pillreminder.ui.viewmodels;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.SparseArray;
import android.util.SparseIntArray;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.databinding.InverseMethod;
import androidx.databinding.Observable;
import androidx.databinding.PropertyChangeRegistry;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dawandeapp.pillreminder.data.model.Box;
import com.dawandeapp.pillreminder.data.model.Bracelet;
import com.dawandeapp.pillreminder.data.model.TimePoint;
import com.dawandeapp.pillreminder.databinding.DialogBoxEditBinding;
import com.dawandeapp.pillreminder.databinding.DialogBoxEditBindingImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

// TODO: добавить обработку ошибок ввода
public class EditBoxViewModel extends ViewModel implements Observable {
    public static final int MAX_TIMEPOINTS = 15;
    public static final int MAX_PILLNAME_LENGTH = 25;
    public static final int MAX_DAYS = 30;

    private PropertyChangeRegistry callbacks = new PropertyChangeRegistry();
    //Todo добавить важность, выкидной список
    int boxId = -1;
    @Bindable String pillName = "";
    @Bindable String pillDescription = "";
    MutableLiveData<Boolean> isDay = new MutableLiveData<>(true);
    String day = "1";
    boolean[] dow;
    HashSet<TimePoint> timePoints = new HashSet<>(MAX_TIMEPOINTS);

    public EditBoxViewModel() {
        dow = new boolean[7];
        Arrays.fill(dow, false);
        timePoints.add(new TimePoint(-1,-1));
    }

    public InputFilter getLetterDigitFilter() {
        return new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!Character.isLetterOrDigit(source.charAt(i))) {
                        return "";
                    }
                }
                return null;    // Accept original
            }
        };
    }

    /** return null if TimePoint wasnot added
     * return TimePoint if OK*/
    public TimePoint addTimePoint() {
        if (timePoints.size() + 1 <= MAX_TIMEPOINTS) {
            TimePoint tp = new TimePoint();
            timePoints.add(tp);
            return tp;
        }
        return null;
    }

    public void removeTimePoint(TimePoint tp) {
        timePoints.remove(tp);
    }

    public void createBox() {
        Bracelet bracelet = Bracelet.getInstance();
        Box.BoxBuilder builder = new Box.BoxBuilder(boxId, false)
                .setPillName(pillName)
                .setPillDescription(pillDescription)
                .setIsDay(isDay.getValue().booleanValue())
                .setDay(Integer.parseInt(day))
                .setDow(dow)
                .setTimePoints(timePoints);

        Box newBox = builder.build();
        bracelet.putBox(newBox);
    }

    public Bundle getMistakes() {
        Bundle bundle = new Bundle();
        //TODO заменить строки на ресурсы, т.е. передавать инты R.string...
        if (boxId < 0 || boxId >= 4) {
            bundle.putString("boxId", "BoxId isn't correct: " + boxId);
        }

        if (pillName == null || pillName.isEmpty()) {
            bundle.putString("pillName", "PillName cannot be empty");
        } else if (pillName.length() > MAX_PILLNAME_LENGTH) {
            bundle.putString("pillName", "PillName is too long: " + pillName.length() + " characters");
        }

        if (pillDescription == null || pillDescription.isEmpty()) {
            bundle.putString("pillDescription", "PillDescription cannot be empty");
        }

        if (pillDescription == null || pillDescription.isEmpty()) {
            bundle.putString("pillDescription", "PillDescription cannot be empty");
        }

        if (isDay.getValue().booleanValue()) {
            int day = Integer.valueOf(this.day);
            if (day <= 0) {
                bundle.putString("day", "Days cannot be negative or equal zero");
            } else if (day > MAX_DAYS) {
                bundle.putString("day", "Days gap it too long. Max is " + MAX_DAYS + " days");
            }
        } else {
            int counter = 0;
            for (boolean dow : this.dow) {
                if (dow) {
                    counter++;
                }
            }
            if (counter < 1) {
                bundle.putString("dow", "Choose at least one day of the week");
            }
        }

        if (timePoints.size() == 0) {
            bundle.putString("timePoints.general", "Add at least one timePoint");
        } else if (timePoints.size() > MAX_TIMEPOINTS) {
            bundle.putString("timePoints.general", "Too many times. Max is " + MAX_TIMEPOINTS);
        } else {    // Проверка на повторения и пустоту
            List<TimePoint> repetitiveTimePoints = new ArrayList<>();   // Список Повторяющихся элементов
            List<TimePoint> wrongTimePoints = new ArrayList<>();        // Список Неверных элементов
            List<TimePoint> emptyTimePoints = new ArrayList<>();        // Список Пустых элементов
            SparseArray<TimePoint> map = new SparseArray<>();           // Список Уникальных и Корректных элементов

            for (TimePoint tp : timePoints) {
                if (tp.getHour() < 0 || tp.getMin() < 0) {
                    emptyTimePoints.add(tp);                            // Пометить пустую точку
                } else {
                    if (tp.getHour() >= 24 || tp.getMin() >= 60) {
                        wrongTimePoints.add(tp);                        // Пометить точку со временем за рамками 60 мин и 24 часов
                    } else {
                        int hash = tp.validationHashCode();             // Получить хэш из полей
                        if (map.get(hash) == null) {                    // Если этот объект уникален
                            map.append(hash, tp);                       //   Добавить
                        } else {                                        // Иначе
                            repetitiveTimePoints.add(tp);               //   Пометить повторяющийся объект
                        }
                    }
                }
            }

            if (emptyTimePoints.size() != 0) {
                bundle.putString("timePoints.empty", "Найдены пустые точки");
                bundle.putInt("timePoints.empty.length", emptyTimePoints.size());
            }

            if (wrongTimePoints.size() != 0) {                          // Записать в свёрток количество пустых (чтобы использовать массив emptyTimePoints[] во View)
                writeFaultTimePointsToBundle(
                        bundle,
                        wrongTimePoints,
                        "wrong",
                        "Найдены неверно заполненные точки");
            }
            if (repetitiveTimePoints.size() != 0) {                     // Записать в свёрток повторяющиеся точки
                writeFaultTimePointsToBundle(
                        bundle,
                        repetitiveTimePoints,
                        "repetitive",
                        "Найдены повторяющиеся точки");
            }
        }

        return bundle;
    }

    private void writeFaultTimePointsToBundle(Bundle bundle, List<TimePoint> faultTps, String faultTag, String faultMessage) {
        int faultSize = faultTps.size();
        int[] hours = new int[faultSize];
        int[] mins = new int[faultSize];
        for (int i = 0; i < faultSize; i++) {
            hours[i] = faultTps.get(i).getHour();
            mins[i] = faultTps.get(i).getMin();
        }
        // Для того, чтобы View быстро нашло необходимые TimePoint'ы
        bundle.putIntArray("timePoints."+faultTag+".hours[]", hours);
        bundle.putIntArray("timePoints."+faultTag+".mins[]", mins);
        bundle.putString("timePoints."+faultTag, faultMessage);
    }

//Set

    public void setBoxId(int boxId) {
        this.boxId = boxId;
    }
    public void setPillName(String pillName) {
        this.pillName = pillName;
    }
    public void setPillDescription(String pillDescription) {
        this.pillDescription = pillDescription;
    }
    public void setIsDay(@NonNull boolean src) {
        if (isDay.getValue().booleanValue() != src) {

        }
        this.isDay.setValue(src);
    }
    public void setDay(String src) {
        this.day = src;
    }
    public void setDow(boolean[] src) {
        this.dow = src;
    }
//Get

    public int getBoxId() {
        return boxId;
    }
    public String getPillName() {
        return pillName;
    }
    public String getPillDescription() {
        return pillDescription;
    }
    public LiveData<Boolean> getIsDay() {
        return isDay;
    }
    public String getDay() {
        return day;
    }
    public boolean[] getDow() {
        return dow;
    }
    public HashSet<TimePoint> getTimePoints() {
        return timePoints;
    }

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        callbacks.add(callback);
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        callbacks.remove(callback);
    }
}
