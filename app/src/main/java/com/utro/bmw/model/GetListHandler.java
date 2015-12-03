package com.utro.bmw.model;

import java.util.ArrayList;

public interface GetListHandler<T> {
    public void done(ArrayList<T> data);
    public void error(String responseError);
    //public void error(String responseError, int statusCode);
}
