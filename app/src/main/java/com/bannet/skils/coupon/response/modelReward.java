package com.bannet.skils.coupon.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class modelReward {


    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("usedlist")
    @Expose
    private List<Used> usedlist;

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

    public List<Used> getUsedlist() {
        return usedlist;
    }

    public void setUsedlist(List<Used> usedlist) {
        this.usedlist = usedlist;
    }

    public class Used {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("radius")
        @Expose
        private String radius;
        @SerializedName("latitude")
        @Expose
        private String latitude;
        @SerializedName("longitude")
        @Expose
        private String longitude;
        @SerializedName("distance")
        @Expose
        private String distance;
        @SerializedName("data")
        @Expose
        private Data data;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRadius() {
            return radius;
        }

        public void setRadius(String radius) {
            this.radius = radius;
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

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

        public class Data {

            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("title")
            @Expose
            private String title;
            @SerializedName("type")
            @Expose
            private String type;
            @SerializedName("amount_flat")
            @Expose
            private String amountFlat;
            @SerializedName("amount_discount")
            @Expose
            private String amountDiscount;
            @SerializedName("qr_image")
            @Expose
            private String qrImage;
            @SerializedName("promo_code")
            @Expose
            private String promoCode;
            @SerializedName("off_details")
            @Expose
            private String offDetails;
            @SerializedName("count")
            @Expose
            private String count;
            @SerializedName("logo")
            @Expose
            private String logo;
            @SerializedName("start_date")
            @Expose
            private String startDate;
            @SerializedName("end_date")
            @Expose
            private String endDate;
            @SerializedName("validity")
            @Expose
            private String validity;
            @SerializedName("status")
            @Expose
            private String status;
            @SerializedName("created_at")
            @Expose
            private String createdAt;
            @SerializedName("users_count")
            @Expose
            private String usersCount;
            @SerializedName("link")
            @Expose
            private String link;

            private String ScratchStatus;

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

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getAmountFlat() {
                return amountFlat;
            }

            public void setAmountFlat(String amountFlat) {
                this.amountFlat = amountFlat;
            }

            public String getAmountDiscount() {
                return amountDiscount;
            }

            public void setAmountDiscount(String amountDiscount) {
                this.amountDiscount = amountDiscount;
            }

            public String getQrImage() {
                return qrImage;
            }

            public void setQrImage(String qrImage) {
                this.qrImage = qrImage;
            }

            public String getPromoCode() {
                return promoCode;
            }

            public void setPromoCode(String promoCode) {
                this.promoCode = promoCode;
            }

            public String getOffDetails() {
                return offDetails;
            }

            public void setOffDetails(String offDetails) {
                this.offDetails = offDetails;
            }

            public String getCount() {
                return count;
            }

            public void setCount(String count) {
                this.count = count;
            }

            public String getLogo() {
                return logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public String getStartDate() {
                return startDate;
            }

            public void setStartDate(String startDate) {
                this.startDate = startDate;
            }

            public String getEndDate() {
                return endDate;
            }

            public void setEndDate(String endDate) {
                this.endDate = endDate;
            }

            public String getValidity() {
                return validity;
            }

            public void setValidity(String validity) {
                this.validity = validity;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public String getUsersCount() {
                return usersCount;
            }

            public void setUsersCount(String usersCount) {
                this.usersCount = usersCount;
            }

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }

            public String getScratchStatus() {
                return ScratchStatus;
            }

            public void setScratchStatus(String scratchStatus) {
                ScratchStatus = scratchStatus;
            }
        }

    }
}



