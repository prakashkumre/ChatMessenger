package com.chatmessenger.model;

import java.io.Serializable;

/**
 * Created by prakashk on 7/7/2017.
 */

public class ChatMessageModel implements Serializable {

    private String messageText;
    private int messageStatus;

    public int getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(int messageStatus) {
        this.messageStatus = messageStatus;
    }

    private long messageTime;
    private int type_row;

    public int getType_row() {
        return type_row;
    }

    public void setType_row(int type_row) {
        this.type_row = type_row;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }


}
