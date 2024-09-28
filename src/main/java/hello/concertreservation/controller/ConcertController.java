package hello.concertreservation.controller;

import hello.concertreservation.dto.request.PostConcertAddRequestDto;
import hello.concertreservation.dto.request.PostConcertReservationLimitRequestDto;
import hello.concertreservation.dto.request.PostConcertReservationRequestDto;
import hello.concertreservation.dto.response.GetReservationSeatLockResponseDto;
import hello.concertreservation.dto.response.PostConcertAddResponseDto;
import hello.concertreservation.dto.response.PostConcertReservationLimitResponseDto;
import hello.concertreservation.dto.response.PostConcertReservationResponseDto;
import hello.concertreservation.service.ConcertService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/concert")
@RequiredArgsConstructor
public class ConcertController {

    private final ConcertService concertService;

    // 콘서트 등록
    @PostMapping("/add")
    public ResponseEntity<? super PostConcertAddResponseDto> add (@RequestBody PostConcertAddRequestDto dto, HttpServletRequest request){
        ResponseEntity<? super PostConcertAddResponseDto> result = concertService.add(dto, request);
        return result;
    }

    // 콘서트 좌석 잠금 기능
    @GetMapping("/reservation/{concertId}/{seatId}")
    public ResponseEntity<? super GetReservationSeatLockResponseDto> reservationSeatLock(@PathVariable(name = "concertId") Long concertId, @PathVariable(name = "seatId") Integer seatId, HttpServletRequest request){
        log.info("ConcertController::reservationSeatLock: start");
        ResponseEntity<? super GetReservationSeatLockResponseDto> result = concertService.reservationSeatLock(concertId, seatId, request);
        return result;
    }


    // 콘서트 좌석 예약하기
    @PostMapping("/reservation")
    public ResponseEntity<? super PostConcertReservationResponseDto> reservation (@RequestBody PostConcertReservationRequestDto dto, HttpServletRequest request){
        ResponseEntity<? super PostConcertReservationResponseDto> result = concertService.reservation(dto, request);
        return result;
    }

    // 콘서트 선착순 예매 기능
    @PostMapping("/reservation-limit")
    public ResponseEntity<? super PostConcertReservationLimitResponseDto> reservationLimit(@RequestBody PostConcertReservationLimitRequestDto dto, HttpServletRequest request){
        ResponseEntity<? super PostConcertReservationLimitResponseDto> result = concertService.reservationLimit(dto, request);
        return result;
    }



}
