package com.example.agarw.mondinermanager_android;

/**
 * Created by agarw on 1/20/2018.
 */

public class offersget {

    String title;
    String description;
    String imageURL;

    public offersget(){

    }

    public offersget(String title, String description, String imageURL) {
        this.title = title;
        this.description = description;
        this.imageURL = imageURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }


}
