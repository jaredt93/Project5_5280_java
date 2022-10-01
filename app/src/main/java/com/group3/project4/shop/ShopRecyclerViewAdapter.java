package com.group3.project4.shop;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.project4.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ShopRecyclerViewAdapter extends RecyclerView.Adapter<ShopRecyclerViewAdapter.ViewHolder> {
    ArrayList<Item> items;
    IShopRecycler mListener;
    private Context context;

    public ShopRecyclerViewAdapter(ArrayList<Item> items, IShopRecycler mListener) {
        this.items = items;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = items.get(position);
        holder.item = item;

        holder.textViewItemName.setText(item.getName());
        holder.textViewItemPrice.setText("$" + item.getPrice().toString());
        holder.textViewItemDiscount.setText(item.getDiscount().toString() + "%");

        String url = item.getPhoto();

        Picasso.get()
                .load(url)
                .placeholder(R.drawable.ic_outline_downloading_24)
                .resize(50, 50)
                .into(holder.imageViewItemPhoto);
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewItemPhoto;
        ImageView imageViewAddItem;
        TextView textViewItemName;
        TextView textViewItemPrice;
        TextView textViewItemDiscount;

        View rootView;
        int position;
        Item item;
        IShopRecycler mListener;

        public ViewHolder(@NonNull View itemView, IShopRecycler mListener) {
            super(itemView);
            rootView = itemView;
            this.mListener = mListener;

            imageViewItemPhoto = itemView.findViewById(R.id.imageViewItemPhoto);
            imageViewAddItem = itemView.findViewById(R.id.imageViewAddItem);
            textViewItemName = itemView.findViewById(R.id.textViewItemName);
            textViewItemPrice = itemView.findViewById(R.id.textViewItemPrice);
            textViewItemDiscount = itemView.findViewById(R.id.textViewItemDiscount);

            imageViewAddItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.addItemToCart(item);
                }
            });
        }
    }

    interface IShopRecycler {
        void addItemToCart(Item item);
    }
}

