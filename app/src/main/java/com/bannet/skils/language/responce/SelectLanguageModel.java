package com.bannet.skils.language.responce;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SelectLanguageModel {


        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("language_list")
        @Expose
        private List<Language> languageList = null;

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

        public List<Language> getLanguageList() {
            return languageList;
        }

        public void setLanguageList(List<Language> languageList) {
            this.languageList = languageList;
        }


    public class Language {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("language")
        @Expose
        private String language;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

    }
}
