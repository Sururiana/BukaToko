package com.sururiana.bukatoko.data.model;


import com.google.gson.annotations.SerializedName;

import java.util.Comparator;
import java.util.List;

public class Product {

    @SerializedName("data")
    public List<Data> products;
    public List<Data> getProducts() {
        return products;
    }

    public static class Data {
        @SerializedName("id")
        private int id;
        @SerializedName("product")
        private String product;
        @SerializedName("price")
        private int price;
        @SerializedName("stock")
        private int stock;
        @SerializedName("description")
        private String description;
        @SerializedName("image")
        private String image;


        public static final Comparator<Product.Data> BY_STOCK = new Comparator<Data>() {
            @Override
            public int compare(Data data, Data t1) {
                return + Integer.valueOf(data.stock).compareTo(Integer.valueOf(t1.stock));
            }
        };

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getProduct() {
            return product;
        }

        public void setProduct(String product) {
            this.product = product;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getStock() {
            return stock;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

    }
}
