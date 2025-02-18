package com.example.demo.OOP;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Messages")
public class Messages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MessagesID")
    private Integer messagesID;

    @Column(name = "MessageSenderID", nullable = false)
    private String senderID;  // ID của người gửi (giáo viên hoặc học sinh)

    @Column(name = "MessageRecipientID", nullable = false)
    private String recipientID;  // ID của người nhận (giáo viên hoặc học sinh)

    @Column(name = "Datetime", nullable = false)
    private LocalDateTime datetime;

    @Column(name = "Text", nullable = false, columnDefinition = "TEXT")
    private String text;

    // Constructors
    public Messages() {
    }

    public Messages(String senderID, String recipientID, LocalDateTime datetime, String text) {
        this.senderID = senderID;
        this.recipientID = recipientID;
        this.datetime = datetime;
        this.text = text;
    }

    // Getters and Setters
    public Integer getMessagesID() {
        return messagesID;
    }

    public void setMessagesID(Integer messagesID) {
        this.messagesID = messagesID;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getRecipientID() {
        return recipientID;
    }

    public void setRecipientID(String recipientID) {
        this.recipientID = recipientID;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


}
