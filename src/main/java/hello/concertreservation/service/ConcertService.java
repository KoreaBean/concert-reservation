package hello.concertreservation.service;

import hello.concertreservation.dto.request.PostConcertAddRequestDto;
import hello.concertreservation.dto.request.PostConcertReservationRequestDto;
import hello.concertreservation.dto.response.GetReservationSeatLockResponseDto;
import hello.concertreservation.dto.response.PostConcertAddResponseDto;
import hello.concertreservation.dto.response.PostConcertReservationResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface ConcertService {
    // 콘서트 등록
    ResponseEntity<? super PostConcertAddResponseDto> add(PostConcertAddRequestDto dto, HttpServletRequest request);
    // 콘서트 예약
    ResponseEntity<? super PostConcertReservationResponseDto> reservation(PostConcertReservationRequestDto dto, HttpServletRequest request);
    // 콘서트 예약 시 좌석 잠금
    ResponseEntity<? super GetReservationSeatLockResponseDto> reservationSeatLock(Integer concertId, Integer seatId, HttpServletRequest request);
}
