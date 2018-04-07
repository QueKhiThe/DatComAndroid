package com.quekhithe.datcomandroid;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.quekhithe.datcomandroid.Database.Database;
import com.quekhithe.datcomandroid.Model.Order;
import com.quekhithe.datcomandroid.Model.Request;
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
    FirebaseUser user;

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

        user = FirebaseAuth.getInstance().getCurrentUser();
        final String email = user.getEmail();

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


                AlertDialog.Builder alert =  new AlertDialog.Builder(GioHangActivity.this);
                alert.setTitle("SDT và địa chỉ");
                alert.setMessage("Vui lòng nhập địa chỉ và số điện thoại để chúng tôi giao hàng");

                LinearLayout layout = new LinearLayout(GioHangActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                final TextView name = new TextView(GioHangActivity.this);
                name.setText(email);
                layout.addView(name);

                final EditText address = new EditText(GioHangActivity.this);
                address.setHint("Địa chỉ");
                layout.addView(address);

                final EditText phone = new EditText(GioHangActivity.this);
                phone.setHint("Số điện thoại");
                layout.addView(phone);

                alert.setView(layout);

                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Request request = new Request(name.getText().toString(),
                                phone.getText().toString(),
                                address.getText().toString(),
                                txtTotal.getText().toString(),
                                "Đã đặt",
                                cart);
                        order.child(String.valueOf(System.currentTimeMillis())).setValue(request);
                        Toast.makeText(GioHangActivity.this, "Đơn hàng của bạn đã được đặt, cảm ơn", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

                alert.setNegativeButton("Xoá", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new Database(getBaseContext()).emptyCart();
                        dialogInterface.dismiss();
                        finish();
                    }
                });

                alert.show();
            }
        });

    }
}
