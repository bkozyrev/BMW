package com.utro.bmw.model;

public interface GetItemHandler<T>{
    public void done(T data);
    public void error(String responseError);
}
