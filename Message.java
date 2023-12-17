/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.facom39701.riakapp1.model;

import com.facom39701.riakapp1.util.DateUtil;

/**
 *
 * @author docker
 */
public class Message {
    
    private String sender;
    private String recipient;
    private String createdAt;
    private String text;
    
    public Message(){
    
    }
    
    public Message(final String sender, final String recipient, final String text){
        this.sender = sender;
        this.recipient = recipient;
        this.createdAt = DateUtil.getCurrentTimestampStr();
        this.text = text;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(final String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(final String recipient) {
        this.recipient = recipient;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final String createdAt) {
        this.createdAt = createdAt;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }
    
    
    
}
