package com.example.refsystem.models;

import com.example.refsystem.enums.InviteStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Invite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_subs_id")
    private Subscriber sender;
    @ManyToOne
    @JoinColumn(name = "receiver_subs_id")
    private Subscriber receiver;
    private Date startDate;
    private Date endDate; // 31.12.2999
    @Enumerated(EnumType.STRING)
    private InviteStatus inviteStatus; // active

    public Invite() { }

    public Invite(Subscriber sender, Subscriber receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    public InviteStatus getInviteStatus() {
        return inviteStatus;
    }

    public void setInviteStatus(InviteStatus inviteStatus) {
        this.inviteStatus = inviteStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Subscriber getSender() {
        return sender;
    }

    public void setSender(Subscriber sender) {
        this.sender = sender;
    }

    public Subscriber getReceiver() {
        return receiver;
    }

    public void setReceiver(Subscriber receiver) {
        this.receiver = receiver;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Invite{" +
                "id=" + id +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
