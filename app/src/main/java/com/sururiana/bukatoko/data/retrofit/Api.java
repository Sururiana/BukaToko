package com.sururiana.bukatoko.data.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {

    private static Retrofit retrofit;
    public static Retrofit getUrl(){
        retrofit = new Retrofit.Builder()
                .baseUrl("http://muslikh.my.id/tokoonline/public/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    public static Retrofit getUrlRajaOngkir(String base_url){
        retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
}