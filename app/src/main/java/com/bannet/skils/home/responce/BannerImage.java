package com.bannet.skils.home.responce;

public class BannerImage {

    String  image;
    String imageUrl;



    public BannerImage(String image, String imageUrl) {
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
