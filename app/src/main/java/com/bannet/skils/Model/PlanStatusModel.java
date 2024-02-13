package com.bannet.skils.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlanStatusModel {


        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("chat_list")
        @Expose
        private List<Chat> chatList = null;

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

        public List<Chat> getChatList() {
            return chatList;
        }

        public void setChatList(List<Chat> chatList) {
            this.chatList = chatList;
        }


    public class Chat {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("expired")
        @Expose
        private String expired;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("plan_id")
        @Expose
        private String planId;
        @SerializedName("city_id")
        @Expose
        private String cityId;
        @SerializedName("state_id")
        @Expose
        private String stateId;
        @SerializedName("country_id")
        @Expose
        private String countryId;
        @SerializedName("city_name")
        @Expose
        private String cityName;
        @SerializedName("state_name")
        @Expose
        private String stateName;
        @SerializedName("country_name")
        @Expose
        private String countryName;
        @SerializedName("transaction_id")
        @Expose
        private String transactionId;
        @SerializedName("device")
        @Expose
        private String device;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getExpired() {
            return expired;
        }

        public void setExpired(String expired) {
            this.expired = expired;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getPlanId() {
            return planId;
        }

        public void setPlanId(String planId) {
            this.planId = planId;
        }

        public String getCityId() {
            return cityId;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }

        public String getStateId() {
            return stateId;
        }

        public void setStateId(String stateId) {
            this.stateId = stateId;
        }

        public String getCountryId() {
            return countryId;
        }

        public void setCountryId(String countryId) {
            this.countryId = countryId;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getStateName() {
            return stateName;
        }

        public void setStateName(String stateName) {
            this.stateName = stateName;
        }

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        public String getDevice() {
            return device;
        }

        public void setDevice(String device) {
            this.device = device;
        }

    }
}
