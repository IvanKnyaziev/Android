package com.example.user.mvvmregistration.api;

import com.example.user.mvvmregistration.model.RegistrationResponseModel;
import com.example.user.mvvmregistration.model.UserDetails.ResponseResource;
import com.example.user.mvvmregistration.model.UserDetails.ResponseUser;
import com.example.user.mvvmregistration.model.User;
import com.example.user.mvvmregistration.model.UsersResponse;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import rx.Single;

public interface IApi {

    @POST("register/")
    Single<RegistrationResponseModel> registration(@Body User user);

    @POST("login/")
    Single<RegistrationResponseModel> login(@Body User user);

    @GET("users/")
    Observable<UsersResponse> getUsers(@Query("page") int page);

    @GET("users/{id}")
    Observable<ResponseUser> getResponseUser(@Path("id") int userId);

    @GET("unknown/{id}")
    Observable<ResponseResource> getResponseResource(@Path("id") int userId);

}
