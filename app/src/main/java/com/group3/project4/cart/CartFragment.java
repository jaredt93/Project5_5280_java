package com.group3.project4.cart;

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

import com.example.project4.R;
import com.example.project4.databinding.FragmentCartBinding;
import com.group3.project4.login.LoginFragment;
import com.group3.project4.profile.User;
import com.group3.project4.profile.UserProfileFragment;
import com.group3.project4.shop.Item;

import java.util.ArrayList;

public class CartFragment extends Fragment implements CartRecyclerViewAdapter.ICartRecycler {
    FragmentCartBinding binding;
    Order order = new Order();
    LinearLayoutManager layoutManager;
    CartRecyclerViewAdapter adapter;
    private static final String ORDER = "ORDER";

    public CartFragment() {
        // Required empty public constructor
    }

    public static CartFragment newInstance(Order order) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putSerializable(ORDER, order);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.order = (Order) getArguments().getSerializable(ORDER);
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

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("JWT", "addItemToCart: " + order.getCartItems().toString());

        binding.imageButtonCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.checkout();
            }
        });

        binding.buttonEmptyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order = new Order();
                mListener.emptyCart();

            }
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

    public interface IListener {
        void deleteItemFromCart(Item cartItem);
        void emptyCart();
        void checkout();
    }
}