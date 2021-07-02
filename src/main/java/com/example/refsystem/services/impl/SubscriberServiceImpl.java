package com.example.refsystem.services.impl;

import com.example.refsystem.dao.SubscriberRepository;
import com.example.refsystem.mappers.SubscriberMapper;
import com.example.refsystem.models.Response;
import com.example.refsystem.models.Subscriber;
import com.example.refsystem.models.dto.SubscriberDto;
import com.example.refsystem.services.InviteService;
import com.example.refsystem.services.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class SubscriberServiceImpl implements SubscriberService {

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Autowired
    private InviteService inviteService;

    @Override
    public Response blockSubscriber(SubscriberDto subscriberDto) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
        Subscriber subscriber = SubscriberMapper.INSTANCE.toSubscriber(subscriberDto);
        if (subscriberRepository.existsById(subscriber.getId())){
            subscriber = subscriberRepository.findById(subscriber.getId()).get();
            subscriber.setActive(false);
            Date date = null;
            try {
                date = sdf.parse(sdf.format(new Date()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            subscriber.setEditDate(date);
            subscriberRepository.save(subscriber);
            return Response.builder().status(202).message("Уважаемый абонент, вы закрылы доступ к приглашениям!").build();

        }else {
            Date date = null;
            try {
                date = sdf.parse(sdf.format(new Date()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            subscriber.setActive(false);
            subscriber.setAddDate(date);
            subscriber.setEditDate(date);
            subscriber = subscriberRepository.save(subscriber);
            return Response.builder().status(202).message("Уважаемый абонент, вы закрылы доступ к приглашениям!").build();

        }
    }

    @Override
    public SubscriberDto saveIfNotExists(SubscriberDto subscriberDto) {

        Subscriber subscriber = SubscriberMapper.INSTANCE.toSubscriber(subscriberDto);
        if (subscriberRepository.existsById(subscriber.getId())){
            subscriber = subscriberRepository.findById(subscriber.getId()).get();
        }else {
            Date date = null;
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
            try {
                date = sdf.parse(sdf.format(new Date()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            subscriber.setActive(true);
            subscriber.setAddDate(date);
            subscriber.setEditDate(date);
            subscriber = subscriberRepository.save(subscriber);
        }
        return SubscriberMapper.INSTANCE.toSubscriberDto(subscriber);
    }

    @Override
    public List<SubscriberDto> selectAllSubscribers() {
        List<Subscriber> subscribers = subscriberRepository.findAll();
        return SubscriberMapper.INSTANCE.toSubscriberDto(subscribers);
    }

    @Override
    public Response acceptInvite(SubscriberDto subscriberDto) {
        SubscriberDto subscriberDto1 = saveIfNotExists(subscriberDto);
        if (inviteService.acceptInviteAndChangeStatusOnAccept(subscriberDto1)){
            return Response.builder().status(203).message("Вы успешно приняли Invite!").build();
        }
        return Response.builder().status(505).message("У вас пока нет активных приглашений!").build();
    }


}
