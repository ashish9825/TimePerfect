package com.example.ashishpanjwani.timeperfectagain.Interfaces;

import com.example.ashishpanjwani.timeperfectagain.Model.ClassDetail;
import com.example.ashishpanjwani.timeperfectagain.Model.CurrentTimeList;
import com.example.ashishpanjwani.timeperfectagain.Model.DayWiseList;
import com.example.ashishpanjwani.timeperfectagain.Model.UserProfile;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class TimePerfectAPIs {

    private static String BASE_URL="https://blooming-thicket-31144.herokuapp.com";

    public static CurrentTimeAPI currentTime=null;

    public static CurrentTimeAPI getCurrentTimeTable() {

        if (currentTime==null) {
            Retrofit retrofit=new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            currentTime=retrofit.create(CurrentTimeAPI.class);
        }
        return currentTime;
    }

    public interface CurrentTimeAPI {

        @FormUrlEncoded
        @POST("/signin/")
        @Headers("Content-Type:application/x-www-form-urlencoded")
        Call<List<UserProfile>> loginUser(
                @Field("email") String email);

        @FormUrlEncoded
        @POST("/register/")
        @Headers("Content-Type:application/x-www-form-urlencoded")
        Call<List<UserProfile>> registerUser(
                @Field("email") String email,
                @Field("name") String name,
                @Field("college") String college,
                @Field("branch") String branch,
                @Field("sem") Integer sem);

        @GET("/{college}/{branch}/{sem}/{day}")
        Call<List<CurrentTimeList>> getCurrentTime(@Path("college") String college, @Path("branch") String currBranch, @Path("sem") Integer currSem, @Path("day") String dayOfWeek);

        @GET("/{college}/{branch}/{sem}/all")
        Call<List<DayWiseList>> getDayList(@Path("college") String college, @Path("branch") String currBranch, @Path("sem") Integer currSem);

        @GET("/{college}/{branch}/{sem}")
        Call<List<ClassDetail>> getClassDetails(@Path("college") String college, @Path("branch") String currBranch, @Path("sem") Integer currSem);
    }
}
