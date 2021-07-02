package com.example.refsystem.controllers;

import com.example.refsystem.models.*;
import com.example.refsystem.models.dto.SubscriberDto;
import com.example.refsystem.services.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/subscriber")
public class SubscriberController {

    @Autowired
    private SubscriberService subscriberService;

    @PutMapping("/block")
    public Response blocSubscriber(@RequestBody SubscriberDto subscriberDto){
        return subscriberService.blockSubscriber(subscriberDto);
    }

    @PutMapping("/accept")
    public Response acceptInvite(@RequestBody SubscriberDto subscriberDto){
        return subscriberService.acceptInvite(subscriberDto);
    }

}
