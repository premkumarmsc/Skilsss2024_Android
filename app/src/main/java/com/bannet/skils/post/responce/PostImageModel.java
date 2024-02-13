package com.bannet.skils.post.responce;

public class PostImageModel {
 String imagePath;
 String imagename;

    public PostImageModel(String imagePath, String imagename) {
        this.imagePath = imagePath;
        this.imagename = imagename;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagename() {
        return imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }
}
