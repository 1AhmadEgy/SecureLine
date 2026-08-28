package com.secureline.secureline.util;

public class Result<T> {

    private final T data;
    private final int errorCode;
    private final String errorMessage;
    private final boolean success;

    private Result(T data, int errorCode, String errorMessage, boolean success) {
        this.data = data;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.success = success;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(data, ErrorCodes.SUCCESS, null, true);
    }

    public static <T> Result<T> error(int errorCode) {
        return new Result<>(null, errorCode, ErrorCodes.getErrorMessage(errorCode), false);
    }

    public static <T> Result<T> error(int errorCode, String message) {
        return new Result<>(null, errorCode, message, false);
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isError() {
        return !success;
    }

    public T getData() {
        return data;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
