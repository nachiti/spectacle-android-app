package com.example.spectacleapp.service;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthenticationInterceptor implements Interceptor {

    private String authToken;

    public AuthenticationInterceptor(String token) {
        this.authToken = token;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder builder = original.newBuilder()
                .header("Authorization", authToken);

        Request request = builder.build();
        System.out.println("********************************************");
        System.out.println("Header: "+request.headers().toString());
        System.out.println("url: "+request.url());
        System.out.println("body: "+request.body());
        System.out.println("********************************************");
        return chain.proceed(request);
    }
}