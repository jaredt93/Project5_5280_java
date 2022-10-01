package com.group3.project4.cart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project4.R;
import com.group3.project4.shop.Item;

import java.util.ArrayList;

public class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.ViewHolder> {
    ArrayList<Item> cartItems;
    CartRecyclerViewAdapter.ICartRecycler mListener;

    public CartRecyclerViewAdapter(ArrayList<Item> cartItems, CartRecyclerViewAdapter.ICartRecycler mListener) {
        this.cartItems = cartItems;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public CartRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_row_item, parent, false);
        CartRecyclerViewAdapter.ViewHolder viewHolder = new CartRecyclerViewAdapter.ViewHolder(view, mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartRecyclerViewAdapter.ViewHolder holder, int position) {
        Item cartItem = cartItems.get(position);
        holder.cartItem = cartItem;

        holder.textViewItemName.setText(cartItem.getName());
        holder.textViewItemPriceFinal.setText("$" + cartItem.getQuantity().toString());
        holder.textViewItemQuantity.setText(cartItem.getQuantity().toString());
    }

    @Override
    public int getItemCount() {
        return this.cartItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewItemPhoto;
        ImageView imageViewDeleteItem;
        TextView textViewItemName;
        TextView textViewItemQuantity;
        TextView textViewItemPriceFinal;

        View rootView;
        int position;
        Item cartItem;
        CartRecyclerViewAdapter.ICartRecycler mListener;

        public ViewHolder(@NonNull View itemView, CartRecyclerViewAdapter.ICartRecycler mListener) {
            super(itemView);
            rootView = itemView;
            this.mListener = mListener;

            imageViewItemPhoto = itemView.findViewById(R.id.imageViewItemPhoto);
            imageViewDeleteItem = itemView.findViewById(R.id.imageViewDeleteItem);
            textViewItemName = itemView.findViewById(R.id.textViewItemName);
            textViewItemPriceFinal = itemView.findViewById(R.id.textViewItemPriceFinal);
            textViewItemQuantity = itemView.findViewById(R.id.textViewItemQuantity);

            imageViewDeleteItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.deleteItemFromCart(cartItem);
                }
            });
        }
    }

    interface ICartRecycler {
        void deleteItemFromCart(Item cartItem);
    }
}

