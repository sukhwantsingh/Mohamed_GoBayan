package com.libyasolutions.libyamarketplace.object;


/**
 * Created by Administrator on 5/29/2017.
 */


public class MessengerFirebase {
    private String datePost;
    private String senderId;
    private String receiverId;
    private String message;
    private String urlFile;
    private String imageH;
    private String imageW;
    private boolean isSending;
    private String buyerId;
    private String shopId;

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public boolean isSending() {
        return isSending;
    }

    public void setSending(boolean sending) {
        isSending = sending;
    }

    public MessengerFirebase() {

    }

    public String getDatePost() {
        return datePost;
    }

    public void setDatePost(String datePost) {
        this.datePost = datePost;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrlFile() {
        return urlFile;
    }

    public void setUrlFile(String urlFile) {
        this.urlFile = urlFile;
    }

    public String getImageH() {
        return imageH;
    }

    public void setImageH(String imageH) {
        this.imageH = imageH;
    }

    public String getImageW() {
        return imageW;
    }

    public void setImageW(String imageW) {
        this.imageW = imageW;
    }

}
