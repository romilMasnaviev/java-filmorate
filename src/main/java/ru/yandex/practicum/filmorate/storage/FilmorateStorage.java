package ru.yandex.practicum.filmorate.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.yandex.practicum.filmorate.adapter.LocalDateAdapter;

import java.time.LocalDate;
import java.util.HashMap;


public class FilmorateStorage {
    protected static final LocalDate FILM_MINIMUM_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    protected static final int FILM_DESCRIPTIONS_LENGTH = 200;
    protected final HashMap<Integer, Object> filmorateStorageHashMap = new HashMap<>();
    protected final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();

    protected int id = 1;

}
