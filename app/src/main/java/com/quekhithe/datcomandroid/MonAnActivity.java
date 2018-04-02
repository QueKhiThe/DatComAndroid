package com.quekhithe.datcomandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.quekhithe.datcomandroid.Interface.ItemClickListener;
import com.quekhithe.datcomandroid.Model.Food;
import com.quekhithe.datcomandroid.ViewHolder.FoodHolder;
import com.squareup.picasso.Picasso;

public class MonAnActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference food;

    RecyclerView recyclerMonAn;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Food, FoodHolder> adapter;

    String categoryID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_an);

        database = FirebaseDatabase.getInstance();
        food = database.getReference("Food");

        recyclerMonAn = findViewById(R.id.recyclerMonAn);
        recyclerMonAn.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerMonAn.setLayoutManager(layoutManager);

        if (getIntent() != null) {
            categoryID = getIntent().getStringExtra("CategoryId");
        }
        if (!categoryID.isEmpty() && categoryID != null) {
            //Load Food
            loadFood(categoryID);

        }



    }

    private void loadFood(String CategoryID) {
        adapter = new FirebaseRecyclerAdapter<Food, FoodHolder>(Food.class, R.layout.food_item, FoodHolder.class, food.orderByChild("CategoryID").equalTo(CategoryID)) {
            @Override
            protected void populateViewHolder(FoodHolder viewHolder, final Food model, int position) {
                viewHolder.food_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.food_image);
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent = new Intent(MonAnActivity.this, ChiTietMonAnActivity.class);
                        intent.putExtra("FoodId", adapter.getRef(position).getKey() );
                        startActivity(intent);
                    }
                });
            }
        };
        recyclerMonAn.setAdapter(adapter);
    }
}
