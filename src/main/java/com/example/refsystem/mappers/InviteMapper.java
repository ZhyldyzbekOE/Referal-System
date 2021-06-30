package com.example.refsystem.mappers;

import com.example.refsystem.models.Invite;
import com.example.refsystem.models.dto.InviteDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface InviteMapper {

    InviteMapper INSTANCE = Mappers.getMapper(InviteMapper.class);

    Invite toInvite(InviteDto inviteDto);
    InviteDto toInviteDto(Invite invite);

    List<Invite> toInvites(List<InviteDto> inviteDtos);
    List<InviteDto> toInviteDtos(List<Invite> invites);
}
