package com.example.dima.newreader;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TopApi {
    @GET("topstories.json")
    Call<List<Integer>> articles();
}
