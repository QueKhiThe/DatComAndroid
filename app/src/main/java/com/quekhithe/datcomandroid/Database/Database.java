package com.quekhithe.datcomandroid.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.quekhithe.datcomandroid.Model.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by QueKhiThe on 3/29/2018.
 */

public class Database extends SQLiteAssetHelper {

    private static final String DB_NAME = "DatCom.db";
    private static final int DB_VERSION = 1;

    public Database(Context context) {
        super(context, DB_NAME,null, DB_VERSION);
    }

    //Constructer



    //Hàm Giỏ hàng
    public List<Order> getCart() {
        SQLiteDatabase database = getReadableDatabase();
        SQLiteQueryBuilder queryBuilder  = new SQLiteQueryBuilder();

        String[] sqlSelcet = {"ProductID", "ProductName", "ProductPrice", "ProductQuantity"};
        String sqlTable = "OrderDetail";

        queryBuilder.setTables(sqlTable);
        Cursor cursor = queryBuilder.query(database, sqlSelcet, null, null, null, null, null);

        final List<Order> result = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                result.add(new Order(cursor.getString(cursor.getColumnIndex("ProductID")),
                        cursor.getString(cursor.getColumnIndex("ProductName")),
                        cursor.getString(cursor.getColumnIndex("ProductPrice")),
                        cursor.getString(cursor.getColumnIndex("ProductQuantity"))
                        ));
            } while (cursor.moveToNext());
        }
        return result;
    }


    //Hàm thêm vào giỏ hàng
    public void addCart(Order order) {
        SQLiteDatabase database = getReadableDatabase();
        String query = String.format("INSERT INTO OrderDetail(ProductID,ProductName,ProductPrice, ProductQuantity) VALUES('%s','%s','%s','%s');",
                order.getProductID(), order.getProductName(), order.getProductPrice(), order.getProductQuantity());
        database.execSQL(query);
    }

    //Hàm làm trỗng giỏ hàng
    public void emptyCart() {
        SQLiteDatabase database =getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail");
        database.execSQL(query);
    }




}
