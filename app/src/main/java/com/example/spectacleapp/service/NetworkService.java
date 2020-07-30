package com.example.spectacleapp.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {

    //public static final String URl_SERVEUR = "http://192.168.0.20:8080/";
    public static final String API_URL = "http://spectacle-serv.herokuapp.com/api/";
    public static final String API_IMAGE = API_URL+"public/images/";

    private static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create();

    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create(gson));

    private static Retrofit retrofit = retrofitBuilder.build();

    //créer une instance de HttpLoggingInterceptor et définir le niveau de journalisation
    private static HttpLoggingInterceptor loggingInterceptor =
            new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY);

    private static OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
            .connectTimeout(2, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES);

    public static <S> S createService(Class<S> serviceClass) {

        if (!okHttpClientBuilder.interceptors().contains(loggingInterceptor)) {
            okHttpClientBuilder.addInterceptor(loggingInterceptor);
            retrofitBuilder.client(okHttpClientBuilder.build());
        }
        retrofitBuilder.client(okHttpClientBuilder.build());

        return retrofit.create(serviceClass);
    }
}
