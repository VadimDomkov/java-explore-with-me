package ru.practicum.exeptions;

public class NotValidArgumentException extends RuntimeException {
    public NotValidArgumentException(String message) {
        super(message);
    }
}
