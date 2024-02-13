package com.bannet.skils.addskilss.responce;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SkillList {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    @SerializedName("total_count")
    @Expose
    private int total_count;
    @SerializedName("skills_list")
    @Expose
    private List<Skills> skillsList = null;

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

    public List<Skills> getSkillsList() {
        return skillsList;
    }

    public void setSkillsList(List<Skills> skillsList) {
        this.skillsList = skillsList;
    }

    public static class Skills {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("skill_name")
        @Expose
        private String skillName;
        @SerializedName("about")
        @Expose
        private String about;
        @SerializedName("skills_image")
        @Expose
        private String skillsImage;
        @SerializedName("skills_image_url")
        @Expose
        private String skillsImageUrl;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSkillName() {
            return skillName;
        }

        public void setSkillName(String skillName) {
            this.skillName = skillName;
        }

        public String getAbout() {
            return about;
        }

        public void setAbout(String about) {
            this.about = about;
        }

        public String getSkillsImage() {
            return skillsImage;
        }

        public void setSkillsImage(String skillsImage) {
            this.skillsImage = skillsImage;
        }

        public String getSkillsImageUrl() {
            return skillsImageUrl;
        }

        public void setSkillsImageUrl(String skillsImageUrl) {
            this.skillsImageUrl = skillsImageUrl;
        }

    }
}
