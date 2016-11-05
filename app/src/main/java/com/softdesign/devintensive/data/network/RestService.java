package com.softdesign.devintensive.data.network;

import com.softdesign.devintensive.data.network.request.UserLoginRequest;
import com.softdesign.devintensive.data.network.response.UserModelResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by OsIpoFF on 05.11.16.
 */

public interface RestService {

    @POST("login")
    Call<UserModelResponse> loginUser (@Body UserLoginRequest request);
}
