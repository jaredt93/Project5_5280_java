package com.group3.project4.history;

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

import com.example.project4.R;
import com.example.project4.databinding.FragmentOrderHistoryBinding;
import com.example.project4.databinding.FragmentShopBinding;
import com.group3.project4.cart.CartFragment;
import com.group3.project4.cart.Order;
import com.group3.project4.profile.User;
import com.group3.project4.shop.Item;
import com.group3.project4.shop.ItemResponse;
import com.group3.project4.shop.ShopRecyclerViewAdapter;
import com.group3.project4.util.Globals;
import com.group3.project4.util.RetrofitInterface;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrderHistoryFragment extends Fragment implements OrderHistoryRecyclerViewAdapter.IOrderRecycler {
    FragmentOrderHistoryBinding binding;
    LinearLayoutManager layoutManager;
    OrderHistoryRecyclerViewAdapter adapter;
    RetrofitInterface retrofitInterface;
    Retrofit retrofit;
    private static final String USER = "USER";
    User user;

    public OrderHistoryFragment() {
        // Required empty public constructor
    }

    public static OrderHistoryFragment newInstance(User user) {
        OrderHistoryFragment fragment = new OrderHistoryFragment();
        Bundle args = new Bundle();
        args.putSerializable(USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        retrofit = new Retrofit.Builder()
                .baseUrl(Globals.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        if (getArguments() != null) {
            this.user = (User) getArguments().getSerializable(USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOrderHistoryBinding.inflate(inflater, container, false);
        getActivity().setTitle("Order History");
        layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(binding.recyclerView.getContext(), layoutManager.getOrientation());
        binding.recyclerView.addItemDecoration(mDividerItemDecoration);

        adapter = new OrderHistoryRecyclerViewAdapter(user.getOrderHistory(), this);
        binding.recyclerView.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    OrderHistoryFragment.IListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (OrderHistoryFragment.IListener) context;
    }

    public interface IListener {
    }
}