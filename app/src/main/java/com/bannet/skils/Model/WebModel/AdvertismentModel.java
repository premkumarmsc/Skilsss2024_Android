package com.bannet.skils.Model.WebModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AdvertismentModel {

        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("ads_list")
        @Expose
        private List<Ads> adsList = null;

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

        public List<Ads> getAdsList() {
            return adsList;
        }

        public void setAdsList(List<Ads> adsList) {
            this.adsList = adsList;
        }

        public class Ads {

            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("ad_id")
            @Expose
            private String adId;
            @SerializedName("ads_id")
            @Expose
            private Object adsId;
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

            public String getAdId() {
                return adId;
            }

            public void setAdId(String adId) {
                this.adId = adId;
            }

            public Object getAdsId() {
                return adsId;
            }

            public void setAdsId(Object adsId) {
                this.adsId = adsId;
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
                @SerializedName("name")
                @Expose
                private String name;
                @SerializedName("ads_image")
                @Expose
                private String adsImage;
                @SerializedName("image_url")
                @Expose
                private String imageUrl;
                @SerializedName("external_link")
                @Expose
                private String externalLink;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getAdsImage() {
                    return adsImage;
                }

                public void setAdsImage(String adsImage) {
                    this.adsImage = adsImage;
                }

                public String getImageUrl() {
                    return imageUrl;
                }

                public void setImageUrl(String imageUrl) {
                    this.imageUrl = imageUrl;
                }

                public String getExternalLink() {
                    return externalLink;
                }

                public void setExternalLink(String externalLink) {
                    this.externalLink = externalLink;
                }

            }
        }


}
