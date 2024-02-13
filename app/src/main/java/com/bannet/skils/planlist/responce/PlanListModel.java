package com.bannet.skils.planlist.responce;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlanListModel {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("plan_list")
    @Expose
    private List<Plan> planList = null;

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

    public List<Plan> getPlanList() {
        return planList;
    }

    public void setPlanList(List<Plan> planList) {
        this.planList = planList;
    }
    public class Plan {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("plan_name")
        @Expose
        private String planName;
        @SerializedName("coverage")
        @Expose
        private String coverage;
        @SerializedName("cost")
        @Expose
        private String cost;
        @SerializedName("validity_months")
        @Expose
        private String validityMonths;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPlanName() {
            return planName;
        }

        public void setPlanName(String planName) {
            this.planName = planName;
        }

        public String getCoverage() {
            return coverage;
        }

        public void setCoverage(String coverage) {
            this.coverage = coverage;
        }

        public String getCost() {
            return cost;
        }

        public void setCost(String cost) {
            this.cost = cost;
        }

        public String getValidityMonths() {
            return validityMonths;
        }

        public void setValidityMonths(String validityMonths) {
            this.validityMonths = validityMonths;
        }

    }
}
