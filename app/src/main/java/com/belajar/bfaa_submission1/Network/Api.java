package com.belajar.bfaa_submission1.Network;

import com.belajar.bfaa_submission1.Model.UsersResponse;
import com.belajar.bfaa_submission1.Model.Users;
import com.belajar.bfaa_submission1.Model.UsersDetail;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    @GET("users")
    Call<List<Users>> getUsers(
            @Header("Authorization") String token
    );

    @GET("users/{username}")
    Call<UsersDetail> getUsersDetail(
            @Path("username") String username
    );

    @GET("users/{username}/followers")
    Call<List<Users>> getUserFollowers(
            @Path("username") String username
    );

    @GET("users/{username}/following")
    Call<List<Users>> getUserFollowing(
            @Path("username") String username
    );

    @GET("search/users")
    Call<UsersResponse> getUsersResponse(
            @Query("q") String username
    );
}
