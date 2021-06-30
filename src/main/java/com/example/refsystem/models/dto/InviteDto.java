package com.example.refsystem.models.dto;

import com.example.refsystem.enums.InviteStatus;
import com.example.refsystem.models.Subscriber;
import lombok.Data;

import java.util.Date;

@Data
public class InviteDto {

    private Long id;
    private SubscriberDto sender;
    private SubscriberDto receiver;
    private Date startDate;
    private Date endDate; // 31.12.2999
    private InviteStatus inviteStatus; // active

}
