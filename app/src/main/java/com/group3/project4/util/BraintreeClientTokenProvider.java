package com.group3.project4.util;

import androidx.annotation.NonNull;

import com.braintreepayments.api.ClientTokenCallback;
import com.braintreepayments.api.ClientTokenProvider;

import java.util.HashMap;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BraintreeClientTokenProvider implements ClientTokenProvider {
    String customerId;
    String JWT;

    private static final Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(Globals.URL)
            .addConverterFactory(GsonConverterFactory.create());

    private static final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    public BraintreeClientTokenProvider() {
        // empty
    }

    public BraintreeClientTokenProvider(String JWT, String customerId) {
        this.customerId = customerId;
        this.JWT = JWT;
    }

    public static RetrofitInterface createService() {
        builder.client(httpClient.build());
        Retrofit retrofit = builder.build();
        return retrofit.create(RetrofitInterface.class);
    }

    public void getClientToken(@NonNull ClientTokenCallback callback) {
        HashMap<String, String> body = new HashMap();
        body.put("customerId", customerId);
        Call<ClientToken> call = createService().getClientToken(JWT);
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
