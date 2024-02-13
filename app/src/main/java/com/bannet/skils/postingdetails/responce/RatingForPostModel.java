package com.bannet.skils.postingdetails.responce;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RatingForPostModel {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("post_details")
    @Expose
    private PostDetails postDetails;

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

    public PostDetails getPostDetails() {
        return postDetails;
    }

    public void setPostDetails(PostDetails postDetails) {
        this.postDetails = postDetails;
    }

    public class PostDetails {

        @SerializedName("ratings")
        @Expose
        private Integer ratings;

        public Integer getRatings() {
            return ratings;
        }

        public void setRatings(Integer ratings) {
            this.ratings = ratings;
        }

    }
}
