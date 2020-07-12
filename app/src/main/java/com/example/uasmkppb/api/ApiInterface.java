package com.example.uasmkppb.api;

import com.example.uasmkppb.models.Search;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ApiInterface {

//    @GET("top-headlines")
//    Call<News> getNews(
//            @Query("country") String country,
//            @Query("apiKey") String apiKey
//    );
        @GET("search")
        Call<Search> search(
                @Header("user-key") String apiKey
        );
}
