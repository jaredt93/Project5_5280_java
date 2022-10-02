package com.group3.project4.util;

import com.group3.project4.profile.UpdateUserResult;
import com.group3.project4.login.LoginResult;
import com.group3.project4.shop.Item;
import com.group3.project4.shop.ItemResponse;
import com.group3.project4.signup.SignupResult;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitInterface {
    @POST("/api/auth")
    Call<UserResult> login(@Body HashMap<String, String> data);

    @POST("/api/signup")
    Call<SignupResult> signup(@Body HashMap<String, String> data);

    @POST("/api/user/update")
    Call<UserResult> updateUser(@Header ("x-jwt-token") String token, @Body HashMap<String, Object> data);

    @GET("/api/getItems")
    Call<ItemResponse> getItems();
}
