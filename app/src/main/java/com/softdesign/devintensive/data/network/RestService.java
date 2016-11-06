package com.softdesign.devintensive.data.network;

import com.softdesign.devintensive.data.network.request.UserLoginRequest;
import com.softdesign.devintensive.data.network.response.UserListResponse;
import com.softdesign.devintensive.data.network.response.UserModelResponse;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by OsIpoFF on 05.11.16.
 */

public interface RestService {

    @POST("login")
    Call<UserModelResponse> loginUser (@Body UserLoginRequest request);

    @Multipart
    @POST("user/{userId}/publicValues/profilePhoto")
    Call<ResponseBody> uploadImage(@Path("userId") String userId,
                                   @Part MultipartBody.Part file);

    @GET("user/list?orderBy=rating")
    Call<UserListResponse> getUserList();
}
