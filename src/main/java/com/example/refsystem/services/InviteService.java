package com.example.refsystem.services;

import com.example.refsystem.models.Response;
import com.example.refsystem.models.dto.InviteDto;

import java.text.ParseException;

public interface InviteService {

    Response send(InviteDto inviteDto) throws ParseException;
}
