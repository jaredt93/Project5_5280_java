package com.group3.project4.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project4.R;
import com.group3.project4.cart.Order;
import com.group3.project4.shop.Item;

import java.util.ArrayList;

public class OrderHistoryRecyclerViewAdapter extends RecyclerView.Adapter<OrderHistoryRecyclerViewAdapter.ViewHolder> {
    ArrayList<Order> orders = new ArrayList<>();
    OrderHistoryRecyclerViewAdapter.IOrderRecycler mListener;
    private Context context;

    public OrderHistoryRecyclerViewAdapter(ArrayList<Order> orders, OrderHistoryRecyclerViewAdapter.IOrderRecycler mListener) {
        this.orders = orders;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public OrderHistoryRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_row_item, parent, false);
        OrderHistoryRecyclerViewAdapter.ViewHolder viewHolder = new OrderHistoryRecyclerViewAdapter.ViewHolder(view, mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryRecyclerViewAdapter.ViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.order = order;

        String orderSummary = "";

        for (Item orderItem: order.getCartItems()) {
            orderSummary = orderSummary + "(" + orderItem.getQuantity() + ")" + " " + orderItem.getName() + "\n";
        }

        holder.textViewOrderItems.setText(orderSummary);
        holder.textViewOrderTotal.setText("$" + order.getOrderTotal());
    }

    @Override
    public int getItemCount() {
        return this.orders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewOrderItems;
        TextView textViewOrderTotal;

        View rootView;
        int position;
        Order order;
        OrderHistoryRecyclerViewAdapter.IOrderRecycler mListener;

        public ViewHolder(@NonNull View itemView, OrderHistoryRecyclerViewAdapter.IOrderRecycler mListener) {
            super(itemView);
            rootView = itemView;
            this.mListener = mListener;

            textViewOrderItems= itemView.findViewById(R.id.textViewOrderItems);
            textViewOrderTotal = itemView.findViewById(R.id.textViewOrderTotal);
        }
    }

    interface IOrderRecycler {
    }
}

