package com.libyasolutions.libyamarketplace.object;

import java.io.Serializable;

/**
 * Created by Administrator on 5/29/2017.
 */

public class Conversation implements Serializable {
    private String lastMessage;
    private String datePost;
    private String senderId;
    private String receiverId;
    private String status;
    private String imageSender;
    private String imageReceiver;
    private String receiverName;
    private String senderName;
    private String statusOnline;
    private String shopId;
    private String shopName;
    private String buyerId;
    private String countShop;

    public String getCountShop() {
        return countShop;
    }

    public void setCountShop(String countShop) {
        this.countShop = countShop;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    //Count Message
    private String unread;
    //====

    public String getStatusOnline() {
        return statusOnline;
    }

    public void setStatusOnline(String statusOnline) {
        this.statusOnline = statusOnline;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getImageSender() {
        return imageSender;
    }

    public void setImageSender(String imageSender) {
        this.imageSender = imageSender;
    }

    public String getImageReceiver() {
        return imageReceiver;
    }

    public void setImageReceiver(String imageReceiver) {
        this.imageReceiver = imageReceiver;
    }

    public Conversation() {
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUnread() {
        return unread;
    }

    public void setUnread(String unread) {
        this.unread = unread;
    }
}
