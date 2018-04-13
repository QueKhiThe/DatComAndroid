package com.quekhithe.datcomandroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.quekhithe.datcomandroid.Interface.ItemClickListener;
import com.quekhithe.datcomandroid.Model.Category;
import com.quekhithe.datcomandroid.Model.Food;
import com.quekhithe.datcomandroid.ViewHolder.FoodHolder;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.UUID;

public class MonAnActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference food;

    FirebaseUser user;

    Uri uri;

    FirebaseStorage storage;
    StorageReference storageReference;


    RecyclerView recyclerMonAn;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Food, FoodHolder> adapter;

    String menuId = "";

    EditText txtAddFoodName, txtAddFoodDescription, txtAddFoodPrice;
    Button btnFoodSelect, btnFoodUpload;

    Food newFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_an);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        user = FirebaseAuth.getInstance().getCurrentUser();
        String name = user.getEmail();

        FloatingActionButton fab3 = findViewById(R.id.fab3);
        if (!name.equals("admin@gmail.com")) {
            fab3.setVisibility(View.GONE);
        }

        database = FirebaseDatabase.getInstance();
        food = database.getReference("Food");

        recyclerMonAn = findViewById(R.id.recyclerMonAn);
        recyclerMonAn.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerMonAn.setLayoutManager(layoutManager);

        if (getIntent() != null) {
            menuId = getIntent().getStringExtra("MenuId");
        }
        if (!menuId.isEmpty() && menuId != null) {
            //Load Food
            loadFood(menuId);

        }

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFood();
            }
        });



    }



    private void loadFood(String menuId) {
        adapter = new FirebaseRecyclerAdapter<Food, FoodHolder>(Food.class, R.layout.food_item, FoodHolder.class, food.orderByChild("categoryID").equalTo(menuId)) {
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

    private void addFood() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Thêm món ăn");
        dialog.setMessage("Vui lòng điền đầy đủ thông tin");

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View add_food = layoutInflater.inflate(R.layout.add_food, null);
        txtAddFoodName = add_food.findViewById(R.id.txtAddFoodName);
        txtAddFoodDescription = add_food.findViewById(R.id.txtAddFoodDescription);
        txtAddFoodPrice = add_food.findViewById(R.id.txtAddFoodPrice);
        btnFoodSelect = add_food.findViewById(R.id.btnFoodSelect);
        btnFoodUpload = add_food.findViewById(R.id.btnFoodUpload);

        btnFoodSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        btnFoodUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

        dialog.setView(add_food);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (newFood != null) {
                    food.push().setValue(newFood);
                }
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        dialog.show();
    }



    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), 112);
    }

    private void uploadImage() {
        if (uri != null) {

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/"+imageName);
            imageFolder.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Toast.makeText(MonAnActivity.this, "Đã tải lên", Toast.LENGTH_SHORT).show();
                            newFood = new Food(txtAddFoodName.getText().toString(),
                                    uri.toString(),
                                    txtAddFoodDescription.getText().toString(),
                                    txtAddFoodPrice.getText().toString(),
                                    menuId);

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MonAnActivity.this, e+ "", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 112 && resultCode == RESULT_OK ) {
            uri = data.getData();
            Toast.makeText(MonAnActivity.this, "Đã chọn hình", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals("Update")) {
            update(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));
        }
        else if (item.getTitle().equals("Delete")) {
            food.child(adapter.getRef(item.getOrder()).getKey()).removeValue();
        }
        return super.onContextItemSelected(item);
    }

    private void update(final String key, final Food item) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Sửa món ăn");
        dialog.setMessage("Vui lòng điền đầy đủ thông tin");

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View add_food = layoutInflater.inflate(R.layout.add_food, null);
        txtAddFoodName = add_food.findViewById(R.id.txtAddFoodName);
        txtAddFoodDescription = add_food.findViewById(R.id.txtAddFoodDescription);
        txtAddFoodPrice = add_food.findViewById(R.id.txtAddFoodPrice);
        btnFoodSelect = add_food.findViewById(R.id.btnFoodSelect);
        btnFoodUpload = add_food.findViewById(R.id.btnFoodUpload);

        txtAddFoodName.setText(item.getName());
        txtAddFoodDescription.setText(item.getDescription());
        txtAddFoodPrice.setText(item.getPrice());

        btnFoodSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        btnFoodUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeImage(item);
            }
        });

        dialog.setView(add_food);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                item.setName(txtAddFoodName.getText().toString());
                item.setDescription(txtAddFoodDescription.getText().toString());
                item.setPrice(txtAddFoodPrice.getText().toString());
                food.child(key).setValue(item);
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        dialog.show();
    }

    private void changeImage(final Food item) {
        if (uri != null) {

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/"+imageName);
            imageFolder.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Toast.makeText(MonAnActivity.this, "Đã tải lên", Toast.LENGTH_SHORT).show();
                            item.setImage(uri.toString());
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MonAnActivity.this, e+ "", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
