package com.bannet.skils.chat.responce;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatListModel {

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
            @SerializedName("chat_id")
            @Expose
            private String chatId;
            @SerializedName("user_id")
            @Expose
            private String userId;
            @SerializedName("opp_user_id")
            @Expose
            private String oppUserId;
            @SerializedName("opp_user_name")
            @Expose
            private String oppUserName;
            @SerializedName("opp_user_image")
            @Expose
            private String oppUserImage;
            @SerializedName("last_chat")
            @Expose
            private String lastChat;
            @SerializedName("last_chat_type")
            @Expose
            private String lastChatType;
            @SerializedName("last_firebase_id")
            @Expose
            private String lastFirebaseId;
            @SerializedName("updated")
            @Expose
            private String updated;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getChatId() {
                return chatId;
            }

            public void setChatId(String chatId) {
                this.chatId = chatId;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getOppUserId() {
                return oppUserId;
            }

            public void setOppUserId(String oppUserId) {
                this.oppUserId = oppUserId;
            }

            public String getOppUserName() {
                return oppUserName;
            }

            public void setOppUserName(String oppUserName) {
                this.oppUserName = oppUserName;
            }

            public String getOppUserImage() {
                return oppUserImage;
            }

            public void setOppUserImage(String oppUserImage) {
                this.oppUserImage = oppUserImage;
            }

            public String getLastChat() {
                return lastChat;
            }

            public void setLastChat(String lastChat) {
                this.lastChat = lastChat;
            }

            public String getLastChatType() {
                return lastChatType;
            }

            public void setLastChatType(String lastChatType) {
                this.lastChatType = lastChatType;
            }

            public String getLastFirebaseId() {
                return lastFirebaseId;
            }

            public void setLastFirebaseId(String lastFirebaseId) {
                this.lastFirebaseId = lastFirebaseId;
            }

            public String getUpdated() {
                return updated;
            }

            public void setUpdated(String updated) {
                this.updated = updated;
            }



    }
}
