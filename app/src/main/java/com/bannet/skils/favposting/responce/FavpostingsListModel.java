package com.bannet.skils.favposting.responce;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FavpostingsListModel {


        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("posting_list")
        @Expose
        private List<Posting> postingList = null;

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

        public List<Posting> getPostingList() {
            return postingList;
        }

        public void setPostingList(List<Posting> postingList) {
            this.postingList = postingList;
        }

    public class Posting {

        @SerializedName("post_id")
        @Expose
        private String postId;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("opp_user")
        @Expose
        private String userId;
        @SerializedName("skills_id")
        @Expose
        private String skillsId;
        @SerializedName("country_id")
        @Expose
        private String countryId;
        @SerializedName("state_id")
        @Expose
        private String stateId;
        @SerializedName("city_id")
        @Expose
        private String cityId;
        @SerializedName("latitude")
        @Expose
        private String latitude;
        @SerializedName("longitude")
        @Expose
        private String longitude;
        @SerializedName("skills_name")
        @Expose
        private String skillsName;
        @SerializedName("post_image_url")
        @Expose
        private String postImageUrl;
        @SerializedName("post_images")
        @Expose
        private String postImages;
        @SerializedName("created_at")
        @Expose
        private String createdAt;

        public String getPostId() {
            return postId;
        }

        public void setPostId(String postId) {
            this.postId = postId;
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

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getSkillsId() {
            return skillsId;
        }

        public void setSkillsId(String skillsId) {
            this.skillsId = skillsId;
        }

        public String getCountryId() {
            return countryId;
        }

        public void setCountryId(String countryId) {
            this.countryId = countryId;
        }

        public String getStateId() {
            return stateId;
        }

        public void setStateId(String stateId) {
            this.stateId = stateId;
        }

        public String getCityId() {
            return cityId;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getSkillsName() {
            return skillsName;
        }

        public void setSkillsName(String skillsName) {
            this.skillsName = skillsName;
        }

        public String getPostImageUrl() {
            return postImageUrl;
        }

        public void setPostImageUrl(String postImageUrl) {
            this.postImageUrl = postImageUrl;
        }

        public String getPostImages() {
            return postImages;
        }

        public void setPostImages(String postImages) {
            this.postImages = postImages;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

    }
}
