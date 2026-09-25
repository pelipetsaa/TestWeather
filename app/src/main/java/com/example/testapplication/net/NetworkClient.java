package com.example.testapplication.net;

import com.example.testapplication.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by alex_himik on 26.01.15.
 */
public class NetworkClient {

    private static final int CONNECTION_TIMEOUT = 20000;   //milliseconds
    private static final String API_KEY_PARAM = "appid";

    private final IRestClient restApi;
    private static volatile NetworkClient instance;

    NetworkClient(String baseUrl, String apiKey) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    HttpUrl url = request.url().newBuilder()
                            .addQueryParameter(API_KEY_PARAM, apiKey)
                            .build();
                    return chain.proceed(request.newBuilder().url(url).build());
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        restApi = retrofit.create(IRestClient.class);
    }

    public static NetworkClient getInstance(String apiKey) {
        if (instance == null) {
            synchronized (NetworkClient.class) {
                if (instance == null) {
                    instance = new NetworkClient(Constants.BASE_URL, apiKey);
                }
            }
        }
        return instance;
    }


    public IRestClient getRestApi() {
        return restApi;
    }

}
