package com.example.refsystem.models.dto;

import lombok.Data;
import java.util.Date;

@Data
public class SubscriberDto {

    private Long id;
    private String phone;
    private boolean active;
    private Date editDate;
    private Date addDate;

}
