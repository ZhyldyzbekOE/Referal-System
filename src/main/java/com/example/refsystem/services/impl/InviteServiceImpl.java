package com.example.refsystem.services.impl;

import com.example.refsystem.dao.InviteRepository;
import com.example.refsystem.enums.InviteStatus;
import com.example.refsystem.mappers.InviteMapper;
import com.example.refsystem.models.Invite;
import com.example.refsystem.models.Response;
import com.example.refsystem.models.dto.InviteDto;
import com.example.refsystem.models.dto.SubscriberDto;
import com.example.refsystem.services.InviteService;
import com.example.refsystem.services.SubscriberService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InviteServiceImpl implements InviteService {

    @Autowired
    private InviteRepository inviteRepository;

    @Autowired
    private SubscriberService subscriberService;

    @Override
    public Response send(InviteDto inviteDto) throws ParseException {

        inviteDto.setSender(subscriberService.saveIfNotExists(inviteDto.getSender()));
        inviteDto.setReceiver(subscriberService.saveIfNotExists(inviteDto.getReceiver()));

        if (!checkStatusReceiver(inviteDto)){
            return Response.builder().status(503).message("Выбранный вами абонент заблокировал доступ к отправке приглошений!").build();
        }

        if (checkSendInviteOnDay(inviteDto)){
            return Response.builder().status(502).message("Уважаемый абонент вы отправляли invite выбранному абоненту! Повторите попытку через 24ч").build();
        }

        if (!checkActiveInvite(inviteDto)){
            changeStatusAndDateInvite(inviteDto);
        }
        if (!checkDateInvite(inviteDto)){
            return Response.builder().status(501).message("Уважаемый абонент вы превысили ежедневный лимит!").build();
        }

        if (!saveInvite(inviteDto)){
            return Response.builder().status(205).message("Возникли ошибки, повторите попытку отправки приглашения!").build();
        }

        return Response.builder().status(201).message("Invite успешно отправлен!").build();

    }
// 2021-06-30 16:08:33.857000
    private boolean saveInvite(InviteDto inviteDto){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
        Date date = null;
        try {
            date = sdf.parse(sdf.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        inviteDto.setStartDate(date);
        inviteDto.setInviteStatus(InviteStatus.ACTIVE);

        Calendar calEnd = new GregorianCalendar();
        Date date1 = null;
        try {
            date1 = sdf.parse(sdf.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert date1 != null;
        calEnd.setTime(date1);
        calEnd.set(Calendar.YEAR, calEnd.get(Calendar.YEAR)+978);
        calEnd.set(Calendar.HOUR_OF_DAY, 0);
        calEnd.set(Calendar.MINUTE, 0);
        calEnd.set(Calendar.SECOND, -1);
        Date midnightTonight = calEnd.getTime();
        inviteDto.setEndDate(midnightTonight);
        Invite invite = InviteMapper.INSTANCE.toInvite(inviteDto);
        Invite inviteToSave = inviteRepository.save(invite);
        return inviteToSave.getId() != null;
    }

    private boolean checkStatusReceiver(InviteDto inviteDto){

        List<SubscriberDto>subscriberDtos = subscriberService.selectAllSubscribers();
        System.out.println("Наш " +subscriberDtos);

        List<SubscriberDto>subscriberDtoList = subscriberDtos.stream()
                .filter(x -> x.getId().equals(inviteDto.getReceiver().getId()))
                .collect(Collectors.toList());
        System.out.println("Filter " + subscriberDtoList);
        for (SubscriberDto s : subscriberDtoList) {
            if (!s.isActive()){
                return false;
            }
        }
        return true;
    }

    private boolean checkDateInvite(InviteDto inviteDto){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        date = cal.getTime();
        System.out.println("dwdawdawdwadw");
        Calendar calEnd = new GregorianCalendar();
        calEnd.setTime(new Date());
        calEnd.set(Calendar.YEAR, calEnd.get(Calendar.YEAR)+978);
        calEnd.set(Calendar.HOUR_OF_DAY, 0);
        calEnd.set(Calendar.MINUTE, 0);
        calEnd.set(Calendar.SECOND, -1);
        Date midnightTonight = calEnd.getTime();
        List<Invite> invites = inviteRepository.findByStartDateBetweenAndSenderId(date, midnightTonight, inviteDto.getSender().getId());
        long count = invites.stream()
                .count();
        if (count >= 5){
            return false;
        }
        List<InviteDto> inviteDtos = InviteMapper.INSTANCE.toInviteDtos(invites);
        return true;
    }

    private boolean checkActiveInvite(InviteDto inviteDto){

        List<Invite> forCheck = inviteRepository.findAll();
        List<Invite> invites = forCheck.stream()
                .filter(x -> x.getReceiver().getPhone().equals(inviteDto.getReceiver().getPhone()))
                .collect(Collectors.toList());
        for (Invite i : invites) {
            if (i.getReceiver().getPhone().equals(inviteDto.getReceiver().getPhone())
                    &&i.getInviteStatus().equals(InviteStatus.ACTIVE)){
                return false;
            }
        }

        return true;
    }

    private boolean checkSendInviteOnDay(InviteDto inviteDto){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        date = cal.getTime();
        System.out.println("пашет");
        Calendar calEnd = new GregorianCalendar();
        calEnd.setTime(new Date());
        calEnd.set(Calendar.YEAR, calEnd.get(Calendar.YEAR)+978);
        calEnd.set(Calendar.HOUR_OF_DAY, 0);
        calEnd.set(Calendar.MINUTE, 0);
        calEnd.set(Calendar.SECOND, -1);
        Date midnightTonight = calEnd.getTime();
        List<Invite> invites = inviteRepository.findByStartDateBetweenAndSenderIdAndReceiverId(date, midnightTonight, inviteDto.getSender().getId(), inviteDto.getReceiver().getId());
        long count = invites.size();
        System.out.println("Размер " +count);
        return count >= 1;
    }

    private void changeStatusAndDateInvite(InviteDto inviteDto){
        List<Invite> invites = inviteRepository.findAll();
        List<Invite> inviteList = invites.stream()
                .filter(x -> x.getReceiver().getPhone().equals(inviteDto.getReceiver().getPhone()) &&
                        x.getInviteStatus().equals(InviteStatus.ACTIVE))
                .collect(Collectors.toList());
        Long id =null;
        for (Invite i : inviteList){
            id = i.getId();
        }
        assert id != null;
        Optional<Invite> optionalInvite = inviteRepository.findById(id);
        if (optionalInvite.isPresent()){
            Invite invite1 = optionalInvite.get();
            invite1.setInviteStatus(InviteStatus.NOT_ACTIVE);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
            Date date = null;
            try {
                date = sdf.parse(sdf.format(new Date()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar calendar = Calendar.getInstance();
            assert date != null;
            calendar.setTime(date);
            calendar.add(Calendar.SECOND, -1);
            date = calendar.getTime();
            invite1.setEndDate(date);
            inviteRepository.save(invite1);

        }
    }
}

