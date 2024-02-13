package com.bannet.skils.postingdetails.responce;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class postingDetailsModel {


        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("favouite")
        @Expose
        private String favouite;
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

        public String getFavouite() {
            return favouite;
        }

        public void setFavouite(String favouite) {
            this.favouite = favouite;
        }

        public PostDetails getPostDetails() {
            return postDetails;
        }

        public void setPostDetails(PostDetails postDetails) {
            this.postDetails = postDetails;
        }

    public class PostDetails {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("user_id")
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
        @SerializedName("country")
        @Expose
        private String country;
        @SerializedName("state")
        @Expose
        private String state;
        @SerializedName("city")
        @Expose
        private String city;
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
        @SerializedName("ratings")
        @Expose
        private String ratings;
        @SerializedName("address")
        @Expose
        private String address;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
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

        public String getRatings() {
            return ratings;
        }

        public void setRatings(String ratings) {
            this.ratings = ratings;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
