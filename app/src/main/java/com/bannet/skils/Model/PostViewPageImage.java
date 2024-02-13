package com.bannet.skils.Model;

public class PostViewPageImage {

    String  image;
    String imageUrl;



    public PostViewPageImage(String image, String imageUrl) {
        this.image = image;
        this.imageUrl = imageUrl;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
