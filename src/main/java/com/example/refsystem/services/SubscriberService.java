package com.example.refsystem.services;

import com.example.refsystem.models.Response;
import com.example.refsystem.models.dto.SubscriberDto;

import java.util.List;

public interface SubscriberService {

    Response blockSubscriber(SubscriberDto subscriberDto);

    SubscriberDto saveIfNotExists(SubscriberDto subscriberDto);

    List<SubscriberDto> selectAllSubscribers();

    Response acceptInvite(SubscriberDto subscriberDto);
}
