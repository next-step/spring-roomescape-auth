package com.nextstep.web.reservation.app;

import com.nextstep.web.common.LoginMember;
import com.nextstep.web.member.repository.MemberDao;
import com.nextstep.web.member.repository.entity.MemberEntity;
import com.nextstep.web.reservation.dto.CreateReservationRequest;
import nextstep.common.BusinessException;
import nextstep.domain.member.Member;
import nextstep.domain.reservation.Reservation;
import nextstep.domain.reservation.usecase.ReservationRepository;
import nextstep.domain.reservation.exception.DuplicationReservationException;
import nextstep.domain.schedule.Schedule;
import nextstep.domain.schedule.usecase.ScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class ReservationCommandService {
    private final ReservationRepository reservationRepository;
    private final ScheduleRepository scheduleRepository;
    private final MemberDao memberDao;;

    public ReservationCommandService(ReservationRepository reservationRepository, ScheduleRepository scheduleRepository, MemberDao memberDao) {
        this.reservationRepository = reservationRepository;
        this.scheduleRepository = scheduleRepository;
        this.memberDao = memberDao;
    }

    public Long save(CreateReservationRequest request, LoginMember loginMember) {
        reservationRepository.findByScheduleId(request.getScheduleId()).ifPresent((reservation -> {
            throw new DuplicationReservationException();
        }));

        Member member = validateMember(loginMember);
        Schedule schedule = scheduleRepository.findById(request.getScheduleId())
                .orElseThrow(() ->
                new BusinessException(""));


        Reservation reservation = new Reservation(null, schedule, LocalDateTime.now(),
                member.getName());
        return reservationRepository.save(reservation);
    }

    public void delete(Long id, LoginMember loginMember) {
        Member member = validateMember(loginMember);
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new BusinessException(""));

        if (!reservation.isReservationBy(member.getName())) {
            throw new BusinessException("");
        }
    }

    private Member validateMember(LoginMember loginMember) {
        MemberEntity memberEntity = memberDao.findById(loginMember.getId()).orElseThrow(() ->
                new BusinessException(""));

        return memberEntity.fromThis();
    }
}
