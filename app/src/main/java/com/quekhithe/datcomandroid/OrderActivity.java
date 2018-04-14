package com.quekhithe.datcomandroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.quekhithe.datcomandroid.Interface.ItemClickListener;
import com.quekhithe.datcomandroid.Model.Request;
import com.quekhithe.datcomandroid.ViewHolder.OrderViewHolder;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {

    FirebaseDatabase database;
    FirebaseUser user;
    DatabaseReference order;

    ArrayList<String> arr;
    ArrayAdapter<String> adapterSpinner;

    public RecyclerView list_order;
    public RecyclerView.LayoutManager layoutManager;

    Spinner spinner;

    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        arr = new ArrayList<>();
        arr.add("Đã đặt");
        arr.add("Đang ship");
        arr.add("Đã thanh toán");

        adapterSpinner = new ArrayAdapter<String>(OrderActivity.this, android.R.layout.simple_spinner_dropdown_item, arr );


        database = FirebaseDatabase.getInstance();
        order = database.getReference("Order");

        list_order = findViewById(R.id.list_order);
        list_order.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        list_order.setLayoutManager(layoutManager);

        user = FirebaseAuth.getInstance().getCurrentUser();
        String name = user.getEmail();

        if (name.equals("admin@gmail.com")) {
            loadOrder();
        } else {
            loadOrder(user.getEmail());
        }


    }

    //Hàm load order theo admin (admin sẽ hiển thị tất cả các order của tất cả các user)
    private void loadOrder() {
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(Request.class, R.layout.order_item, OrderViewHolder.class, order) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, Request model, int position) {
                viewHolder.txtOrderID.setText(adapter.getRef(position).getKey());
                viewHolder.txtOrderPhone.setText(model.getPhone());
                viewHolder.txtOrderAddress.setText(model.getAddress());
                viewHolder.txtOrderTotal.setText(model.getTotal());
                viewHolder.txtOrderStatus.setText(toStatus(model.getStatus()));

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });
            }
        };
        list_order.setAdapter(adapter);
    }


    //Hàm load order theo user bình thường
    private void loadOrder(String name) {
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(Request.class, R.layout.order_item, OrderViewHolder.class, order.orderByChild("name").equalTo(name)) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, Request model, int position) {
                viewHolder.txtOrderID.setText(adapter.getRef(position).getKey());
                viewHolder.txtOrderPhone.setText(model.getPhone());
                viewHolder.txtOrderAddress.setText(model.getAddress());
                viewHolder.txtOrderTotal.setText(model.getTotal());
                viewHolder.txtOrderStatus.setText(toStatus(model.getStatus()));

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });
            }
        };
        list_order.setAdapter(adapter);
    }

    //Hàm xử lý status
    private String toStatus(String status) {
        if (status.equals("Đã đặt")) {
            return "Đã đặt";
        }
        else if (status.equals("Đang ship")) {
            return "Đang ship";
        }
        else
            return "Đã thanh toán";

    }

    //Hàm xử lý context cho order (chỉ dành cho admin)
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals("Update")) {
            update(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));
        }
        else if (item.getTitle().equals("Delete")) {
            order.child(adapter.getRef(item.getOrder()).getKey()).removeValue();
        }

        return super.onContextItemSelected(item);
    }

    // Hàm update trạng thái (chỉ dành cho admin)
    private void update(final String key, final Request item) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Update trạng thái");
        dialog.setMessage("Chọn trạng thái");

        LayoutInflater inflater = this.getLayoutInflater();
        View change_status_order = inflater.inflate(R.layout.change_status_order, null);

        spinner = change_status_order.findViewById(R.id.spinner);
        spinner.setAdapter(adapterSpinner);

        dialog.setView(change_status_order);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                item.setStatus(String.valueOf(spinner.getSelectedItem()));
                order.child(key).setValue(item);
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }
}
