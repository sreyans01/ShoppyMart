package com.shopping.shoppymart.ModelClasses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductDetails implements Serializable {

    private String productName;
    private long productPrice;
    private String productSeason;
    private String productDistrictOfOrigin;
    private long productShelfLife;
    private String productDescription;
    private String productRegion;
    private String productDateOfManufacture;
    private String productBasicIngredients;
    private String productStorageTips;
    private String productBasicTaste;
    private String productCategory;//premium buy or everyday essential
    private String productClassification; //snacks, spices or pickles
    private long productImageCount;
    private List<String> productImageUrls;

    public ProductDetails() {
        this.productName = "";
        this.productPrice = -1;
        this.productSeason = "Summer";
        this.productDistrictOfOrigin = "";
        this.productShelfLife = -1;
        this.productDescription = "";
        this.productRegion = "Rajasthan";
        this.productDateOfManufacture = "";
        this.productBasicIngredients = "";
        this.productStorageTips = "";
        this.productBasicTaste = "Sweet";
        this.productCategory = "Everyday Essentials";
        this.productClassification = "Snacks";
        this.productImageCount=0;
        this.productImageUrls = new ArrayList<>();
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSeason() {
        return productSeason;
    }

    public void setProductSeason(String productSeason) {
        this.productSeason = productSeason;
    }

    public long getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(long productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductDistrictOfOrigin() {
        return productDistrictOfOrigin;
    }

    public void setProductDistrictOfOrigin(String productDistrictOfOrigin) {
        this.productDistrictOfOrigin = productDistrictOfOrigin;
    }

    public long getProductShelfLife() {
        return productShelfLife;
    }

    public void setProductShelfLife(long productShelfLife) {
        this.productShelfLife = productShelfLife;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductRegion() {
        return productRegion;
    }

    public void setProductRegion(String productRegion) {
        this.productRegion = productRegion;
    }

    public String getProductDateOfManufacture() {
        return productDateOfManufacture;
    }

    public void setProductDateOfManufacture(String productDateOfManufacture) {
        this.productDateOfManufacture = productDateOfManufacture;
    }

    public String getProductBasicIngredients() {
        return productBasicIngredients;
    }

    public void setProductBasicIngredients(String productBasicIngredients) {
        this.productBasicIngredients = productBasicIngredients;
    }

    public String getProductStorageTips() {
        return productStorageTips;
    }

    public void setProductStorageTips(String productStorageTips) {
        this.productStorageTips = productStorageTips;
    }

    public String getProductBasicTaste() {
        return productBasicTaste;
    }

    public void setProductBasicTaste(String productBasicTaste) {
        this.productBasicTaste = productBasicTaste;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductClassification() {
        return productClassification;
    }

    public void setProductClassification(String productClassification) {
        this.productClassification = productClassification;
    }

    public long getProductImageCount() {
        return productImageCount;
    }

    public void setProductImageCount(long productImageCount) {
        this.productImageCount = productImageCount;
    }

    public List<String> getProductImageUrls() {
        return productImageUrls;
    }

    public void setProductImageUrls(List<String> productImageUrls) {
        this.productImageUrls = productImageUrls;
    }
}
