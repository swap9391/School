package com.exa.mydemoapp.webservice;

/**
 * Created by midt-078 on 20/2/18.
 */
public interface VolleyResponseListener<T> {


    void onResponse(T[] object);
    void onResponse(T object);

    void onError(String message);


    interface PostResponse {

        void onResponse(String id);

        void onError(String message);

    }

}