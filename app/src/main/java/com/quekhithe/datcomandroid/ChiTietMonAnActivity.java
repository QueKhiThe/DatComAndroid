package com.quekhithe.datcomandroid;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quekhithe.datcomandroid.Database.Database;
import com.quekhithe.datcomandroid.Model.Food;
import com.quekhithe.datcomandroid.Model.Order;
import com.squareup.picasso.Picasso;

public class ChiTietMonAnActivity extends AppCompatActivity {

    TextView DetailName, DetailPrice, DetailDescription;
    ImageView DetailImage;
    FloatingActionButton fabAdd;
    ElegantNumberButton DetailButton;

    FirebaseDatabase database;
    DatabaseReference detail;

    FirebaseUser user;

    String FoodID = "";

    Food food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_mon_an);

        DetailName = findViewById(R.id.DetailName);
        DetailPrice = findViewById(R.id.DetailPrice);
        DetailDescription = findViewById(R.id.DetailDescription);
        DetailImage = findViewById(R.id.DetailImage);
        fabAdd = findViewById(R.id.fabAdd);
        DetailButton = findViewById(R.id.DetailButton);

        database = FirebaseDatabase.getInstance();
        detail = database.getReference("Food");

        user = FirebaseAuth.getInstance().getCurrentUser();
        String name = user.getEmail();

        if (name.equals("admin@gmail.com")) {
            fabAdd.setVisibility(View.GONE);
            DetailButton.setVisibility(View.GONE);
        }

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Database(getBaseContext()).addCart(new Order(FoodID, food.getName(), food.getPrice(), DetailButton.getNumber()));

                Toast.makeText(ChiTietMonAnActivity.this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            }
        });

        if (getIntent() != null) {
            FoodID = getIntent().getStringExtra("FoodId");
        }
        if (!FoodID.isEmpty() && FoodID!= null) {
            detailFood(FoodID);
        }
    }

    private void detailFood(String FoodID) {
        detail.child(FoodID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                food = dataSnapshot.getValue(Food.class);
                DetailName.setText(food.getName());
                Picasso.with(getBaseContext()).load(food.getImage()).into(DetailImage);
                DetailDescription.setText(food.getDescription());
                DetailPrice.setText(food.getPrice());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
