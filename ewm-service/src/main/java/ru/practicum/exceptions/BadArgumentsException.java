package ru.practicum.exceptions;

public class BadArgumentsException extends RuntimeException {
    public BadArgumentsException(String message) {
        super(message);
    }
}
