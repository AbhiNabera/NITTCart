package com.example.avinash.nittcart.retrofit_api_calls;


import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by AVINASH on 3/22/2017.
 */
public class ApiClient {

    private static final String BASE_URL = "http://139.59.79.221:80";   //set base url
    private static ApiClient apibuilder;
    private static Retrofit mRetrofit;

    private ApiClient() {

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public static ApiClient getInstance() {
        if (apibuilder == null) {
            synchronized (ApiClient.class) {
                if (apibuilder == null)
                    apibuilder = new ApiClient();
            }
        }
        return apibuilder;
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }
}
