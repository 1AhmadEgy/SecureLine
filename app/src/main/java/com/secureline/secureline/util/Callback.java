package com.secureline.secureline.util;

public interface Callback<T> {
    void onSuccess(T result);
    void onError(int errorCode, String errorMessage);
}
