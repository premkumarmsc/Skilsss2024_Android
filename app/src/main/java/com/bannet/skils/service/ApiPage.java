package com.bannet.skils.service;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiPage {

    private static Retrofit retrofit = null;
    public static ApiInterfacepagi getClient() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        // change your base URL
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(EndPoint.BASE_URL_pagnation)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        //Creating object for our interface
        ApiInterfacepagi api = retrofit.create(ApiInterfacepagi.class);
        return api; // return the APIInterface object
    }

}
