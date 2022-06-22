package com.macdev.whatsgrouplinks;

public class CategoriesModel {

    private String categoriesName;
    private int images;

    public CategoriesModel(String categoriesName, int images) {
        this.categoriesName = categoriesName;
        this.images = images;
    }

    public String getCategoriesName() {
        return categoriesName;
    }

    public int getImages() {
        return images;
    }
}
