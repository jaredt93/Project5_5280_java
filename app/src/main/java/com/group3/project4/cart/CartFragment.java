package com.group3.project4.cart;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project4.R;
import com.example.project4.databinding.FragmentCartBinding;
import com.group3.project4.shop.Item;

import java.util.ArrayList;

public class CartFragment extends Fragment implements CartRecyclerViewAdapter.ICartRecycler {
    FragmentCartBinding binding;
    ArrayList<CartItem> cartItems = new ArrayList<>();
    LinearLayoutManager layoutManager;
    CartRecyclerViewAdapter adapter;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        getActivity().setTitle("Your Items");

        layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(binding.recyclerView.getContext(), layoutManager.getOrientation());
        binding.recyclerView.addItemDecoration(mDividerItemDecoration);

        adapter = new CartRecyclerViewAdapter(cartItems, this);
        binding.recyclerView.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void getItems() {
    }

    @Override
    public void deleteItemFromCart(CartItem cartItem) {
        mListener.deleteItemFromCart(cartItem);
    }

    IListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (IListener) context;
    }

    public interface IListener {
        void deleteItemFromCart(CartItem cartItem);
    }
}