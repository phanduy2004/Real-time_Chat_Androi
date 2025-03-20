package com.myjob.real_time_chat_final.api;

import com.myjob.real_time_chat_final.model.ConversationMember;
import com.myjob.real_time_chat_final.model.User;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {
    @GET("api/users/all")
    Call<List<User>> getAllUsers();
    @POST("api/users/login") // Đường dẫn đến API login
    Call<Map<String, Object>> login(@Body Map<String, String> body);

    @GET("api/users/contacts")
    Call<List<ConversationMember>> getContacts(@Query("user_id") int userId);

}
