package com.quekhithe.datcomandroid.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.quekhithe.datcomandroid.Interface.ItemClickListener;
import com.quekhithe.datcomandroid.R;

/**
 * Created by QueKhiThe on 3/22/2018.
 */

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

    public TextView menuName;
    public ImageView menuImage;

    FirebaseUser user;



    private ItemClickListener itemClickListener;

    public MenuViewHolder(View itemView) {
        super(itemView);

        user = FirebaseAuth.getInstance().getCurrentUser();
        String name = user.getEmail();



        menuImage = itemView.findViewById(R.id.menu_image);
        menuName = itemView.findViewById(R.id.menu_name);

        //Hiển thị ContextMenu nếu là admin
        itemView.setOnClickListener(this);
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
