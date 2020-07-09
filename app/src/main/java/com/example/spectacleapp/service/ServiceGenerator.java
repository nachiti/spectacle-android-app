package com.example.spectacleapp.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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

import static androidx.core.content.ContextCompat.getSystemService;

public class ServiceGenerator {

    public static final String API_URL = "http://spectacle-serv.herokuapp.com/api/";
    public static final String API_IMAGE = API_URL+"images/";

    private static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create();

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create(gson));

    private static Retrofit retrofit = builder.build();

    //créer une instance de HttpLoggingInterceptor et définir le niveau de journalisation
    private static HttpLoggingInterceptor loggingInterceptor =
            new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BASIC);

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS);

    private static Interceptor interceptor = new Interceptor() {

        @Override
        public Response intercept(Chain chain) throws IOException {
            System.out.println("coucou intercept:)");
            Request request = chain.request();
            // try the request
            Response response = chain.proceed(request);
            int tryCount = 0;
            while (!response.isSuccessful() && tryCount < 3) {
                System.out.println("coucou intercept:"+"Request is not successful - " + tryCount);
                tryCount++;
                // retry the request
                response = chain.proceed(request);
            }
            // otherwise just pass the original response on
            return response;
        }
    };

    public static <S> S createService(Class<S> serviceClass)
    {
        if (!httpClient.interceptors().contains(loggingInterceptor)) {
            httpClient.addInterceptor(loggingInterceptor);
            builder.client(httpClient.build());
        }
        System.out.println("coucou test oo");
        httpClient.addInterceptor(interceptor);
        builder.client(httpClient.build());

        return retrofit.create(serviceClass);
    }
}
