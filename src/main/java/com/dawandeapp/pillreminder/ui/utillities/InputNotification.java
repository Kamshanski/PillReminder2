package com.dawandeapp.pillreminder.ui.utillities;

import java.util.HashSet;
import java.util.Set;

// Неплохой, но самонеконтроллируемый и потоконебезопасный FlyWeight. Жаль бесполезый(
public class InputNotification {
    public static final String OK = "OK";
    public static final String OK_MSG = null;
    public static final String NAME_SIZE_OUT_OF_RANGE = "NameSizeOutOfRange";
    public static final String NAME_SIZE_OUT_OF_RANGE_MSG = "Название слишком длинное, более Х символов";

    private static Set<InputNotification> pool = new HashSet<>();
    // TODO замениить на инты, а сами тексты перенести в Ресурсы
    String notification;
    String fullMessage;
    boolean busy = true;



    private InputNotification() {}


    public static InputNotification getOK() {
        InputNotification in = getFreeNotif();
        in.notification = OK;
        in.fullMessage = OK_MSG;
        in.busy = true;
        return in;
    }
//Бесполезная ошибка, все названия и так большие
    public static InputNotification getNameSizeOutOfRange() {
        InputNotification in = getFreeNotif();
        in.notification = NAME_SIZE_OUT_OF_RANGE;
        in.fullMessage = NAME_SIZE_OUT_OF_RANGE_MSG;
        in.busy = true;
        return in;
    }


    /**
     * Must be executed after processing. Needs to free memory and keep it clear
     */
    public void recycle() {
        busy = false;
        if (pool.size() > 1) {
            pool.remove(this);
        }
    }

    public static InputNotification getFreeNotif() {
        for (InputNotification notif : pool) {
            if (notif.busy) {
                return notif;
            }
        }
        InputNotification newNotif = new InputNotification();
        pool.add(newNotif);
        return newNotif;
    }

    public String getNotification() {
        return notification;
    }

    public String getFullMessage() {
        return fullMessage;
    }
}
