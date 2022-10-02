package com.group3.project4.shop;

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
import com.example.project4.databinding.FragmentShopBinding;
import com.group3.project4.login.LoginResult;
import com.group3.project4.util.Globals;
import com.group3.project4.util.RetrofitInterface;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShopFragment extends Fragment implements ShopRecyclerViewAdapter.IShopRecycler {
    FragmentShopBinding binding;
    ArrayList<Item> items = new ArrayList<>();
    LinearLayoutManager layoutManager;
    ShopRecyclerViewAdapter adapter;
    RetrofitInterface retrofitInterface;
    Retrofit retrofit;

    public ShopFragment() {
        // Required empty public constructor
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

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentShopBinding.inflate(inflater, container, false);
        getActivity().setTitle("Browse Items");
        layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(binding.recyclerView.getContext(), layoutManager.getOrientation());
        binding.recyclerView.addItemDecoration(mDividerItemDecoration);

        adapter = new ShopRecyclerViewAdapter(items, this);
        binding.recyclerView.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getItems();
    }

    private void getItems() {
        Call<ItemResponse> call = retrofitInterface.getItems();
        call.enqueue(new Callback<ItemResponse>() {
            @Override
            public void onResponse(Call<ItemResponse> call, Response<ItemResponse> response) {
                ItemResponse itemResponse = response.body();
                items = new ArrayList<>(Arrays.asList(itemResponse.getItemsArray()));
                PutDataIntoRecyclerView(items);
            }

            @Override
            public void onFailure(Call<ItemResponse> call, Throwable t) {
                Log.d("JWT", "Fail");
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void PutDataIntoRecyclerView(ArrayList<Item> itemList) {
        adapter = new ShopRecyclerViewAdapter(itemList, this);
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void addItemToCart(Item item) {
        mListener.addItemToCart(item);
    }

    IListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (IListener) context;
    }

    public interface IListener {
        void addItemToCart(Item item);
    }
}