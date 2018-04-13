package com.quekhithe.datcomandroid.ViewHolder;

import android.content.DialogInterface;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.quekhithe.datcomandroid.Interface.ItemClickListener;
import com.quekhithe.datcomandroid.R;

/**
 * Created by QueKhiThe on 3/23/2018.
 */

public class FoodHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

    FirebaseUser user;


    public TextView food_name;
    public ImageView food_image;

    private ItemClickListener itemClickListener;

    public FoodHolder(View itemView) {
        super(itemView);

        user = FirebaseAuth.getInstance().getCurrentUser();
        String name = user.getEmail();

        food_name = itemView.findViewById(R.id.food_name);
        food_image = itemView.findViewById(R.id.food_image);

        itemView.setOnClickListener(this);

        //Chỉ hiển thị ContextMenu với admin
        if (name.equals("admin@gmail.com")) {
            itemView.setOnCreateContextMenuListener(this);
        }

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
