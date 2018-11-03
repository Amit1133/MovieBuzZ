package com.example.amit.moviebuzz;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstances {
    private static Retrofit retrofit;
    private static final String BASE_URL="http://www.omdbapi.com/";
    //http://www.omdbapi.com/?apikey=7a8658b4&

    public static Retrofit getRetrofitClientInstance(){
        if(retrofit==null){
            retrofit= new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
