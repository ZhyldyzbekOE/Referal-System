package com.example.refsystem.controllers;

import com.example.refsystem.models.Response;
import com.example.refsystem.models.dto.InviteDto;
import com.example.refsystem.services.InviteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/api/v1/invite")
public class InviteController {

    @Autowired
    private InviteService inviteService;

    @PostMapping("/send")
    public Response send(@RequestBody InviteDto inviteDto) throws ParseException {
        return inviteService.send(inviteDto);
    }

}
