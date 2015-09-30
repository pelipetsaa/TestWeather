package com.example.testapplication.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.testapplication.Constants;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by alex_himik on 26.01.15.
 */
public class NetworkClient {

    private static final int CONNECTION_TIMEOUT = 20000;   //milliseconds

    private IRestClient restApi;
    private static NetworkClient instance;

    private NetworkClient() {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constants.BASE_URL)
                .setClient(new OkClient(client))
                .build();

        restApi = restAdapter.create(IRestClient.class);
    }

    public static NetworkClient getInstance() {
        if(instance == null) {
            instance = new NetworkClient();
        }
        return instance;
    }


    public IRestClient getRestApi() {
        return restApi;
    }

}
