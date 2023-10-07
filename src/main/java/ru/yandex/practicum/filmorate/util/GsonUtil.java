package ru.yandex.practicum.filmorate.util;

import com.google.gson.GsonBuilder;
import ru.yandex.practicum.filmorate.adapter.LocalDateAdapter;

import java.time.LocalDate;


public class GsonUtil {
    private static final com.google.gson.Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();

    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> type) {
        return gson.fromJson(json, type);
    }
}
