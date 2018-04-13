package com.quekhithe.datcomandroid.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import com.quekhithe.datcomandroid.Interface.ItemClickListener;
import com.quekhithe.datcomandroid.R;

/**
 * Created by QueKhiThe on 4/7/2018.
 */

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

    public TextView txtOrderID, txtOrderTotal, txtOrderPhone, txtOrderAddress, txtOrderStatus;

    private ItemClickListener itemClickListener;

    public OrderViewHolder(View itemView) {
        super(itemView);

        txtOrderID = itemView.findViewById(R.id.txtOrderID);
        txtOrderTotal = itemView.findViewById(R.id.txtOrderTotal);
        txtOrderPhone = itemView.findViewById(R.id.txtOrderPhone);
        txtOrderAddress = itemView.findViewById(R.id.txtOrderAddress);
        txtOrderStatus = itemView.findViewById(R.id.txtOrderStatus);

        itemView.setOnClickListener(this);

        itemView.setOnCreateContextMenuListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.add(0,0, getAdapterPosition(), "Update");
        contextMenu.add(0,1, getAdapterPosition(), "Delete");
    }
}
