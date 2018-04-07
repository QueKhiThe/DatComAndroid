package com.quekhithe.datcomandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.quekhithe.datcomandroid.Model.Request;
import com.quekhithe.datcomandroid.ViewHolder.OrderViewHolder;

public class OrderActivity extends AppCompatActivity {

    FirebaseDatabase database;
    FirebaseUser user;
    DatabaseReference order;

    public RecyclerView list_order;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        database = FirebaseDatabase.getInstance();
        order = database.getReference("Order");

        list_order = findViewById(R.id.list_order);
        list_order.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        list_order.setLayoutManager(layoutManager);

        user = FirebaseAuth.getInstance().getCurrentUser();

        loadOrder(user.getEmail());
    }

    private void loadOrder(String name) {
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(Request.class, R.layout.order_item, OrderViewHolder.class, order.orderByChild("name").equalTo(name)) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, Request model, int position) {
                viewHolder.txtOrderID.setText(adapter.getRef(position).getKey());
                viewHolder.txtOrderPhone.setText(model.getPhone());
                viewHolder.txtOrderAddress.setText(model.getAddress());
                viewHolder.txtOrderTotal.setText(model.getTotal());
                viewHolder.txtOrderStatus.setText(toStatus(model.getStatus()));
            }
        };
        list_order.setAdapter(adapter);
    }

    private String toStatus(String status) {
        if (status.equals("0")) {
            return "Đã đặt";
        }
        else if (status.equals("1")) {
            return "Đang ship";
        }
        else
            return "Đã thanh toán";

    }
}
