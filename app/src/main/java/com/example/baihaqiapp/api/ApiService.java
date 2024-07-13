package com.example.baihaqiapp.api;

import com.example.baihaqiapp.model.User;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    @GET("users")
    Call<ResponseData> getUsers();

    @PUT("users/{id}")
    Call<User> updateUser(@Path("id") int id, @Body User user);

    @POST("users")
    Call<User> createUser(@Body User user);

    @DELETE("users/{id}")
    Call<Void> deleteUser(@Path("id") int id);

    class ResponseData {
        public List<User> data;
    }
}
