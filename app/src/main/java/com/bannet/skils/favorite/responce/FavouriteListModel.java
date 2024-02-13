package com.bannet.skils.favorite.responce;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FavouriteListModel {

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

        @SerializedName("prof_id")
        @Expose
        private String profId;
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
        @SerializedName("country_name")
        @Expose
        private String countryName;
        @SerializedName("state_name")
        @Expose
        private String stateName;
        @SerializedName("city_name")
        @Expose
        private String cityName;
        @SerializedName("about")
        @Expose
        private String about;
        @SerializedName("ratings")
        @Expose
        private String ratings;
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

        public String getProfId() {
            return profId;
        }

        public void setProfId(String profId) {
            this.profId = profId;
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

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public String getStateName() {
            return stateName;
        }

        public void setStateName(String stateName) {
            this.stateName = stateName;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getAbout() {
            return about;
        }

        public void setAbout(String about) {
            this.about = about;
        }

        public String getRatings() {
            return ratings;
        }

        public void setRatings(String ratings) {
            this.ratings = ratings;
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

    }
}
