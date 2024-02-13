package com.bannet.skils.profile.responce;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageUpload {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("file_url")
    @Expose
    private String fileUrl;
    @SerializedName("file_name")
    @Expose
    private String fileName;
    @SerializedName("file_size")
    @Expose
    private Integer fileSize;
    @SerializedName("file_formate")
    @Expose
    private String fileFormate;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileFormate() {
        return fileFormate;
    }

    public void setFileFormate(String fileFormate) {
        this.fileFormate = fileFormate;
    }
}
