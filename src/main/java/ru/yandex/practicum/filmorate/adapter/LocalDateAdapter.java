package ru.yandex.practicum.filmorate.adapter;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public JsonElement serialize(LocalDate date, Type type, JsonSerializationContext context) {
        String dateString = date.format(DATE_FORMATTER);
        return new JsonPrimitive(dateString);
    }


    @Override
    public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext context)
            throws JsonParseException {
        String dateString = json.getAsString();
        return LocalDate.parse(dateString, DATE_FORMATTER);
    }
}