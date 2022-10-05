package com.group3.project4.cart;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.braintreepayments.api.BraintreeClient;
import com.braintreepayments.api.DropInClient;
import com.braintreepayments.api.DropInListener;
import com.braintreepayments.api.DropInRequest;
import com.braintreepayments.api.DropInResult;
import com.braintreepayments.api.FetchMostRecentPaymentMethodCallback;
import com.braintreepayments.api.UserCanceledException;
import com.example.project4.R;
import com.example.project4.databinding.FragmentCartBinding;
import com.group3.project4.MainActivity;
import com.group3.project4.login.LoginFragment;
import com.group3.project4.profile.User;
import com.group3.project4.profile.UserProfileFragment;
import com.group3.project4.shop.Item;
import com.group3.project4.util.BraintreeClientTokenProvider;
import com.group3.project4.util.Globals;
import com.group3.project4.util.RetrofitInterface;
import com.group3.project4.util.UserResult;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CartFragment extends Fragment implements CartRecyclerViewAdapter.ICartRecycler, DropInListener {
    FragmentCartBinding binding;
    Order order = new Order();
    User user;
    LinearLayoutManager layoutManager;
    CartRecyclerViewAdapter adapter;
    private DropInClient dropInClient;
    RetrofitInterface retrofitInterface;
    Retrofit retrofit;
    private static final int DROP_IN_REQUEST_CODE = 001;


    private static final String ORDER = "ORDER";
    private static final String USER = "USER";

    public CartFragment() {
        // Required empty public constructor
    }

    public static CartFragment newInstance(Order order, User user) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putSerializable(ORDER, order);
        args.putSerializable(USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrofit = new Retrofit.Builder()
                .baseUrl(Globals.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
        if (getArguments() != null) {
            this.order = (Order) getArguments().getSerializable(ORDER);
            this.user = (User) getArguments().getSerializable(USER);
            if(this.order == null) {
                this.order = new Order();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        getActivity().setTitle("Your Cart");

        if(order != null) {
            binding.textViewOrderTotal.setText("Total: $" + order.getOrderTotal());
        }

        layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(binding.recyclerView.getContext(), layoutManager.getOrientation());
        binding.recyclerView.addItemDecoration(mDividerItemDecoration);

        adapter = new CartRecyclerViewAdapter(order.cartItems, this);
        binding.recyclerView.setAdapter(adapter);

        DropInRequest dropInRequest = new DropInRequest();
        dropInClient = new DropInClient(this, dropInRequest, new BraintreeClientTokenProvider(user.getToken(), user.getCustomerId()));
        dropInClient.setListener((DropInListener) this);
        dropInClient.fetchMostRecentPaymentMethod(getActivity(), (dropInResult, error) -> dropInResult.describeContents());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("JWT", "addItemToCart: " + order.getCartItems().toString());

        binding.imageButtonCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!order.getCartItems().isEmpty()) {
                    dropInClient.launchDropInForResult(getActivity(), DROP_IN_REQUEST_CODE);
                }
            }
        });

        binding.buttonEmptyCart.setOnClickListener(view1 -> {
            order = new Order();
            mListener.emptyCart();
        });
    }

    private void getItems() {
    }

    @Override
    public void deleteItemFromCart(Item cartItem) {
        mListener.deleteItemFromCart(cartItem);
    }

    IListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (IListener) context;
    }

    @Override
    public void onDropInSuccess(@NonNull DropInResult dropInResult) {
        Log.d("JWT", "onDropInSuccess: " + dropInResult.getPaymentMethodNonce().getString());
        Activity activity = getActivity();
        HashMap<String, Object> data = new HashMap<>();

        Log.d("JWT", "onDropInSuccess: " + user.getOrder().getOrderTotal().toString());
        data.put("amount", user.getOrder().getOrderTotal().toString());
        data.put("order", user.getOrder().getCartItems());
        data.put("paymentMethodNonce", dropInResult.getPaymentMethodNonce().getString());
        data.put("customerId", user.getCustomerId());

        Call<UserResult> call = retrofitInterface.checkout(user.getToken(), data);
        call.enqueue(new Callback<UserResult>() {
            @Override
            public void onResponse(Call<UserResult> call, Response<UserResult> response) {
                if (response.code() == 200) {
                    UserResult result = response.body();
                    Toast.makeText(activity, "Transaction complete.", Toast.LENGTH_LONG).show();
                    mListener.clearAddToHistory();
                } else {
                    Toast.makeText(activity, "Something went wrong.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserResult> call, Throwable t) {
                Toast.makeText(activity, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onDropInFailure(@NonNull Exception error) {
        Log.d("JWT", "onDropInFailure: " + error.getMessage());
        if (error instanceof UserCanceledException) {
            // user canceled
        } else {
            // handle error
        }
    }

    public interface IListener {
        void deleteItemFromCart(Item cartItem);
        void emptyCart();
        void clearAddToHistory();
    }
}