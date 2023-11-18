package ru.yandex.practicum.filmorate.exception;

public class ServerException extends RuntimeException {
    public ServerException(String message) {
        System.out.println(message);
    }
}
