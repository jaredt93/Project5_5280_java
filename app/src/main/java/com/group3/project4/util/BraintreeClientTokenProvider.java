package com.group3.project4.util;

import androidx.annotation.NonNull;

import com.braintreepayments.api.ClientTokenCallback;
import com.braintreepayments.api.ClientTokenProvider;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BraintreeClientTokenProvider implements ClientTokenProvider {

    private static final Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(Globals.URL)
            .addConverterFactory(GsonConverterFactory.create());

    private static final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    public static RetrofitInterface createService() {
        builder.client(httpClient.build());
        Retrofit retrofit = builder.build();
        return retrofit.create(RetrofitInterface.class);
    }

    public void getClientToken(@NonNull ClientTokenCallback callback) {
        Call<ClientToken> call = createService().getClientToken();
        call.enqueue(new Callback<ClientToken>() {
            @Override
            public void onResponse(Call<ClientToken> call, Response<ClientToken> response) {
                callback.onSuccess(response.body().getToken());
            }

            @Override
            public void onFailure(Call<ClientToken> call, Throwable t) {
                callback.onFailure(new Exception(t));
            }
        });
    }
}
