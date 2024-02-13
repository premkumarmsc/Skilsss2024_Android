package com.bannet.skils.subcategoryhome.responce;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubcategoryResponse {

        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("total_count")
        @Expose
        private Integer totalCount;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("skill_list")
        @Expose
        private List<Skill> skillList;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Integer getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(Integer totalCount) {
            this.totalCount = totalCount;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<Skill> getSkillList() {
            return skillList;
        }

        public void setSkillList(List<Skill> skillList) {
            this.skillList = skillList;
        }


    public static class Skill {

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

        private boolean selected = false;

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


        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }

}
