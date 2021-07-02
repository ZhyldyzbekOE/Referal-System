package com.example.refsystem.dao;

import com.example.refsystem.enums.InviteStatus;
import com.example.refsystem.models.Invite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface InviteRepository extends JpaRepository<Invite, Long> {

    List<Invite> findByStartDateBetweenAndSenderId(Date start, Date end, Long id);

    List<Invite> findByStartDateBetweenAndSenderIdAndReceiverId(Date start, Date end, Long idSend, Long idRec);

    Invite findByReceiverIdAndInviteStatus(Long id, InviteStatus inviteStatus);
}
