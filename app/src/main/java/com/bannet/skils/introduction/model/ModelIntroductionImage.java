package com.bannet.skils.introduction.model;

public class ModelIntroductionImage {
    Integer image;
    String title;
    String content;
    String btnTitle;

    public ModelIntroductionImage(Integer image, String title, String content, String btnTitle) {
        this.image = image;
        this.title = title;
        this.content = content;
        this.btnTitle = btnTitle;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBtnTitle() {
        return btnTitle;
    }

    public void setBtnTitle(String btnTitle) {
        this.btnTitle = btnTitle;
    }
}

