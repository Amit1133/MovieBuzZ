package com.example.amit.moviebuzz;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface GetDataServices {
    //?apikey=7a8658b4&s=batman
    //@GET("?apikey=7a8658b4&s={sr}")
    @GET
    Call<SearchedData> getJsonData(@Url String u);

    @GET
    Call<SingleMovie> getSingleMOvieJsonData(@Url String u);

    @GET
    Response getObjectData(@Url String u);


}
