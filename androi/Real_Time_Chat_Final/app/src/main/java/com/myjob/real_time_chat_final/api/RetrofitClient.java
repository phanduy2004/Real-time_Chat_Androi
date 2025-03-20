package com.myjob.real_time_chat_final.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://10.0.2.2:8686/";

    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    // Hàm này giúp lấy ApiService
    public static UserService getApiUserService() {
        return getInstance().create(UserService.class);
    }
    public static MessageService getApiMessageService() {
        return getInstance().create(MessageService.class);
    }
}
