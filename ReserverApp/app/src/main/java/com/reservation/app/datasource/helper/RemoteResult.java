package com.reservation.app.datasource.helper;

/**
 * @author Fatema
 * since 9/1/21.
 */
public interface RemoteResult<T> {

    void onSuccess(T data);

    void onFailure(Exception error);
}
