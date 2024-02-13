package com.bannet.skils.professinoldetails.responce;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class professionalDetailsModel {

        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("favouite")
        @Expose
        private String favouite;
        @SerializedName("israted")
        @Expose
        private String israted;
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

        public String getIsrated() {
            return israted;
        }

        public void setIsrated(String israted) {
            this.israted = israted;
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
        @SerializedName("first_name")
        @Expose
        private String firstName;
        @SerializedName("last_name")
        @Expose
        private String lastName;
        @SerializedName("image_name")
        @Expose
        private String imageName;
        @SerializedName("image_url")
        @Expose
        private String imageUrl;
        @SerializedName("image_approve")
        @Expose
        private String imageApprove;
        @SerializedName("banner_image")
        @Expose
        private String bannerImage;
        @SerializedName("company_name")
        @Expose
        private String companyName;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("latitude")
        @Expose
        private String latitude;
        @SerializedName("longitude")
        @Expose
        private String longitude;
        @SerializedName("country")
        @Expose
        private String country;
        @SerializedName("state")
        @Expose
        private String state;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("about")
        @Expose
        private String about;
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("certification")
        @Expose
        private String certification;
        @SerializedName("skill_id")
        @Expose
        private String skillId;
        @SerializedName("available_from")
        @Expose
        private String availableFrom;
        @SerializedName("available_to")
        @Expose
        private String availableTo;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("skills_name")
        @Expose
        private String skillsName;
        @SerializedName("ratings")
        @Expose
        private String ratings;
        @SerializedName("phone_no")
        @Expose
        private String phone_no;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getImageName() {
            return imageName;
        }

        public void setImageName(String imageName) {
            this.imageName = imageName;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getImageApprove() {
            return imageApprove;
        }

        public void setImageApprove(String imageApprove) {
            this.imageApprove = imageApprove;
        }

        public String getBannerImage() {
            return bannerImage;
        }

        public void setBannerImage(String bannerImage) {
            this.bannerImage = bannerImage;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
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

        public String getAbout() {
            return about;
        }

        public void setAbout(String about) {
            this.about = about;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getCertification() {
            return certification;
        }

        public void setCertification(String certification) {
            this.certification = certification;
        }

        public String getSkillId() {
            return skillId;
        }

        public void setSkillId(String skillId) {
            this.skillId = skillId;
        }

        public String getAvailableFrom() {
            return availableFrom;
        }

        public void setAvailableFrom(String availableFrom) {
            this.availableFrom = availableFrom;
        }

        public String getAvailableTo() {
            return availableTo;
        }

        public void setAvailableTo(String availableTo) {
            this.availableTo = availableTo;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSkillsName() {
            return skillsName;
        }

        public void setSkillsName(String skillsName) {
            this.skillsName = skillsName;
        }

        public String getRatings() {
            return ratings;
        }

        public void setRatings(String ratings) {
            this.ratings = ratings;
        }

        public String getPhone_no() {
            return phone_no;
        }

        public void setPhone_no(String phone_no) {
            this.phone_no = phone_no;
        }
    }
}
