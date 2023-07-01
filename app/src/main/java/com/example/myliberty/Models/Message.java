package com.example.myliberty.Models;

public class Message {
    String messageId;
    String messageSubject;
    String messageContent;
    String datePosted;

    public Message(String messageId, String messageSubject, String messageContent, String datePosted) {
        this.messageId = messageId;
        this.messageSubject = messageSubject;
        this.messageContent = messageContent;
        this.datePosted = datePosted;
    }
    public Message(){

    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageSubject() {
        return messageSubject;
    }

    public void setMessageSubject(String messageSubject) {
        this.messageSubject = messageSubject;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }
}
