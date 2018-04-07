package com.quekhithe.datcomandroid.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.quekhithe.datcomandroid.Interface.ItemClickListener;
import com.quekhithe.datcomandroid.Model.Order;
import com.quekhithe.datcomandroid.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtCartName, txtCartTotal;
    public ImageView imgCount;

    private ItemClickListener itemClickListener;

    public void setTxtCartName(TextView txtOrderName) {
        this.txtCartName = txtOrderName;
    }

    public void setTxtCartTotal(TextView txtOrderTotal) {
        this.txtCartTotal = txtOrderTotal;
    }

    public void setImgCount(ImageView imgCount) {
        this.imgCount = imgCount;
    }

    public CartViewHolder(View itemView) {
        super(itemView);

        txtCartName = itemView.findViewById(R.id.txtCartName);
        txtCartTotal = itemView.findViewById(R.id.txtCartTotal);
        imgCount = itemView.findViewById(R.id.imgCount);
    }

    @Override
    public void onClick(View view) {

    }
}

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    private List<Order> listData = new ArrayList<>();
    private Context context;

    public CartAdapter(List<Order> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        TextDrawable drawable = TextDrawable.builder().buildRound(""+listData.get(position).getProductQuantity(), Color.RED);
        holder.imgCount.setImageDrawable(drawable);
        holder.txtCartName.setText(listData.get(position).getProductName());

        Locale locale = new Locale("en", "US");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        int total = Integer.parseInt(listData.get(position).getProductPrice()) * Integer.parseInt(listData.get(position).getProductQuantity());
        holder.txtCartTotal.setText(numberFormat.format(total));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
