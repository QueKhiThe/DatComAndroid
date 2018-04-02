package com.quekhithe.datcomandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.quekhithe.datcomandroid.Database.Database;
import com.quekhithe.datcomandroid.Model.Order;
import com.quekhithe.datcomandroid.ViewHolder.CartAdapter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GioHangActivity extends AppCompatActivity {

    TextView txtTotal;
    Button btnDatHang;

    RecyclerView recyclerGioHang;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference order;

    List<Order> cart = new ArrayList<>();
    CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);

        txtTotal = findViewById(R.id.txtTotal);
        btnDatHang = findViewById(R.id.btnDatHang);

        database = FirebaseDatabase.getInstance();
        order = database.getReference("Order");

        recyclerGioHang = findViewById(R.id.recyclerGioHang);
        recyclerGioHang.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerGioHang.setLayoutManager(layoutManager);

        //Load danh sách Món ăn lên giỏ hàng
        cart = new Database(this).getCart();
        cartAdapter = new CartAdapter(cart, this);
        recyclerGioHang.setAdapter(cartAdapter);


        //Tính tiền
        int total =0;
        for (Order order: cart) {
            total+=(Integer.parseInt(order.getProductPrice()))*(Integer.parseInt(order.getProductQuantity()));
        }
        Locale locale = new Locale("en", "US");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        txtTotal.setText(numberFormat.format(total));

        btnDatHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Database(getBaseContext()).emptyCart();
                Toast.makeText(GioHangActivity.this, "Cảm ơn bạn đã đặt hàng", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}
