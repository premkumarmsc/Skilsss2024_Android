package com.bannet.skils.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EditskillsModel {


        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("user_details")
        @Expose
        private UserDetails userDetails;

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

        public UserDetails getUserDetails() {
            return userDetails;
        }

        public void setUserDetails(UserDetails userDetails) {
            this.userDetails = userDetails;
        }

    public class UserDetails {

        @SerializedName("skill_id")
        @Expose
        private String skillId;
        @SerializedName("available_from")
        @Expose
        private String availableFrom;
        @SerializedName("available_to")
        @Expose
        private String availableTo;
        @SerializedName("skills_name")
        @Expose
        private String skillsName;
        @SerializedName("banner_image")
        @Expose
        private String bannerImage;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;

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

        public String getSkillsName() {
            return skillsName;
        }

        public void setSkillsName(String skillsName) {
            this.skillsName = skillsName;
        }

        public String getBannerImage() {
            return bannerImage;
        }

        public void setBannerImage(String bannerImage) {
            this.bannerImage = bannerImage;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

    }
}
