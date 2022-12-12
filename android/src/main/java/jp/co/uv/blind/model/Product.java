package jp.co.uv.blind.model;

import java.util.ArrayList;

public class Product {
    private String janCode;
    private String languageCode;
    private String categoryNameJP;
    private String categoryNameAnother;
    private String markerName;
    private String productName;
    private String productSiteUrl;
    private String informationProvider;
    private ArrayList<String> imgUrls;

    public Product() {
    }

    public String getJanCode() {
        return janCode;
    }

    public void setJanCode(String janCode) {
        this.janCode = janCode;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getCategoryNameJP() {
        return categoryNameJP;
    }

    public void setCategoryNameJP(String categoryNameJP) {
        this.categoryNameJP = categoryNameJP;
    }

    public String getCategoryNameAnother() {
        return categoryNameAnother;
    }

    public void setCategoryNameAnother(String categoryNameAnother) {
        this.categoryNameAnother = categoryNameAnother;
    }

    public String getMarkerName() {
        return markerName;
    }

    public void setMarkerName(String markerName) {
        this.markerName = markerName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSiteUrl() {
        return productSiteUrl;
    }

    public void setProductSiteUrl(String productSiteUrl) {
        this.productSiteUrl = productSiteUrl;
    }

    public String getInformationProvider() {
        return informationProvider;
    }

    public void setInformationProvider(String informationProvider) {
        this.informationProvider = informationProvider;
    }

    public ArrayList<String> getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(ArrayList<String> imgUrls) {
        this.imgUrls = imgUrls;
    }
}
