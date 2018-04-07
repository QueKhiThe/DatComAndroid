package com.quekhithe.datcomandroid.Model;

/**
 * Created by QueKhiThe on 3/29/2018.
 */

public class Order {
    private String ProductID;
    private String ProductName;
    private String ProductPrice;
    private String ProductQuantity;

    public Order() {
    }

    public Order(String productName, String productPrice, String productQuantity) {
    }

    public Order( String productID, String productName, String productPrice, String productQuantity) {

        ProductID = productID;
        ProductName = productName;
        ProductPrice = productPrice;
        ProductQuantity = productQuantity;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(String productPrice) {
        ProductPrice = productPrice;
    }

    public String getProductQuantity() {
        return ProductQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        ProductQuantity = productQuantity;
    }
}
